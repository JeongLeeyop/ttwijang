package com.ttwijang.cms.api.cash.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.cash.dto.CashDto;
import com.ttwijang.cms.api.cash.repository.CashTransactionRepository;
import com.ttwijang.cms.api.cash.repository.CashWalletRepository;
import com.ttwijang.cms.api.cash.repository.TeamSponsorshipRepository;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.entity.CashTransaction;
import com.ttwijang.cms.entity.CashWallet;
import com.ttwijang.cms.entity.Team;
import com.ttwijang.cms.entity.TeamSponsorship;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CashService {

    private final CashWalletRepository walletRepository;
    private final CashTransactionRepository transactionRepository;
    private final TeamSponsorshipRepository sponsorshipRepository;
    private final TeamRepository teamRepository;

    /**
     * 지갑 조회 (없으면 생성)
     */
    @Transactional
    public CashDto.WalletResponse getOrCreateWallet(String userUid) {
        CashWallet wallet = walletRepository.findByUserUid(userUid)
                .orElseGet(() -> {
                    CashWallet newWallet = CashWallet.builder()
                            .userUid(userUid)
                            .balance(0)
                            .totalCharged(0)
                            .totalUsed(0)
                            .build();
                    return walletRepository.save(newWallet);
                });
        return toWalletResponse(wallet);
    }

    /**
     * 캐시 충전
     */
    @Transactional
    public CashDto.TransactionResponse charge(CashDto.ChargeRequest request, String userUid) {
        CashWallet wallet = walletRepository.findByUserUid(userUid)
                .orElseGet(() -> {
                    CashWallet newWallet = CashWallet.builder()
                            .userUid(userUid)
                            .balance(0)
                            .totalCharged(0)
                            .totalUsed(0)
                            .build();
                    return walletRepository.save(newWallet);
                });

        int newBalance = wallet.getBalance() + request.getAmount();
        wallet.setBalance(newBalance);
        wallet.setTotalCharged(wallet.getTotalCharged() + request.getAmount());
        wallet.setLastChargedDate(LocalDateTime.now());
        walletRepository.save(wallet);

        CashTransaction transaction = CashTransaction.builder()
                .walletUid(wallet.getUid())
                .userUid(userUid)
                .type(CashTransaction.TransactionType.CHARGE)
                .amount(request.getAmount())
                .balanceAfter(newBalance)
                .description("캐시 충전 (" + request.getPaymentMethod() + ")")
                .paymentUid(request.getPaymentReferenceId())
                .build();
        transaction = transactionRepository.save(transaction);

        return toTransactionResponse(transaction);
    }

    /**
     * 캐시 사용
     */
    @Transactional
    public CashDto.TransactionResponse use(CashDto.UseRequest request, String userUid) {
        CashWallet wallet = walletRepository.findByUserUid(userUid)
                .orElseThrow(() -> new IllegalArgumentException("지갑이 존재하지 않습니다."));

        if (wallet.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        int newBalance = wallet.getBalance() - request.getAmount();
        wallet.setBalance(newBalance);
        wallet.setTotalUsed(wallet.getTotalUsed() + request.getAmount());
        walletRepository.save(wallet);

        CashTransaction transaction = CashTransaction.builder()
                .walletUid(wallet.getUid())
                .userUid(userUid)
                .type(CashTransaction.TransactionType.USE)
                .amount(request.getAmount())
                .balanceAfter(newBalance)
                .description(request.getDescription())
                .referenceUid(request.getReferenceUid())
                .build();
        transaction = transactionRepository.save(transaction);

        return toTransactionResponse(transaction);
    }

    /**
     * 캐시 적립 (포인트 획득 등)
     */
    @Transactional
    public CashDto.TransactionResponse earn(int amount, String description, String userUid) {
        CashWallet wallet = walletRepository.findByUserUid(userUid)
                .orElseGet(() -> {
                    CashWallet newWallet = CashWallet.builder()
                            .userUid(userUid)
                            .balance(0)
                            .totalCharged(0)
                            .totalUsed(0)
                            .build();
                    return walletRepository.save(newWallet);
                });

        int newBalance = wallet.getBalance() + amount;
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        CashTransaction transaction = CashTransaction.builder()
                .walletUid(wallet.getUid())
                .userUid(userUid)
                .type(CashTransaction.TransactionType.EARN)
                .amount(amount)
                .balanceAfter(newBalance)
                .description(description)
                .build();
        transaction = transactionRepository.save(transaction);

        return toTransactionResponse(transaction);
    }

    /**
     * 거래 내역 조회
     */
    @Transactional(readOnly = true)
    public Page<CashDto.TransactionResponse> getTransactions(String userUid, Pageable pageable) {
        CashWallet wallet = walletRepository.findByUserUid(userUid)
                .orElseThrow(() -> new IllegalArgumentException("지갑이 존재하지 않습니다."));
        return transactionRepository.findByWalletUid(wallet.getUid(), pageable)
                .map(this::toTransactionResponse);
    }

    /**
     * 기간별 거래 내역 조회
     */
    @Transactional(readOnly = true)
    public Page<CashDto.TransactionResponse> getTransactionsByDateRange(
            String userUid, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        CashWallet wallet = walletRepository.findByUserUid(userUid)
                .orElseThrow(() -> new IllegalArgumentException("지갑이 존재하지 않습니다."));
        return transactionRepository.findByWalletUidAndDateRange(wallet.getUid(), startDate, endDate, pageable)
                .map(this::toTransactionResponse);
    }

    /**
     * 팀 후원하기
     */
    @Transactional
    public CashDto.SponsorshipResponse sponsorTeam(CashDto.SponsorshipRequest request, String sponsorUid) {
        Team team = teamRepository.findByUid(request.getTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 캐시 차감 (1회성, 정기 후원만)
        if (request.getSponsorshipType() != TeamSponsorship.SponsorshipType.OWNER && request.getAmount() != null) {
            CashDto.UseRequest useRequest = CashDto.UseRequest.builder()
                    .amount(request.getAmount())
                    .description(team.getName() + " 팀 후원")
                    .referenceUid(request.getTeamUid())
                    .build();
            use(useRequest, sponsorUid);
        }

        TeamSponsorship sponsorship = TeamSponsorship.builder()
                .teamUid(request.getTeamUid())
                .sponsorUid(sponsorUid)
                .type(request.getSponsorshipType())
                .amount(request.getAmount())
                .totalAmount(request.getAmount())
                .message(request.getMessage())
                .status(TeamSponsorship.SponsorshipStatus.ACTIVE)
                .build();
        sponsorship = sponsorshipRepository.save(sponsorship);

        // 팀의 총 후원금 업데이트
        if (request.getAmount() != null) {
            int currentTotal = team.getTotalSponsorship() != null ? team.getTotalSponsorship() : 0;
            team.setTotalSponsorship(currentTotal + request.getAmount());
            teamRepository.save(team);
        }

        return toSponsorshipResponse(sponsorship, team);
    }

    /**
     * 팀 후원 목록 조회
     */
    @Transactional(readOnly = true)
    public List<CashDto.SponsorshipResponse> getTeamSponsorships(String teamUid) {
        Team team = teamRepository.findByUid(teamUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        return sponsorshipRepository.findByTeamUid(teamUid).stream()
                .map(s -> toSponsorshipResponse(s, team))
                .collect(Collectors.toList());
    }

    /**
     * 내 후원 목록 조회
     */
    @Transactional(readOnly = true)
    public List<CashDto.SponsorshipResponse> getMySponsorships(String sponsorUid) {
        return sponsorshipRepository.findBySponsorUid(sponsorUid).stream()
                .map(s -> {
                    Team team = teamRepository.findByUid(s.getTeamUid()).orElse(null);
                    return toSponsorshipResponse(s, team);
                })
                .collect(Collectors.toList());
    }

    /**
     * 팀 후원 요약 조회
     */
    @Transactional(readOnly = true)
    public CashDto.TeamSponsorshipSummary getTeamSponsorshipSummary(String teamUid) {
        Team team = teamRepository.findByUid(teamUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        List<TeamSponsorship> sponsorships = sponsorshipRepository.findByTeamUidAndStatus(teamUid, TeamSponsorship.SponsorshipStatus.ACTIVE);

        int ownerCount = (int) sponsorships.stream()
                .filter(s -> s.getType() == TeamSponsorship.SponsorshipType.OWNER)
                .count();
        int regularCount = (int) sponsorships.stream()
                .filter(s -> s.getType() == TeamSponsorship.SponsorshipType.REGULAR)
                .count();
        int oneTimeCount = (int) sponsorships.stream()
                .filter(s -> s.getType() == TeamSponsorship.SponsorshipType.ONE_TIME)
                .count();
        long totalAmount = sponsorships.stream()
                .mapToLong(s -> s.getTotalAmount() != null ? s.getTotalAmount() : 0)
                .sum();

        return CashDto.TeamSponsorshipSummary.builder()
                .teamUid(teamUid)
                .teamName(team.getName())
                .ownerCount(ownerCount)
                .regularSponsorCount(regularCount)
                .oneTimeSponsorCount(oneTimeCount)
                .totalSponsorshipAmount(totalAmount)
                .build();
    }

    private CashDto.WalletResponse toWalletResponse(CashWallet wallet) {
        return CashDto.WalletResponse.builder()
                .uid(wallet.getUid())
                .userUid(wallet.getUserUid())
                .balance(wallet.getBalance())
                .totalCharged(wallet.getTotalCharged())
                .totalUsed(wallet.getTotalUsed())
                .lastChargedDate(wallet.getLastChargedDate())
                .build();
    }

    private CashDto.TransactionResponse toTransactionResponse(CashTransaction transaction) {
        return CashDto.TransactionResponse.builder()
                .uid(transaction.getUid())
                .walletUid(transaction.getWalletUid())
                .transactionType(transaction.getType())
                .amount(transaction.getAmount())
                .balanceAfter(transaction.getBalanceAfter())
                .description(transaction.getDescription())
                .createdDate(transaction.getCreatedDate())
                .build();
    }

    private CashDto.SponsorshipResponse toSponsorshipResponse(TeamSponsorship sponsorship, Team team) {
        return CashDto.SponsorshipResponse.builder()
                .uid(sponsorship.getUid())
                .teamUid(sponsorship.getTeamUid())
                .teamName(team != null ? team.getName() : "")
                .sponsorUid(sponsorship.getSponsorUid())
                .sponsorshipType(sponsorship.getType())
                .amount(sponsorship.getAmount())
                .totalAmount(sponsorship.getTotalAmount())
                .status(sponsorship.getStatus())
                .message(sponsorship.getMessage())
                .createdDate(sponsorship.getCreatedDate())
                .build();
    }
}

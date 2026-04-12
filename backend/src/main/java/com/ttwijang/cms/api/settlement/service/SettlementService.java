package com.ttwijang.cms.api.settlement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.ttwijang.cms.api.notification.service.NotificationService;
import com.ttwijang.cms.api.settlement.dto.SettlementDto;
import com.ttwijang.cms.api.settlement.repository.SettlementConfigRepository;
import com.ttwijang.cms.api.settlement.repository.SettlementRepository;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.entity.Notification;
import com.ttwijang.cms.entity.Settlement;
import com.ttwijang.cms.entity.SettlementConfig;
import com.ttwijang.cms.entity.Team;
import com.ttwijang.cms.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private static final String TOSS_PAYOUT_URL = "https://api.tosspayments.com/v1/payouts";
    private static final long CONFIG_ID = 1L;
    private static final int DEFAULT_SETTLEMENT_DAYS = 3;

    private static final Map<String, String> BANK_CODE_MAP = new HashMap<>();
    static {
        BANK_CODE_MAP.put("국민은행", "004");
        BANK_CODE_MAP.put("기업은행", "003");
        BANK_CODE_MAP.put("신한은행", "088");
        BANK_CODE_MAP.put("우리은행", "020");
        BANK_CODE_MAP.put("하나은행", "081");
        BANK_CODE_MAP.put("농협은행", "011");
        BANK_CODE_MAP.put("NH농협", "011");
        BANK_CODE_MAP.put("SC제일은행", "023");
        BANK_CODE_MAP.put("카카오뱅크", "090");
        BANK_CODE_MAP.put("케이뱅크", "089");
        BANK_CODE_MAP.put("토스뱅크", "092");
        BANK_CODE_MAP.put("씨티은행", "027");
        BANK_CODE_MAP.put("대구은행", "031");
        BANK_CODE_MAP.put("부산은행", "032");
        BANK_CODE_MAP.put("경남은행", "039");
        BANK_CODE_MAP.put("광주은행", "034");
        BANK_CODE_MAP.put("전북은행", "037");
        BANK_CODE_MAP.put("제주은행", "035");
        BANK_CODE_MAP.put("수협은행", "007");
        BANK_CODE_MAP.put("우체국", "071");
        BANK_CODE_MAP.put("산업은행", "002");
    }

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    private final WebClient webClient = WebClient.builder().build();
    private final SettlementRepository settlementRepository;
    private final SettlementConfigRepository configRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // ─────────────────────────────────────────────
    // 설정 관리
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public SettlementDto.ConfigResponse getConfig() {
        SettlementConfig config = configRepository.findById(CONFIG_ID)
                .orElse(SettlementConfig.builder()
                        .id(CONFIG_ID)
                        .settlementDays(DEFAULT_SETTLEMENT_DAYS)
                        .useTossPayout(false)
                        .autoSettleDay(1)
                        .description("")
                        .build());
        return SettlementDto.ConfigResponse.builder()
                .settlementDays(config.getSettlementDays())
                .useTossPayout(config.getUseTossPayout())
                .autoSettleDay(config.getAutoSettleDay())
                .description(config.getDescription())
                .build();
    }

    @Transactional
    public SettlementDto.ConfigResponse updateConfig(SettlementDto.ConfigUpdateRequest request) {
        SettlementConfig config = configRepository.findById(CONFIG_ID)
                .orElse(SettlementConfig.builder().id(CONFIG_ID).build());
        if (request.getSettlementDays() != null) config.setSettlementDays(request.getSettlementDays());
        if (request.getUseTossPayout() != null) config.setUseTossPayout(request.getUseTossPayout());
        if (request.getAutoSettleDay() != null) config.setAutoSettleDay(request.getAutoSettleDay());
        if (request.getDescription() != null) config.setDescription(request.getDescription());
        config = configRepository.save(config);
        return SettlementDto.ConfigResponse.builder()
                .settlementDays(config.getSettlementDays())
                .useTossPayout(config.getUseTossPayout())
                .autoSettleDay(config.getAutoSettleDay())
                .description(config.getDescription())
                .build();
    }

    // ─────────────────────────────────────────────
    // 월별 집계
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<SettlementDto.MonthlySummary> getMonthlySummary(String period) {
        int days = getSettlementDays();
        List<Object[]> rows = settlementRepository.getMonthlyRawSummary(period, days);

        return rows.stream().map(row -> {
            String teamUid   = (String) row[0];
            String teamName  = (String) row[1];
            String ownerUid  = (String) row[2];
            String bankName  = (String) row[3];
            String bankAcct  = (String) row[4];
            Long totalAmount = ((Number) row[5]).longValue();
            Long itemCount   = ((Number) row[6]).longValue();

            Settlement existing = settlementRepository.findByTeamUidAndPeriod(teamUid, period).orElse(null);

            return SettlementDto.MonthlySummary.builder()
                    .teamUid(teamUid)
                    .teamName(teamName)
                    .ownerUid(ownerUid)
                    .bankName(bankName)
                    .bankAccount(bankAcct)
                    .totalAmount(totalAmount)
                    .itemCount(itemCount)
                    .period(period)
                    .alreadySettled(existing != null && existing.getStatus() == Settlement.SettlementStatus.COMPLETED)
                    .settlementUid(existing != null ? existing.getUid() : null)
                    .build();
        }).collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────
    // 모달용 건별 내역 (특정 팀 + 기간)
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<SettlementDto.ItemResponse> getModalItems(String teamUid, String period) {
        int days = getSettlementDays();
        return settlementRepository.getModalItemsRaw(teamUid, period, days)
                .stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────
    // 건별 내역 (탭, 페이징)
    // ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<SettlementDto.ItemResponse> getDetailItems(
            String teamName, String startDate, String endDate, Pageable pageable) {
        int days = getSettlementDays();
        String start = startDate + " 00:00:00";
        String end   = endDate   + " 23:59:59";
        String name  = (teamName != null && !teamName.isEmpty()) ? teamName : null;

        return settlementRepository.getDetailItemsRaw(name, start, end, days, pageable)
                .map(this::toItemResponse);
    }

    // ─────────────────────────────────────────────
    // 정산 완료 처리 (Toss 페이아웃)
    // ─────────────────────────────────────────────

    @Transactional
    public SettlementDto.HistoryResponse createSettlement(SettlementDto.CreateRequest request) {
        settlementRepository.findByTeamUidAndPeriod(request.getTeamUid(), request.getPeriod())
                .ifPresent(s -> { throw new IllegalArgumentException("이미 해당 기간의 정산이 존재합니다."); });

        int days = getSettlementDays();
        List<Object[]> rows = settlementRepository.getMonthlyRawSummary(request.getPeriod(), days);
        Object[] matched = rows.stream()
                .filter(r -> request.getTeamUid().equals(r[0]))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 기간에 정산할 거래 내역이 없습니다."));

        Settlement settlement = Settlement.builder()
                .teamUid((String) matched[0])
                .teamName((String) matched[1])
                .ownerUid((String) matched[2])
                .bankName((String) matched[3])
                .bankAccount((String) matched[4])
                .totalAmount(((Number) matched[5]).intValue())
                .itemCount(((Number) matched[6]).intValue())
                .period(request.getPeriod())
                .status(Settlement.SettlementStatus.PENDING)
                .build();

        return toHistoryResponse(settlementRepository.save(settlement));
    }

    @Transactional
    public SettlementDto.HistoryResponse completeSettlement(String uid, SettlementDto.CompleteRequest request) {
        Settlement settlement = settlementRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("정산 내역을 찾을 수 없습니다."));

        // Toss 자동이체 설정 확인
        boolean useToss = configRepository.findById(CONFIG_ID)
                .map(c -> Boolean.TRUE.equals(c.getUseTossPayout()))
                .orElse(false);

        if (useToss) {
            String holderName = resolveHolderName(settlement.getOwnerUid());
            executeTossPayout(settlement, holderName);
        } else {
            log.info("[Settlement] 수동 정산 처리 — uid={}, amount={}",
                    settlement.getUid(), settlement.getTotalAmount());
        }

        settlement.setStatus(Settlement.SettlementStatus.COMPLETED);
        settlement.setSettledAt(LocalDateTime.now());
        settlement.setAdminNote(request.getAdminNote());

        Settlement saved = settlementRepository.save(settlement);

        // 팀 관리자에게 정산 완료 알림 발송
        if (saved.getOwnerUid() != null) {
            notificationService.createNotification(
                    saved.getOwnerUid(),
                    Notification.NotificationType.SYSTEM,
                    "정산이 완료되었습니다",
                    saved.getTeamName() + " 팀의 " + saved.getPeriod() + " 정산이 완료되었습니다. "
                            + "금액: " + String.format("%,d", saved.getTotalAmount()) + "원",
                    saved.getUid(),
                    "SETTLEMENT",
                    "/mypage"
            );
        }

        return toHistoryResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<SettlementDto.HistoryResponse> getHistory(Pageable pageable) {
        return settlementRepository.findAllByOrderByCreatedDateDesc(pageable)
                .map(this::toHistoryResponse);
    }

    // ─────────────────────────────────────────────
    // 월별 자동 정산 스케줄러 (매일 오전 2시 실행)
    // ─────────────────────────────────────────────

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void runAutoMonthlySettle() {
        SettlementConfig config = configRepository.findById(CONFIG_ID).orElse(null);
        if (config == null
                || !Boolean.TRUE.equals(config.getUseTossPayout())
                || config.getAutoSettleDay() == null) {
            return;
        }

        int today = LocalDate.now().getDayOfMonth();
        if (today != config.getAutoSettleDay()) return;

        // 전월 기준 period (예: 오늘이 2026-04-01이면 → "2026-03")
        String period = LocalDate.now().minusMonths(1)
                .format(DateTimeFormatter.ofPattern("yyyy-MM"));

        log.info("[AutoSettle] 월별 자동 정산 시작 — period={}", period);

        int days = config.getSettlementDays() != null ? config.getSettlementDays() : DEFAULT_SETTLEMENT_DAYS;
        List<Object[]> rows = settlementRepository.getMonthlyRawSummary(period, days);

        for (Object[] row : rows) {
            String teamUid = (String) row[0];
            try {
                // 이미 정산된 팀은 건너뜀
                boolean alreadyDone = settlementRepository.findByTeamUidAndPeriod(teamUid, period)
                        .map(s -> s.getStatus() == Settlement.SettlementStatus.COMPLETED)
                        .orElse(false);
                if (alreadyDone) continue;

                // PENDING 상태로 정산 생성 (없으면 신규 생성)
                Settlement settlement = settlementRepository.findByTeamUidAndPeriod(teamUid, period)
                        .orElseGet(() -> {
                            Settlement s = Settlement.builder()
                                    .teamUid((String) row[0])
                                    .teamName((String) row[1])
                                    .ownerUid((String) row[2])
                                    .bankName((String) row[3])
                                    .bankAccount((String) row[4])
                                    .totalAmount(((Number) row[5]).intValue())
                                    .itemCount(((Number) row[6]).intValue())
                                    .period(period)
                                    .status(Settlement.SettlementStatus.PENDING)
                                    .build();
                            return settlementRepository.save(s);
                        });

                // Toss 자동이체 설정 시 실제 이체
                if (Boolean.TRUE.equals(config.getUseTossPayout())) {
                    String holderName = resolveHolderName(settlement.getOwnerUid());
                    executeTossPayout(settlement, holderName);
                }

                settlement.setStatus(Settlement.SettlementStatus.COMPLETED);
                settlement.setSettledAt(LocalDateTime.now());
                settlement.setAdminNote("월별 자동 정산");
                settlementRepository.save(settlement);

                // 팀 운영자 알림
                if (settlement.getOwnerUid() != null) {
                    notificationService.createNotification(
                            settlement.getOwnerUid(),
                            Notification.NotificationType.SYSTEM,
                            "정산이 완료되었습니다",
                            settlement.getTeamName() + " 팀의 " + period + " 정산이 완료되었습니다. "
                                    + "금액: " + String.format("%,d", settlement.getTotalAmount()) + "원",
                            settlement.getUid(),
                            "SETTLEMENT",
                            "/mypage"
                    );
                }

                log.info("[AutoSettle] 완료 — teamUid={}, amount={}", teamUid, settlement.getTotalAmount());
            } catch (Exception e) {
                log.error("[AutoSettle] 팀 정산 실패 — teamUid={}, error={}", teamUid, e.getMessage());
            }
        }

        log.info("[AutoSettle] 월별 자동 정산 종료 — period={}, 처리팀수={}", period, rows.size());
    }

    // ─────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────

    private int getSettlementDays() {
        return configRepository.findById(CONFIG_ID)
                .map(SettlementConfig::getSettlementDays)
                .orElse(DEFAULT_SETTLEMENT_DAYS);
    }

    private String resolveHolderName(String ownerUid) {
        if (ownerUid == null) return "팀 운영자";
        return userRepository.findByUid(ownerUid)
                .map(User::getActualName)
                .filter(n -> n != null && !n.isEmpty())
                .orElse("팀 운영자");
    }

    private void executeTossPayout(Settlement settlement, String holderName) {
        if (settlement.getBankAccount() == null || settlement.getBankAccount().isEmpty()) {
            throw new IllegalArgumentException("정산 계좌 정보가 없습니다. 팀 운영자가 환불 계좌를 등록해야 합니다.");
        }

        String bankCode = BANK_CODE_MAP.getOrDefault(settlement.getBankName(), settlement.getBankName());
        String encodedKey = Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes());

        Map<String, Object> body = new HashMap<>();
        body.put("amount", settlement.getTotalAmount());
        body.put("bankCode", bankCode);
        body.put("accountNumber", settlement.getBankAccount().replaceAll("-", ""));
        body.put("holderName", holderName);
        body.put("payoutKey", "settlement-" + settlement.getUid()); // 멱등성 키

        try {
            webClient.post()
                    .uri(TOSS_PAYOUT_URL)
                    .header("Authorization", "Basic " + encodedKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.info("[Settlement] Toss 페이아웃 성공 — uid={}, amount={}, bank={}",
                    settlement.getUid(), settlement.getTotalAmount(), settlement.getBankName());

        } catch (WebClientResponseException e) {
            log.error("[Settlement] Toss 페이아웃 실패: {}", e.getResponseBodyAsString());
            throw new IllegalStateException("Toss 계좌이체 실패: " + e.getResponseBodyAsString());
        }
    }

    private SettlementDto.ItemResponse toItemResponse(Object[] row) {
        return SettlementDto.ItemResponse.builder()
                .teamName((String) row[0])
                .stadiumName((String) row[1])
                .matchDate((String) row[2])
                .userName((String) row[3])
                .amount(((Number) row[4]).intValue())
                .createdDate(row[5] != null ? ((java.sql.Timestamp) row[5]).toLocalDateTime() : null)
                .build();
    }

    private SettlementDto.HistoryResponse toHistoryResponse(Settlement s) {
        return SettlementDto.HistoryResponse.builder()
                .uid(s.getUid())
                .teamUid(s.getTeamUid())
                .teamName(s.getTeamName())
                .ownerUid(s.getOwnerUid())
                .bankName(s.getBankName())
                .bankAccount(s.getBankAccount())
                .totalAmount(s.getTotalAmount())
                .itemCount(s.getItemCount())
                .period(s.getPeriod())
                .status(s.getStatus())
                .settledAt(s.getSettledAt())
                .adminNote(s.getAdminNote())
                .createdDate(s.getCreatedDate())
                .build();
    }
}

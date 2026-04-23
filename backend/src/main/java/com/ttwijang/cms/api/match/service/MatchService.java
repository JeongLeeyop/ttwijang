package com.ttwijang.cms.api.match.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.match.dto.MatchDto;
import com.ttwijang.cms.api.match.repository.FutsalMatchRepository;
import com.ttwijang.cms.api.match.repository.MatchApplicationRepository;
import com.ttwijang.cms.api.match.repository.MatchConfigRepository;
import com.ttwijang.cms.api.cash.dto.CashDto;
import com.ttwijang.cms.api.cash.repository.CashTransactionRepository;
import com.ttwijang.cms.api.cash.service.CashService;
import com.ttwijang.cms.api.guest.repository.GuestApplicationRepository;
import com.ttwijang.cms.api.guest.repository.GuestRecruitmentRepository;
import com.ttwijang.cms.api.manner.repository.MannerRatingRepository;
import com.ttwijang.cms.api.notification.service.NotificationService;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.api.team.repository.TeamMemberRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.entity.CashTransaction;
import com.ttwijang.cms.entity.FutsalMatch;
import com.ttwijang.cms.entity.GuestApplication;
import com.ttwijang.cms.entity.MatchApplication;
import com.ttwijang.cms.entity.MatchConfig;
import com.ttwijang.cms.entity.Notification;
import com.ttwijang.cms.entity.Team;
import com.ttwijang.cms.entity.TeamMember;
import com.ttwijang.cms.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final FutsalMatchRepository matchRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final MatchConfigRepository matchConfigRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final CashService cashService;
    private final CashTransactionRepository cashTransactionRepository;
    private final GuestApplicationRepository guestApplicationRepository;
    private final GuestRecruitmentRepository guestRecruitmentRepository;
    private final NotificationService notificationService;
    private final TeamMemberRepository teamMemberRepository;
    private final MannerRatingRepository mannerRatingRepository;

    private static final Map<String, Integer> FORMAT_MAX_PLAYERS = new HashMap<>();
    static {
        FORMAT_MAX_PLAYERS.put("4vs4", 8);
        FORMAT_MAX_PLAYERS.put("5vs5", 10);
        FORMAT_MAX_PLAYERS.put("6vs6", 12);
        FORMAT_MAX_PLAYERS.put("7vs7", 14);
        FORMAT_MAX_PLAYERS.put("FOUR_VS_FOUR", 8);
        FORMAT_MAX_PLAYERS.put("FIVE_VS_FIVE", 10);
        FORMAT_MAX_PLAYERS.put("SIX_VS_SIX", 12);
        FORMAT_MAX_PLAYERS.put("SEVEN_VS_SEVEN", 14);
    }

    /**
     * 매치 방식에 따른 최대 인원 (양 팀 합산)
     */
    public static int getMaxPlayersByFormat(String format) {
        return FORMAT_MAX_PLAYERS.getOrDefault(format, 10);
    }

    /**
     * 매치 생성
     */
    @Transactional
    public MatchDto.DetailResponse createMatch(MatchDto.CreateRequest request, String userUid) {
        Team hostTeam = teamRepository.findByUid(request.getHostTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 팀 운영자 권한 확인
        if (!hostTeam.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("매치 생성 권한이 없습니다.");
        }

        FutsalMatch match = FutsalMatch.builder()
                .hostTeamUid(request.getHostTeamUid())
                .matchType(request.getMatchType())
                .matchFormat(request.getMatchFormat())
                .matchDate(request.getMatchDate())
                .matchTime(request.getMatchTime())
                .durationHours(request.getDurationHours())
                .stadiumName(request.getStadiumName())
                .stadiumAddress(request.getStadiumAddress())
                .fee(request.getFee() != null ? request.getFee() : 0)
                .regionSido(request.getRegionSido())
                .regionSigungu(request.getRegionSigungu())
                .additionalInfo(request.getAdditionalInfo())
                .status(FutsalMatch.FutsalMatchStatus.RECRUITING)
                .build();

        match = matchRepository.save(match);
        return toDetailResponse(match);
    }

    /**
     * 매치 상세 조회
     */
    @Transactional(readOnly = true)
    public MatchDto.DetailResponse getMatchDetail(String uid) {
        return getMatchDetail(uid, null);
    }

    /**
     * 매치 상세 조회 (로그인 사용자 신청 여부 포함)
     */
    @Transactional(readOnly = true)
    public MatchDto.DetailResponse getMatchDetail(String uid, String userUid) {
        FutsalMatch match = matchRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매치입니다."));
        MatchDto.DetailResponse response = toDetailResponse(match, userUid);
        if (userUid != null && !userUid.isEmpty()) {
            boolean appliedAsTeam = matchApplicationRepository.existsByMatchUidAndApplicantUserUidAndStatus(
                    match.getUid(), userUid, MatchApplication.ApplicationStatus.APPROVED);
            // 게스트로 참여했는지도 확인
            boolean appliedAsGuest = false;
            if (!appliedAsTeam) {
                appliedAsGuest = guestRecruitmentRepository.findFirstByMatchUidOrderByCreatedDateDesc(match.getUid())
                        .map(recruitment -> guestApplicationRepository.findByRecruitmentUidAndUserUid(
                                recruitment.getUid(), userUid)
                                .filter(app -> app.getStatus() == GuestApplication.ApplicationStatus.APPROVED)
                                .isPresent())
                        .orElse(false);
            }
            response.setAlreadyApplied(appliedAsTeam || appliedAsGuest);
            response.setHasRatedManner(
                    mannerRatingRepository.existsByMatchUidAndRaterUserUid(match.getUid(), userUid));
        } else {
            response.setAlreadyApplied(false);
            response.setHasRatedManner(false);
        }
        return response;
    }

    /**
     * 매치 목록 조회
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchList(String region, LocalDate date, 
            FutsalMatch.FutsalMatchStatus status, Pageable pageable) {
        Page<FutsalMatch> matches;
        
        if (date != null) {
            matches = matchRepository.findByMatchDate(date, pageable);
        } else if (status != null && region != null && !region.isEmpty()) {
            String[] parts = region.split(" ", 2);
            if (parts.length == 2) {
                matches = matchRepository.findByStatusAndRegionSidoAndSigungu(status, parts[0], parts[1], pageable);
            } else {
                matches = matchRepository.findByStatusAndRegion(status, region, pageable);
            }
        } else if (status != null) {
            matches = matchRepository.findByStatus(status, pageable);
        } else {
            matches = matchRepository.findAll(pageable);
        }
        
        return matches.map(this::toListResponse);
    }

    /**
     * 매치 목록 조회 - 시/군/구 이름으로만 조회 (도 필터 없이, regionCode 사용 시)
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchListBySigungu(String sigungu, LocalDate date,
            FutsalMatch.FutsalMatchStatus status, Pageable pageable) {
        Page<FutsalMatch> matches;
        if (date != null) {
            matches = matchRepository.findByMatchDate(date, pageable);
        } else if (status != null && sigungu != null && !sigungu.isEmpty()) {
            matches = matchRepository.findByStatusAndSigungu(status, sigungu, pageable);
        } else if (status != null) {
            matches = matchRepository.findByStatus(status, pageable);
        } else {
            matches = matchRepository.findAll(pageable);
        }
        return matches.map(this::toListResponse);
    }

    /**
     * 날짜 범위별 매치 조회 (캘린더용) - 지역 필터 지원
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByDateRange(LocalDate startDate, LocalDate endDate,
            String region, Pageable pageable) {
        Page<FutsalMatch> matches;
        if (region != null && !region.isEmpty()) {
            // 지역 필터가 있으면 "시도 시군구" 형태에서 파싱하여 regionSido/regionSigungu로 검색
            String[] parts = region.split(" ", 2);
            if (parts.length == 2) {
                matches = matchRepository.findByMatchDateBetweenAndRegionSidoAndRegionSigungu(
                        startDate, endDate, parts[0], parts[1], pageable);
            } else {
                matches = matchRepository.findByMatchDateBetween(startDate, endDate, pageable);
            }
        } else {
            matches = matchRepository.findByMatchDateBetween(startDate, endDate, pageable);
        }
        return matches.map(this::toListResponse);
    }

    /**
     * 날짜 범위별 매치 조회 - 시/군/구 이름으로만 조회 (도 필터 없이, regionCode 사용 시)
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByDateRangeBySigungu(LocalDate startDate, LocalDate endDate,
            String sigungu, Pageable pageable) {
        Page<FutsalMatch> matches;
        if (sigungu != null && !sigungu.isEmpty()) {
            matches = matchRepository.findByMatchDateBetweenAndRegionSigungu(
                    startDate, endDate, sigungu, pageable);
        } else {
            matches = matchRepository.findByMatchDateBetween(startDate, endDate, pageable);
        }
        return matches.map(this::toListResponse);
    }

    /**
     * BR-06: 팀별 매치 조회 (주최팀 또는 상대팀으로 참여한 모든 매치)
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByTeam(String teamUid, Pageable pageable) {
        return matchRepository.findByTeamUid(teamUid, pageable)
                .map(this::toListResponse);
    }

    /**
     * BR-06 + BR-08: 팀별 + 매치 타입별 조회
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByTeamAndType(String teamUid, FutsalMatch.MatchType matchType, Pageable pageable) {
        return matchRepository.findByTeamUidAndMatchType(teamUid, matchType, pageable)
                .map(this::toListResponse);
    }

    /**
     * 팀별 날짜 범위 매치 조회 (캘린더용)
     */
    @Transactional(readOnly = true)
    public Page<MatchDto.ListResponse> getMatchesByTeamAndDateRange(String teamUid, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return matchRepository.findByTeamUidAndMatchDateBetween(teamUid, startDate, endDate, pageable)
                .map(this::toListResponse);
    }

    /**
     * 매치 신청
     */
    @Transactional
    public MatchDto.DetailResponse applyMatch(MatchDto.ApplyRequest request, String userUid) {
        FutsalMatch match = matchRepository.findByUid(request.getMatchUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매치입니다."));

        if (match.getStatus() != FutsalMatch.FutsalMatchStatus.RECRUITING) {
            throw new IllegalArgumentException("모집 중인 매치가 아닙니다.");
        }

        Team applicantTeam = teamRepository.findByUid(request.getApplicantTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 팀 운영자 권한 확인
        if (!applicantTeam.getOwnerUid().equals(userUid)) {
            // throw new IllegalArgumentException("매치 신청 권한이 없습니다.");
        }

        // 자기 팀에 신청하는지 확인
        if (match.getHostTeamUid().equals(request.getApplicantTeamUid())) {
            // throw new IllegalArgumentException("자신의 팀에는 신청할 수 없습니다.");
        }

        // 이미 신청했는지 확인 (CANCELLED/REJECTED는 재신청 허용)
        if (matchApplicationRepository.existsByMatchUidAndApplicantTeamUidAndStatus(
                request.getMatchUid(), request.getApplicantTeamUid(),
                MatchApplication.ApplicationStatus.APPROVED)) {
            throw new IllegalArgumentException("이미 신청한 매치입니다.");
        }

        // 정원 초과 확인
        int maxPlayers = getMaxPlayersByFormat(match.getMatchFormat());
        long currentApproved = matchApplicationRepository.countByMatchUidAndStatus(
                match.getUid(), MatchApplication.ApplicationStatus.APPROVED);
        if (currentApproved >= maxPlayers) {
            throw new IllegalArgumentException("모집 인원이 마감되었습니다.");
        }

        // 신청 저장
        MatchApplication application = MatchApplication.builder()
                .matchUid(match.getUid())
                .applicantTeamUid(request.getApplicantTeamUid())
                .applicantUserUid(userUid)
                .status(MatchApplication.ApplicationStatus.APPROVED)
                .message(request.getMessage())
                .build();
        matchApplicationRepository.save(application);

        // 참가비가 있으면 캐시 차감
        int fee = match.getFee() != null ? match.getFee() : 0;
        if (fee > 0) {
            CashDto.UseRequest useRequest = CashDto.UseRequest.builder()
                    .amount(fee)
                    .description("매치 참가비 (" + applicantTeam.getName() + " / " + match.getStadiumName() + ")")
                    .referenceUid(match.getUid())
                    .referenceType("MATCH")
                    .build();
            cashService.use(useRequest, userUid);
        }

        // 친선경기(FRIENDLY)는 인원이 다 차면 매칭 성사
        if (match.getMatchType() == FutsalMatch.MatchType.FRIENDLY) {
            match.setGuestTeamUid(request.getApplicantTeamUid());
            currentApproved = matchApplicationRepository.countByMatchUidAndStatus(
                    match.getUid(), MatchApplication.ApplicationStatus.APPROVED);
            if (currentApproved >= maxPlayers) {
                match.setStatus(FutsalMatch.FutsalMatchStatus.MATCHED);
            }
            match = matchRepository.save(match);
        }

        return toDetailResponse(match);
    }

    /**
     * 매치 삭제 (취소) — 참가비 전액 환불 + 알림
     */
    @Transactional
    public void cancelMatch(String matchUid, String userUid) {
        FutsalMatch match = matchRepository.findByUid(matchUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매치입니다."));

        Team hostTeam = teamRepository.findByUid(match.getHostTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        if (!hostTeam.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("매치 취소 권한이 없습니다.");
        }

        String stadiumName = match.getStadiumName() != null ? match.getStadiumName() : "";
        String matchInfo = match.getMatchDate() + " " + stadiumName;

        // 팀 신청자 환불 + 알림
        List<MatchApplication> approvedApps = matchApplicationRepository.findByMatchUidAndStatus(
                matchUid, MatchApplication.ApplicationStatus.APPROVED);
        List<CashTransaction> useTransactions = cashTransactionRepository
                .findByReferenceUidAndReferenceTypeAndType(matchUid, "MATCH", CashTransaction.TransactionType.USE);
        Map<String, Integer> refundMap = new HashMap<>();
        for (CashTransaction tx : useTransactions) {
            refundMap.merge(tx.getUserUid(), tx.getAmount(), Integer::sum);
        }
        for (MatchApplication app : approvedApps) {
            int refundAmount = refundMap.getOrDefault(app.getApplicantUserUid(), 0);
            if (refundAmount > 0) {
                cashService.refund(app.getApplicantUserUid(), refundAmount,
                        "매치 취소 환불 (" + stadiumName + ")", matchUid, "MATCH");
            }
            notificationService.createNotification(
                    app.getApplicantUserUid(),
                    Notification.NotificationType.MATCH,
                    "매치가 취소되었습니다",
                    matchInfo + " 매치가 취소되어 참가비가 환불 처리되었습니다.",
                    matchUid, "MATCH", "/match-detail/" + matchUid);
            app.setStatus(MatchApplication.ApplicationStatus.CANCELLED);
            matchApplicationRepository.save(app);
        }

        // 게스트 신청자 환불 + 알림
        guestRecruitmentRepository.findFirstByMatchUidOrderByCreatedDateDesc(matchUid)
                .ifPresent(recruitment -> {
                    List<GuestApplication> guestApps = guestApplicationRepository.findByRecruitmentUidAndStatus(
                            recruitment.getUid(), GuestApplication.ApplicationStatus.APPROVED);
                    List<CashTransaction> guestTxs = cashTransactionRepository
                            .findByReferenceUidAndReferenceTypeAndType(
                                    recruitment.getUid(), "GUEST", CashTransaction.TransactionType.USE);
                    Map<String, Integer> guestRefundMap = new HashMap<>();
                    for (CashTransaction tx : guestTxs) {
                        guestRefundMap.merge(tx.getUserUid(), tx.getAmount(), Integer::sum);
                    }
                    for (GuestApplication gApp : guestApps) {
                        int refundAmount = guestRefundMap.getOrDefault(gApp.getUserUid(), 0);
                        if (refundAmount > 0) {
                            cashService.refund(gApp.getUserUid(), refundAmount,
                                    "매치 취소 환불 (게스트) (" + stadiumName + ")",
                                    recruitment.getUid(), "GUEST");
                        }
                        notificationService.createNotification(
                                gApp.getUserUid(),
                                Notification.NotificationType.MATCH,
                                "매치가 취소되었습니다",
                                matchInfo + " 매치가 취소되어 참가비가 환불 처리되었습니다.",
                                matchUid, "MATCH", "/match-detail/" + matchUid);
                        gApp.setStatus(GuestApplication.ApplicationStatus.CANCELLED);
                        guestApplicationRepository.save(gApp);
                    }
                });

        match.setStatus(FutsalMatch.FutsalMatchStatus.CANCELLED);
        matchRepository.save(match);
    }

    /**
     * 매치 수정 — OWNER 전용, RECRUITING 상태에서만 가능
     */
    @Transactional
    public MatchDto.DetailResponse updateMatch(String matchUid, MatchDto.UpdateRequest request, String userUid) {
        FutsalMatch match = matchRepository.findByUid(matchUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매치입니다."));
        Team hostTeam = teamRepository.findByUid(match.getHostTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        if (!hostTeam.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("매치 수정 권한이 없습니다.");
        }
        if (match.getStatus() != FutsalMatch.FutsalMatchStatus.RECRUITING) {
            throw new IllegalArgumentException("모집 중인 매치만 수정할 수 있습니다.");
        }
        if (request.getMatchDate() != null) match.setMatchDate(request.getMatchDate());
        if (request.getMatchTime() != null) match.setMatchTime(request.getMatchTime());
        if (request.getDurationHours() != null) match.setDurationHours(request.getDurationHours());
        if (request.getStadiumName() != null) match.setStadiumName(request.getStadiumName());
        if (request.getStadiumAddress() != null) match.setStadiumAddress(request.getStadiumAddress());
        if (request.getMatchFormat() != null) match.setMatchFormat(request.getMatchFormat());
        if (request.getFee() != null) match.setFee(request.getFee());
        if (request.getAdditionalInfo() != null) match.setAdditionalInfo(request.getAdditionalInfo());
        match = matchRepository.save(match);
        return toDetailResponse(match);
    }

    /**
     * 개인 매치 신청 취소 — N일 전까지만 가능, 참가비 환불
     */
    @Transactional
    public void cancelMyApplication(String matchUid, String userUid) {
        FutsalMatch match = matchRepository.findByUid(matchUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매치입니다."));

        MatchApplication application = matchApplicationRepository
                .findByMatchUidAndStatus(matchUid, MatchApplication.ApplicationStatus.APPROVED)
                .stream()
                .filter(a -> a.getApplicantUserUid().equals(userUid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("신청 내역을 찾을 수 없습니다."));

        MatchConfig config = matchConfigRepository.findByUid("default")
                .orElse(MatchConfig.builder().uid("default").cancelDaysBeforeMatch(1).build());

        LocalDate cancelDeadline = LocalDate.now().plusDays(config.getCancelDaysBeforeMatch());
        if (!match.getMatchDate().isAfter(cancelDeadline)) {
            throw new IllegalArgumentException(
                    "취소 기한이 지났습니다. 경기 " + config.getCancelDaysBeforeMatch() + "일 전까지만 취소 가능합니다.");
        }

        List<CashTransaction> useTxs = cashTransactionRepository
                .findByReferenceUidAndReferenceTypeAndType(matchUid, "MATCH", CashTransaction.TransactionType.USE);
        int refundAmount = useTxs.stream()
                .filter(tx -> tx.getUserUid().equals(userUid))
                .mapToInt(CashTransaction::getAmount)
                .sum();
        if (refundAmount > 0) {
            cashService.refund(userUid, refundAmount,
                    "매치 신청 취소 환불 (" + match.getStadiumName() + ")", matchUid, "MATCH");
        }

        application.setStatus(MatchApplication.ApplicationStatus.CANCELLED);
        matchApplicationRepository.save(application);
    }

    /**
     * 매치 설정 조회
     */
    @Transactional(readOnly = true)
    public MatchDto.ConfigResponse getMatchConfig() {
        MatchConfig config = matchConfigRepository.findByUid("default")
                .orElse(MatchConfig.builder().uid("default").cancelDaysBeforeMatch(1).build());
        return MatchDto.ConfigResponse.builder()
                .cancelDaysBeforeMatch(config.getCancelDaysBeforeMatch())
                .build();
    }

    /**
     * 매치 설정 수정 — 관리자 전용
     */
    @Transactional
    public MatchDto.ConfigResponse updateMatchConfig(MatchDto.ConfigUpdateRequest request) {
        MatchConfig config = matchConfigRepository.findByUid("default")
                .orElse(MatchConfig.builder().uid("default").cancelDaysBeforeMatch(1).build());
        config.setCancelDaysBeforeMatch(request.getCancelDaysBeforeMatch());
        config = matchConfigRepository.save(config);
        return MatchDto.ConfigResponse.builder()
                .cancelDaysBeforeMatch(config.getCancelDaysBeforeMatch())
                .build();
    }

    /**
     * 경기 종료 및 결과 입력
     */
    @Transactional
    public MatchDto.DetailResponse completeMatch(MatchDto.CompleteMatchRequest request, String userUid) {
        FutsalMatch match = matchRepository.findByUid(request.getMatchUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매치입니다."));

        Team hostTeam = teamRepository.findByUid(match.getHostTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 팀 운영자 권한 확인
        if (!hostTeam.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("경기 종료 권한이 없습니다.");
        }

        // 상태 변경
        match.setStatus(FutsalMatch.FutsalMatchStatus.COMPLETED);
        if (request.getHomeScore() != null) {
            match.setHomeScore(request.getHomeScore());
        }
        if (request.getAwayScore() != null) {
            match.setAwayScore(request.getAwayScore());
        }
        match = matchRepository.save(match);

        // 경기 참여자들에게 알림 발송 - 상대팀 매너 점수 기록 요청
        sendMannerRatingNotifications(match, hostTeam);

        return toDetailResponse(match, userUid);
    }

    /**
     * 경기 종료 후 참여자들에게 매너 점수 평가 알림 발송
     */
    private void sendMannerRatingNotifications(FutsalMatch match, Team hostTeam) {
        String matchInfo = String.format("%02d월 %02d일 %s에서 진행한",
                match.getMatchDate().getMonthValue(),
                match.getMatchDate().getDayOfMonth(),
                match.getStadiumName());

        // 홈팀 멤버들에게 알림 (상대팀 평가)
        List<TeamMember> hostMembers = teamMemberRepository.findByTeamUidAndStatus(
                match.getHostTeamUid(), TeamMember.MemberStatus.APPROVED);
        String guestTeamName = "";
        if (match.getGuestTeamUid() != null) {
            Team guestTeam = teamRepository.findByUid(match.getGuestTeamUid()).orElse(null);
            guestTeamName = guestTeam != null ? guestTeam.getName() : "상대팀";
        }

        for (TeamMember member : hostMembers) {
            notificationService.createNotification(
                    member.getUserUid(),
                    Notification.NotificationType.MATCH,
                    "상대팀의 매너 점수를 기록해 주세요!",
                    matchInfo + " " + guestTeamName + "의 매너 점수를 기록해주세요.",
                    match.getUid(),
                    "MATCH",
                    "/match-detail/" + match.getUid() + "?type=match"
            );
        }

        // 게스트 참여자들에게 알림 (홈팀 평가)
        guestRecruitmentRepository.findFirstByMatchUidOrderByCreatedDateDesc(match.getUid())
                .ifPresent(recruitment -> {
                    List<GuestApplication> guestApps = guestApplicationRepository.findByRecruitmentUidAndStatus(
                            recruitment.getUid(), GuestApplication.ApplicationStatus.APPROVED);
                    for (GuestApplication gApp : guestApps) {
                        notificationService.createNotification(
                                gApp.getUserUid(),
                                Notification.NotificationType.MATCH,
                                "상대팀의 매너 점수를 기록해 주세요!",
                                matchInfo + " " + hostTeam.getName() + "의 매너 점수를 기록해주세요.",
                                match.getUid(),
                                "MATCH",
                                "/match-detail/" + match.getUid() + "?type=match"
                        );
                    }
                });

        // 상대팀 멤버들에게 알림 (홈팀 평가)
/*         if (match.getGuestTeamUid() != null) {
            List<TeamMember> guestMembers = teamMemberRepository.findByTeamUidAndStatus(
                    match.getGuestTeamUid(), TeamMember.MemberStatus.APPROVED);
            for (TeamMember member : guestMembers) {
                notificationService.createNotification(
                        member.getUserUid(),
                        Notification.NotificationType.MATCH,
                        "상대팀의 매너 점수를 기록해 주세요!",
                        matchInfo + " " + hostTeam.getName() + "의 매너 점수를 기록해주세요.",
                        match.getUid(),
                        "MATCH",
                        "/match-detail/" + match.getUid() + "?type=match"
                );
            }
        } */
    }

    /**
     * 내 매치 신청 내역 조회
     */
    @Transactional(readOnly = true)
    public List<MatchDto.MyApplicationResponse> getMyApplications(String userUid) {
        return matchApplicationRepository.findByApplicantUserUidOrderByCreatedDateDesc(userUid)
                .stream()
                .map(app -> {
                    FutsalMatch match = matchRepository.findByUid(app.getMatchUid()).orElse(null);
                    Team hostTeam = match != null ? teamRepository.findByUid(match.getHostTeamUid()).orElse(null) : null;
                    return MatchDto.MyApplicationResponse.builder()
                            .applicationUid(app.getUid())
                            .matchUid(app.getMatchUid())
                            .hostTeamName(hostTeam != null ? hostTeam.getName() : "")
                            .stadiumName(match != null ? match.getStadiumName() : "")
                            .matchDate(match != null ? match.getMatchDate() : null)
                            .matchTime(match != null ? (match.getMatchTime() != null ? match.getMatchTime().toString() : null) : null)
                            .matchType(match != null && match.getMatchType() != null ? match.getMatchType().name() : "")
                            .matchFormat(match != null && match.getMatchFormat() != null ? match.getMatchFormat() : "")
                            .matchStatus(match != null && match.getStatus() != null ? match.getStatus().name() : "")
                            .applicationStatus(app.getStatus() != null ? app.getStatus().name() : "")
                            .appliedAt(app.getCreatedDate())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private MatchDto.DetailResponse toDetailResponse(FutsalMatch match) {
        return toDetailResponse(match, null);
    }

    private MatchDto.DetailResponse toDetailResponse(FutsalMatch match, String currentUserUid) {
        Team hostTeam = teamRepository.findByUid(match.getHostTeamUid()).orElse(null);
        Team guestTeam = match.getGuestTeamUid() != null ? 
                teamRepository.findByUid(match.getGuestTeamUid()).orElse(null) : null;

        int maxPlayers = getMaxPlayersByFormat(match.getMatchFormat());
        long currentPlayers = matchApplicationRepository.countByMatchUidAndStatus(
                match.getUid(), MatchApplication.ApplicationStatus.APPROVED);

        // 참여자 명단 조회 - 매치 신청자 (팀 vs 팀)
        List<MatchApplication> applications = matchApplicationRepository.findByMatchUidAndStatus(
                match.getUid(), MatchApplication.ApplicationStatus.APPROVED);
        List<MatchDto.ParticipantInfo> participants = new java.util.ArrayList<>(applications.stream()
                .map(app -> {
                    User user = userRepository.findById(app.getApplicantUserUid()).orElse(null);
                    Team applicantTeam = teamRepository.findByUid(app.getApplicantTeamUid()).orElse(null);
                    return MatchDto.ParticipantInfo.builder()
                            .uid(app.getApplicantUserUid())
                            .name(user != null ? user.getActualName() : "알 수 없음")
                            .teamName(applicantTeam != null ? applicantTeam.getName() : "")
                            .profileImageUrl(user != null ? user.getProfileImageUrl() : null)
                            .build();
                })
                .collect(Collectors.toList()));

        // 게스트 모집 참여자도 포함
        guestRecruitmentRepository.findFirstByMatchUidOrderByCreatedDateDesc(match.getUid())
                .ifPresent(recruitment -> {
                    List<GuestApplication> guestApps = guestApplicationRepository.findByRecruitmentUidAndStatus(
                            recruitment.getUid(), GuestApplication.ApplicationStatus.APPROVED);
                    guestApps.forEach(gApp -> {
                        User gUser = userRepository.findById(gApp.getUserUid()).orElse(null);
                        participants.add(MatchDto.ParticipantInfo.builder()
                                .uid(gApp.getUserUid())
                                .name(gUser != null ? gUser.getActualName() : "알 수 없음")
                                .teamName("게스트")
                                .profileImageUrl(gUser != null ? gUser.getProfileImageUrl() : null)
                                .build());
                    });
                });

        // 현재 사용자가 팀 운영자 또는 매니저인지 확인
        boolean isTeamOwner = false;
        if (currentUserUid != null && hostTeam != null) {
            if (hostTeam.getOwnerUid().equals(currentUserUid)) {
                isTeamOwner = true;
            } else {
                // MANAGER 역할도 경기 종료 등 관리 권한 부여
                isTeamOwner = teamMemberRepository.findByTeamUidAndUserUid(hostTeam.getUid(), currentUserUid)
                        .map(member -> member.getRole() == TeamMember.MemberRole.MANAGER
                                || member.getRole() == TeamMember.MemberRole.OWNER)
                        .orElse(false);
            }
        }

        return MatchDto.DetailResponse.builder()
                .uid(match.getUid())
                .hostTeamUid(match.getHostTeamUid())
                .hostTeamName(hostTeam != null ? hostTeam.getName() : "")
                .hostTeamLogoUrl(hostTeam != null ? hostTeam.getLogoFileUid() : null)
                .hostTeamMannerScore(hostTeam != null ? hostTeam.getMannerScore() : 0.0)
                .guestTeamUid(match.getGuestTeamUid())
                .guestTeamName(guestTeam != null ? guestTeam.getName() : "")
                .guestTeamLogoUrl(guestTeam != null ? guestTeam.getLogoFileUid() : null)
                .matchType(match.getMatchType())
                .matchFormat(match.getMatchFormat())
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .durationHours(match.getDurationHours())
                .stadiumName(match.getStadiumName())
                .stadiumAddress(match.getStadiumAddress())
                .fee(match.getFee())
                .regionSido(match.getRegionSido())
                .regionSigungu(match.getRegionSigungu())
                .additionalInfo(match.getAdditionalInfo())
                .homeScore(match.getHomeScore())
                .awayScore(match.getAwayScore())
                .status(match.getStatus())
                .maxPlayers(maxPlayers)
                .currentPlayers((int) currentPlayers)
                .participants(participants)
                .isTeamOwner(isTeamOwner)
                .guestTeamMannerScore(guestTeam != null ? guestTeam.getMannerScore() : 0.0)
                .createdDate(match.getCreatedDate())
                .build();
    }

    private MatchDto.ListResponse toListResponse(FutsalMatch match) {
        Team hostTeam = teamRepository.findByUid(match.getHostTeamUid()).orElse(null);
        String region = "";
        if (hostTeam != null) {
            region = (hostTeam.getRegionSido() != null ? hostTeam.getRegionSido() : "")
                    + " " + (hostTeam.getRegionSigungu() != null ? hostTeam.getRegionSigungu() : "");
        }

        int maxPlayers = getMaxPlayersByFormat(match.getMatchFormat());
        long currentPlayers = matchApplicationRepository.countByMatchUidAndStatus(
                match.getUid(), MatchApplication.ApplicationStatus.APPROVED);

        boolean hasGuestRecruitment = guestRecruitmentRepository
                .existsByMatchUidAndStatusIn(match.getUid(),
                        java.util.Arrays.asList(
                                com.ttwijang.cms.entity.GuestRecruitment.RecruitmentStatus.RECRUITING,
                                com.ttwijang.cms.entity.GuestRecruitment.RecruitmentStatus.COMPLETED));

        Integer guestCurrentMembers = null;
        Integer guestMaxMembers = null;
        if (hasGuestRecruitment) {
            java.util.Optional<com.ttwijang.cms.entity.GuestRecruitment> guestOpt =
                    guestRecruitmentRepository.findFirstByMatchUidOrderByCreatedDateDesc(match.getUid());
            if (guestOpt.isPresent()) {
                com.ttwijang.cms.entity.GuestRecruitment guest = guestOpt.get();
                guestCurrentMembers = (int) guestApplicationRepository.countByRecruitmentUidAndStatus(
                        guest.getUid(), GuestApplication.ApplicationStatus.APPROVED);
                guestMaxMembers = guest.getMaxGuests();
            }
        }

        int teamMemberCount = (int) currentPlayers;

        return MatchDto.ListResponse.builder()
                .uid(match.getUid())
                .hostTeamUid(match.getHostTeamUid())
                .hostTeamName(hostTeam != null ? hostTeam.getName() : "")
                .hostTeamLogoUrl(hostTeam != null ? hostTeam.getLogoFileUid() : null)
                .hostTeamMannerScore(hostTeam != null ? hostTeam.getMannerScore() : 0.0)
                .matchType(match.getMatchType())
                .matchFormat(match.getMatchFormat())
                .matchDate(match.getMatchDate())
                .matchTime(match.getMatchTime())
                .stadiumName(match.getStadiumName())
                .region(region.trim())
                .fee(match.getFee())
                .status(match.getStatus())
                .maxPlayers(maxPlayers)
                .currentPlayers((int) currentPlayers)
                .hasGuestRecruitment(hasGuestRecruitment)
                .guestCurrentMembers(guestCurrentMembers)
                .guestMaxMembers(guestMaxMembers)
                .teamMemberCount(teamMemberCount)
                .build();
    }
}

package com.ttwijang.cms.api.guest.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.cash.dto.CashDto;
import com.ttwijang.cms.api.cash.service.CashService;
import com.ttwijang.cms.api.guest.dto.GuestDto;
import com.ttwijang.cms.api.guest.repository.GuestApplicationRepository;
import com.ttwijang.cms.api.guest.repository.GuestRecruitmentRepository;
import com.ttwijang.cms.api.match.repository.MatchApplicationRepository;
import com.ttwijang.cms.api.match.service.MatchService;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.api.team.repository.TeamMemberRepository;
import com.ttwijang.cms.entity.FutsalMatch;
import com.ttwijang.cms.entity.GuestApplication;
import com.ttwijang.cms.entity.GuestRecruitment;
import com.ttwijang.cms.entity.MatchApplication;
import com.ttwijang.cms.entity.Team;
import com.ttwijang.cms.entity.TeamMember;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRecruitmentRepository recruitmentRepository;
    private final GuestApplicationRepository applicationRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MatchApplicationRepository matchApplicationRepository;
    private final UserRepository userRepository;
    private final CashService cashService;

    private static final int MAX_RECRUITMENT_DAYS = 7;

    /**
     * 게스트 모집 생성
     */
    @Transactional
    public GuestDto.DetailResponse createRecruitment(GuestDto.CreateRequest request, String userUid) {
        Team team = teamRepository.findByUid(request.getTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 팀 운영자 권한 확인
        if (!team.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("게스트 모집 권한이 없습니다.");
        }

        // 7일 이내 경기만 모집 가능
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(MAX_RECRUITMENT_DAYS);
        if (request.getMatchDate().isBefore(today) || request.getMatchDate().isAfter(maxDate)) {
            throw new IllegalArgumentException("게스트 모집은 오늘부터 7일 이내의 경기만 가능합니다.");
        }

        // 중복 모집 방지: 같은 팀이 같은 매치(또는 같은 날짜)에 이미 모집 중인지 확인
        java.util.List<GuestRecruitment.RecruitmentStatus> activeStatuses =
                java.util.Arrays.asList(GuestRecruitment.RecruitmentStatus.RECRUITING, GuestRecruitment.RecruitmentStatus.COMPLETED);
        if (request.getMatchUid() != null && !request.getMatchUid().isEmpty()) {
            if (recruitmentRepository.existsByTeamUidAndMatchUidAndStatusIn(
                    request.getTeamUid(), request.getMatchUid(), activeStatuses)) {
                throw new IllegalArgumentException("해당 매치에 이미 게스트 모집이 등록되어 있습니다.");
            }
        } else {
            if (recruitmentRepository.existsByTeamUidAndMatchDateAndStatusIn(
                    request.getTeamUid(), request.getMatchDate(), activeStatuses)) {
                throw new IllegalArgumentException("해당 날짜에 이미 게스트 모집이 등록되어 있습니다.");
            }
        }

        GuestRecruitment recruitment = GuestRecruitment.builder()
                .teamUid(request.getTeamUid())
                .matchUid(request.getMatchUid())
                .matchDate(request.getMatchDate())
                .matchTime(request.getMatchTime())
                .stadiumName(request.getStadiumName())
                .stadiumAddress(request.getStadiumAddress())
                .regionSido(request.getRegionSido())
                .regionSigungu(request.getRegionSigungu())
                .genderType(request.getGenderType())
                .ageGroups(request.getAgeGroups())
                .positionType(request.getPositionType())
                .maxGuests(request.getMaxGuests())
                .currentGuests(0)
                .fee(request.getFee() != null ? request.getFee() : 0)
                .guaranteedMinutes(request.getGuaranteedMinutes())
                .additionalInfo(request.getAdditionalInfo())
                .status(GuestRecruitment.RecruitmentStatus.RECRUITING)
                .build();

        recruitment = recruitmentRepository.save(recruitment);
        return toDetailResponse(recruitment);
    }

    /**
     * 게스트 모집 상세 조회
     */
    @Transactional(readOnly = true)
    public GuestDto.DetailResponse getRecruitmentDetail(String uid) {
        return getRecruitmentDetail(uid, null);
    }

    /**
     * 게스트 모집 상세 조회 (로그인 사용자 신청 여부 포함)
     */
    @Transactional(readOnly = true)
    public GuestDto.DetailResponse getRecruitmentDetail(String uid, String userUid) {
        GuestRecruitment recruitment = recruitmentRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게스트 모집입니다."));
        GuestDto.DetailResponse response = toDetailResponse(recruitment);
        if (userUid != null && !userUid.isEmpty()) {
            response.setAlreadyApplied(
                    applicationRepository.existsByRecruitmentUidAndUserUid(recruitment.getUid(), userUid));
        } else {
            response.setAlreadyApplied(false);
        }
        return response;
    }

    /**
     * 게스트 모집 목록 조회
     */
    @Transactional(readOnly = true)
    public Page<GuestDto.ListResponse> getRecruitmentList(String region, LocalDate date,
            GuestRecruitment.RecruitmentStatus status, Pageable pageable) {
        Page<GuestRecruitment> recruitments;

        if (date != null) {
            recruitments = recruitmentRepository.findByMatchDate(date, pageable);
        } else if (status != null && region != null && !region.isEmpty()) {
            String[] parts = region.split(" ", 2);
            if (parts.length == 2) {
                recruitments = recruitmentRepository.findByStatusAndRegionSidoAndSigungu(status, parts[0], parts[1], pageable);
            } else {
                recruitments = recruitmentRepository.findByStatusAndRegion(status, region, pageable);
            }
        } else if (status != null) {
            recruitments = recruitmentRepository.findByStatus(status, pageable);
        } else {
            recruitments = recruitmentRepository.findAll(pageable);
        }

        return recruitments.map(this::toListResponse);
    }

    /**
     * 게스트 모집 목록 조회 - 시/군/구 이름으로만 조회 (도 필터 없이, regionCode 사용 시)
     */
    @Transactional(readOnly = true)
    public Page<GuestDto.ListResponse> getRecruitmentListBySigungu(String sigungu, LocalDate date,
            GuestRecruitment.RecruitmentStatus status, Pageable pageable) {
        Page<GuestRecruitment> recruitments;
        if (date != null) {
            recruitments = recruitmentRepository.findByMatchDate(date, pageable);
        } else if (status != null && sigungu != null && !sigungu.isEmpty()) {
            recruitments = recruitmentRepository.findByStatusAndSigungu(status, sigungu, pageable);
        } else if (status != null) {
            recruitments = recruitmentRepository.findByStatus(status, pageable);
        } else {
            recruitments = recruitmentRepository.findAll(pageable);
        }
        return recruitments.map(this::toListResponse);
    }

    /**
     * 날짜 범위별 게스트 모집 조회 (캘린더용) - 지역 필터 지원
     */
    @Transactional(readOnly = true)
    public Page<GuestDto.ListResponse> getRecruitmentsByDateRange(LocalDate startDate, LocalDate endDate,
            String region, Pageable pageable) {
        Page<GuestRecruitment> recruitments;
        if (region != null && !region.isEmpty()) {
            String[] parts = region.split(" ", 2);
            if (parts.length == 2) {
                recruitments = recruitmentRepository.findByMatchDateBetweenAndRegionSidoAndRegionSigungu(
                        startDate, endDate, parts[0], parts[1], pageable);
            } else {
                recruitments = recruitmentRepository.findByMatchDateBetween(startDate, endDate, pageable);
            }
        } else {
            recruitments = recruitmentRepository.findByMatchDateBetween(startDate, endDate, pageable);
        }
        return recruitments.map(this::toListResponse);
    }

    /**
     * 날짜 범위별 게스트 모집 조회 - 시/군/구 이름으로만 조회 (도 필터 없이, regionCode 사용 시)
     */
    @Transactional(readOnly = true)
    public Page<GuestDto.ListResponse> getRecruitmentsByDateRangeBySigungu(LocalDate startDate, LocalDate endDate,
            String sigungu, Pageable pageable) {
        Page<GuestRecruitment> recruitments;
        if (sigungu != null && !sigungu.isEmpty()) {
            recruitments = recruitmentRepository.findByMatchDateBetweenAndRegionSigungu(
                    startDate, endDate, sigungu, pageable);
        } else {
            recruitments = recruitmentRepository.findByMatchDateBetween(startDate, endDate, pageable);
        }
        return recruitments.map(this::toListResponse);
    }

    /**
     * 게스트 신청
     */
    @Transactional
    public GuestDto.ApplicationResponse applyAsGuest(GuestDto.ApplyRequest request, String userUid) {
        GuestRecruitment recruitment = recruitmentRepository.findByUid(request.getRecruitmentUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게스트 모집입니다."));

        if (recruitment.getStatus() != GuestRecruitment.RecruitmentStatus.RECRUITING) {
            throw new IllegalArgumentException("모집 중인 게스트가 아닙니다.");
        }

        // 이미 신청했는지 확인
        if (applicationRepository.existsByRecruitmentUidAndUserUid(request.getRecruitmentUid(), userUid)) {
            throw new IllegalArgumentException("이미 신청하셨습니다.");
        }

        // 모집 마감 확인
        if (recruitment.getCurrentGuests() >= recruitment.getMaxGuests()) {
            throw new IllegalArgumentException("모집이 마감되었습니다.");
        }

        // 자기 소속 팀 또는 자기가 만든 팀의 게스트 모집에는 신청 불가
        String recruitTeamUid = recruitment.getTeamUid();
        if (recruitTeamUid != null) {
            // 팀 소유자(운영자) 확인
            Team recruitTeam = teamRepository.findByUid(recruitTeamUid).orElse(null);
            if (recruitTeam != null && recruitTeam.getOwnerUid().equals(userUid)) {
                throw new IllegalArgumentException("본인이 운영하는 팀의 게스트 모집에는 신청할 수 없습니다.");
            }
            // 팀 소속 멤버 확인 (승인된 멤버)
            if (teamMemberRepository.existsByTeamUidAndUserUidAndStatus(
                    recruitTeamUid, userUid, TeamMember.MemberStatus.APPROVED)) {
                throw new IllegalArgumentException("소속 팀의 게스트 모집에는 신청할 수 없습니다.");
            }
        }

        GuestApplication application = GuestApplication.builder()
                .recruitmentUid(request.getRecruitmentUid())
                .userUid(userUid)
                .position(request.getPosition())
                .age(request.getAge())
                .message(request.getMessage())
                .status(GuestApplication.ApplicationStatus.APPROVED)
                .paymentCompleted(false)
                .build();

        application = applicationRepository.save(application);

        // 즉시 승인이므로 currentGuests 증가
        recruitment.setCurrentGuests(recruitment.getCurrentGuests() + 1);
        if (recruitment.getCurrentGuests() >= recruitment.getMaxGuests()) {
            recruitment.setStatus(GuestRecruitment.RecruitmentStatus.COMPLETED);
        }
        recruitmentRepository.save(recruitment);

        // 참가비가 있으면 캐시 차감
        int fee = recruitment.getFee() != null ? recruitment.getFee() : 0;
        if (fee > 0) {
            Team recruitTeam = teamRepository.findByUid(recruitment.getTeamUid()).orElse(null);
            String recruitTeamName = recruitTeam != null ? recruitTeam.getName() : "";
            CashDto.UseRequest useRequest = CashDto.UseRequest.builder()
                    .amount(fee)
                    .description("게스트 참가비 (" + recruitTeamName + " / " + recruitment.getStadiumName() + ")")
                    .referenceUid(recruitment.getUid())
                    .build();
            cashService.use(useRequest, userUid);
        }

        return toApplicationResponse(application);
    }

    /**
     * 게스트 신청 처리
     */
    @Transactional
    public GuestDto.ApplicationResponse processApplication(GuestDto.ProcessRequest request, String ownerUid) {
        GuestApplication application = applicationRepository.findByUid(request.getApplicationUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청입니다."));

        GuestRecruitment recruitment = recruitmentRepository.findByUid(application.getRecruitmentUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게스트 모집입니다."));

        Team team = teamRepository.findByUid(recruitment.getTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 운영자 권한 확인
        if (!team.getOwnerUid().equals(ownerUid)) {
            throw new IllegalArgumentException("신청 처리 권한이 없습니다.");
        }

        if (request.getApproved()) {
            application.setStatus(GuestApplication.ApplicationStatus.APPROVED);
            recruitment.setCurrentGuests(recruitment.getCurrentGuests() + 1);

            // 모집 완료 확인
            if (recruitment.getCurrentGuests() >= recruitment.getMaxGuests()) {
                recruitment.setStatus(GuestRecruitment.RecruitmentStatus.COMPLETED);
            }
            recruitmentRepository.save(recruitment);
        } else {
            application.setStatus(GuestApplication.ApplicationStatus.REJECTED);
        }

        application = applicationRepository.save(application);
        return toApplicationResponse(application);
    }

    /**
     * 내 게스트 신청 목록 조회
     */
    @Transactional(readOnly = true)
    public List<GuestDto.ApplicationResponse> getMyApplications(String userUid) {
        return applicationRepository.findByUserUid(userUid).stream()
                .map(this::toApplicationResponse)
                .collect(Collectors.toList());
    }

    /**
     * 모집의 신청자 목록 조회
     */
    @Transactional(readOnly = true)
    public List<GuestDto.ApplicationResponse> getRecruitmentApplications(String recruitmentUid, String ownerUid) {
        GuestRecruitment recruitment = recruitmentRepository.findByUid(recruitmentUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게스트 모집입니다."));

        Team team = teamRepository.findByUid(recruitment.getTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        if (!team.getOwnerUid().equals(ownerUid)) {
            throw new IllegalArgumentException("조회 권한이 없습니다.");
        }

        return applicationRepository.findByRecruitmentUid(recruitmentUid).stream()
                .map(this::toApplicationResponse)
                .collect(Collectors.toList());
    }

    /**
     * 게스트 모집 취소
     */
    @Transactional
    public void cancelRecruitment(String recruitmentUid, String userUid) {
        GuestRecruitment recruitment = recruitmentRepository.findByUid(recruitmentUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게스트 모집입니다."));

        Team team = teamRepository.findByUid(recruitment.getTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        if (!team.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("게스트 모집 취소 권한이 없습니다.");
        }

        recruitment.setStatus(GuestRecruitment.RecruitmentStatus.CANCELLED);
        recruitmentRepository.save(recruitment);
    }

    /**
     * 팀의 게스트 모집 목록 조회 (7일 이내)
     */
    @Transactional(readOnly = true)
    public List<GuestDto.ListResponse> getTeamRecruitments(String teamUid) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(MAX_RECRUITMENT_DAYS);
        List<GuestRecruitment> recruitments = recruitmentRepository.findByTeamUidAndDateRange(teamUid, startDate, endDate);
        return recruitments.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    private GuestDto.DetailResponse toDetailResponse(GuestRecruitment recruitment) {
        Team team = teamRepository.findByUid(recruitment.getTeamUid()).orElse(null);

        // 참여자 명단 조회 - 매치 신청자 (팀 vs 팀) 먼저 추가
        List<GuestDto.ParticipantInfo> participants = new java.util.ArrayList<>();
        if (recruitment.getMatchUid() != null) {
            List<MatchApplication> matchApps = matchApplicationRepository.findByMatchUidAndStatus(
                    recruitment.getMatchUid(), MatchApplication.ApplicationStatus.APPROVED);
            matchApps.forEach(mApp -> {
                User mUser = userRepository.findById(mApp.getApplicantUserUid()).orElse(null);
                Team mTeam = teamRepository.findByUid(mApp.getApplicantTeamUid()).orElse(null);
                participants.add(GuestDto.ParticipantInfo.builder()
                        .uid(mApp.getApplicantUserUid())
                        .name(mUser != null ? mUser.getActualName() : "알 수 없음")
                        .teamName(mTeam != null ? mTeam.getName() : "")
                        .build());
            });
        }

        // 게스트 신청자 추가
        List<GuestApplication> approvedApps = applicationRepository.findByRecruitmentUidAndStatus(
                recruitment.getUid(), GuestApplication.ApplicationStatus.APPROVED);
        approvedApps.forEach(app -> {
            User user = userRepository.findById(app.getUserUid()).orElse(null);
            participants.add(GuestDto.ParticipantInfo.builder()
                    .uid(app.getUserUid())
                    .name(user != null ? user.getActualName() : "알 수 없음")
                    .teamName("게스트")
                    .build());
        });

        return GuestDto.DetailResponse.builder()
                .uid(recruitment.getUid())
                .teamUid(recruitment.getTeamUid())
                .teamName(team != null ? team.getName() : "")
                .teamMannerScore(team != null ? team.getMannerScore() : 0.0)
                .matchUid(recruitment.getMatchUid())
                .matchDate(recruitment.getMatchDate())
                .matchTime(recruitment.getMatchTime())
                .stadiumName(recruitment.getStadiumName())
                .stadiumAddress(recruitment.getStadiumAddress())
                .regionSido(recruitment.getRegionSido())
                .regionSigungu(recruitment.getRegionSigungu())
                .genderType(recruitment.getGenderType())
                .ageGroups(recruitment.getAgeGroups())
                .positionType(recruitment.getPositionType())
                .maxGuests(recruitment.getMaxGuests())
                .currentGuests(recruitment.getCurrentGuests())
                .fee(recruitment.getFee())
                .guaranteedMinutes(recruitment.getGuaranteedMinutes())
                .additionalInfo(recruitment.getAdditionalInfo())
                .status(recruitment.getStatus())
                .participants(participants)
                .createdDate(recruitment.getCreatedDate())
                .build();
    }

    private GuestDto.ListResponse toListResponse(GuestRecruitment recruitment) {
        Team team = teamRepository.findByUid(recruitment.getTeamUid()).orElse(null);
        String region = "";
        if (team != null) {
            region = (team.getRegionSido() != null ? team.getRegionSido() : "")
                    + " " + (team.getRegionSigungu() != null ? team.getRegionSigungu() : "");
        }

        // 매치 포맷 및 팀 참여 인원 계산
        String matchFormat = null;
        int teamMemberCount = 0;
        int maxPlayers = 0;
        if (recruitment.getMatchUid() != null) {
            teamMemberCount = (int) matchApplicationRepository.countByMatchUidAndStatus(
                    recruitment.getMatchUid(),
                    MatchApplication.ApplicationStatus.APPROVED);
            FutsalMatch match = recruitment.getMatch();
            if (match != null) {
                matchFormat = match.getMatchFormat();
                maxPlayers = MatchService.getMaxPlayersByFormat(matchFormat);
            }
        }

        int actualCurrentGuests = (int) applicationRepository.countByRecruitmentUidAndStatus(
                recruitment.getUid(), GuestApplication.ApplicationStatus.APPROVED);

        return GuestDto.ListResponse.builder()
                .uid(recruitment.getUid())
                .teamUid(recruitment.getTeamUid())
                .teamName(team != null ? team.getName() : "")
                .teamLogoUrl(team != null ? team.getLogoFileUid() : null)
                .teamMannerScore(team != null ? team.getMannerScore() : 0.0)
                .matchDate(recruitment.getMatchDate())
                .matchTime(recruitment.getMatchTime())
                .stadiumName(recruitment.getStadiumName())
                .region(region.trim())
                .positionType(recruitment.getPositionType())
                .maxGuests(recruitment.getMaxGuests())
                .currentGuests(actualCurrentGuests)
                .fee(recruitment.getFee())
                .guaranteedMinutes(recruitment.getGuaranteedMinutes())
                .matchFormat(matchFormat)
                .maxPlayers(maxPlayers)
                .teamMemberCount(teamMemberCount)
                .status(recruitment.getStatus())
                .build();
    }

    private GuestDto.ApplicationResponse toApplicationResponse(GuestApplication application) {
        return GuestDto.ApplicationResponse.builder()
                .uid(application.getUid())
                .recruitmentUid(application.getRecruitmentUid())
                .userUid(application.getUserUid())
                .position(application.getPosition())
                .age(application.getAge())
                .status(application.getStatus())
                .paymentCompleted(application.getPaymentCompleted())
                .createdDate(application.getCreatedDate())
                .build();
    }

    private int getMaxPerSide(String matchFormat) {
        if (matchFormat == null) return 5;
        switch (matchFormat) {
            case "FOUR_VS_FOUR": return 4;
            case "FIVE_VS_FIVE": return 5;
            case "SIX_VS_SIX": return 6;
            case "SEVEN_VS_SEVEN": return 7;
            default: return 5;
        }
    }
}

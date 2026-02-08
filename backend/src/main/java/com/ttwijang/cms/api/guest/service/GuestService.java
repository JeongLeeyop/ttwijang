package com.ttwijang.cms.api.guest.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.guest.dto.GuestDto;
import com.ttwijang.cms.api.guest.repository.GuestApplicationRepository;
import com.ttwijang.cms.api.guest.repository.GuestRecruitmentRepository;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.entity.GuestApplication;
import com.ttwijang.cms.entity.GuestRecruitment;
import com.ttwijang.cms.entity.Team;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRecruitmentRepository recruitmentRepository;
    private final GuestApplicationRepository applicationRepository;
    private final TeamRepository teamRepository;

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
        GuestRecruitment recruitment = recruitmentRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게스트 모집입니다."));
        return toDetailResponse(recruitment);
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

        GuestApplication application = GuestApplication.builder()
                .recruitmentUid(request.getRecruitmentUid())
                .userUid(userUid)
                .position(request.getPosition())
                .age(request.getAge())
                .message(request.getMessage())
                .status(GuestApplication.ApplicationStatus.PENDING)
                .paymentCompleted(false)
                .build();

        application = applicationRepository.save(application);
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

    private GuestDto.DetailResponse toDetailResponse(GuestRecruitment recruitment) {
        Team team = teamRepository.findByUid(recruitment.getTeamUid()).orElse(null);

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
                .createdDate(recruitment.getCreatedDate())
                .build();
    }

    private GuestDto.ListResponse toListResponse(GuestRecruitment recruitment) {
        Team team = teamRepository.findByUid(recruitment.getTeamUid()).orElse(null);

        return GuestDto.ListResponse.builder()
                .uid(recruitment.getUid())
                .teamUid(recruitment.getTeamUid())
                .teamName(team != null ? team.getName() : "")
                .matchDate(recruitment.getMatchDate())
                .matchTime(recruitment.getMatchTime())
                .stadiumName(recruitment.getStadiumName())
                .region(recruitment.getRegionSido() + " " + (recruitment.getRegionSigungu() != null ? recruitment.getRegionSigungu() : ""))
                .positionType(recruitment.getPositionType())
                .maxGuests(recruitment.getMaxGuests())
                .currentGuests(recruitment.getCurrentGuests())
                .fee(recruitment.getFee())
                .guaranteedMinutes(recruitment.getGuaranteedMinutes())
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
}

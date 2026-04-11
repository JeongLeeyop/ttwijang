package com.ttwijang.cms.api.sponsor.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.sponsor.dto.SponsorDto;
import com.ttwijang.cms.api.sponsor.repository.SponsorSettingRepository;
import com.ttwijang.cms.api.sponsor.repository.TeamSponsorBannerRepository;
import com.ttwijang.cms.entity.SponsorSetting;
import com.ttwijang.cms.entity.TeamSponsorBanner;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SponsorService {

    private static final String DEFAULT_SETTING_UID = "default";

    private final SponsorSettingRepository sponsorSettingRepository;
    private final TeamSponsorBannerRepository teamSponsorBannerRepository;

    // ── 구단주 금액 설정 ──────────────────────────────────

    /**
     * 구단주 신청 금액 조회
     */
    @Transactional(readOnly = true)
    public SponsorDto.FeeResponse getSponsorFee() {
        SponsorSetting setting = sponsorSettingRepository.findByUid(DEFAULT_SETTING_UID)
                .orElse(SponsorSetting.builder()
                        .uid(DEFAULT_SETTING_UID)
                        .ownerAmount(0)
                        .build());
        return SponsorDto.FeeResponse.builder()
                .amount(setting.getOwnerAmount())
                .updatedDate(setting.getUpdatedDate())
                .build();
    }

    /**
     * 구단주 신청 금액 설정
     */
    @Transactional
    public SponsorDto.FeeResponse updateSponsorFee(SponsorDto.FeeRequest request) {
        if (request.getAmount() == null || request.getAmount() < 0) {
            throw new IllegalArgumentException("금액은 0 이상이어야 합니다.");
        }
        SponsorSetting setting = sponsorSettingRepository.findByUid(DEFAULT_SETTING_UID)
                .orElse(SponsorSetting.builder()
                        .uid(DEFAULT_SETTING_UID)
                        .ownerAmount(0)
                        .build());
        setting.setOwnerAmount(request.getAmount());
        setting = sponsorSettingRepository.save(setting);
        return SponsorDto.FeeResponse.builder()
                .amount(setting.getOwnerAmount())
                .updatedDate(setting.getUpdatedDate())
                .build();
    }

    // ── 팀별 후원 배너 ────────────────────────────────────

    /**
     * 팀별 배너 목록 조회 (관리자용 전체 목록)
     */
    @Transactional(readOnly = true)
    public Page<SponsorDto.TeamBannerResponse> getTeamBannerList(Pageable pageable) {
        return teamSponsorBannerRepository.findAll(pageable)
                .map(this::toTeamBannerResponse);
    }

    /**
     * 특정 팀의 배너 조회 (사용자용 — teamUid 기준)
     */
    @Transactional(readOnly = true)
    public List<SponsorDto.TeamBannerResponse> getTeamBannersByTeamUid(String teamUid) {
        return teamSponsorBannerRepository.findByTeamUid(teamUid).stream()
                .map(this::toTeamBannerResponse)
                .collect(Collectors.toList());
    }

    /**
     * 팀 배너 등록
     */
    @Transactional
    public SponsorDto.TeamBannerResponse createTeamBanner(SponsorDto.TeamBannerCreateRequest request) {
        TeamSponsorBanner banner = TeamSponsorBanner.builder()
                .teamUid(request.getTeamUid())
                .teamName(request.getTeamName())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .build();
        banner = teamSponsorBannerRepository.save(banner);
        return toTeamBannerResponse(banner);
    }

    /**
     * 팀 배너 수정
     */
    @Transactional
    public SponsorDto.TeamBannerResponse updateTeamBanner(String uid, SponsorDto.TeamBannerUpdateRequest request) {
        TeamSponsorBanner banner = teamSponsorBannerRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배너입니다."));
        if (request.getTeamUid() != null) banner.setTeamUid(request.getTeamUid());
        if (request.getTeamName() != null) banner.setTeamName(request.getTeamName());
        if (request.getImageUrl() != null) banner.setImageUrl(request.getImageUrl());
        if (request.getDescription() != null) banner.setDescription(request.getDescription());
        banner = teamSponsorBannerRepository.save(banner);
        return toTeamBannerResponse(banner);
    }

    /**
     * 팀 배너 삭제
     */
    @Transactional
    public void deleteTeamBanner(String uid) {
        TeamSponsorBanner banner = teamSponsorBannerRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배너입니다."));
        teamSponsorBannerRepository.delete(banner);
    }

    private SponsorDto.TeamBannerResponse toTeamBannerResponse(TeamSponsorBanner banner) {
        return SponsorDto.TeamBannerResponse.builder()
                .uid(banner.getUid())
                .teamUid(banner.getTeamUid())
                .teamName(banner.getTeamName())
                .imageUrl(banner.getImageUrl())
                .description(banner.getDescription())
                .createdDate(banner.getCreatedDate())
                .updatedDate(banner.getUpdatedDate())
                .build();
    }
}

package com.ttwijang.cms.api.banner.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.banner.dto.BannerDto;
import com.ttwijang.cms.api.banner.repository.BannerRepository;
import com.ttwijang.cms.entity.Banner;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    /**
     * 배너 생성
     */
    @Transactional
    public BannerDto.DetailResponse createBanner(BannerDto.CreateRequest request) {
        Banner banner = Banner.builder()
                .title(request.getTitle())
                .imageUrl(request.getImageUrl())
                .linkUrl(request.getLinkUrl())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .status(request.getStatus() != null ? request.getStatus() : Banner.BannerStatus.INACTIVE)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .regionSido(request.getRegionSido())
                .regionSigungu(request.getRegionSigungu())
                .targetPage(request.getTargetPage() != null ? request.getTargetPage() : Banner.TargetPage.ALL)
                .build();

        banner = bannerRepository.save(banner);
        return toDetailResponse(banner);
    }

    /**
     * 배너 수정
     */
    @Transactional
    public BannerDto.DetailResponse updateBanner(String uid, BannerDto.UpdateRequest request) {
        Banner banner = bannerRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배너입니다."));

        if (request.getTitle() != null) {
            banner.setTitle(request.getTitle());
        }
        if (request.getImageUrl() != null) {
            banner.setImageUrl(request.getImageUrl());
        }
        if (request.getLinkUrl() != null) {
            banner.setLinkUrl(request.getLinkUrl());
        }
        if (request.getDisplayOrder() != null) {
            banner.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getStatus() != null) {
            banner.setStatus(request.getStatus());
        }
        if (request.getStartDate() != null) {
            banner.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            banner.setEndDate(request.getEndDate());
        }
        if (request.getRegionSido() != null) {
            banner.setRegionSido(request.getRegionSido());
        }
        if (request.getRegionSigungu() != null) {
            banner.setRegionSigungu(request.getRegionSigungu());
        }
        if (request.getTargetPage() != null) {
            banner.setTargetPage(request.getTargetPage());
        }
        banner.setUpdatedDate(LocalDateTime.now());

        banner = bannerRepository.save(banner);
        return toDetailResponse(banner);
    }

    /**
     * 배너 삭제
     */
    @Transactional
    public void deleteBanner(String uid) {
        Banner banner = bannerRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배너입니다."));
        bannerRepository.delete(banner);
    }

    /**
     * 배너 상세 조회
     */
    @Transactional(readOnly = true)
    public BannerDto.DetailResponse getBannerDetail(String uid) {
        Banner banner = bannerRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배너입니다."));
        return toDetailResponse(banner);
    }

    /**
     * 배너 목록 조회 (관리자용)
     */
    @Transactional(readOnly = true)
    public Page<BannerDto.ListResponse> getBannerList(Banner.BannerStatus status, Pageable pageable) {
        Page<Banner> banners;
        if (status != null) {
            banners = bannerRepository.findByStatus(status, pageable);
        } else {
            banners = bannerRepository.findAll(pageable);
        }
        return banners.map(this::toListResponse);
    }

    /**
     * 활성화된 배너 조회 (사용자용 - 타겟 페이지별)
     */
    @Transactional(readOnly = true)
    public List<BannerDto.ListResponse> getActiveBanners(Banner.TargetPage targetPage, String sigungu) {
        LocalDate today = LocalDate.now();
        List<Banner> banners;
        
        if (sigungu != null && !sigungu.isEmpty()) {
            banners = bannerRepository.findActiveBannersByPageAndRegion(today, targetPage, sigungu);
        } else {
            banners = bannerRepository.findActiveBannersByPage(today, targetPage);
        }
        
        return banners.stream()
                .map(this::toListResponse)
                .collect(Collectors.toList());
    }

    private BannerDto.DetailResponse toDetailResponse(Banner banner) {
        return BannerDto.DetailResponse.builder()
                .uid(banner.getUid())
                .title(banner.getTitle())
                .imageUrl(banner.getImageUrl())
                .linkUrl(banner.getLinkUrl())
                .displayOrder(banner.getDisplayOrder())
                .status(banner.getStatus())
                .startDate(banner.getStartDate())
                .endDate(banner.getEndDate())
                .regionSido(banner.getRegionSido())
                .regionSigungu(banner.getRegionSigungu())
                .targetPage(banner.getTargetPage())
                .createdDate(banner.getCreatedDate())
                .updatedDate(banner.getUpdatedDate())
                .build();
    }

    private BannerDto.ListResponse toListResponse(Banner banner) {
        return BannerDto.ListResponse.builder()
                .uid(banner.getUid())
                .title(banner.getTitle())
                .imageUrl(banner.getImageUrl())
                .linkUrl(banner.getLinkUrl())
                .displayOrder(banner.getDisplayOrder())
                .status(banner.getStatus())
                .startDate(banner.getStartDate())
                .endDate(banner.getEndDate())
                .regionSido(banner.getRegionSido())
                .regionSigungu(banner.getRegionSigungu())
                .targetPage(banner.getTargetPage())
                .build();
    }
}

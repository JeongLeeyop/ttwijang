package com.ttwijang.cms.api.banner.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.banner.dto.BannerDto;
import com.ttwijang.cms.api.banner.service.BannerService;
import com.ttwijang.cms.entity.Banner;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Banner", description = "배너 관리 API")
@RestController
@RequestMapping("/api/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @Operation(summary = "배너 생성", description = "새로운 배너를 생성합니다. (관리자)")
    @PostMapping
    public ResponseEntity<BannerDto.DetailResponse> createBanner(@RequestBody BannerDto.CreateRequest request) {
        return ResponseEntity.ok(bannerService.createBanner(request));
    }

    @Operation(summary = "배너 수정", description = "배너 정보를 수정합니다. (관리자)")
    @PutMapping("/{uid}")
    public ResponseEntity<BannerDto.DetailResponse> updateBanner(
            @PathVariable String uid,
            @RequestBody BannerDto.UpdateRequest request) {
        return ResponseEntity.ok(bannerService.updateBanner(uid, request));
    }

    @Operation(summary = "배너 삭제", description = "배너를 삭제합니다. (관리자)")
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteBanner(@PathVariable String uid) {
        bannerService.deleteBanner(uid);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "배너 상세 조회", description = "특정 배너의 상세 정보를 조회합니다.")
    @GetMapping("/{uid}")
    public ResponseEntity<BannerDto.DetailResponse> getBannerDetail(@PathVariable String uid) {
        return ResponseEntity.ok(bannerService.getBannerDetail(uid));
    }

    @Operation(summary = "배너 목록 조회", description = "배너 목록을 조회합니다. (관리자용)")
    @GetMapping
    public ResponseEntity<Page<BannerDto.ListResponse>> getBannerList(
            @RequestParam(required = false) Banner.BannerStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("displayOrder").ascending());
        return ResponseEntity.ok(bannerService.getBannerList(status, pageable));
    }

    @Operation(summary = "활성화된 배너 조회", description = "특정 페이지의 활성화된 배너를 조회합니다. (사용자용)")
    @GetMapping("/active")
    public ResponseEntity<List<BannerDto.ListResponse>> getActiveBanners(
            @RequestParam(required = false) Banner.TargetPage targetPage,
            @RequestParam(required = false) String regionSigungu) {
        Banner.TargetPage page = targetPage != null ? targetPage : Banner.TargetPage.ALL;
        return ResponseEntity.ok(bannerService.getActiveBanners(page, regionSigungu));
    }
}

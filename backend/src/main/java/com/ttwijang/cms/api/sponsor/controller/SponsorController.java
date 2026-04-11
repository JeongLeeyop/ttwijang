package com.ttwijang.cms.api.sponsor.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.sponsor.dto.SponsorDto;
import com.ttwijang.cms.api.sponsor.service.SponsorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Sponsor Admin", description = "구단주 관리 API (최고관리자 전용)")
@RestController
@RequestMapping("/api/admin/sponsor")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class SponsorController {

    private final SponsorService sponsorService;

    // ── 구단주 신청 금액 ──────────────────────────────────

    @Operation(summary = "구단주 신청 금액 조회")
    @GetMapping("/fee")
    public ResponseEntity<SponsorDto.FeeResponse> getSponsorFee() {
        return ResponseEntity.ok(sponsorService.getSponsorFee());
    }

    @Operation(summary = "구단주 신청 금액 설정")
    @PutMapping("/fee")
    public ResponseEntity<SponsorDto.FeeResponse> updateSponsorFee(
            @RequestBody SponsorDto.FeeRequest request) {
        return ResponseEntity.ok(sponsorService.updateSponsorFee(request));
    }

    // ── 팀별 후원 배너 ────────────────────────────────────

    @Operation(summary = "팀별 배너 목록 조회 (관리자)")
    @GetMapping("/team-banners")
    public ResponseEntity<Page<SponsorDto.TeamBannerResponse>> getTeamBannerList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return ResponseEntity.ok(sponsorService.getTeamBannerList(pageable));
    }

    @Operation(summary = "팀 배너 등록")
    @PostMapping("/team-banners")
    public ResponseEntity<SponsorDto.TeamBannerResponse> createTeamBanner(
            @RequestBody SponsorDto.TeamBannerCreateRequest request) {
        return ResponseEntity.ok(sponsorService.createTeamBanner(request));
    }

    @Operation(summary = "팀 배너 수정")
    @PutMapping("/team-banners/{uid}")
    public ResponseEntity<SponsorDto.TeamBannerResponse> updateTeamBanner(
            @PathVariable String uid,
            @RequestBody SponsorDto.TeamBannerUpdateRequest request) {
        return ResponseEntity.ok(sponsorService.updateTeamBanner(uid, request));
    }

    @Operation(summary = "팀 배너 삭제")
    @DeleteMapping("/team-banners/{uid}")
    public ResponseEntity<Void> deleteTeamBanner(@PathVariable String uid) {
        sponsorService.deleteTeamBanner(uid);
        return ResponseEntity.ok().build();
    }

}

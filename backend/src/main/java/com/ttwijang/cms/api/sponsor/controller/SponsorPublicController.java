package com.ttwijang.cms.api.sponsor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.sponsor.service.SponsorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 구단주 관련 공개 API (사용자 앱용 — 인증 불필요)
 */
@Tag(name = "Sponsor Public", description = "구단주 관련 공개 API")
@RestController
@RequestMapping("/api/sponsor")
@RequiredArgsConstructor
public class SponsorPublicController {

    private final SponsorService sponsorService;

    @Operation(summary = "구단주 신청 금액 조회 (사용자 앱용)")
    @GetMapping("/fee")
    public ResponseEntity<?> getSponsorFee() {
        return ResponseEntity.ok(sponsorService.getSponsorFee());
    }

    @Operation(summary = "특정 팀의 후원 배너 조회 (나의 팀 → 후원내역)")
    @GetMapping("/team-banners/team/{teamUid}")
    public ResponseEntity<?> getTeamBanners(@PathVariable String teamUid) {
        return ResponseEntity.ok(sponsorService.getTeamBannersByTeamUid(teamUid));
    }
}

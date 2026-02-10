package com.ttwijang.cms.api.guest.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.guest.dto.GuestDto;
import com.ttwijang.cms.api.guest.service.GuestService;
import com.ttwijang.cms.api.region.service.RegionCodeService;
import com.ttwijang.cms.entity.GuestRecruitment;
import com.ttwijang.cms.oauth.SinghaUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Guest", description = "게스트(용병) 모집 관리 API")
@RestController
@RequestMapping("/api/guest")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;
    private final RegionCodeService regionCodeService;

    @Operation(summary = "게스트 모집 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<GuestDto.DetailResponse> createRecruitment(
            @Valid @RequestBody GuestDto.CreateRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(guestService.createRecruitment(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "게스트 모집 상세 조회")
    @GetMapping("/{uid}")
    public ResponseEntity<GuestDto.DetailResponse> getRecruitmentDetail(
            @Parameter(description = "모집 UID") @PathVariable String uid) {
        return ResponseEntity.ok(guestService.getRecruitmentDetail(uid));
    }

    @Operation(summary = "게스트 모집 목록 조회")
    @GetMapping
    public ResponseEntity<Page<GuestDto.ListResponse>> getRecruitmentList(
            @RequestParam(required = false) String regionCode,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String regionSido,
            @RequestParam(required = false) String regionSigungu,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) GuestRecruitment.RecruitmentStatus status,
            @PageableDefault(direction = Direction.ASC, sort = "matchDate") Pageable pageable) {
        // regionCode가 제공되면 코드로부터 시/군/구 이름을 조회하여 필터링 (도 필터 없이)
        if (regionCode != null && !regionCode.isEmpty()) {
            String sigunguName = regionCodeService.resolveRegionName(regionCode);
            return ResponseEntity.ok(guestService.getRecruitmentListBySigungu(sigunguName, date, status, pageable));
        }
        String effectiveRegion = region;
        if (regionSido != null && regionSigungu != null) {
            effectiveRegion = regionSido + " " + regionSigungu;
        }
        return ResponseEntity.ok(guestService.getRecruitmentList(effectiveRegion, date, status, pageable));
    }

    @Operation(summary = "날짜 범위별 게스트 모집 조회 (캘린더용)")
    @GetMapping("/calendar")
    public ResponseEntity<Page<GuestDto.ListResponse>> getRecruitmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String regionCode,
            @RequestParam(required = false) String regionSido,
            @RequestParam(required = false) String regionSigungu,
            @PageableDefault(direction = Direction.ASC, sort = "matchDate") Pageable pageable) {
        // regionCode가 제공되면 코드로부터 시/군/구 이름을 조회하여 필터링 (도 필터 없이)
        if (regionCode != null && !regionCode.isEmpty()) {
            String sigunguName = regionCodeService.resolveRegionName(regionCode);
            return ResponseEntity.ok(guestService.getRecruitmentsByDateRangeBySigungu(startDate, endDate, sigunguName, pageable));
        }
        String effectiveRegion = null;
        if (regionSido != null && regionSigungu != null) {
            effectiveRegion = regionSido + " " + regionSigungu;
        }
        return ResponseEntity.ok(guestService.getRecruitmentsByDateRange(startDate, endDate, effectiveRegion, pageable));
    }

    @Operation(summary = "게스트 신청", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/apply")
    public ResponseEntity<GuestDto.ApplicationResponse> applyAsGuest(
            @Valid @RequestBody GuestDto.ApplyRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(guestService.applyAsGuest(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "게스트 신청 처리", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/process")
    public ResponseEntity<GuestDto.ApplicationResponse> processApplication(
            @Valid @RequestBody GuestDto.ProcessRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(guestService.processApplication(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "내 게스트 신청 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/my-applications")
    public ResponseEntity<List<GuestDto.ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(guestService.getMyApplications(userDetails.getUser().getUid()));
    }

    @Operation(summary = "모집의 신청자 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{recruitmentUid}/applications")
    public ResponseEntity<List<GuestDto.ApplicationResponse>> getRecruitmentApplications(
            @PathVariable String recruitmentUid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(guestService.getRecruitmentApplications(recruitmentUid, userDetails.getUser().getUid()));
    }

    @Operation(summary = "게스트 모집 취소", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{recruitmentUid}")
    public ResponseEntity<Void> cancelRecruitment(
            @PathVariable String recruitmentUid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        guestService.cancelRecruitment(recruitmentUid, userDetails.getUser().getUid());
        return ResponseEntity.ok().build();
    }
}

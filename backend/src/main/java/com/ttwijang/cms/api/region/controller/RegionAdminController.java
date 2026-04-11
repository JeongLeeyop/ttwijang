package com.ttwijang.cms.api.region.controller;

import com.ttwijang.cms.api.region.dto.RegionCodeDto;
import com.ttwijang.cms.api.region.service.RegionCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "지역 코드 Admin", description = "지역 코드 관리자 API")
@RestController
@RequestMapping("/api/admin/regions")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class RegionAdminController {

    private final RegionCodeService regionCodeService;

    @Operation(summary = "시/도 추가")
    @PostMapping("/sido")
    public ResponseEntity<RegionCodeDto.Response> createSido(
            @RequestBody RegionCodeDto.SidoCreateRequest request) {
        return ResponseEntity.ok(regionCodeService.createSido(request));
    }

    @Operation(summary = "시/군/구 추가")
    @PostMapping("/sigungu")
    public ResponseEntity<RegionCodeDto.Response> createSigungu(
            @RequestBody RegionCodeDto.SigunguCreateRequest request) {
        return ResponseEntity.ok(regionCodeService.createSigungu(request));
    }

    @Operation(summary = "시/도별 리그 목록 조회")
    @GetMapping("/{sidoCode}/leagues")
    public ResponseEntity<List<RegionCodeDto.RegionLeagueSummary>> getLeaguesBySido(
            @PathVariable String sidoCode) {
        return ResponseEntity.ok(regionCodeService.getLeaguesBySidoCode(sidoCode));
    }

    @Operation(summary = "시/도별 팀 목록 조회")
    @GetMapping("/{sidoCode}/teams")
    public ResponseEntity<List<RegionCodeDto.RegionTeamSummary>> getTeamsBySido(
            @PathVariable String sidoCode) {
        return ResponseEntity.ok(regionCodeService.getTeamsBySidoCode(sidoCode));
    }
}

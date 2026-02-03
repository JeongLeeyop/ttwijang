package com.ttwijang.cms.api.match.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.match.dto.MatchDto;
import com.ttwijang.cms.api.match.service.MatchService;
import com.ttwijang.cms.entity.FutsalMatch;
import com.ttwijang.cms.oauth.SinghaUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Match", description = "매치 관리 API")
@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @Operation(summary = "매치 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<MatchDto.DetailResponse> createMatch(
            @Valid @RequestBody MatchDto.CreateRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(matchService.createMatch(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "매치 상세 조회")
    @GetMapping("/{uid}")
    public ResponseEntity<MatchDto.DetailResponse> getMatchDetail(
            @Parameter(description = "매치 UID") @PathVariable String uid) {
        return ResponseEntity.ok(matchService.getMatchDetail(uid));
    }

    @Operation(summary = "매치 목록 조회")
    @GetMapping
    public ResponseEntity<Page<MatchDto.ListResponse>> getMatchList(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) FutsalMatch.FutsalMatchStatus status,
            @PageableDefault(direction = Direction.ASC, sort = "matchDate") Pageable pageable) {
        return ResponseEntity.ok(matchService.getMatchList(region, date, status, pageable));
    }

    @Operation(summary = "날짜 범위별 매치 조회 (캘린더용)")
    @GetMapping("/calendar")
    public ResponseEntity<Page<MatchDto.ListResponse>> getMatchesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(direction = Direction.ASC, sort = "matchDate") Pageable pageable) {
        return ResponseEntity.ok(matchService.getMatchesByDateRange(startDate, endDate, pageable));
    }

    @Operation(summary = "팀별 매치 조회")
    @GetMapping("/team/{teamUid}")
    public ResponseEntity<Page<MatchDto.ListResponse>> getMatchesByTeam(
            @PathVariable String teamUid,
            @PageableDefault(direction = Direction.DESC, sort = "matchDate") Pageable pageable) {
        return ResponseEntity.ok(matchService.getMatchesByTeam(teamUid, pageable));
    }

    @Operation(summary = "매치 신청", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/apply")
    public ResponseEntity<MatchDto.DetailResponse> applyMatch(
            @Valid @RequestBody MatchDto.ApplyRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(matchService.applyMatch(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "매치 취소", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{matchUid}")
    public ResponseEntity<Void> cancelMatch(
            @PathVariable String matchUid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        matchService.cancelMatch(matchUid, userDetails.getUser().getUid());
        return ResponseEntity.ok().build();
    }
}

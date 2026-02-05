package com.ttwijang.cms.api.league.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.league.dto.LeagueDto;
import com.ttwijang.cms.api.league.service.LeagueService;
import com.ttwijang.cms.entity.League;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "League", description = "리그 관리 API")
@RestController
@RequestMapping("/api/league")
@RequiredArgsConstructor
public class LeagueController {

    private final LeagueService leagueService;

    @Operation(summary = "리그 목록 조회")
    @GetMapping
    public ResponseEntity<Page<LeagueDto.ListResponse>> getLeagueList(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) League.LeagueStatus status,
            @PageableDefault(direction = Direction.DESC, sort = "createdDate") Pageable pageable) {
        return ResponseEntity.ok(leagueService.getLeagueList(region, status, pageable));
    }

    @Operation(summary = "리그 상세 조회")
    @GetMapping("/{uid}")
    public ResponseEntity<LeagueDto.DetailResponse> getLeagueDetail(
            @Parameter(description = "리그 UID") @PathVariable String uid) {
        return ResponseEntity.ok(leagueService.getLeagueDetail(uid));
    }

    @Operation(summary = "리그 순위표 조회")
    @GetMapping("/{leagueUid}/standings")
    public ResponseEntity<List<LeagueDto.LeagueStandingResponse>> getLeagueStandings(
            @PathVariable String leagueUid) {
        return ResponseEntity.ok(leagueService.getLeagueStandings(leagueUid));
    }

    @Operation(summary = "리그 일정 조회 (월별)")
    @GetMapping("/{leagueUid}/schedule")
    public ResponseEntity<List<LeagueDto.MatchResponse>> getLeagueSchedule(
            @PathVariable String leagueUid,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        return ResponseEntity.ok(leagueService.getLeagueSchedule(leagueUid, year, month));
    }

    @Operation(summary = "다가오는 경기 조회")
    @GetMapping("/{leagueUid}/upcoming")
    public ResponseEntity<List<LeagueDto.MatchResponse>> getUpcomingMatches(
            @PathVariable String leagueUid,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(leagueService.getUpcomingMatches(leagueUid, limit));
    }

    @Operation(summary = "최근 경기 결과 조회")
    @GetMapping("/{leagueUid}/results")
    public ResponseEntity<List<LeagueDto.MatchResponse>> getRecentResults(
            @PathVariable String leagueUid,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(leagueService.getRecentResults(leagueUid, limit));
    }

    @Operation(summary = "경기 결과 입력", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/match/result")
    public ResponseEntity<LeagueDto.MatchResponse> updateMatchResult(
            @Valid @RequestBody LeagueDto.MatchResultRequest request) {
        return ResponseEntity.ok(leagueService.updateMatchResult(request));
    }

    @Operation(summary = "등급별 리그 조회")
    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<LeagueDto.ListResponse>> getLeaguesByGrade(
            @PathVariable String grade) {
        return ResponseEntity.ok(leagueService.getLeaguesByGrade(grade));
    }

    @Operation(summary = "리그 참가 팀 목록 조회")
    @GetMapping("/{leagueUid}/teams")
    public ResponseEntity<List<LeagueDto.LeagueTeamResponse>> getLeagueTeams(
            @PathVariable String leagueUid) {
        return ResponseEntity.ok(leagueService.getLeagueTeams(leagueUid));
    }
}

package com.ttwijang.cms.api.league.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.league.dto.LeagueDto;
import com.ttwijang.cms.api.league.service.LeagueService;
import com.ttwijang.cms.api.region.service.RegionCodeService;
import com.ttwijang.cms.entity.League;
import com.ttwijang.cms.oauth.SinghaUser;

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
    private final RegionCodeService regionCodeService;

    @Operation(summary = "리그 목록 조회 (BR-04: 지역별 리그 전환)")
    @GetMapping
    public ResponseEntity<Page<LeagueDto.ListResponse>> getLeagueList(
            @RequestParam(required = false) String regionCode,
            @RequestParam(required = false) String regionSido,
            @RequestParam(required = false) String regionSigungu,
            @RequestParam(required = false) League.LeagueStatus status,
            @PageableDefault(direction = Direction.DESC, sort = "createdDate") Pageable pageable) {
        // regionCode가 제공되면 코드로부터 시/군/구 이름을 조회하여 필터링 (도 필터 없이)
        if (regionCode != null && !regionCode.isEmpty()) {
            regionSigungu = regionCodeService.resolveRegionName(regionCode);
            regionSido = null;
        }
        return ResponseEntity.ok(leagueService.getLeagueList(regionSido, regionSigungu, status, pageable));
    }

    @Operation(summary = "지역별 다가오는 리그 경기 조회")
    @GetMapping("/upcoming")
    public ResponseEntity<List<LeagueDto.MatchResponse>> getUpcomingMatchesByRegion(
            @RequestParam(required = false) String regionCode,
            @RequestParam(defaultValue = "20") int limit) {
        String regionSigungu = null;
        if (regionCode != null && !regionCode.isEmpty()) {
            regionSigungu = regionCodeService.resolveRegionName(regionCode);
        }
        return ResponseEntity.ok(leagueService.getUpcomingMatchesByRegion(regionSigungu, limit));
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
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
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

    @Operation(summary = "리그 경기 단건 조회")
    @GetMapping("/match/{matchUid}")
    public ResponseEntity<LeagueDto.MatchResponse> getLeagueMatchDetail(
            @PathVariable String matchUid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        String userUid = userDetails != null ? userDetails.getUser().getUid() : null;
        return ResponseEntity.ok(leagueService.getLeagueMatchDetail(matchUid, userUid));
    }

    @Operation(summary = "경기 결과 입력", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/match/result")
    public ResponseEntity<LeagueDto.MatchResponse> updateMatchResult(
            @Valid @RequestBody LeagueDto.MatchResultRequest request) {
        return ResponseEntity.ok(leagueService.updateMatchResult(request));
    }

    @Operation(summary = "내 리그 경기 신청 내역 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/match/my-applications")
    public ResponseEntity<List<LeagueDto.MyLeagueMatchApplicationResponse>> getMyLeagueMatchApplications(
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(leagueService.getMyLeagueMatchApplications(userDetails.getUser().getUid()));
    }

    @Operation(summary = "리그 경기 참가 신청 (무료)", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/match/{matchUid}/apply")
    public ResponseEntity<Void> applyToLeagueMatch(
            @PathVariable String matchUid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        leagueService.applyToLeagueMatch(matchUid, userDetails.getUser().getUid());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리그 경기 참가 신청 취소", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/match/{matchUid}/cancel-application")
    public ResponseEntity<Void> cancelLeagueMatchApplication(
            @PathVariable String matchUid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        leagueService.cancelLeagueMatchApplication(matchUid, userDetails.getUser().getUid());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리그 참가 팀 목록 조회")
    @GetMapping("/{leagueUid}/teams")
    public ResponseEntity<List<LeagueDto.LeagueTeamResponse>> getLeagueTeams(
            @PathVariable String leagueUid) {
        return ResponseEntity.ok(leagueService.getLeagueTeams(leagueUid));
    }

    @Operation(summary = "전체 리그 참가 팀 목록 조회 (지역/리그/키워드 필터)")
    @GetMapping("/all-teams")
    public ResponseEntity<Page<LeagueDto.LeagueTeamResponse>> getAllLeagueTeams(
            @RequestParam(required = false) String regionSido,
            @RequestParam(required = false) String regionSigungu,
            @RequestParam(required = false) String leagueUid,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(leagueService.getAllLeagueTeams(regionSido, regionSigungu, leagueUid, keyword, pageable));
    }

    @Operation(summary = "팀이 참가 중인 리그 목록 조회")
    @GetMapping("/team/{teamUid}")
    public ResponseEntity<List<LeagueDto.ListResponse>> getLeaguesByTeam(
            @PathVariable String teamUid) {
        return ResponseEntity.ok(leagueService.getLeaguesByTeam(teamUid));
    }

    @Operation(summary = "리그 참여 신청 (팀 OWNER 전용)", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/{leagueUid}/apply")
    public ResponseEntity<LeagueDto.LeagueTeamResponse> applyToLeague(
            @PathVariable String leagueUid,
            @AuthenticationPrincipal SinghaUser user) {
        return ResponseEntity.ok(leagueService.applyToLeague(leagueUid, user.getUid()));
    }

    // ==================== 최고관리자 전용 API (BR-11, BR-12) ====================

    @Operation(summary = "[관리자] 리그 생성 (BR-11)", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/admin")
    public ResponseEntity<LeagueDto.DetailResponse> createLeague(
            @Valid @RequestBody LeagueDto.CreateRequest request) {
        return ResponseEntity.ok(leagueService.createLeague(request));
    }

    @Operation(summary = "[관리자] 리그 수정 (BR-11)", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/admin/{uid}")
    public ResponseEntity<LeagueDto.DetailResponse> updateLeague(
            @PathVariable String uid,
            @Valid @RequestBody LeagueDto.UpdateRequest request) {
        return ResponseEntity.ok(leagueService.updateLeague(uid, request));
    }

    @Operation(summary = "[관리자] 리그 삭제 (BR-11)", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/admin/{uid}")
    public ResponseEntity<Void> deleteLeague(@PathVariable String uid) {
        leagueService.deleteLeague(uid);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[관리자] 리그에 팀 추가 (BR-12)", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/admin/{leagueUid}/teams/{teamUid}")
    public ResponseEntity<LeagueDto.LeagueTeamResponse> addTeamToLeague(
            @PathVariable String leagueUid,
            @PathVariable String teamUid) {
        return ResponseEntity.ok(leagueService.addTeamToLeague(leagueUid, teamUid));
    }

    @Operation(summary = "[관리자] 리그에서 팀 제거 (BR-12)", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/admin/{leagueUid}/teams/{teamUid}")
    public ResponseEntity<Void> removeTeamFromLeague(
            @PathVariable String leagueUid,
            @PathVariable String teamUid) {
        leagueService.removeTeamFromLeague(leagueUid, teamUid);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[관리자] 리그 매치 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/admin/match")
    public ResponseEntity<LeagueDto.MatchResponse> createLeagueMatch(
            @Valid @RequestBody LeagueDto.CreateMatchRequest request) {
        return ResponseEntity.ok(leagueService.createLeagueMatch(request));
    }

    @Operation(summary = "[관리자] 리그 전체 경기 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/admin/{leagueUid}/matches")
    public ResponseEntity<List<LeagueDto.MatchResponse>> getAllLeagueMatches(
            @PathVariable String leagueUid) {
        return ResponseEntity.ok(leagueService.getAllLeagueMatches(leagueUid));
    }
}

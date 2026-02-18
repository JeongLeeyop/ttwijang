package com.ttwijang.cms.api.team.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ttwijang.cms.api.attached_file.service.AttachedFileService;
import com.ttwijang.cms.api.team.dto.TeamDto;
import com.ttwijang.cms.api.team.dto.TeamMemberDto;
import com.ttwijang.cms.api.team.service.TeamService;
import com.ttwijang.cms.api.region.service.RegionCodeService;
import com.ttwijang.cms.entity.AttachedFile;
import com.ttwijang.cms.oauth.SinghaUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Team", description = "팀 관리 API")
@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final RegionCodeService regionCodeService;
    private final AttachedFileService attachedFileService;

    @Operation(summary = "팀 관련 파일 업로드 (로고, 단체사진, 커뮤니티 이미지 등)")
    @PostMapping("/upload")
    public ResponseEntity<AttachedFile> fileUpload(MultipartFile file) {
        return ResponseEntity.ok(attachedFileService.save(file, "team"));
    }

    @Operation(summary = "팀 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<TeamDto.DetailResponse> createTeam(
            @Valid @RequestBody TeamDto.CreateRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(teamService.createTeam(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "팀 상세 조회")
    @GetMapping("/{uid}")
    public ResponseEntity<TeamDto.DetailResponse> getTeamDetail(
            @Parameter(description = "팀 UID") @PathVariable String uid) {
        return ResponseEntity.ok(teamService.getTeamDetail(uid));
    }

    @Operation(summary = "팀 코드로 조회")
    @GetMapping("/code/{teamCode}")
    public ResponseEntity<TeamDto.DetailResponse> getTeamByCode(
            @Parameter(description = "팀 코드") @PathVariable String teamCode) {
        return ResponseEntity.ok(teamService.getTeamByCode(teamCode));
    }

    @Operation(summary = "팀 목록 조회")
    @GetMapping
    public ResponseEntity<Page<TeamDto.ListResponse>> getTeamList(
            @RequestParam(required = false) String regionCode,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String regionSido,
            @RequestParam(required = false) String regionSigungu,
            @RequestParam(required = false) Boolean recruiting,
            @PageableDefault(direction = Direction.DESC, sort = "createdDate") Pageable pageable) {
        // regionCode가 제공되면 코드로부터 시/군/구 이름을 조회하여 필터링 (도 필터 없이)
        if (regionCode != null && !regionCode.isEmpty()) {
            String sigunguName = regionCodeService.resolveRegionName(regionCode);
            return ResponseEntity.ok(teamService.getTeamListBySigungu(sigunguName, recruiting, pageable));
        }
        String effectiveRegion = region;
        if (regionSido != null && regionSigungu != null) {
            effectiveRegion = regionSido + " " + regionSigungu;
        }
        return ResponseEntity.ok(teamService.getTeamList(effectiveRegion, recruiting, pageable));
    }

    @Operation(summary = "팀 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{uid}")
    public ResponseEntity<TeamDto.DetailResponse> updateTeam(
            @PathVariable String uid,
            @Valid @RequestBody TeamDto.UpdateRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(teamService.updateTeam(uid, request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "팀 가입 신청", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/join")
    public ResponseEntity<TeamMemberDto.Response> joinTeam(
            @Valid @RequestBody TeamMemberDto.JoinRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(teamService.joinTeam(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "초대 코드로 팀 가입", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/join/code/{teamCode}")
    public ResponseEntity<TeamMemberDto.Response> joinTeamByCode(
            @PathVariable String teamCode,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(teamService.joinTeamByCode(teamCode, userDetails.getUser().getUid()));
    }

    @Operation(summary = "가입 신청 처리", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/join/process")
    public ResponseEntity<TeamMemberDto.Response> processJoinRequest(
            @Valid @RequestBody TeamMemberDto.ProcessRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(teamService.processJoinRequest(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "팀원 목록 조회")
    @GetMapping("/{teamUid}/members")
    public ResponseEntity<List<TeamMemberDto.Response>> getTeamMembers(
            @PathVariable String teamUid) {
        return ResponseEntity.ok(teamService.getTeamMembers(teamUid));
    }

    @Operation(summary = "가입 신청 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{teamUid}/pending")
    public ResponseEntity<List<TeamMemberDto.Response>> getPendingRequests(
            @PathVariable String teamUid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(teamService.getPendingRequests(teamUid, userDetails.getUser().getUid()));
    }

    @Operation(summary = "팀 코드 사용 가능 여부 확인")
    @GetMapping("/check-code/{teamCode}")
    public ResponseEntity<Boolean> checkTeamCode(@PathVariable String teamCode) {
        return ResponseEntity.ok(teamService.checkTeamCodeAvailable(teamCode));
    }

    @Operation(summary = "랜덤 팀 코드 생성")
    @GetMapping("/generate-code")
    public ResponseEntity<String> generateTeamCode() {
        return ResponseEntity.ok(teamService.generateTeamCode());
    }

    @Operation(summary = "팀 가입/생성 가능 여부 확인 (BR-01, BR-02)", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/membership-status")
    public ResponseEntity<TeamDto.MembershipStatus> checkMembershipStatus(
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(teamService.checkMembershipStatus(userDetails.getUser().getUid()));
    }

    @Operation(summary = "내 소속 팀 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/my")
    public ResponseEntity<TeamDto.DetailResponse> getMyTeam(
            @AuthenticationPrincipal SinghaUser userDetails) {
        TeamDto.DetailResponse myTeam = teamService.getMyTeam(userDetails.getUser().getUid());
        return ResponseEntity.ok(myTeam);
    }

    @Operation(summary = "운영자 위임", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/{teamUid}/delegate/{newOwnerUid}")
    public ResponseEntity<Void> delegateOwner(
            @PathVariable String teamUid,
            @PathVariable String newOwnerUid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        teamService.delegateOwner(teamUid, newOwnerUid, userDetails.getUser().getUid());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "팀 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{teamUid}")
    public ResponseEntity<Void> deleteTeam(
            @PathVariable String teamUid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        teamService.deleteTeam(teamUid, userDetails.getUser().getUid());
        return ResponseEntity.ok().build();
    }
}

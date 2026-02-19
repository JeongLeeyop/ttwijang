package com.ttwijang.cms.api.team.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.api.team.dto.TeamDto;
import com.ttwijang.cms.api.team.dto.TeamMemberDto;
import com.ttwijang.cms.api.team.repository.TeamMemberRepository;
import com.ttwijang.cms.api.team.repository.TeamRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.entity.Team;
import com.ttwijang.cms.entity.TeamMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    private static final String TEAM_CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TEAM_CODE_LENGTH = 8;

    /**
     * 팀 생성
     * BR-02: 1계정 1팀 생성 제한 적용
     * BR-01: 이미 팀에 소속된 경우 팀 생성 불가
     */
    @Transactional
    public TeamDto.DetailResponse createTeam(TeamDto.CreateRequest request, String ownerUid) {
        // BR-02: 이미 팀을 생성한 적이 있는지 확인
        if (teamRepository.existsByOwnerUidAndStatus(ownerUid, Team.TeamStatus.ACTIVE)) {
            throw new IllegalArgumentException("이미 생성한 팀이 있습니다. 계정 하나당 하나의 팀만 생성할 수 있습니다.");
        }

        // BR-01: 이미 다른 팀에 소속되어 있는지 확인
        if (teamMemberRepository.existsByUserUidAndStatus(ownerUid, TeamMember.MemberStatus.APPROVED)) {
            throw new IllegalArgumentException("이미 소속된 팀이 있습니다. 계정 하나당 하나의 팀에만 가입할 수 있습니다.");
        }

        // 팀 코드 중복 확인
        if (teamRepository.existsByTeamCode(request.getTeamCode())) {
            throw new IllegalArgumentException("이미 사용 중인 팀 코드입니다.");
        }

        Team team = Team.builder()
                .name(request.getName())
                .teamCode(request.getTeamCode())
                .logoFileUid(request.getLogoFileUid())
                .description(request.getDescription())
                .bankName(request.getBankName())
                .bankAccount(request.getBankAccount())
                .activeDays(request.getActiveDays())
                .activeTimeSlots(request.getActiveTimeSlots())
                .genderType(request.getGenderType())
                .ageGroups(request.getAgeGroups())
                .regionSido(request.getRegionSido())
                .regionSigungu(request.getRegionSigungu())
                .homeStadium(request.getHomeStadium())
                .homeStadiumAddress(request.getHomeStadiumAddress())
                .monthlyFee(request.getMonthlyFee())
                .ownerUid(ownerUid)
                .mannerScore(0.0)
                .memberCount(1)
                .status(Team.TeamStatus.ACTIVE)
                .recruitingMembers(false)
                .build();

        team = teamRepository.save(team);

        // 팀 운영자로 자동 가입
        TeamMember owner = TeamMember.builder()
                .teamUid(team.getUid())
                .userUid(ownerUid)
                .role(TeamMember.MemberRole.OWNER)
                .status(TeamMember.MemberStatus.APPROVED)
                .build();
        teamMemberRepository.save(owner);

        return toDetailResponse(team);
    }

    /**
     * 팀 상세 조회
     */
    @Transactional(readOnly = true)
    public TeamDto.DetailResponse getTeamDetail(String uid) {
        Team team = teamRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        return toDetailResponse(team);
    }

    /**
     * 팀 코드로 조회
     */
    @Transactional(readOnly = true)
    public TeamDto.DetailResponse getTeamByCode(String teamCode) {
        Team team = teamRepository.findByTeamCode(teamCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        return toDetailResponse(team);
    }

    /**
     * 팀 목록 조회 (지역 필터 지원)
     */
    @Transactional(readOnly = true)
    public Page<TeamDto.ListResponse> getTeamList(String region, Boolean recruiting, Pageable pageable) {
        Page<Team> teams;

        // 지역 필터 파싱
        String sido = null;
        String sigungu = null;
        if (region != null && !region.isEmpty()) {
            String[] parts = region.split(" ", 2);
            sido = parts[0];
            sigungu = parts.length > 1 ? parts[1] : null;
        }

        if (recruiting != null && recruiting) {
            if (sido != null && sigungu != null) {
                teams = teamRepository.findByRecruitingMembersTrueAndRegionSidoAndRegionSigungu(sido, sigungu, pageable);
            } else {
                teams = teamRepository.findByRecruitingMembersTrue(pageable);
            }
        } else if (sido != null) {
            teams = teamRepository.findActiveByRegion(sido, sigungu, pageable);
        } else {
            teams = teamRepository.findByStatus(Team.TeamStatus.ACTIVE, pageable);
        }
        return teams.map(this::toListResponse);
    }

    /**
     * 팀 목록 조회 - 시/군/구 이름으로만 조회 (도 필터 없이, regionCode 사용 시)
     */
    @Transactional(readOnly = true)
    public Page<TeamDto.ListResponse> getTeamListBySigungu(String sigungu, Boolean recruiting, Pageable pageable) {
        Page<Team> teams;
        if (sigungu == null || sigungu.isEmpty()) {
            if (recruiting != null && recruiting) {
                teams = teamRepository.findByRecruitingMembersTrue(pageable);
            } else {
                teams = teamRepository.findByStatus(Team.TeamStatus.ACTIVE, pageable);
            }
        } else if (recruiting != null && recruiting) {
            teams = teamRepository.findByRecruitingMembersTrueAndRegionSigungu(sigungu, pageable);
        } else {
            teams = teamRepository.findActiveBySigungu(sigungu, pageable);
        }
        return teams.map(this::toListResponse);
    }

    /**
     * 팀 수정
     */
    @Transactional
    public TeamDto.DetailResponse updateTeam(String uid, TeamDto.UpdateRequest request, String userUid) {
        Team team = teamRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 운영자 권한 확인
        if (!team.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("팀 수정 권한이 없습니다.");
        }

        if (request.getName() != null) team.setName(request.getName());
        if (request.getLogoFileUid() != null) team.setLogoFileUid(request.getLogoFileUid());
        if (request.getDescription() != null) team.setDescription(request.getDescription());
        if (request.getBankName() != null) team.setBankName(request.getBankName());
        if (request.getBankAccount() != null) team.setBankAccount(request.getBankAccount());
        if (request.getRefundBankName() != null) team.setRefundBankName(request.getRefundBankName());
        if (request.getRefundBankAccount() != null) team.setRefundBankAccount(request.getRefundBankAccount());
        if (request.getActiveDays() != null) team.setActiveDays(request.getActiveDays());
        if (request.getActiveTimeSlots() != null) team.setActiveTimeSlots(request.getActiveTimeSlots());
        if (request.getGenderType() != null) team.setGenderType(request.getGenderType());
        if (request.getAgeGroups() != null) team.setAgeGroups(request.getAgeGroups());
        if (request.getRegionSido() != null) team.setRegionSido(request.getRegionSido());
        if (request.getRegionSigungu() != null) team.setRegionSigungu(request.getRegionSigungu());
        if (request.getHomeStadium() != null) team.setHomeStadium(request.getHomeStadium());
        if (request.getHomeStadiumAddress() != null) team.setHomeStadiumAddress(request.getHomeStadiumAddress());
        if (request.getMonthlyFee() != null) team.setMonthlyFee(request.getMonthlyFee());
        if (request.getFeatureTags() != null) team.setFeatureTags(request.getFeatureTags());
        if (request.getRecruitingMembers() != null) team.setRecruitingMembers(request.getRecruitingMembers());
        if (request.getRecruitmentDescription() != null) team.setRecruitmentDescription(request.getRecruitmentDescription());
        if (request.getTeamPhotoFileUid() != null) team.setTeamPhotoFileUid(request.getTeamPhotoFileUid());

        team = teamRepository.save(team);
        return toDetailResponse(team);
    }

    /**
     * 팀 가입 신청
     * BR-01: 1계정 1팀 가입 제한 적용
     */
    @Transactional
    public TeamMemberDto.Response joinTeam(TeamMemberDto.JoinRequest request, String userUid) {
        // BR-01: 이미 다른 팀에 소속되어 있는지 글로벌 확인
        if (teamMemberRepository.existsByUserUidAndStatus(userUid, TeamMember.MemberStatus.APPROVED)) {
            throw new IllegalArgumentException("이미 소속된 팀이 있습니다. 계정 하나당 하나의 팀에만 가입할 수 있습니다.");
        }

        // 해당 팀에 이미 가입 신청 중인지 확인
        if (teamMemberRepository.existsByTeamUidAndUserUid(request.getTeamUid(), userUid)) {
            throw new IllegalArgumentException("이미 가입 신청하였거나 팀원입니다.");
        }

        // 대기 중인 다른 팀 가입 신청이 있는지 확인
        if (teamMemberRepository.existsActiveOrPendingMembershipByUserUid(userUid)) {
            throw new IllegalArgumentException("이미 다른 팀에 가입 신청 중입니다. 기존 신청을 취소 후 다시 시도해주세요.");
        }

        Team team = teamRepository.findByUid(request.getTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        TeamMember member = TeamMember.builder()
                .teamUid(request.getTeamUid())
                .userUid(userUid)
                .role(TeamMember.MemberRole.MEMBER)
                .position(request.getPosition())
                .status(TeamMember.MemberStatus.PENDING)
                .applicationRegion(request.getRegion())
                .applicationAge(request.getAge())
                .applicationExperience(request.getExperience())
                .build();

        member = teamMemberRepository.save(member);
        return toMemberResponse(member);
    }

    /**
     * 초대 코드로 팀 가입 (즉시 승인)
     * BR-01: 1계정 1팀 가입 제한 적용
     */
    @Transactional
    public TeamMemberDto.Response joinTeamByCode(String teamCode, String userUid) {
        // BR-01: 이미 다른 팀에 소속되어 있는지 글로벌 확인
        if (teamMemberRepository.existsByUserUidAndStatus(userUid, TeamMember.MemberStatus.APPROVED)) {
            throw new IllegalArgumentException("이미 소속된 팀이 있습니다. 계정 하나당 하나의 팀에만 가입할 수 있습니다.");
        }

        // 대기 중인 다른 팀 가입 신청이 있는지 확인
        if (teamMemberRepository.existsActiveOrPendingMembershipByUserUid(userUid)) {
            throw new IllegalArgumentException("이미 다른 팀에 가입 신청 중입니다. 기존 신청을 취소 후 다시 시도해주세요.");
        }

        Team team = teamRepository.findByTeamCode(teamCode)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 팀 코드입니다."));

        if (teamMemberRepository.existsByTeamUidAndUserUid(team.getUid(), userUid)) {
            throw new IllegalArgumentException("이미 팀원입니다.");
        }

        TeamMember member = TeamMember.builder()
                .teamUid(team.getUid())
                .userUid(userUid)
                .role(TeamMember.MemberRole.MEMBER)
                .status(TeamMember.MemberStatus.APPROVED)
                .build();

        member = teamMemberRepository.save(member);

        // 팀 멤버 수 업데이트
        team.setMemberCount(team.getMemberCount() + 1);
        teamRepository.save(team);

        return toMemberResponse(member);
    }

    /**
     * 가입 신청 처리
     * BR-01: 승인 시 해당 사용자가 다른 팀에 소속되지 않았는지 재검증
     */
    @Transactional
    public TeamMemberDto.Response processJoinRequest(TeamMemberDto.ProcessRequest request, String ownerUid) {
        TeamMember member = teamMemberRepository.findById(request.getMemberUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가입 신청입니다."));

        Team team = teamRepository.findByUid(member.getTeamUid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 운영자 권한 확인
        if (!team.getOwnerUid().equals(ownerUid)) {
            throw new IllegalArgumentException("가입 신청 처리 권한이 없습니다.");
        }

        if (request.getApproved()) {
            // BR-01: 승인 시점에 이미 다른 팀에 소속되었는지 재확인
            if (teamMemberRepository.existsByUserUidAndStatus(member.getUserUid(), TeamMember.MemberStatus.APPROVED)) {
                throw new IllegalArgumentException("해당 사용자가 이미 다른 팀에 소속되어 있어 승인할 수 없습니다.");
            }

            member.setStatus(TeamMember.MemberStatus.APPROVED);
            team.setMemberCount(team.getMemberCount() + 1);
            teamRepository.save(team);
        } else {
            member.setStatus(TeamMember.MemberStatus.REJECTED);
        }

        member = teamMemberRepository.save(member);
        return toMemberResponse(member);
    }

    /**
     * 팀원 목록 조회
     */
    @Transactional(readOnly = true)
    public List<TeamMemberDto.Response> getTeamMembers(String teamUid) {
        return teamMemberRepository.findWithUserByTeamUidAndStatus(teamUid, TeamMember.MemberStatus.APPROVED)
                .stream()
                .map(this::toMemberResponse)
                .collect(Collectors.toList());
    }

    /**
     * 가입 신청 목록 조회
     */
    @Transactional(readOnly = true)
    public List<TeamMemberDto.Response> getPendingRequests(String teamUid, String ownerUid) {
        Team team = teamRepository.findByUid(teamUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        if (!team.getOwnerUid().equals(ownerUid)) {
            throw new IllegalArgumentException("조회 권한이 없습니다.");
        }

        return teamMemberRepository.findByTeamUidAndStatus(teamUid, TeamMember.MemberStatus.PENDING)
                .stream()
                .map(this::toMemberResponse)
                .collect(Collectors.toList());
    }

    /**
     * 팀 코드 중복 확인
     */
    public boolean checkTeamCodeAvailable(String teamCode) {
        return !teamRepository.existsByTeamCode(teamCode);
    }

    /**
     * 내 소속 팀 조회
     */
    @Transactional(readOnly = true)
    public TeamDto.DetailResponse getMyTeam(String userUid) {
        TeamMember membership = teamMemberRepository.findApprovedMembershipByUserUid(userUid)
                .orElse(null);
        if (membership == null) {
            return null;
        }
        Team team = teamRepository.findByUid(membership.getTeamUid())
                .orElse(null);
        return team != null ? toDetailResponse(team) : null;
    }

    /**
     * 팀 가입 가능 여부 확인 (BR-01, BR-02)
     */
    @Transactional(readOnly = true)
    public TeamDto.MembershipStatus checkMembershipStatus(String userUid) {
        boolean hasApprovedTeam = teamMemberRepository.existsByUserUidAndStatus(userUid, TeamMember.MemberStatus.APPROVED);
        boolean hasPendingRequest = teamMemberRepository.existsActiveOrPendingMembershipByUserUid(userUid);
        boolean hasCreatedTeam = teamRepository.existsByOwnerUidAndStatus(userUid, Team.TeamStatus.ACTIVE);

        return TeamDto.MembershipStatus.builder()
                .hasTeam(hasApprovedTeam)
                .hasPendingRequest(hasPendingRequest && !hasApprovedTeam)
                .hasCreatedTeam(hasCreatedTeam)
                .canJoinTeam(!hasApprovedTeam && !hasPendingRequest)
                .canCreateTeam(!hasApprovedTeam && !hasCreatedTeam && !hasPendingRequest)
                .build();
    }

    /**
     * 랜덤 팀 코드 생성
     */
    public String generateTeamCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code;
        do {
            code = new StringBuilder();
            for (int i = 0; i < TEAM_CODE_LENGTH; i++) {
                code.append(TEAM_CODE_CHARS.charAt(random.nextInt(TEAM_CODE_CHARS.length())));
            }
        } while (teamRepository.existsByTeamCode(code.toString()));
        return code.toString();
    }

    /**
     * 운영자 위임
     */
    @Transactional
    public void delegateOwner(String teamUid, String newOwnerUid, String currentOwnerUid) {
        Team team = teamRepository.findByUid(teamUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        if (!team.getOwnerUid().equals(currentOwnerUid)) {
            throw new IllegalArgumentException("운영자 권한이 없습니다.");
        }

        // 새 운영자가 팀원인지 확인
        TeamMember newOwner = teamMemberRepository.findByTeamUidAndUserUid(teamUid, newOwnerUid)
                .orElseThrow(() -> new IllegalArgumentException("팀원이 아닙니다."));

        // 기존 운영자를 일반 멤버로 변경
        TeamMember currentOwner = teamMemberRepository.findByTeamUidAndUserUid(teamUid, currentOwnerUid)
                .orElseThrow(() -> new IllegalArgumentException("기존 운영자 정보를 찾을 수 없습니다."));
        currentOwner.setRole(TeamMember.MemberRole.MEMBER);
        teamMemberRepository.save(currentOwner);

        // 새 운영자로 변경
        newOwner.setRole(TeamMember.MemberRole.OWNER);
        teamMemberRepository.save(newOwner);

        team.setOwnerUid(newOwnerUid);
        teamRepository.save(team);
    }

    /**
     * 팀 삭제 (소프트 삭제)
     */
    @Transactional
    public void deleteTeam(String teamUid, String ownerUid) {
        Team team = teamRepository.findByUid(teamUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        if (!team.getOwnerUid().equals(ownerUid)) {
            throw new IllegalArgumentException("팀 삭제 권한이 없습니다.");
        }

        team.setStatus(Team.TeamStatus.DELETED);
        teamRepository.save(team);
    }

    /**
     * 회원 모집 설정 저장
     */
    @Transactional
    public TeamDto.DetailResponse saveRecruitment(String teamUid, TeamDto.RecruitmentRequest request, String userUid) {
        Team team = teamRepository.findByUid(teamUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        if (!team.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("팀 수정 권한이 없습니다.");
        }

        if (request.getFeatureTags() != null) team.setFeatureTags(request.getFeatureTags());
        if (request.getActiveDays() != null) team.setActiveDays(request.getActiveDays());
        if (request.getActiveTimeSlots() != null) team.setActiveTimeSlots(request.getActiveTimeSlots());
        if (request.getRegionSido() != null) team.setRegionSido(request.getRegionSido());
        if (request.getRegionSigungu() != null) team.setRegionSigungu(request.getRegionSigungu());
        if (request.getMonthlyFee() != null) team.setMonthlyFee(request.getMonthlyFee());
        if (request.getGenderType() != null) team.setGenderType(request.getGenderType());
        if (request.getAgeGroups() != null) team.setAgeGroups(request.getAgeGroups());
        if (request.getTeamPhotoFileUid() != null) team.setTeamPhotoFileUid(request.getTeamPhotoFileUid());
        if (request.getRecruitmentDescription() != null) team.setRecruitmentDescription(request.getRecruitmentDescription());
        team.setRecruitingMembers(true);

        team = teamRepository.save(team);
        return toDetailResponse(team);
    }

    /**
     * 회원 모집 중인 팀 목록 조회 (필터링)
     */
    @Transactional(readOnly = true)
    public Page<TeamDto.RecruitmentListResponse> getRecruitingTeams(
            String regionSido, String regionSigungu,
            Integer genderType, Integer ageGroups,
            Integer activeDays, Integer activeTimeSlots,
            String featureTag,
            Pageable pageable) {

        // native query 사용 시 Pageable sort 의 Java 필드명이 그대로 SQL에 추가되어 오류 발생.
        // ORDER BY 는 native query 내부에 이미 정의되어 있으므로 sort 없는 Pageable 로 교체.
        Pageable unsorted = org.springframework.data.domain.PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Team> teams = teamRepository.findRecruitingTeams(
                regionSido, regionSigungu,
                genderType, ageGroups,
                activeDays, activeTimeSlots,
                featureTag,
                unsorted);

        return teams.map(this::toRecruitmentListResponse);
    }

    /**
     * 회원 모집 종료
     */
    @Transactional
    public void stopRecruitment(String teamUid, String userUid) {
        Team team = teamRepository.findByUid(teamUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        if (!team.getOwnerUid().equals(userUid)) {
            throw new IllegalArgumentException("팀 수정 권한이 없습니다.");
        }
        team.setRecruitingMembers(false);
        teamRepository.save(team);
    }

    /**
     * 내 가입 대기 정보 조회
     */
    @Transactional(readOnly = true)
    public TeamDto.PendingInfoResponse getMyPendingInfo(String userUid) {
        TeamMember pending = teamMemberRepository.findPendingMembershipByUserUid(userUid)
                .orElse(null);
        if (pending == null) {
            return null;
        }

        Team team = pending.getTeam();
        String logoUrl = (team.getLogoFileUid() != null && !team.getLogoFileUid().isEmpty())
                ? "/api/attached-file/" + team.getLogoFileUid()
                : null;
        String ownerName = userRepository.findByUid(team.getOwnerUid())
                .map(u -> u.getActualName())
                .orElse(null);
        String region = "";
        if (team.getRegionSido() != null) {
            region = team.getRegionSido();
            if (team.getRegionSigungu() != null) {
                region += " " + team.getRegionSigungu();
            }
        }

        return TeamDto.PendingInfoResponse.builder()
                .memberUid(pending.getUid())
                .teamUid(team.getUid())
                .teamName(team.getName())
                .teamCode(team.getTeamCode())
                .teamLogoUrl(logoUrl)
                .ownerName(ownerName)
                .memberCount(team.getMemberCount())
                .region(region)
                .position(pending.getPosition())
                .appliedDate(pending.getCreatedDate())
                .build();
    }

    /**
     * 가입 신청 취소
     */
    @Transactional
    public void cancelJoinRequest(String userUid) {
        TeamMember pending = teamMemberRepository.findPendingMembershipByUserUid(userUid)
                .orElseThrow(() -> new IllegalArgumentException("대기 중인 가입 신청이 없습니다."));

        teamMemberRepository.delete(pending);
    }

    private TeamDto.DetailResponse toDetailResponse(Team team) {
        String logoUrl = (team.getLogoFileUid() != null && !team.getLogoFileUid().isEmpty())
                ? "/api/attached-file/" + team.getLogoFileUid()
                : null;
        String ownerName = userRepository.findByUid(team.getOwnerUid())
                .map(u -> u.getActualName())
                .orElse(null);
        return TeamDto.DetailResponse.builder()
                .uid(team.getUid())
                .name(team.getName())
                .teamCode(team.getTeamCode())
                .logoUrl(logoUrl)
                .description(team.getDescription())
                .bankName(team.getBankName())
                .bankAccount(team.getBankAccount())
                .refundBankName(team.getRefundBankName())
                .refundBankAccount(team.getRefundBankAccount())
                .mannerScore(team.getMannerScore())
                .memberCount(team.getMemberCount())
                .regionSido(team.getRegionSido())
                .regionSigungu(team.getRegionSigungu())
                .homeStadium(team.getHomeStadium())
                .homeStadiumAddress(team.getHomeStadiumAddress())
                .activeDays(team.getActiveDays())
                .activeTimeSlots(team.getActiveTimeSlots())
                .genderType(team.getGenderType())
                .ageGroups(team.getAgeGroups())
                .monthlyFee(team.getMonthlyFee())
                .ownerUid(team.getOwnerUid())
                .ownerName(ownerName)
                .sponsorOwnerUid(team.getSponsorOwnerUid())
                .featureTags(team.getFeatureTags())
                .recruitingMembers(team.getRecruitingMembers())
                .recruitmentDescription(team.getRecruitmentDescription())
                .teamPhotoFileUid(team.getTeamPhotoFileUid())
                .status(team.getStatus())
                .createdDate(team.getCreatedDate())
                .build();
    }

    private TeamDto.ListResponse toListResponse(Team team) {
        String logoUrl = (team.getLogoFileUid() != null && !team.getLogoFileUid().isEmpty())
                ? "/api/attached-file/" + team.getLogoFileUid()
                : null;
        return TeamDto.ListResponse.builder()
                .uid(team.getUid())
                .name(team.getName())
                .teamCode(team.getTeamCode())
                .logoUrl(logoUrl)
                .mannerScore(team.getMannerScore())
                .memberCount(team.getMemberCount())
                .region(team.getRegionSido() + " " + team.getRegionSigungu())
                .recruitingMembers(team.getRecruitingMembers())
                .build();
    }

    private TeamDto.RecruitmentListResponse toRecruitmentListResponse(Team team) {
        String region = "";
        if (team.getRegionSido() != null) {
            region = team.getRegionSido();
            if (team.getRegionSigungu() != null) {
                region += " " + team.getRegionSigungu();
            }
        }
        long applicationCount = teamMemberRepository.countByTeamUidAndStatus(team.getUid(), TeamMember.MemberStatus.PENDING);

        String logoUrl = (team.getLogoFileUid() != null && !team.getLogoFileUid().isEmpty())
                ? "/api/attached-file/" + team.getLogoFileUid()
                : null;

        return TeamDto.RecruitmentListResponse.builder()
                .uid(team.getUid())
                .name(team.getName())
                .teamCode(team.getTeamCode())
                .logoUrl(logoUrl)
                .mannerScore(team.getMannerScore())
                .memberCount(team.getMemberCount())
                .region(region)
                .genderType(team.getGenderType())
                .ageGroups(team.getAgeGroups())
                .activeDays(team.getActiveDays())
                .activeTimeSlots(team.getActiveTimeSlots())
                .monthlyFee(team.getMonthlyFee())
                .featureTags(team.getFeatureTags())
                .applicationCount(applicationCount)
                .build();
    }

    private TeamMemberDto.Response toMemberResponse(TeamMember member) {
        TeamMemberDto.Response.ResponseBuilder builder = TeamMemberDto.Response.builder()
                .uid(member.getUid())
                .userUid(member.getUserUid())
                .role(member.getRole())
                .position(member.getPosition())
                .backNumber(member.getBackNumber())
                .status(member.getStatus())
                .createdDate(member.getCreatedDate())
                .applicationRegion(member.getApplicationRegion())
                .applicationAge(member.getApplicationAge())
                .applicationExperience(member.getApplicationExperience());

        // User 정보 매핑
        if (member.getUser() != null) {
            builder.userName(member.getUser().getActualName());
            builder.gender(member.getUser().getGender());
            builder.birth(member.getUser().getBirth());
        }

        return builder.build();
    }
}

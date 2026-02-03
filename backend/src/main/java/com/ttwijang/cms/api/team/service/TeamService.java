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
import com.ttwijang.cms.entity.Team;
import com.ttwijang.cms.entity.TeamMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    private static final String TEAM_CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TEAM_CODE_LENGTH = 8;

    /**
     * 팀 생성
     */
    @Transactional
    public TeamDto.DetailResponse createTeam(TeamDto.CreateRequest request, String ownerUid) {
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
     * 팀 목록 조회
     */
    @Transactional(readOnly = true)
    public Page<TeamDto.ListResponse> getTeamList(String region, Boolean recruiting, Pageable pageable) {
        Page<Team> teams;
        if (recruiting != null && recruiting) {
            teams = teamRepository.findByRecruitingMembersTrue(pageable);
        } else {
            teams = teamRepository.findByStatus(Team.TeamStatus.ACTIVE, pageable);
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

        team = teamRepository.save(team);
        return toDetailResponse(team);
    }

    /**
     * 팀 가입 신청
     */
    @Transactional
    public TeamMemberDto.Response joinTeam(TeamMemberDto.JoinRequest request, String userUid) {
        // 이미 가입되어 있는지 확인
        if (teamMemberRepository.existsByTeamUidAndUserUid(request.getTeamUid(), userUid)) {
            throw new IllegalArgumentException("이미 가입 신청하였거나 팀원입니다.");
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
     */
    @Transactional
    public TeamMemberDto.Response joinTeamByCode(String teamCode, String userUid) {
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
        return teamMemberRepository.findByTeamUidAndStatus(teamUid, TeamMember.MemberStatus.APPROVED)
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

    private TeamDto.DetailResponse toDetailResponse(Team team) {
        return TeamDto.DetailResponse.builder()
                .uid(team.getUid())
                .name(team.getName())
                .teamCode(team.getTeamCode())
                .description(team.getDescription())
                .grade(team.getGrade())
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
                .sponsorOwnerUid(team.getSponsorOwnerUid())
                .featureTags(team.getFeatureTags())
                .recruitingMembers(team.getRecruitingMembers())
                .status(team.getStatus())
                .createdDate(team.getCreatedDate())
                .build();
    }

    private TeamDto.ListResponse toListResponse(Team team) {
        return TeamDto.ListResponse.builder()
                .uid(team.getUid())
                .name(team.getName())
                .teamCode(team.getTeamCode())
                .grade(team.getGrade())
                .mannerScore(team.getMannerScore())
                .memberCount(team.getMemberCount())
                .region(team.getRegionSido() + " " + team.getRegionSigungu())
                .recruitingMembers(team.getRecruitingMembers())
                .build();
    }

    private TeamMemberDto.Response toMemberResponse(TeamMember member) {
        return TeamMemberDto.Response.builder()
                .uid(member.getUid())
                .userUid(member.getUserUid())
                .role(member.getRole())
                .position(member.getPosition())
                .backNumber(member.getBackNumber())
                .status(member.getStatus())
                .createdDate(member.getCreatedDate())
                .build();
    }
}

package com.ttwijang.cms.api.team.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.TeamMember;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, String>, QuerydslPredicateExecutor<TeamMember> {

    Optional<TeamMember> findByTeamUidAndUserUid(String teamUid, String userUid);

    List<TeamMember> findByTeamUid(String teamUid);

    List<TeamMember> findByUserUid(String userUid);

    List<TeamMember> findByTeamUidAndStatus(String teamUid, TeamMember.MemberStatus status);

    List<TeamMember> findByTeamUidAndStatusIn(String teamUid, java.util.List<TeamMember.MemberStatus> statuses);

    @EntityGraph(attributePaths = {"user"})
    List<TeamMember> findWithUserByTeamUidAndStatus(String teamUid, TeamMember.MemberStatus status);

    boolean existsByTeamUidAndUserUid(String teamUid, String userUid);

    boolean existsByTeamUidAndUserUidAndStatus(String teamUid, String userUid, TeamMember.MemberStatus status);

    long countByTeamUidAndStatus(String teamUid, TeamMember.MemberStatus status);

    /**
     * BR-01: 사용자가 이미 다른 팀에 소속(APPROVED, PENDING, LEAVE_PENDING)되어 있는지 확인
     */
    @Query("SELECT CASE WHEN COUNT(tm) > 0 THEN true ELSE false END FROM TeamMember tm " +
           "WHERE tm.userUid = :userUid AND tm.status IN ('APPROVED', 'PENDING', 'LEAVE_PENDING')")
    boolean existsActiveOrPendingMembershipByUserUid(@Param("userUid") String userUid);

    /**
     * BR-01: 사용자가 승인된 팀 멤버십이 있는지 확인
     */
    boolean existsByUserUidAndStatus(String userUid, TeamMember.MemberStatus status);

    /**
     * 사용자의 현재 소속팀(승인된) 조회
     */
    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.team WHERE tm.userUid = :userUid AND tm.status = 'APPROVED'")
    Optional<TeamMember> findApprovedMembershipByUserUid(@Param("userUid") String userUid);

    /**
     * 사용자의 현재 소속팀(APPROVED 또는 LEAVE_PENDING) 조회 — 탈퇴 신청 중에도 팀 접근 허용
     */
    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.team WHERE tm.userUid = :userUid AND tm.status IN ('APPROVED', 'LEAVE_PENDING')")
    Optional<TeamMember> findActiveMembershipByUserUid(@Param("userUid") String userUid);

    /**
     * 사용자의 대기 중(PENDING) 가입 신청 조회
     */
    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.team WHERE tm.userUid = :userUid AND tm.status = 'PENDING'")
    Optional<TeamMember> findPendingMembershipByUserUid(@Param("userUid") String userUid);

    /**
     * 여러 사용자의 승인된 팀 멤버십 일괄 조회 (N+1 방지용)
     */
    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.team WHERE tm.userUid IN :userUids AND tm.status = 'APPROVED'")
    List<TeamMember> findApprovedMembershipsByUserUids(@Param("userUids") List<String> userUids);
}

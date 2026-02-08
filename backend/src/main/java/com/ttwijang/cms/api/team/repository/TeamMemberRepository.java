package com.ttwijang.cms.api.team.repository;

import java.util.List;
import java.util.Optional;

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

    boolean existsByTeamUidAndUserUid(String teamUid, String userUid);

    long countByTeamUidAndStatus(String teamUid, TeamMember.MemberStatus status);

    /**
     * BR-01: 사용자가 이미 다른 팀에 소속(APPROVED 또는 PENDING)되어 있는지 확인
     */
    @Query("SELECT CASE WHEN COUNT(tm) > 0 THEN true ELSE false END FROM TeamMember tm " +
           "WHERE tm.userUid = :userUid AND tm.status IN ('APPROVED', 'PENDING')")
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
}

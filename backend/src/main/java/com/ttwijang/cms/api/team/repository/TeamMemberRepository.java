package com.ttwijang.cms.api.team.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
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
}

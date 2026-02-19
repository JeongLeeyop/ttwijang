package com.ttwijang.cms.api.match.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.MatchApplication;

@Repository
public interface MatchApplicationRepository extends JpaRepository<MatchApplication, String>, QuerydslPredicateExecutor<MatchApplication> {

    Optional<MatchApplication> findByUid(String uid);

    boolean existsByMatchUidAndApplicantUserUid(String matchUid, String userUid);

    boolean existsByMatchUidAndApplicantTeamUid(String matchUid, String teamUid);

    long countByMatchUidAndStatusIn(String matchUid, java.util.Collection<MatchApplication.ApplicationStatus> statuses);

    long countByMatchUidAndStatus(String matchUid, MatchApplication.ApplicationStatus status);

    List<MatchApplication> findByMatchUidAndStatus(String matchUid, MatchApplication.ApplicationStatus status);
}

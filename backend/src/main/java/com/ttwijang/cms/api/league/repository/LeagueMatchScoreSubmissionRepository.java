package com.ttwijang.cms.api.league.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.LeagueMatchScoreSubmission;

@Repository
public interface LeagueMatchScoreSubmissionRepository extends JpaRepository<LeagueMatchScoreSubmission, String> {

    List<LeagueMatchScoreSubmission> findByMatchUid(String matchUid);

    Optional<LeagueMatchScoreSubmission> findByMatchUidAndTeamUid(String matchUid, String teamUid);

    void deleteByMatchUid(String matchUid);
}

package com.ttwijang.cms.api.league.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.LeagueTeam;

@Repository
public interface LeagueTeamRepository extends JpaRepository<LeagueTeam, String>, QuerydslPredicateExecutor<LeagueTeam> {

    Optional<LeagueTeam> findByLeagueUidAndTeamUid(String leagueUid, String teamUid);

    List<LeagueTeam> findByLeagueUid(String leagueUid);

    List<LeagueTeam> findByTeamUid(String teamUid);

    @Query("SELECT lt FROM LeagueTeam lt WHERE lt.leagueUid = :leagueUid ORDER BY lt.points DESC, lt.goalDifference DESC, lt.goalsFor DESC")
    List<LeagueTeam> findByLeagueUidOrderByRanking(@Param("leagueUid") String leagueUid);

    boolean existsByLeagueUidAndTeamUid(String leagueUid, String teamUid);

    long countByLeagueUid(String leagueUid);
}

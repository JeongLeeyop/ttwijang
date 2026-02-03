package com.ttwijang.cms.api.league.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.LeagueMatch;

@Repository
public interface LeagueMatchRepository extends JpaRepository<LeagueMatch, String>, QuerydslPredicateExecutor<LeagueMatch> {

    Optional<LeagueMatch> findByUid(String uid);

    List<LeagueMatch> findByLeagueUid(String leagueUid);

    List<LeagueMatch> findByLeagueUidAndMatchDateBetween(String leagueUid, LocalDate startDate, LocalDate endDate);

    Page<LeagueMatch> findByLeagueUidOrderByMatchDateAscMatchTimeAsc(String leagueUid, Pageable pageable);

    List<LeagueMatch> findByHomeTeamUidOrAwayTeamUid(String homeTeamUid, String awayTeamUid);

    @Query("SELECT lm FROM LeagueMatch lm WHERE lm.leagueUid = :leagueUid AND (lm.homeTeamUid = :teamUid OR lm.awayTeamUid = :teamUid)")
    List<LeagueMatch> findByLeagueUidAndTeamUid(@Param("leagueUid") String leagueUid, @Param("teamUid") String teamUid);

    List<LeagueMatch> findByMatchDate(LocalDate matchDate);

    @Query("SELECT lm FROM LeagueMatch lm WHERE lm.leagueUid = :leagueUid AND lm.status = 'COMPLETED' ORDER BY lm.matchDate DESC")
    List<LeagueMatch> findCompletedMatchesByLeagueUid(@Param("leagueUid") String leagueUid, Pageable pageable);
}

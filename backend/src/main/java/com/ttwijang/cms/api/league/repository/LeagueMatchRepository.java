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
import com.ttwijang.cms.entity.Team;

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

    /**
     * 지역별 다가오는 리그 매치 조회 — 참여 팀(홈/어웨이)의 지역 기준
     */
    @Query("SELECT DISTINCT lm FROM LeagueMatch lm "
            + "LEFT JOIN Team ht ON lm.homeTeamUid = ht.uid "
            + "LEFT JOIN Team awt ON lm.awayTeamUid = awt.uid "
            + "WHERE (ht.regionSigungu = :regionSigungu OR awt.regionSigungu = :regionSigungu) "
            + "AND lm.matchDate >= :today "
            + "AND lm.status = 'SCHEDULED' "
            + "ORDER BY lm.matchDate ASC, lm.matchTime ASC")
    List<LeagueMatch> findUpcomingByRegionSigungu(@Param("regionSigungu") String regionSigungu,
                                                  @Param("today") LocalDate today,
                                                  Pageable pageable);

    @Query("SELECT lm FROM LeagueMatch lm "
            + "WHERE lm.matchDate >= :today "
            + "AND lm.status = 'SCHEDULED' "
            + "ORDER BY lm.matchDate ASC, lm.matchTime ASC")
    List<LeagueMatch> findUpcomingAll(@Param("today") LocalDate today, Pageable pageable);
}

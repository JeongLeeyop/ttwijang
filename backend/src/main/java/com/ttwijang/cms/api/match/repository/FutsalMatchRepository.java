package com.ttwijang.cms.api.match.repository;

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

import com.ttwijang.cms.entity.FutsalMatch;

@Repository
public interface FutsalMatchRepository extends JpaRepository<FutsalMatch, String>, QuerydslPredicateExecutor<FutsalMatch> {

    Optional<FutsalMatch> findByUid(String uid);

    Page<FutsalMatch> findByStatus(FutsalMatch.FutsalMatchStatus status, Pageable pageable);

    Page<FutsalMatch> findByMatchDate(LocalDate matchDate, Pageable pageable);

    Page<FutsalMatch> findByMatchDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<FutsalMatch> findByHostTeamUid(String hostTeamUid, Pageable pageable);

    @Query("SELECT m FROM FutsalMatch m WHERE m.status = :status AND m.regionSido = :sido")
    Page<FutsalMatch> findByStatusAndRegion(@Param("status") FutsalMatch.FutsalMatchStatus status, 
                                             @Param("sido") String sido, 
                                             Pageable pageable);

    @Query("SELECT m FROM FutsalMatch m WHERE (m.hostTeamUid = :teamUid OR m.guestTeamUid = :teamUid) AND m.status = 'COMPLETED'")
    List<FutsalMatch> findCompletedMatchesByTeamUid(@Param("teamUid") String teamUid);

    List<FutsalMatch> findByMatchDateAndStatus(LocalDate matchDate, FutsalMatch.FutsalMatchStatus status);

    /**
     * BR-06: 소속 팀의 매치 조회 (주최 또는 상대팀)
     */
    @Query("SELECT m FROM FutsalMatch m WHERE m.hostTeamUid = :teamUid OR m.guestTeamUid = :teamUid")
    Page<FutsalMatch> findByTeamUid(@Param("teamUid") String teamUid, Pageable pageable);

    /**
     * 소속 팀의 매치 조회 + 타입 필터
     */
    @Query("SELECT m FROM FutsalMatch m WHERE (m.hostTeamUid = :teamUid OR m.guestTeamUid = :teamUid) AND m.matchType = :matchType")
    Page<FutsalMatch> findByTeamUidAndMatchType(@Param("teamUid") String teamUid,
                                                  @Param("matchType") FutsalMatch.MatchType matchType,
                                                  Pageable pageable);

    /**
     * 게스트 모집을 위한 7일 이내 매치 조회
     */
    @Query("SELECT m FROM FutsalMatch m WHERE m.hostTeamUid = :teamUid AND m.matchDate BETWEEN :startDate AND :endDate AND m.status = 'RECRUITING'")
    List<FutsalMatch> findUpcomingMatchesForGuest(@Param("teamUid") String teamUid,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
}

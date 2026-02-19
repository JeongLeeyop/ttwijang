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
import com.ttwijang.cms.entity.Team;

@Repository
public interface FutsalMatchRepository extends JpaRepository<FutsalMatch, String>, QuerydslPredicateExecutor<FutsalMatch> {

    Optional<FutsalMatch> findByUid(String uid);

    Page<FutsalMatch> findByStatus(FutsalMatch.FutsalMatchStatus status, Pageable pageable);

    Page<FutsalMatch> findByMatchDate(LocalDate matchDate, Pageable pageable);

    Page<FutsalMatch> findByMatchDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<FutsalMatch> findByHostTeamUid(String hostTeamUid, Pageable pageable);

    /**
     * 지역(시/도)으로 상태별 매치 조회 — 호스트 팀의 지역 기준
     */
    @Query("SELECT m FROM FutsalMatch m JOIN Team t ON m.hostTeamUid = t.uid "
            + "WHERE m.status = :status AND t.regionSido = :sido")
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

    /**
     * 지역(시/도 + 시/군/구)으로 상태별 매치 조회 — 호스트 팀의 지역 기준
     */
    @Query("SELECT m FROM FutsalMatch m JOIN Team t ON m.hostTeamUid = t.uid "
            + "WHERE m.status = :status AND t.regionSido = :sido AND t.regionSigungu = :sigungu")
    Page<FutsalMatch> findByStatusAndRegionSidoAndSigungu(@Param("status") FutsalMatch.FutsalMatchStatus status,
                                                          @Param("sido") String sido,
                                                          @Param("sigungu") String sigungu,
                                                          Pageable pageable);

    /**
     * 날짜 범위 + 지역 필터 조회 — 호스트 팀의 지역 기준
     */
    @Query("SELECT m FROM FutsalMatch m JOIN Team t ON m.hostTeamUid = t.uid "
            + "WHERE m.matchDate BETWEEN :startDate AND :endDate AND t.regionSido = :sido AND t.regionSigungu = :sigungu")
    Page<FutsalMatch> findByMatchDateBetweenAndRegionSidoAndRegionSigungu(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sido") String sido,
            @Param("sigungu") String sigungu,
            Pageable pageable);

    /**
     * 시/군/구 이름으로 상태별 조회 (도 필터 없이) — 호스트 팀의 지역 기준
     */
    @Query("SELECT m FROM FutsalMatch m JOIN Team t ON m.hostTeamUid = t.uid "
            + "WHERE m.status = :status AND t.regionSigungu = :sigungu")
    Page<FutsalMatch> findByStatusAndSigungu(@Param("status") FutsalMatch.FutsalMatchStatus status,
                                              @Param("sigungu") String sigungu,
                                              Pageable pageable);

    /**
     * 날짜 범위 + 시/군/구 이름으로 조회 (도 필터 없이) — 호스트 팀의 지역 기준
     */
    @Query("SELECT m FROM FutsalMatch m JOIN Team t ON m.hostTeamUid = t.uid "
            + "WHERE m.matchDate BETWEEN :startDate AND :endDate AND t.regionSigungu = :sigungu")
    Page<FutsalMatch> findByMatchDateBetweenAndRegionSigungu(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sigungu") String sigungu,
            Pageable pageable);

    /**
     * 팀별 날짜 범위 매치 조회 (캘린더용)
     */
    @Query("SELECT m FROM FutsalMatch m WHERE (m.hostTeamUid = :teamUid OR m.guestTeamUid = :teamUid) AND m.matchDate BETWEEN :startDate AND :endDate")
    Page<FutsalMatch> findByTeamUidAndMatchDateBetween(
            @Param("teamUid") String teamUid,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}

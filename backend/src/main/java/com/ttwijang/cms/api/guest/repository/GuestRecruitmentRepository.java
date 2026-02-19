package com.ttwijang.cms.api.guest.repository;

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

import com.ttwijang.cms.entity.GuestRecruitment;
import com.ttwijang.cms.entity.Team;

@Repository
public interface GuestRecruitmentRepository extends JpaRepository<GuestRecruitment, String>, QuerydslPredicateExecutor<GuestRecruitment> {

    Optional<GuestRecruitment> findByUid(String uid);

    boolean existsByMatchUidAndStatusIn(String matchUid,
                                         java.util.Collection<GuestRecruitment.RecruitmentStatus> statuses);

    boolean existsByTeamUidAndMatchUidAndStatusIn(String teamUid, String matchUid,
                                                    java.util.Collection<GuestRecruitment.RecruitmentStatus> statuses);

    boolean existsByTeamUidAndMatchDateAndStatusIn(String teamUid, LocalDate matchDate,
                                                    java.util.Collection<GuestRecruitment.RecruitmentStatus> statuses);

    Page<GuestRecruitment> findByStatus(GuestRecruitment.RecruitmentStatus status, Pageable pageable);

    Page<GuestRecruitment> findByMatchDate(LocalDate matchDate, Pageable pageable);

    Page<GuestRecruitment> findByMatchDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<GuestRecruitment> findByTeamUid(String teamUid, Pageable pageable);

    /**
     * 지역(시/도)으로 상태별 게스트 모집 조회 — 모집 팀의 지역 기준
     */
    @Query("SELECT gr FROM GuestRecruitment gr JOIN Team t ON gr.teamUid = t.uid "
            + "WHERE gr.status = :status AND t.regionSido = :sido")
    Page<GuestRecruitment> findByStatusAndRegion(@Param("status") GuestRecruitment.RecruitmentStatus status,
                                                   @Param("sido") String sido,
                                                   Pageable pageable);

    // 7일 이내 모집만 가능한 조건 확인
    @Query("SELECT gr FROM GuestRecruitment gr WHERE gr.teamUid = :teamUid AND gr.matchDate BETWEEN :startDate AND :endDate")
    List<GuestRecruitment> findByTeamUidAndDateRange(@Param("teamUid") String teamUid,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    /**
     * 지역(시/도 + 시/군/구)으로 상태별 게스트 모집 조회 — 모집 팀의 지역 기준
     */
    @Query("SELECT gr FROM GuestRecruitment gr JOIN Team t ON gr.teamUid = t.uid "
            + "WHERE gr.status = :status AND t.regionSido = :sido AND t.regionSigungu = :sigungu")
    Page<GuestRecruitment> findByStatusAndRegionSidoAndSigungu(
            @Param("status") GuestRecruitment.RecruitmentStatus status,
            @Param("sido") String sido,
            @Param("sigungu") String sigungu,
            Pageable pageable);

    /**
     * 날짜 범위 + 지역 필터 조회 — 모집 팀의 지역 또는 모집 글 자체 지역 기준
     */
    @Query("SELECT gr FROM GuestRecruitment gr LEFT JOIN Team t ON gr.teamUid = t.uid "
            + "WHERE gr.matchDate BETWEEN :startDate AND :endDate "
            + "AND ((t.regionSido = :sido AND t.regionSigungu = :sigungu) OR (gr.regionSido = :sido AND gr.regionSigungu = :sigungu))")
    Page<GuestRecruitment> findByMatchDateBetweenAndRegionSidoAndRegionSigungu(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sido") String sido,
            @Param("sigungu") String sigungu,
            Pageable pageable);

    /**
     * 시/군/구 이름으로 상태별 조회 (도 필터 없이) — 모집 팀의 지역 기준
     */
    @Query("SELECT gr FROM GuestRecruitment gr JOIN Team t ON gr.teamUid = t.uid "
            + "WHERE gr.status = :status AND t.regionSigungu = :sigungu")
    Page<GuestRecruitment> findByStatusAndSigungu(
            @Param("status") GuestRecruitment.RecruitmentStatus status,
            @Param("sigungu") String sigungu,
            Pageable pageable);

    /**
     * 날짜 범위 + 시/군/구 이름으로 조회 (도 필터 없이) — 모집 팀의 지역 또는 모집 글 자체 지역 기준
     */
    @Query("SELECT gr FROM GuestRecruitment gr LEFT JOIN Team t ON gr.teamUid = t.uid "
            + "WHERE gr.matchDate BETWEEN :startDate AND :endDate AND (t.regionSigungu = :sigungu OR gr.regionSigungu = :sigungu)")
    Page<GuestRecruitment> findByMatchDateBetweenAndRegionSigungu(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sigungu") String sigungu,
            Pageable pageable);
}

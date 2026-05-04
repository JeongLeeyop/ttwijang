package com.ttwijang.cms.api.banner.repository;

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

import com.ttwijang.cms.entity.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, String>, QuerydslPredicateExecutor<Banner> {

    Optional<Banner> findByUid(String uid);

    Page<Banner> findByStatus(Banner.BannerStatus status, Pageable pageable);

    /**
     * 활성화된 배너 조회 (기간 내 + 타겟 페이지)
     */
    @Query("SELECT b FROM Banner b WHERE b.status = 'ACTIVE' "
            + "AND (:today BETWEEN b.startDate AND b.endDate) "
            + "AND (b.targetPage = :targetPage OR b.targetPage = 'ALL') "
            + "ORDER BY b.displayOrder ASC")
    List<Banner> findActiveBannersByPage(@Param("today") LocalDate today,
                                          @Param("targetPage") Banner.TargetPage targetPage);

    /**
     * 지역별 활성화된 배너 조회 (시/도+시/군/구 계층 필터)
     * - 전국 배너: regionSido가 없는 배너
     * - 시/도 배너: regionSido만 설정된 배너 → 해당 시/도 소속 시/군/구 사용자에게만 노출
     * - 시/군/구 배너: regionSigungu가 설정된 배너 → 해당 시/군/구 사용자에게만 노출
     */
    @Query("SELECT b FROM Banner b WHERE b.status = 'ACTIVE' "
            + "AND (:today BETWEEN b.startDate AND b.endDate) "
            + "AND (b.targetPage = :targetPage OR b.targetPage = 'ALL') "
            + "AND ("
            + "  (b.regionSido IS NULL OR b.regionSido = '') "
            + "  OR ((b.regionSigungu IS NULL OR b.regionSigungu = '') "
            + "      AND (b.regionSido = :sidoCode OR b.regionSido = :sidoName)) "
            + "  OR (b.regionSigungu = :sigunguCode OR b.regionSigungu = :sigunguName)"
            + ") "
            + "ORDER BY b.displayOrder ASC")
    List<Banner> findActiveBannersByPageAndSidoSigungu(@Param("today") LocalDate today,
                                                        @Param("targetPage") Banner.TargetPage targetPage,
                                                        @Param("sidoCode") String sidoCode,
                                                        @Param("sidoName") String sidoName,
                                                        @Param("sigunguCode") String sigunguCode,
                                                        @Param("sigunguName") String sigunguName);

    /**
     * 팀별 활성화된 배너 조회
     */
    @Query("SELECT b FROM Banner b WHERE b.status = 'ACTIVE' "
            + "AND (:today BETWEEN b.startDate AND b.endDate) "
            + "AND b.targetPage = 'TEAM' "
            + "AND b.teamUid = :teamUid "
            + "ORDER BY b.displayOrder ASC")
    List<Banner> findActiveBannersByTeam(@Param("today") LocalDate today,
                                          @Param("teamUid") String teamUid);
}

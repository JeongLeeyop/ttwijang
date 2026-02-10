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
     * 지역별 활성화된 배너 조회
     */
    @Query("SELECT b FROM Banner b WHERE b.status = 'ACTIVE' "
            + "AND (:today BETWEEN b.startDate AND b.endDate) "
            + "AND (b.targetPage = :targetPage OR b.targetPage = 'ALL') "
            + "AND (b.regionSigungu IS NULL OR b.regionSigungu = :sigungu) "
            + "ORDER BY b.displayOrder ASC")
    List<Banner> findActiveBannersByPageAndRegion(@Param("today") LocalDate today,
                                                    @Param("targetPage") Banner.TargetPage targetPage,
                                                    @Param("sigungu") String sigungu);
}

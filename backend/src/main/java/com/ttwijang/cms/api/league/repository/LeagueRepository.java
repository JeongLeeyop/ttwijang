package com.ttwijang.cms.api.league.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.League;

@Repository
public interface LeagueRepository extends JpaRepository<League, String>, QuerydslPredicateExecutor<League> {

    Optional<League> findByUid(String uid);

    Page<League> findByStatus(League.LeagueStatus status, Pageable pageable);

    Page<League> findByRegionSidoAndStatus(String sido, League.LeagueStatus status, Pageable pageable);

    List<League> findBySeason(String season);

    /**
     * BR-04: 지역별 리그 조회 (시/군/구 포함)
     */
    @Query("SELECT l FROM League l WHERE l.regionSido = :sido AND (:sigungu IS NULL OR l.regionSigungu = :sigungu)")
    Page<League> findByRegion(@Param("sido") String sido, @Param("sigungu") String sigungu, Pageable pageable);

    /**
     * BR-04: 지역별 리그 목록 조회 (상태 필터 포함)
     */
    @Query("SELECT l FROM League l WHERE l.regionSido = :sido AND (:sigungu IS NULL OR l.regionSigungu = :sigungu) " +
           "AND (:status IS NULL OR l.status = :status)")
    Page<League> findByRegionAndStatus(@Param("sido") String sido,
                                         @Param("sigungu") String sigungu,
                                         @Param("status") League.LeagueStatus status,
                                         Pageable pageable);

    /**
     * 시/군/구 이름으로 리그 조회 (도 필터 없이)
     */
    @Query("SELECT l FROM League l WHERE l.regionSigungu = :sigungu AND (:status IS NULL OR l.status = :status)")
    Page<League> findBySigunguAndStatus(@Param("sigungu") String sigungu,
                                         @Param("status") League.LeagueStatus status,
                                         Pageable pageable);
}

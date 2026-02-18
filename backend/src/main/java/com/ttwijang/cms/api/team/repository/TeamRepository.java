package com.ttwijang.cms.api.team.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, String>, QuerydslPredicateExecutor<Team> {

    Optional<Team> findByUid(String uid);

    Optional<Team> findByTeamCode(String teamCode);

    boolean existsByTeamCode(String teamCode);

    Page<Team> findByStatus(Team.TeamStatus status, Pageable pageable);

    Page<Team> findByRegionSidoAndRegionSigungu(String sido, String sigungu, Pageable pageable);

    Page<Team> findByRecruitingMembersTrue(Pageable pageable);

    Page<Team> findByRecruitingMembersTrueAndRegionSidoAndRegionSigungu(String sido, String sigungu, Pageable pageable);

    List<Team> findByOwnerUid(String ownerUid);

    /**
     * BR-02: 사용자가 이미 팀을 생성했는지 확인 (ACTIVE 상태 팀 기준)
     */
    boolean existsByOwnerUidAndStatus(String ownerUid, Team.TeamStatus status);

    @Query("SELECT t FROM Team t WHERE t.status = :status AND t.regionSido = :sido")
    Page<Team> findByStatusAndRegion(@Param("status") Team.TeamStatus status, 
                                      @Param("sido") String sido, 
                                      Pageable pageable);

    /**
     * 지역별 팀 목록 조회 (시/군/구 포함)
     */
    @Query("SELECT t FROM Team t WHERE t.status = 'ACTIVE' AND t.regionSido = :sido AND (:sigungu IS NULL OR t.regionSigungu = :sigungu)")
    Page<Team> findActiveByRegion(@Param("sido") String sido, 
                                   @Param("sigungu") String sigungu, 
                                   Pageable pageable);

    /**
     * 시/군/구 이름으로 팀 조회 (도 필터 없이)
     */
    @Query("SELECT t FROM Team t WHERE t.status = 'ACTIVE' AND t.regionSigungu = :sigungu")
    Page<Team> findActiveBySigungu(@Param("sigungu") String sigungu, Pageable pageable);

    /**
     * 시/군/구 이름으로 모집 중인 팀 조회 (도 필터 없이)
     */
    Page<Team> findByRecruitingMembersTrueAndRegionSigungu(String sigungu, Pageable pageable);

    /**
     * 회원 모집 중인 팀 목록 조회 (다중 필터)
     * native query 사용: JPQL의 function('BIT_AND',...) 은 MySQL 집계 함수로 매핑되므로
     * MySQL 비트 AND 연산자(&)를 직접 사용하기 위해 nativeQuery = true 처리
     */
    @Query(value = "SELECT t.* FROM team t WHERE t.status = 'ACTIVE' AND t.recruiting_members = true"
            + " AND (:regionSido IS NULL OR t.region_sido = :regionSido)"
            + " AND (:regionSigungu IS NULL OR t.region_sigungu = :regionSigungu)"
            + " AND (:genderType IS NULL OR t.gender_type = :genderType)"
            + " AND (:ageGroups IS NULL OR (t.age_groups IS NOT NULL AND (t.age_groups & :ageGroups) > 0))"
            + " AND (:activeDays IS NULL OR (t.active_days IS NOT NULL AND (t.active_days & :activeDays) > 0))"
            + " AND (:activeTimeSlots IS NULL OR (t.active_time_slots IS NOT NULL AND (t.active_time_slots & :activeTimeSlots) > 0))"
            + " AND (:featureTag IS NULL OR t.feature_tags LIKE CONCAT('%', :featureTag, '%'))"
            + " ORDER BY t.created_date DESC",
            countQuery = "SELECT COUNT(t.uid) FROM team t WHERE t.status = 'ACTIVE' AND t.recruiting_members = true"
            + " AND (:regionSido IS NULL OR t.region_sido = :regionSido)"
            + " AND (:regionSigungu IS NULL OR t.region_sigungu = :regionSigungu)"
            + " AND (:genderType IS NULL OR t.gender_type = :genderType)"
            + " AND (:ageGroups IS NULL OR (t.age_groups IS NOT NULL AND (t.age_groups & :ageGroups) > 0))"
            + " AND (:activeDays IS NULL OR (t.active_days IS NOT NULL AND (t.active_days & :activeDays) > 0))"
            + " AND (:activeTimeSlots IS NULL OR (t.active_time_slots IS NOT NULL AND (t.active_time_slots & :activeTimeSlots) > 0))"
            + " AND (:featureTag IS NULL OR t.feature_tags LIKE CONCAT('%', :featureTag, '%'))",
            nativeQuery = true)
    Page<Team> findRecruitingTeams(
            @Param("regionSido") String regionSido,
            @Param("regionSigungu") String regionSigungu,
            @Param("genderType") Integer genderType,
            @Param("ageGroups") Integer ageGroups,
            @Param("activeDays") Integer activeDays,
            @Param("activeTimeSlots") Integer activeTimeSlots,
            @Param("featureTag") String featureTag,
            Pageable pageable);
}

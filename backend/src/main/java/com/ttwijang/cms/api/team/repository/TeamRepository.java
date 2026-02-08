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
}

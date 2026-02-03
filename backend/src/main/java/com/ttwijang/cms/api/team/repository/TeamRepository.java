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

    List<Team> findByOwnerUid(String ownerUid);

    @Query("SELECT t FROM Team t WHERE t.status = :status AND t.regionSido = :sido")
    Page<Team> findByStatusAndRegion(@Param("status") Team.TeamStatus status, 
                                      @Param("sido") String sido, 
                                      Pageable pageable);
}

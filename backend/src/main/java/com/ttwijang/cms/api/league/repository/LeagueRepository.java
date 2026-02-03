package com.ttwijang.cms.api.league.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.League;

@Repository
public interface LeagueRepository extends JpaRepository<League, String>, QuerydslPredicateExecutor<League> {

    Optional<League> findByUid(String uid);

    Page<League> findByStatus(League.LeagueStatus status, Pageable pageable);

    Page<League> findByRegionSidoAndStatus(String sido, League.LeagueStatus status, Pageable pageable);

    List<League> findBySeason(String season);

    List<League> findByGrade(String grade);
}

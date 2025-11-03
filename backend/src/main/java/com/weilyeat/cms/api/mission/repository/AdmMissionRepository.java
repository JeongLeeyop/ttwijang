package com.weilyeat.cms.api.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.Mission;

public interface AdmMissionRepository extends JpaRepository<Mission, String>, QuerydslPredicateExecutor<Mission> {
    List<Mission> findAll(Predicate search);
}

package com.weilyeat.cms.api.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.weilyeat.cms.entity.MissionCategory;

public interface AdmMissionCategoryRepository extends JpaRepository<MissionCategory, String>, QuerydslPredicateExecutor<MissionCategory>{
}

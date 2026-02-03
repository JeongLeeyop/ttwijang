package com.ttwijang.cms.api.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.MissionCategory;

public interface AdmMissionCategoryRepository extends JpaRepository<MissionCategory, String>, QuerydslPredicateExecutor<MissionCategory>{
}

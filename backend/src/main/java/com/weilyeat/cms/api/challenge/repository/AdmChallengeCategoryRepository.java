package com.weilyeat.cms.api.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.weilyeat.cms.entity.ChallengeCategory;

public interface AdmChallengeCategoryRepository extends JpaRepository<ChallengeCategory, String>, QuerydslPredicateExecutor<ChallengeCategory>{
}

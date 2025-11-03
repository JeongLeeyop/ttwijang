package com.weilyeat.cms.api.challenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.Challenge;

public interface AdmChallengeRepository extends JpaRepository<Challenge, String>, QuerydslPredicateExecutor<Challenge> {
    List<Challenge> findAll(Predicate search);
}

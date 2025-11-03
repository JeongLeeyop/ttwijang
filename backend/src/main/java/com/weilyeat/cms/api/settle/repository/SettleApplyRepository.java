package com.weilyeat.cms.api.settle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.weilyeat.cms.entity.SettleApply;

public interface SettleApplyRepository extends JpaRepository<SettleApply, Integer>, QuerydslPredicateExecutor<SettleApply> {
    
}

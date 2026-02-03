package com.ttwijang.cms.api.settle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.SettleApply;

public interface SettleApplyRepository extends JpaRepository<SettleApply, Integer>, QuerydslPredicateExecutor<SettleApply> {
    
}

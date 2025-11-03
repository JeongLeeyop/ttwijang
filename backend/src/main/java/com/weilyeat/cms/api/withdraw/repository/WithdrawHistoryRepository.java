package com.ttwijang.cms.api.withdraw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ttwijang.cms.entity.WithdrawHistory;

public interface WithdrawHistoryRepository extends JpaRepository<WithdrawHistory, Integer> {
    
}

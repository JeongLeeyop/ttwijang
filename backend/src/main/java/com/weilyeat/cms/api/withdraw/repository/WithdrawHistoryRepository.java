package com.weilyeat.cms.api.withdraw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weilyeat.cms.entity.WithdrawHistory;

public interface WithdrawHistoryRepository extends JpaRepository<WithdrawHistory, Integer> {
    
}

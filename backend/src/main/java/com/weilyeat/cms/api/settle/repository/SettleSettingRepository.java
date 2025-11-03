package com.weilyeat.cms.api.settle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weilyeat.cms.entity.SettleSetting;

public interface SettleSettingRepository extends JpaRepository<SettleSetting, Integer> {

    Optional<SettleSetting> findTopByOrderByIdxDesc();
    
}

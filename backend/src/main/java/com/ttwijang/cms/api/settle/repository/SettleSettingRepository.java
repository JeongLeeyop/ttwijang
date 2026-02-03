package com.ttwijang.cms.api.settle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ttwijang.cms.entity.SettleSetting;

public interface SettleSettingRepository extends JpaRepository<SettleSetting, Integer> {

    Optional<SettleSetting> findTopByOrderByIdxDesc();
    
}

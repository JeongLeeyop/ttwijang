package com.weilyeat.cms.api.delivery_price.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weilyeat.cms.entity.DeliveryPrice;

public interface DeliveryPriceRepository extends JpaRepository<DeliveryPrice, Integer> {

    Optional<DeliveryPrice> findTopByOrderByIdxDesc();
    
}

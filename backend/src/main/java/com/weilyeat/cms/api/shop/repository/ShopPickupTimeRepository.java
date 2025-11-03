package com.weilyeat.cms.api.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.weilyeat.cms.entity.ShopPickupTime;

public interface ShopPickupTimeRepository extends JpaRepository<ShopPickupTime, Integer> {

    @Modifying
    void deleteByShopIdx(Integer idx);
    
}

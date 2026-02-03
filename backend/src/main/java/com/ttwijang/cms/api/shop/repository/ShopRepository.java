package com.ttwijang.cms.api.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Integer>, QuerydslPredicateExecutor<Shop> {

    List<Shop> findByWithdrawStatus(boolean b);
    
}

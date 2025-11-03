package com.weilyeat.cms.api.product.repository;

import java.util.List;

import com.weilyeat.cms.entity.ProductOrderWeek;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderWeekRepository extends JpaRepository<ProductOrderWeek, Integer> {

    List<ProductOrderWeek> findAllByGroupId(Integer idx);
    
}

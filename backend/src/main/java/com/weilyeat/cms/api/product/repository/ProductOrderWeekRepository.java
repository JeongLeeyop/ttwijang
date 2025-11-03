package com.ttwijang.cms.api.product.repository;

import java.util.List;

import com.ttwijang.cms.entity.ProductOrderWeek;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderWeekRepository extends JpaRepository<ProductOrderWeek, Integer> {

    List<ProductOrderWeek> findAllByGroupId(Integer idx);
    
}

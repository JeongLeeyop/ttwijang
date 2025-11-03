package com.ttwijang.cms.api.product.repository;

import java.util.List;

import com.ttwijang.cms.entity.Product;
import com.ttwijang.cms.entity.ProductRotation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ProductRotationRepository extends JpaRepository<ProductRotation, Long> {

  List<ProductRotation> findByWeekNum(Integer weekNum);

  @Modifying
  void deleteByProductIdx(Long idx);
    
}

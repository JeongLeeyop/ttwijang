package com.weilyeat.cms.api.product.repository;

import java.util.List;

import com.weilyeat.cms.entity.ProductRotation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmProductRotationRepository extends JpaRepository<ProductRotation, Long>{

    void deleteByWeekNumAndProductIdxNotIn(Integer weekNum, List<Long> productsIdx);

    List<ProductRotation> findAllByWeekNum(Integer weekNum);

    void deleteByWeekNum(Integer weekNum);
    
}

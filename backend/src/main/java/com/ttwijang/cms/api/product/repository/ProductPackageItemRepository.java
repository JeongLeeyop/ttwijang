package com.ttwijang.cms.api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ttwijang.cms.entity.ProductPackageItem;

public interface ProductPackageItemRepository extends JpaRepository<ProductPackageItem, Long> {
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductPackageItem ppi WHERE ppi.packageIdx = ?1")
    void deleteByPackageIdx(Long packageIdx);
}

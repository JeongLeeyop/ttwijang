package com.ttwijang.cms.api.product.repository;

import java.util.List;
import java.util.Optional;

import com.ttwijang.cms.entity.ProductOrderGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductOrderGroupRepository extends JpaRepository<ProductOrderGroup, Integer>{

    Page<ProductOrderGroup> findAllByUserUid(Pageable pageable, String uid);

    ProductOrderGroup findByIdxAndUserUid(Integer groupId, String uid);

    Optional<ProductOrderGroup> findByOrderId(String orderId);
    Page<ProductOrderGroup> findAllByUserUidAndPaymentStatus(Pageable pageable, String uid, int i);

    @Modifying
    @Query("UPDATE ProductOrderGroup SET userUid = null WHERE userUid = ?1")
    void withdrawUser(String uid);
    
}

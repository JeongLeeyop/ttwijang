package com.weilyeat.cms.api.product.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.weilyeat.cms.entity.Product;
import com.weilyeat.cms.entity.ProductType;

public interface AdmProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

    List<Product> findAllBySellStatus(boolean i);
    List<Product> findAllByIdxIn(List<Long> productsIdx);

    @Modifying
    @Query("UPDATE ProductOrder po SET po.paymentStatus = :paymentStatus, po.orderStatus = :orderStatus WHERE po.groupId = :groupId")
    @Transactional
    void updatePaymentStatus(@Param("paymentStatus") int paymentStatus, @Param("orderStatus") int orderStatus, @Param("groupId") int groupId);
    
    List<Product> findAllBySellStatusAndDeleteStatusAndProductType(boolean b, boolean c, ProductType productType);

}

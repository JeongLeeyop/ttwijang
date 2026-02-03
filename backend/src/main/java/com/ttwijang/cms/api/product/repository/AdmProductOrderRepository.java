package com.ttwijang.cms.api.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.ProductOrder;

public interface AdmProductOrderRepository extends JpaRepository<ProductOrder, Integer>, QuerydslPredicateExecutor<ProductOrder> {

    @Modifying
    @Query(value = "UPDATE product_order po SET po.receive_status = ?1 WHERE po.idx IN ?2", nativeQuery = true)
    void updateReceiveStatusByIdx(int receiveStatus, List<Integer> productIdxList);
    
    @Modifying
    @Query(value = "UPDATE product_order po SET po.pickup_status = ?1 WHERE po.idx IN ?2", nativeQuery = true)
    void updatePickupStatusByIdx(int pickupStatus, List<Integer> productIdxList);
    
    // @Query(value = "select * from product_order where day_id = ?1 and pickup_status = 1 and order_status != -1 and receive_status = 2 and settle_status = 0", nativeQuery = true)
    @Query(value = "select * from product_order where day_id = ?1 and order_status != -1", nativeQuery = true)
    List<ProductOrder> detailByDay(Integer idx);
    
    // @Query(value = "select po.group_id, po.week_num, po.day_num, pog.user_uid, s.name, po.pickup_time from product_order po join product_order_group pog on po.group_id = pog.idx join shop s on pog.shop_id = s.idx\r\n" + //
    //         "where po.idx in (:productIdxList) group by po.group_id, po.week_num, po.day_num, pog.user_uid, s.name, po.pickup_time", nativeQuery = true)
    // void selectProductOrderListForFcm(List<Integer> productIdxList);
}

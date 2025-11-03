package com.weilyeat.cms.api.product.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.weilyeat.cms.api.product.dto.ProductOrderDto;
import com.weilyeat.cms.entity.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer>, QuerydslPredicateExecutor<ProductOrder> {

    @Query("SELECT po FROM ProductOrder po WHERE po.group.shopId = ?1 AND po.idx IN ?2")
    List<ProductOrder> findByShopIdxAndIdxIn(Integer shopIdx, List<Integer> orderIdxList);
    
        @Query(value = "select * from product_order where day_id IN ?1 and pickup_status = 1 and order_status != -1 and receive_status = 2 and settle_status = 0", nativeQuery = true)
    List<ProductOrder> findByDayIdxAndIdxIn(List<Integer> dayIdxList);

    @Query("SELECT COUNT(po) FROM ProductOrder po join ProductOrderGroup pog on po.groupId = pog.idx WHERE po.group.userUid = ?1 AND po.pickupStatus = 0 And pog.paymentStatus = 1 and pog.orderStatus!=-1")
    int getRemainCount(String uid);

    @Query(value = "select * from product_order where day_id = ?1 and pickup_status = 1 and order_status != -1 and receive_status = 2 and settle_status = 0", nativeQuery = true)
    List<ProductOrder> detailByDay(Integer idx);
    

    List<ProductOrder> findAllByGroupIdAndWeekNum(Integer orderId, Integer week);

    List<ProductOrder> findAllByGroupId(Integer orderGroupId);
}

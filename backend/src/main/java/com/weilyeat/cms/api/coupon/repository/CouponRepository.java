package com.ttwijang.cms.api.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer>, QuerydslPredicateExecutor<Coupon> {

    @Query("SELECT c FROM Coupon c WHERE c.deleteStatus = 0 AND c.type = 'BUY' AND c.giveStandardPrice <= ?1 AND ?2 BETWEEN c.startDate AND c.endDate")
    List<Coupon> getBuyEventCouponList(int amount, LocalDateTime now);

    @Query("SELECT c FROM Coupon c WHERE c.deleteStatus = 0 AND c.type = 'JOIN' AND ?1 BETWEEN c.startDate AND c.endDate")
    List<Coupon> getJoinEventCouponList(LocalDateTime now);
    
}

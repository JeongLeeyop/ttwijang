package com.ttwijang.cms.api.coupon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ttwijang.cms.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Integer>, QuerydslPredicateExecutor<UserCoupon> {
    
    List<UserCoupon> findByOrderIdx(Integer idx);
}

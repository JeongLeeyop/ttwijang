package com.weilyeat.cms.api.coupon.dto.search;

import java.time.LocalDateTime;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.QUserCoupon;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserCouponSearch {
    private String userUid;
    private Boolean downloadStatus;
    
    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QUserCoupon userCoupon = QUserCoupon.userCoupon;

        builder.and(userCoupon.userUid.eq(userUid));
        builder.and(userCoupon.expiredDate.after(LocalDateTime.now()));
        builder.and(userCoupon.useStatus.eq(false));
        if (downloadStatus != null) builder.and(userCoupon.downloadStatus.eq(downloadStatus));

        return builder;
    }
}

package com.ttwijang.cms.api.coupon.dto.search;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.ttwijang.cms.entity.QCoupon;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdmCouponSearch {
    private String startDate;
    private String endDate;

    private String name;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QCoupon coupon = QCoupon.coupon;

        builder.and(coupon.deleteStatus.eq(false));

        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            StringTemplate formattedStartDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                coupon.startDate,
                ConstantImpl.create("%Y-%m-%d")
            );
            StringTemplate formattedEndDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                coupon.endDate,
                ConstantImpl.create("%Y-%m-%d")
            );
            builder.and(formattedStartDate.between(startDate, endDate).or(formattedEndDate.between(startDate, endDate)));
        }

        if (StringUtils.hasText(name)) builder.and(coupon.name.contains(name));

        return builder;
    }
}

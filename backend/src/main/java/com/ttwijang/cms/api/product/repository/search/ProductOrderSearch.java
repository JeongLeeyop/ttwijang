package com.ttwijang.cms.api.product.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.QProductOrder;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductOrderSearch {
    private String userUid;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QProductOrder order = QProductOrder.productOrder;

        builder.and(order.group.userUid.eq(userUid));
        
        return builder;
    }
}

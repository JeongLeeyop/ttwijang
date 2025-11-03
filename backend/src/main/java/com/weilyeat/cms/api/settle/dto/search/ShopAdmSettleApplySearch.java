package com.weilyeat.cms.api.settle.dto.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.QSettleApply;

import lombok.Data;

@Data
public class ShopAdmSettleApplySearch {
    private Integer shopIdx;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QSettleApply settleApply = QSettleApply.settleApply;

        builder.and(settleApply.cancelStatus.eq(false));
        builder.and(settleApply.shop.idx.eq(shopIdx));
        return builder;
    }
}

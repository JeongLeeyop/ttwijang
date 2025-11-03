package com.weilyeat.cms.api.product.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdmProductOrderGroupSearch {
    private String startDate;
    private String endDate;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();

        return builder;
    }
}

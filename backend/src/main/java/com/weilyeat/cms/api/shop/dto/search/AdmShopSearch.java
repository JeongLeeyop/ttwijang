package com.ttwijang.cms.api.shop.dto.search;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.QShop;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdmShopSearch {
    private String searchType;
    private String searchValue;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QShop shop = QShop.shop;

        builder.and(shop.withdrawStatus.eq(false));

        if (StringUtils.hasText(searchType) && StringUtils.hasText(searchValue)) {
            if (searchType.equals("name")) builder.and(shop.name.contains(searchValue));
            if (searchType.equals("tel")) builder.and(shop.tel.contains(searchValue));
        }

        return builder;
    }
}

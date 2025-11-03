package com.weilyeat.cms.api.food.dto.search;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.QFood;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FoodSearch {
    private String searchValue;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QFood food = QFood.food;

        if (StringUtils.hasText(searchValue)) {
            builder.and(food.name.contains(searchValue));
        }

        builder.and(food.deleteStatus.eq(false));

        return builder;
    }
}

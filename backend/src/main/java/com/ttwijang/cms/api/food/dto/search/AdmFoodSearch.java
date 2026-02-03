package com.ttwijang.cms.api.food.dto.search;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.QFood;

import lombok.Data;

@Data
public class AdmFoodSearch {
    private String searchType;
    private String searchValue;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QFood food = QFood.food;

        if (StringUtils.hasText(searchValue)) {
            if (searchType.equals("ref")) builder.and(food.ref.contains(searchValue));
            if (searchType.equals("name")) builder.and(food.name.contains(searchValue));
        }

        builder.and(food.deleteStatus.eq(false));

        return builder;
    }
}

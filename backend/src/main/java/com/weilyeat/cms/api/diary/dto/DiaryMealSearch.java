package com.ttwijang.cms.api.diary.dto;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.QDiaryMeal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DiaryMealSearch {
    private String userUid;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QDiaryMeal diaryMeal = QDiaryMeal.diaryMeal;

        builder.and(diaryMeal.userUid.eq(userUid));

        return builder;
    }
}

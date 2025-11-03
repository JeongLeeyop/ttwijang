package com.weilyeat.cms.api.review.dto.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.QReview;

import lombok.Data;

@Data
public class ReviewSearch {
    private boolean isMyReview;
    private String userUid;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QReview review = QReview.review;

        builder.and(review.deleteStatus.eq(false));

        if (isMyReview) builder.and(review.userUid.eq(userUid));

        return builder;
    }
}

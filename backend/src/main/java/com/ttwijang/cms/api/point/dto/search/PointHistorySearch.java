package com.ttwijang.cms.api.point.dto.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.QPointHistory;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PointHistorySearch {
    private String userUid;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QPointHistory pointHistory = QPointHistory.pointHistory;

        builder.and(pointHistory.userUid.eq(userUid));
        
        return builder;
    }

}

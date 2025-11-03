package com.weilyeat.cms.api.push_alarm.dto.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.QPushAlarm;


import lombok.Data;

@Data
public class PushAlarmSearch {
    private String userUid;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QPushAlarm pushAlarm = QPushAlarm.pushAlarm;

        builder.and(pushAlarm.userUid.eq(userUid));
        
        return builder;
    }
}

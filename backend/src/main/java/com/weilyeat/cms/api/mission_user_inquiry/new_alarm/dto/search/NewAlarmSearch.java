package com.weilyeat.cms.api.mission_user_inquiry.new_alarm.dto.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.QNewAlarm;


import lombok.Data;

@Data
public class NewAlarmSearch {
    private String userUid;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QNewAlarm newAlarm = QNewAlarm.newAlarm;

        builder.and(newAlarm.userUid.eq(userUid));
        
        return builder;
    }
}

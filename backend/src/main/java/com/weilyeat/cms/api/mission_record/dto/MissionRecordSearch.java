package com.ttwijang.cms.api.mission_record.dto;

import java.time.LocalDate;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.common.search.SearchDefault;
import com.ttwijang.cms.entity.QMissionRecord;

import lombok.Data;

@Data
public class MissionRecordSearch extends SearchDefault {
    private String searchDate;
    private boolean myFlag;
    private String userUid;
    private String missionUid;

    public Predicate search() {
        QMissionRecord missionRecord = QMissionRecord.missionRecord;
        BooleanBuilder builder = super.initSearch(missionRecord);

        if (StringUtils.hasText(searchDate)) {
            LocalDate date = LocalDate.parse(searchDate);
            builder.and(missionRecord.createDate.between(date.atStartOfDay(), date.atTime(23, 59, 59)));
        }
        if (StringUtils.hasText(userUid)) {
            builder.and(missionRecord.userUid.eq(userUid));
        }
        if (StringUtils.hasText(missionUid)) {
            builder.and(missionRecord.missionUid.eq(missionUid));
        }
        return builder;
    }
}

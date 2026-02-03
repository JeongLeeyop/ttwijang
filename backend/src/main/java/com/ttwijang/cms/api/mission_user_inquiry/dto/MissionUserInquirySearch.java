package com.ttwijang.cms.api.mission_user_inquiry.dto;

import java.time.LocalDate;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.common.search.SearchDefault;
import com.ttwijang.cms.entity.QMissionUserInquiry;

import lombok.Data;

@Data
public class MissionUserInquirySearch extends SearchDefault {
    private String searchDate;
    private boolean myFlag;
    private String userUid;
    private String missionUid;

    public Predicate search() {
        QMissionUserInquiry missionUserInquiry = QMissionUserInquiry.missionUserInquiry;
        BooleanBuilder builder = super.initSearch(missionUserInquiry);

        if (StringUtils.hasText(searchDate)) {
            LocalDate date = LocalDate.parse(searchDate);
            builder.and(missionUserInquiry.createDate.between(date.atStartOfDay(), date.atTime(23, 59, 59)));
        }
        if (StringUtils.hasText(userUid)) {
            // builder.and(missionUserInquiry.userUid.eq(userUid));
        }
        if (StringUtils.hasText(missionUid)) {
            // builder.and(missionUserInquiry.missionUid.eq(missionUid));
        }
        return builder;
    }
}

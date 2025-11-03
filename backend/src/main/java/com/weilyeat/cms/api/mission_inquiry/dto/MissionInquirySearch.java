package com.weilyeat.cms.api.mission_inquiry.dto;

import java.time.LocalDate;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.common.search.SearchDefault;
import com.weilyeat.cms.entity.QMissionInquiry;

import lombok.Data;

@Data
public class MissionInquirySearch extends SearchDefault {
    private String searchDate;
    private boolean myFlag;
    private String userUid;
    private String missionUid;
    private Integer pageNum;

    public Predicate search() {
        QMissionInquiry missionInquiry = QMissionInquiry.missionInquiry;
        BooleanBuilder builder = super.initSearch(missionInquiry);

        if (StringUtils.hasText(searchDate)) {
            LocalDate date = LocalDate.parse(searchDate);
            builder.and(missionInquiry.createDate.between(date.atStartOfDay(), date.atTime(23, 59, 59)));
        }
        if (StringUtils.hasText(userUid)) {
            // builder.and(missionInquiry.userUid.eq(userUid));
        }
        if (StringUtils.hasText(missionUid)) {
            // builder.and(missionInquiry.missionUid.eq(missionUid));
        }
        if (pageNum != null) {
            builder.and(missionInquiry.pageNum.eq(pageNum));
        }
        return builder;
    }
}

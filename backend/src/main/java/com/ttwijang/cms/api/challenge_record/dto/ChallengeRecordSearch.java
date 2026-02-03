package com.ttwijang.cms.api.challenge_record.dto;

import java.time.LocalDate;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.common.search.SearchDefault;
import com.ttwijang.cms.entity.QChallengeRecord;

import lombok.Data;

@Data
public class ChallengeRecordSearch extends SearchDefault {
    private String searchDate;
    private boolean myFlag;
    private String userUid;
    private String challengeUid;

    public Predicate search() {
        QChallengeRecord challengeRecord = QChallengeRecord.challengeRecord;
        BooleanBuilder builder = super.initSearch(challengeRecord);

        if (StringUtils.hasText(searchDate)) {
            LocalDate date = LocalDate.parse(searchDate);
            builder.and(challengeRecord.createDate.between(date.atStartOfDay(), date.atTime(23, 59, 59)));
        }
        if (StringUtils.hasText(userUid)) {
            builder.and(challengeRecord.userUid.eq(userUid));
        }
        if (StringUtils.hasText(challengeUid)) {
            builder.and(challengeRecord.challengeUid.eq(challengeUid));
        }
        return builder;
    }
}

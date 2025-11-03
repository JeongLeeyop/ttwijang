package com.ttwijang.cms.api.challenge.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.ttwijang.cms.common.search.SearchDefault;
import com.ttwijang.cms.entity.QChallenge;

import lombok.Data;

@Data
public class ChallengeSearch extends SearchDefault {
    private String startDate;
    private String endDate;
    private boolean useStatus;
    private boolean myFlag;
    private String userUid;
    private String title;
    private String categoryUid;

    public Predicate search() {
        QChallenge challenge = QChallenge.challenge;
        BooleanBuilder builder = super.initSearch(challenge);

        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            StringTemplate formattedStartDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                challenge.startDate,
                ConstantImpl.create("%Y-%m-%d")
            );
            StringTemplate formattedEndDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                challenge.endDate,
                ConstantImpl.create("%Y-%m-%d")
            );
            builder.and(formattedStartDate.between(startDate, endDate).or(formattedEndDate.between(startDate, endDate)));
        }
        if (StringUtils.hasText(title)) builder.and(challenge.title.contains(title));
        if (StringUtils.hasText(categoryUid)) builder.and(challenge.challengeCategory.uid.eq(categoryUid));
        if (myFlag) {
           builder.and(challenge.challengeUserList.any().userUid.eq(userUid));
        } 
            builder.and(challenge.deleteStatus.eq(false));
        return builder;
    }
}

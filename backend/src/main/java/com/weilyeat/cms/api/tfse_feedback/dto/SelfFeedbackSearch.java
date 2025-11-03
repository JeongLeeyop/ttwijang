package com.weilyeat.cms.api.tfse_feedback.dto;

import java.time.LocalDate;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.weilyeat.cms.common.search.SearchDefault;
import com.weilyeat.cms.entity.QSelfFeedback;
import com.weilyeat.cms.entity.QUser;

import lombok.Data;

@Data
public class SelfFeedbackSearch extends SearchDefault {
    private String searchDate;
    private String userUid;
    private String searchType;
	private String searchValue;
    
    public Predicate search() {
        QSelfFeedback selfFeedback = QSelfFeedback.selfFeedback;
        QUser user = QUser.user;
        BooleanBuilder builder = super.initSearch(selfFeedback);

        if (StringUtils.hasText(searchDate)) {
            LocalDate date = LocalDate.parse(searchDate);
            builder.and(selfFeedback.selfFeedbackDate.eq(date));
        }
        if (StringUtils.hasText(userUid)) builder.and(selfFeedback.userUid.eq(userUid));

        if(StringUtils.hasText(searchValue)) {
				switch (searchType) {
				case "writer":
                    BooleanExpression subquery = JPAExpressions.selectOne()
                    .from(user)
                    .where(user.uid.eq(selfFeedback.userUid).and(user.actualName.contains(searchValue)))
                    .exists();
                    builder.and(subquery);
                    break;
				case "content":
					builder.and(selfFeedback.question1.contains(searchValue).or(selfFeedback.question2.contains(searchValue)).or(selfFeedback.question3.contains(searchValue)));
					break;
			}
        }
        return builder;
    }
}

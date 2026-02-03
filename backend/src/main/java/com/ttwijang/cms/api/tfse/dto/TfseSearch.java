package com.ttwijang.cms.api.tfse.dto;

import java.time.LocalDate;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.ttwijang.cms.common.search.SearchDefault;
import com.ttwijang.cms.entity.QProduct;
import com.ttwijang.cms.entity.QTfse;
import com.ttwijang.cms.entity.QUser;

import lombok.Data;

@Data
public class TfseSearch extends SearchDefault {
    private String searchDate;
    private String userUid;
    private boolean communitySearch;
    private String searchType;
	private String searchValue;

    public Predicate search() {
        QTfse tfse = QTfse.tfse;
        QUser user = QUser.user;
        BooleanBuilder builder = super.initSearch(tfse);

        if (StringUtils.hasText(searchDate)) {
            LocalDate date = LocalDate.parse(searchDate);
            builder.and(tfse.tfseDate.between(date.atStartOfDay(), date.atTime(23, 59, 59)));
        }
        if (StringUtils.hasText(userUid)) builder.and(tfse.userUid.eq(userUid));
        
        if(communitySearch){
            builder.and(tfse.secretStatus.eq(false));
        }

        if(StringUtils.hasText(searchValue)) {
				switch (searchType) {
				case "writer":
                    BooleanExpression subquery = JPAExpressions.selectOne()
                    .from(user)
                    .where(user.uid.eq(tfse.userUid).and(user.actualName.contains(searchValue)))
                    .exists();
                    builder.and(subquery);
                    break;
				case "content":
					builder.and(tfse.foodText.contains(searchValue).or(tfse.emotionText.contains(searchValue)));
					break;
			}
        }
        return builder;
    }
}

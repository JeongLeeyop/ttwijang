package com.weilyeat.cms.api.mission.dto;

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
import com.weilyeat.cms.common.search.SearchDefault;
import com.weilyeat.cms.entity.QMissionTemplate;

import lombok.Data;

@Data
public class MissionTemplateSearch extends SearchDefault {
    private boolean useStatus;
    private String title;
    private String categoryUid;

    public Predicate search() {
        QMissionTemplate missionTemplate = QMissionTemplate.missionTemplate;
        BooleanBuilder builder = super.initSearch(missionTemplate);
        if (StringUtils.hasText(title)) builder.and(missionTemplate.title.contains(title));
        if (StringUtils.hasText(categoryUid)) builder.and(missionTemplate.missionCategory.uid.eq(categoryUid));

        builder.and(missionTemplate.useStatus.eq(true));
        return builder;
    }
}

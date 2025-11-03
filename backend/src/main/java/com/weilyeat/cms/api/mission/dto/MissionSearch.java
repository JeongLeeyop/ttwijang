package com.ttwijang.cms.api.mission.dto;

import java.time.LocalDate;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.ttwijang.cms.common.search.SearchDefault;
import com.ttwijang.cms.entity.QMission;

import lombok.Data;

@Data
public class MissionSearch extends SearchDefault {
    private String startDate;
    private String endDate;
    private boolean useStatus;
    private boolean myFlag;
    private Boolean approveStatus;
    private String status; // current, past, future
    private String userUid;
    private String title;
    private String categoryUid;
    private String missionUid;

    public Predicate search() {
        QMission mission = QMission.mission;
        BooleanBuilder builder = super.initSearch(mission);

        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            StringTemplate formattedStartDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                mission.startDate,
                ConstantImpl.create("%Y-%m-%d")
            );
            StringTemplate formattedEndDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                mission.endDate,
                ConstantImpl.create("%Y-%m-%d")
            );
            builder.and(formattedStartDate.between(startDate, endDate).or(formattedEndDate.between(startDate, endDate)));
        }

        if (missionUid != null) {
            builder.and(mission.uid.eq(missionUid));
        }

        if (status != null) {
            LocalDate today = LocalDate.now();
            switch (status) {
                case "current":
                    // 진행 중인 미션: 시작했고 아직 종료되지 않은 미션
                    builder.and(mission.startDate.loe(today).and(mission.endDate.goe(today)));
                    builder.and(mission.missionUserList.any().approveStatus.eq(true));
                    break;
                case "past":
                    // 지난 미션: 종료된 미션 (완료된 미션 + 실패한 미션)
                    // 1. 종료일이 지난 미션 (실패한 미션 포함)
                    // 2. 완료된 미션은 별도 로직이 필요 (서비스에서 처리)
                    builder.and(mission.missionUserList.any().approveStatus.eq(true));
                    // 여기서는 모든 승인된 미션을 가져오고, 서비스에서 필터링
                    break;
                case "future":
                    // 미래 미션
                    builder.and(mission.startDate.gt(today));
                    break;
                case "approved":
                    builder.and(mission.missionUserList.any().approveStatus.eq(true));
                    break;
                case "unapproved":
                    builder.and(mission.missionUserList.any().approveStatus.eq(false));
                    break;
                default:
                    break;
            }
        }

        if (approveStatus != null) {
            builder.and(mission.missionUserList.any().approveStatus.eq(approveStatus));
        }
        
        if (StringUtils.hasText(title)) builder.and(mission.title.contains(title));
        if (StringUtils.hasText(categoryUid)) builder.and(mission.missionCategory.uid.eq(categoryUid));
        if (myFlag) {
           builder.and(mission.missionUserList.any().userUid.eq(userUid));
        } 
            builder.and(mission.deleteStatus.eq(false));
        return builder;
    }
}

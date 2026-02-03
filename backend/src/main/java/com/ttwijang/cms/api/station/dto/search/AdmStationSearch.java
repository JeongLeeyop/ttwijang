package com.ttwijang.cms.api.station.dto.search;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.QStation;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdmStationSearch {
    private String searchType;
    private String searchValue;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QStation station = QStation.station;

        if (StringUtils.hasText(searchType) && StringUtils.hasText(searchValue)) {
            if (searchType.equals("name")) builder.and(station.name.contains(searchValue));
        }

        return builder;
    }
}

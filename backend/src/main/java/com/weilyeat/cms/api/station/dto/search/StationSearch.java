package com.ttwijang.cms.api.station.dto.search;

import java.time.LocalDate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.QStation;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StationSearch {
    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QStation station = QStation.station;
        LocalDate currentDate = LocalDate.now();
        builder.and(station.useStatus.eq(true));
        return builder;
    }
}

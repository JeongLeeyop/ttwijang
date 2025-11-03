package com.weilyeat.cms.api.shop.dto.search;

import java.time.LocalDate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.weilyeat.cms.entity.QShop;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShopSearch {
    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QShop shop = QShop.shop;
        LocalDate currentDate = LocalDate.now();
        builder.and(shop.withdrawStatus.eq(false));
        builder.and(shop.holidayStartDate.after(currentDate).or(shop.holidayEndDate.before(currentDate)).or(shop.holidayStartDate.isNull()));
        return builder;
    }
}

        // LocalDate today = LocalDate.now();
        // DayOfWeek dayOfWeek = today.getDayOfWeek();
        // int dayIndex = (dayOfWeek.getValue() % 7);
        // if(dayIndex == 1) builder.and(shop.holidays.mon.isNull().or(shop.holidays.mon.ne(true)));
        // else if(dayIndex == 2) builder.and(shop.holidays.tue.isNull().or(shop.holidays.tue.ne(true)));
        // else if(dayIndex == 3) builder.and(shop.holidays.wed.isNull().or(shop.holidays.wed.ne(true)));
        // else if(dayIndex == 4) builder.and(shop.holidays.thu.isNull().or(shop.holidays.thu.ne(true)));
        // else if(dayIndex == 5) builder.and(shop.holidays.fri.isNull().or(shop.holidays.fri.ne(true)));
        // else if(dayIndex == 6) builder.and(shop.holidays.sat.isNull().or(shop.holidays.sat.ne(true)));
        // else if(dayIndex == 0) builder.and(shop.holidays.sun.isNull().or(shop.holidays.sun.ne(true)));
package com.ttwijang.cms.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtil {
    
    public static LocalDate today() {
        return LocalDate.now();
    }

    public static int todayWeekNum() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfMonth());
    }

    public static LocalDate todayWeekNumDate(int weekNum) {
        LocalDate today = LocalDate.now();

        LocalDate eightWeeksLater = today.plusWeeks(8);

        return eightWeeksLater;

    }

    public static int weekNum(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfMonth());

    }

    public static LocalDate startWeekDay(int weekNum) {
        LocalDate currentDate = LocalDate.now().plusWeeks(weekNum);
        LocalDate firstDayOfWeek = currentDate.minusDays(currentDate.getDayOfWeek().getValue());
        return firstDayOfWeek;
    }

    public static LocalDate  date(int weekNum, int dayNum) {
        LocalDate currentDate = LocalDate.now().plusWeeks(weekNum);
        int dayOfWeek = currentDate.getDayOfWeek().getValue();
        if (dayOfWeek > dayNum) {
            currentDate = currentDate.minusDays(dayOfWeek - dayNum);    
        } else {
            currentDate = currentDate.plusDays(dayNum - dayOfWeek);
        }
        return currentDate;
    }

    public static LocalDate endWeekDay(int weekNum) {
        LocalDate currentDate = LocalDate.now().plusWeeks(weekNum);
        LocalDate lastDayOfWeek = currentDate.plusDays(6-currentDate.getDayOfWeek().getValue());
        return lastDayOfWeek;
   }
}

package com.ttwijang.cms.api.shop.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

public class ShopDto {
    @Data
    public static class list {
        private Integer idx;
        private String name;
        private String tel;
        private String address;
        private String addressDetail;
        private String lat;
        private String lon;
        private LocalTime openTime;
        private LocalTime closeTime;
        private LocalDate holidayStartDate;
        private LocalDate holidayEndDate;
        private ShopHolidayDto.list holidays;
        private int maxHoldCnt;
    }

    @Data
    public static class detail {
        private Integer idx;
        private String name;
        private String tel;
        private String address;
        private String addressDetail;
        private String lat;
        private String lon;
        private LocalTime openTime;
        private LocalTime closeTime;
        private LocalDate holidayStartDate;
        private LocalDate holidayEndDate;
        private ShopHolidayDto.detail holidays;
        private int maxHoldCnt;
    }

    @Data
    public static class pickupTime {
        private String time;

        public pickupTime(String time) {
            this.time = time;
        }
    }
}

package com.weilyeat.cms.api.shop.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

public class AdmShopDto {
    @Data
    public static class list {
        private Integer idx;
        private String name;
        private String tel;
        private String postCode;
        private String address;
        private String addressDetail;
        private String lat;
        private String lon;
        private LocalTime openTime;
        private LocalTime closeTime;
        private LocalDateTime createdDate;
        private LocalDate openingDate; // 개업일
        private int maxHoldCnt;
    }

    @Data
    public static class detail {
        private Integer idx;
        private String name;
        private String tel;
        private String postCode;
        private String address;
        private String addressDetail;
        private String lat;
        private String lon;
        private LocalTime openTime;
        private LocalTime closeTime;
        private LocalDate holidayStartDate;
        private LocalDate holidayEndDate;
        private LocalDateTime createdDate;
        private LocalDate openingDate; // 개업일
        private String bankCode; // 은행사 코드
        private String holderName; // 예금주
        private String accountNumber; // 계좌번호
        private int maxHoldCnt;
        private AdmShopPickupTimeDto.detail pickupTimes = new AdmShopPickupTimeDto.detail();
        private AdmShopHolidayDto.detail holidays = new AdmShopHolidayDto.detail();
    }

    @Data
    public static class add {
        private String name;
        private String tel;
        private String postCode;
        private String address;
        private String addressDetail;
        private String lat;
        private String lon;
        private LocalTime openTime;
        private LocalTime closeTime;
        private LocalDate openingDate; // 개업일
        private String bankCode; // 은행사 코드
        private String holderName; // 예금주
        private String accountNumber; // 계좌번호
        private int maxHoldCnt;
        private AdmShopPickupTimeDto.save pickupTimes = new AdmShopPickupTimeDto.save();
        private AdmShopHolidayDto.save holidays = new AdmShopHolidayDto.save();
    }

    @Data
    public static class update {
        private String name;
        private String tel;
        private String postCode;
        private String address;
        private String addressDetail;
        private String lat;
        private String lon;
        private LocalTime openTime;
        private LocalTime closeTime;
        private LocalDate holidayStartDate;
        private LocalDate holidayEndDate;
        private LocalDate openingDate; // 개업일
        private String bankCode; // 은행사 코드
        private String holderName; // 예금주
        private String accountNumber; // 계좌번호
        private int maxHoldCnt;
        private AdmShopPickupTimeDto.save pickupTimes = new AdmShopPickupTimeDto.save();
        private AdmShopHolidayDto.save holidays = new AdmShopHolidayDto.save();
    }
}

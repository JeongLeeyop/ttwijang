package com.ttwijang.cms.api.user.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

public class ClientUserDto {
    @Data
    public static class join {
        @NotBlank(message = "성명을 입력하세요.")
        private String actualName;
        
        @NotBlank(message = "연락처를 입력하세요.")
        private String concatNumber;

        private LocalDate birth;

        @NotBlank(message = "이메일을 입력하세요.")
        @Email(message = "이메일 형식으로 입력하세요.")
        private String email;

        private String postCode;

        @NotBlank(message = "주소를 입력하세요.")
        private String address;

        private String addressDetail;

        private String lat;
        
        private String lon;

        @NotNull(message = "성별을 선택하세요.")
        private Integer gender;

        private boolean dietExperience;

        @NotBlank(message = "키를 입력하세요.")
        private String height;

        @NotBlank(message = "체중을 입력하세요.")
        private String weight;

        @NotBlank(message = "목표 체중을 입력하세요.")
        private String goalWeight;

        private LocalDate goalDate;

        private int activityLevel;

        @NotBlank(message = "식단관리 목적을 입력하세요.")
        private String dietPurpose;

        @NotBlank(message = "식단제공 시 유의사항을 입력하세요.")
        private String dietPrecaution;

        private boolean marketingStatus;
    }

    @Data
    public static class info {
        private String userId;
        private String actualName;
        private String concatNumber;
        private LocalDate birth;
        private String email;
        private String postCode;
        private String address;
        private String addressDetail;
        private String lat;        
        private String lon;
        private Integer gender;
        private Integer shopIdx;
        private Integer stationIdx;
        private boolean registerInfoStatus;
        private boolean missionInquiryStatus;

        // 식단관리 경험 여부
        private boolean dietExperience;

        // 키
        private String height;

        // 체중
        private String weight;

        // 목표 체중
        private String goalWeight;

        // 목표달성 희망일자
        private LocalDate goalDate;

        // 활동수준(1: 비활동적, 2: 저활동적, 3: 활동적, 4: 매우 활동적)
        private Integer activityLevel;

        // 식단관리 목적
        private String dietPurpose;

        // 식단 제공 시 유의사항
        private String dietPrecaution;

        // 마케팅 동의 여부
        private boolean marketingStatus;

        private Integer point;
    }

    @Data
    public static class update {
        private String actualName;
        private String concatNumber;
        private LocalDate birth;
        private String email;
        private String postCode;
        private String address;
        private String addressDetail;
        private String lat;        
        private String lon;
        private Integer gender;
        private Integer point;

        // 식단관리 경험 여부
        private boolean dietExperience;

        // 키
        private String height;

        // 체중
        private String weight;

        // 목표 체중
        private String goalWeight;

        // 목표달성 희망일자
        private LocalDate goalDate;

        // 활동수준(1: 비활동적, 2: 저활동적, 3: 활동적, 4: 매우 활동적)
        private Integer activityLevel;

        // 식단관리 목적
        private String dietPurpose;

        // 식단 제공 시 유의사항
        private String dietPrecaution;

        // 마케팅 동의 여부
        private boolean marketingStatus;
    }

    @Data
    public static class withdraw {
        private String reason;
    }
}

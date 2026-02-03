package com.ttwijang.cms.api.user.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

public class ClientUserDto {
    @Data
    public static class join {
        @NotBlank(message = "이메일을 입력하세요.")
        @Email(message = "이메일 형식으로 입력하세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력하세요.")
        private String password;

        private String actualName;
        
        private String concatNumber;

        private LocalDate birth;

        // 주소 관련 필드 (향후 사용 가능성을 위해 유지)
        private String postCode;

        private String address;

        private String addressDetail;

        private String lat;
        
        private String lon;

        @NotNull(message = "성별을 선택하세요.")
        private Integer gender;

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

        // 마케팅 동의 여부
        private boolean marketingStatus;
    }

    @Data
    public static class withdraw {
        private String reason;
    }
}

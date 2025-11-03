package com.weilyeat.cms.api.user.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

public class UserFcmTokenDto {
    @Data
    public static class Detail {
        private String token;
        private String userUid;
    }

    @Data
    public static class Save {
        @NotBlank(message = "토큰값은 필수 입력 항목입니다.")
        private String token;
    }
}

package com.ttwijang.cms.api.board.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

public class BoardFieldDto {
    @Data
    public static class Detail {
        private String uid;
        private String fieldName;
        private String fieldTypeCode;
        private int inputLimit;
        private Integer fileSizeLimit;
        private boolean requiredState;
        private boolean searchState;
    }
    
    @Data
    public static class Add {
        @NotBlank(message = "필드 이름을 입력하세요.")
        private String fieldName;

        @NotBlank(message = "필드 타입을 입력하세요.")
        private String fieldTypeCode;

        @NotNull(message = "입력길이(파일 개수)를 입력하세요.")
        private int inputLimit;

        @Max(value = 20, message = "파일 최대 용량은 20(MB)입니다.")
        private Integer fileSizeLimit;

        private boolean requiredState;

        private boolean searchState;
    }

    @Data
    public static class Update {
        private String uid;
        
        @NotBlank(message = "필드 이름을 입력하세요.")
        private String fieldName;

        @NotBlank(message = "필드 타입을 입력하세요.")
        private String fieldTypeCode;

        @NotNull(message = "입력길이(파일 개수)를 입력하세요.")
        private int inputLimit;

        @Max(value = 20, message = "파일 최대 용량은 20(MB)입니다.")
        private Integer fileSizeLimit;

        private boolean requiredState;

        private boolean searchState;
    }
}

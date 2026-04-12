package com.ttwijang.cms.api.faq.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FaqDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String uid;
        private String question;
        private String answer;
        private String category;
        private Integer displayOrder;
        private LocalDateTime createdDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "질문은 필수입니다")
        private String question;
        private String answer;
        private String category;
        private Integer displayOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String question;
        private String answer;
        private String category;
        private Integer displayOrder;
    }
}

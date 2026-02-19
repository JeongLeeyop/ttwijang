package com.ttwijang.cms.api.manner.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MannerRatingDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 매너 평가 요청")
    public static class RateTeamRequest {
        @NotBlank(message = "매치 UID는 필수입니다")
        @Schema(description = "매치 UID", required = true)
        private String matchUid;

        @NotBlank(message = "평가 대상 팀 UID는 필수입니다")
        @Schema(description = "평가 대상 팀 UID", required = true)
        private String ratedTeamUid;

        @NotNull(message = "매너 점수는 필수입니다")
        @Min(value = 1, message = "매너 점수는 최소 1점입니다")
        @Max(value = 5, message = "매너 점수는 최대 5점입니다")
        @Schema(description = "매너 점수 (1~5)", required = true)
        private Double score;

        @Schema(description = "평가 코멘트")
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "매너 평가 응답")
    public static class RateResponse {
        @Schema(description = "평가 UID")
        private String uid;

        @Schema(description = "팀 UID")
        private String ratedTeamUid;

        @Schema(description = "평균 매너 점수")
        private Double averageScore;

        @Schema(description = "메시지")
        private String message;
    }
}

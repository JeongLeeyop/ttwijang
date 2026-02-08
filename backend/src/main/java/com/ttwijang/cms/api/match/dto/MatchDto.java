package com.ttwijang.cms.api.match.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ttwijang.cms.entity.FutsalMatch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MatchDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "매치 생성 요청")
    public static class CreateRequest {
        @NotBlank(message = "주최 팀 UID는 필수입니다")
        @Schema(description = "주최 팀 UID", required = true)
        private String hostTeamUid;

        @NotNull(message = "매치 타입은 필수입니다")
        @Schema(description = "매치 타입 (FRIENDLY: 친선경기, FREE: 자체경기)", required = true)
        private FutsalMatch.MatchType matchType;

        @NotBlank(message = "매치 방식은 필수입니다")
        @Schema(description = "매치 방식 (4vs4, 5vs5, 6vs6, 7vs7)", required = true)
        private String matchFormat;

        @NotNull(message = "경기 일자는 필수입니다")
        @Schema(description = "경기 일자", required = true)
        private LocalDate matchDate;

        @NotNull(message = "경기 시작 시간은 필수입니다")
        @Schema(description = "경기 시작 시간", required = true)
        private LocalTime matchTime;

        @Schema(description = "경기 소요 시간 (1, 2, 3, 4시간)")
        private Integer durationHours;

        @NotBlank(message = "구장명은 필수입니다")
        @Schema(description = "구장명", required = true)
        private String stadiumName;

        @Schema(description = "구장 주소")
        private String stadiumAddress;

        @Schema(description = "참가비 (팀당)")
        private Integer fee;

        @Schema(description = "활동 지역 - 도/시")
        private String regionSido;

        @Schema(description = "활동 지역 - 시/군/구")
        private String regionSigungu;

        @Schema(description = "추가 안내 사항")
        private String additionalInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "매치 목록 응답")
    public static class ListResponse {
        @Schema(description = "매치 UID")
        private String uid;

        @Schema(description = "주최 팀 UID")
        private String hostTeamUid;

        @Schema(description = "주최 팀 이름")
        private String hostTeamName;

        @Schema(description = "주최 팀 로고 URL")
        private String hostTeamLogoUrl;

        @Schema(description = "주최 팀 매너 점수")
        private Double hostTeamMannerScore;

        @Schema(description = "매치 타입")
        private FutsalMatch.MatchType matchType;

        @Schema(description = "매치 방식")
        private String matchFormat;

        @Schema(description = "경기 일자")
        private LocalDate matchDate;

        @Schema(description = "경기 시간")
        private LocalTime matchTime;

        @Schema(description = "구장명")
        private String stadiumName;

        @Schema(description = "활동 지역")
        private String region;

        @Schema(description = "참가비")
        private Integer fee;

        @Schema(description = "매치 상태")
        private FutsalMatch.FutsalMatchStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "매치 상세 응답")
    public static class DetailResponse {
        @Schema(description = "매치 UID")
        private String uid;

        @Schema(description = "주최 팀 UID")
        private String hostTeamUid;

        @Schema(description = "주최 팀 이름")
        private String hostTeamName;

        @Schema(description = "주최 팀 로고 URL")
        private String hostTeamLogoUrl;

        @Schema(description = "주최 팀 매너 점수")
        private Double hostTeamMannerScore;

        @Schema(description = "상대 팀 UID")
        private String guestTeamUid;

        @Schema(description = "상대 팀 이름")
        private String guestTeamName;

        @Schema(description = "상대 팀 로고 URL")
        private String guestTeamLogoUrl;

        @Schema(description = "매치 타입")
        private FutsalMatch.MatchType matchType;

        @Schema(description = "매치 방식")
        private String matchFormat;

        @Schema(description = "경기 일자")
        private LocalDate matchDate;

        @Schema(description = "경기 시간")
        private LocalTime matchTime;

        @Schema(description = "경기 소요 시간")
        private Integer durationHours;

        @Schema(description = "구장명")
        private String stadiumName;

        @Schema(description = "구장 주소")
        private String stadiumAddress;

        @Schema(description = "참가비")
        private Integer fee;

        @Schema(description = "활동 지역 - 도/시")
        private String regionSido;

        @Schema(description = "활동 지역 - 시/군/구")
        private String regionSigungu;

        @Schema(description = "추가 안내 사항")
        private String additionalInfo;

        @Schema(description = "홈팀 득점")
        private Integer homeScore;

        @Schema(description = "원정팀 득점")
        private Integer awayScore;

        @Schema(description = "매치 상태")
        private FutsalMatch.FutsalMatchStatus status;

        @Schema(description = "생성일")
        private LocalDateTime createdDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "매치 신청 요청")
    public static class ApplyRequest {
        @NotBlank(message = "매치 UID는 필수입니다")
        @Schema(description = "매치 UID", required = true)
        private String matchUid;

        @NotBlank(message = "신청 팀 UID는 필수입니다")
        @Schema(description = "신청 팀 UID", required = true)
        private String applicantTeamUid;

        @Schema(description = "신청 메시지")
        private String message;
    }
}

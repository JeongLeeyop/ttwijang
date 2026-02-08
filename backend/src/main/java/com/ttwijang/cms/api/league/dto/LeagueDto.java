package com.ttwijang.cms.api.league.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ttwijang.cms.entity.League;
import com.ttwijang.cms.entity.LeagueMatch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LeagueDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "리그 목록 응답")
    public static class ListResponse {
        @Schema(description = "리그 UID")
        private String uid;

        @Schema(description = "리그 이름")
        private String name;

        @Schema(description = "시즌")
        private String season;

        @Schema(description = "활동 지역")
        private String region;

        @Schema(description = "참가 팀 수")
        private Integer currentTeams;

        @Schema(description = "최대 팀 수")
        private Integer maxTeams;

        @Schema(description = "리그 상태")
        private League.LeagueStatus status;

        @Schema(description = "배너 이미지 URL")
        private String bannerUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "리그 상세 응답")
    public static class DetailResponse {
        @Schema(description = "리그 UID")
        private String uid;

        @Schema(description = "리그 이름")
        private String name;

        @Schema(description = "리그 설명")
        private String description;

        @Schema(description = "시즌")
        private String season;

        @Schema(description = "시작일")
        private LocalDate startDate;

        @Schema(description = "종료일")
        private LocalDate endDate;

        @Schema(description = "활동 지역 - 도/시")
        private String regionSido;

        @Schema(description = "활동 지역 - 시/군/구")
        private String regionSigungu;

        @Schema(description = "참가 팀 수")
        private Integer currentTeams;

        @Schema(description = "최대 팀 수")
        private Integer maxTeams;

        @Schema(description = "리그 상태")
        private League.LeagueStatus status;

        @Schema(description = "리그 규칙")
        private String rules;

        @Schema(description = "배너 이미지 URL")
        private String bannerUrl;

        @Schema(description = "순위표")
        private List<LeagueStandingResponse> standings;

        @Schema(description = "다가오는 경기")
        private List<MatchResponse> upcomingMatches;

        @Schema(description = "최근 경기 결과")
        private List<MatchResponse> recentResults;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "리그 순위 응답")
    public static class LeagueStandingResponse {
        @Schema(description = "순위")
        private Integer ranking;

        @Schema(description = "팀 UID")
        private String teamUid;

        @Schema(description = "팀 이름")
        private String teamName;

        @Schema(description = "팀 로고 URL")
        private String teamLogoUrl;

        @Schema(description = "경기 수")
        private Integer played;

        @Schema(description = "승")
        private Integer wins;

        @Schema(description = "무")
        private Integer draws;

        @Schema(description = "패")
        private Integer losses;

        @Schema(description = "득점")
        private Integer goalsFor;

        @Schema(description = "실점")
        private Integer goalsAgainst;

        @Schema(description = "득실차")
        private Integer goalDifference;

        @Schema(description = "승점")
        private Integer points;

        @Schema(description = "매너 점수")
        private Double mannerScore;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "경기 응답")
    public static class MatchResponse {
        @Schema(description = "경기 UID")
        private String uid;

        @Schema(description = "리그 UID")
        private String leagueUid;

        @Schema(description = "홈팀 UID")
        private String homeTeamUid;

        @Schema(description = "홈팀 이름")
        private String homeTeamName;

        @Schema(description = "홈팀 로고 URL")
        private String homeTeamLogoUrl;

        @Schema(description = "원정팀 UID")
        private String awayTeamUid;

        @Schema(description = "원정팀 이름")
        private String awayTeamName;

        @Schema(description = "원정팀 로고 URL")
        private String awayTeamLogoUrl;

        @Schema(description = "경기 일자")
        private LocalDate matchDate;

        @Schema(description = "경기 시간")
        private LocalTime matchTime;

        @Schema(description = "구장명")
        private String stadiumName;

        @Schema(description = "구장 주소")
        private String stadiumAddress;

        @Schema(description = "홈팀 득점")
        private Integer homeScore;

        @Schema(description = "원정팀 득점")
        private Integer awayScore;

        @Schema(description = "경기 상태")
        private LeagueMatch.MatchStatus status;

        @Schema(description = "라운드")
        private Integer round;

        @Schema(description = "리그 이름")
        private String leagueName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "경기 결과 입력 요청")
    public static class MatchResultRequest {
        @NotBlank(message = "경기 UID는 필수입니다")
        @Schema(description = "경기 UID", required = true)
        private String matchUid;

        @NotNull(message = "홈팀 득점은 필수입니다")
        @Schema(description = "홈팀 득점", required = true)
        private Integer homeScore;

        @NotNull(message = "원정팀 득점은 필수입니다")
        @Schema(description = "원정팀 득점", required = true)
        private Integer awayScore;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "일정 조회 요청")
    public static class ScheduleRequest {
        @Schema(description = "연도")
        private Integer year;

        @Schema(description = "월")
        private Integer month;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "리그 참가 팀 응답")
    public static class LeagueTeamResponse {
        @Schema(description = "팀 UID")
        private String teamUid;

        @Schema(description = "팀 이름")
        private String teamName;

        @Schema(description = "팀 로고 URL")
        private String teamLogoUrl;

        @Schema(description = "리그 이름")
        private String leagueName;

        @Schema(description = "순위")
        private Integer ranking;

        @Schema(description = "승점")
        private Integer points;
    }

    /**
     * BR-11: 리그 생성 요청 (최고관리자 전용)
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "리그 생성 요청")
    public static class CreateRequest {
        @NotBlank(message = "리그 이름은 필수입니다")
        @Schema(description = "리그 이름", required = true)
        private String name;

        @Schema(description = "리그 설명")
        private String description;

        @Schema(description = "시즌 (예: 2026-01)")
        private String season;

        @Schema(description = "시작일")
        private LocalDate startDate;

        @Schema(description = "종료일")
        private LocalDate endDate;

        @NotBlank(message = "활동 지역(도/시)은 필수입니다")
        @Schema(description = "활동 지역 - 도/시", required = true)
        private String regionSido;

        @Schema(description = "활동 지역 - 시/군/구")
        private String regionSigungu;

        @Schema(description = "최대 참가 팀 수")
        private Integer maxTeams;

        @Schema(description = "리그 규칙")
        private String rules;

        @Schema(description = "배너 이미지 파일 UID")
        private String bannerFileUid;
    }

    /**
     * BR-11: 리그 수정 요청 (최고관리자 전용)
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "리그 수정 요청")
    public static class UpdateRequest {
        @Schema(description = "리그 이름")
        private String name;

        @Schema(description = "리그 설명")
        private String description;

        @Schema(description = "시즌")
        private String season;

        @Schema(description = "시작일")
        private LocalDate startDate;

        @Schema(description = "종료일")
        private LocalDate endDate;

        @Schema(description = "최대 참가 팀 수")
        private Integer maxTeams;

        @Schema(description = "리그 상태")
        private League.LeagueStatus status;

        @Schema(description = "리그 규칙")
        private String rules;

        @Schema(description = "배너 이미지 파일 UID")
        private String bannerFileUid;
    }

    /**
     * 리그 매치 생성 요청 (최고관리자 전용)
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "리그 매치 생성 요청")
    public static class CreateMatchRequest {
        @NotBlank(message = "리그 UID는 필수입니다")
        @Schema(description = "리그 UID", required = true)
        private String leagueUid;

        @NotBlank(message = "홈팀 UID는 필수입니다")
        @Schema(description = "홈팀 UID", required = true)
        private String homeTeamUid;

        @NotBlank(message = "원정팀 UID는 필수입니다")
        @Schema(description = "원정팀 UID", required = true)
        private String awayTeamUid;

        @NotNull(message = "경기 일자는 필수입니다")
        @Schema(description = "경기 일자", required = true)
        private LocalDate matchDate;

        @Schema(description = "경기 시작 시간")
        private LocalTime matchTime;

        @Schema(description = "경기 소요 시간 (분)")
        private Integer durationMinutes;

        @Schema(description = "구장명")
        private String stadiumName;

        @Schema(description = "구장 주소")
        private String stadiumAddress;

        @Schema(description = "라운드")
        private Integer round;
    }
}

package com.ttwijang.cms.api.guest.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ttwijang.cms.entity.GuestApplication;
import com.ttwijang.cms.entity.GuestRecruitment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GuestDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "게스트 모집 생성 요청")
    public static class CreateRequest {
        @NotBlank(message = "팀 UID는 필수입니다")
        @Schema(description = "모집 팀 UID", required = true)
        private String teamUid;

        @Schema(description = "관련 매치 UID")
        private String matchUid;

        @NotNull(message = "경기 일자는 필수입니다")
        @Schema(description = "경기 일자 (7일 이내만 가능)", required = true)
        private LocalDate matchDate;

        @NotNull(message = "경기 시간은 필수입니다")
        @Schema(description = "경기 시간", required = true)
        private LocalTime matchTime;

        @NotBlank(message = "구장명은 필수입니다")
        @Schema(description = "구장명", required = true)
        private String stadiumName;

        @Schema(description = "구장 주소")
        private String stadiumAddress;

        @Schema(description = "활동 지역 - 도/시")
        private String regionSido;

        @Schema(description = "활동 지역 - 시/군/구")
        private String regionSigungu;

        @Schema(description = "모집 성별 (0: 남자, 1: 여자, 2: 무관)")
        private Integer genderType;

        @Schema(description = "모집 연령대 (비트마스크)")
        private Integer ageGroups;

        @Schema(description = "모집 포지션 (FIELD, GK, ANY)")
        private GuestRecruitment.PositionType positionType;

        @NotNull(message = "모집 인원은 필수입니다")
        @Schema(description = "모집 인원", required = true)
        private Integer maxGuests;

        @Schema(description = "참가비 (0: 무료)")
        private Integer fee;

        @Schema(description = "보장 시간 (분 단위: 30, 60, 80, 100)")
        private Integer guaranteedMinutes;

        @Schema(description = "추가 안내 사항")
        private String additionalInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "게스트 모집 목록 응답")
    public static class ListResponse {
        @Schema(description = "모집 UID")
        private String uid;

        @Schema(description = "팀 UID")
        private String teamUid;

        @Schema(description = "팀 이름")
        private String teamName;

        @Schema(description = "팀 로고 URL")
        private String teamLogoUrl;

        @Schema(description = "팀 매너 점수")
        private Double teamMannerScore;

        @Schema(description = "경기 일자")
        private LocalDate matchDate;

        @Schema(description = "경기 시간")
        private LocalTime matchTime;

        @Schema(description = "구장명")
        private String stadiumName;

        @Schema(description = "활동 지역")
        private String region;

        @Schema(description = "모집 포지션")
        private GuestRecruitment.PositionType positionType;

        @Schema(description = "모집 인원")
        private Integer maxGuests;

        @Schema(description = "현재 신청 인원")
        private Integer currentGuests;

        @Schema(description = "참가비")
        private Integer fee;

        @Schema(description = "보장 시간")
        private Integer guaranteedMinutes;

        @Schema(description = "모집 상태")
        private GuestRecruitment.RecruitmentStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "게스트 모집 상세 응답")
    public static class DetailResponse {
        @Schema(description = "모집 UID")
        private String uid;

        @Schema(description = "팀 UID")
        private String teamUid;

        @Schema(description = "팀 이름")
        private String teamName;

        @Schema(description = "팀 로고 URL")
        private String teamLogoUrl;

        @Schema(description = "팀 매너 점수")
        private Double teamMannerScore;

        @Schema(description = "관련 매치 UID")
        private String matchUid;

        @Schema(description = "경기 일자")
        private LocalDate matchDate;

        @Schema(description = "경기 시간")
        private LocalTime matchTime;

        @Schema(description = "구장명")
        private String stadiumName;

        @Schema(description = "구장 주소")
        private String stadiumAddress;

        @Schema(description = "활동 지역 - 도/시")
        private String regionSido;

        @Schema(description = "활동 지역 - 시/군/구")
        private String regionSigungu;

        @Schema(description = "모집 성별")
        private Integer genderType;

        @Schema(description = "모집 연령대")
        private Integer ageGroups;

        @Schema(description = "모집 포지션")
        private GuestRecruitment.PositionType positionType;

        @Schema(description = "모집 인원")
        private Integer maxGuests;

        @Schema(description = "현재 신청 인원")
        private Integer currentGuests;

        @Schema(description = "참가비")
        private Integer fee;

        @Schema(description = "보장 시간")
        private Integer guaranteedMinutes;

        @Schema(description = "추가 안내 사항")
        private String additionalInfo;

        @Schema(description = "모집 상태")
        private GuestRecruitment.RecruitmentStatus status;

        @Schema(description = "참여자 명단")
        private List<ParticipantInfo> participants;

        @Schema(description = "생성일")
        private LocalDateTime createdDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "참여자 정보")
    public static class ParticipantInfo {
        @Schema(description = "사용자 UID")
        private String uid;

        @Schema(description = "이름")
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "게스트 신청 요청")
    public static class ApplyRequest {
        @NotBlank(message = "모집 UID는 필수입니다")
        @Schema(description = "모집 UID", required = true)
        private String recruitmentUid;

        @Schema(description = "포지션")
        private String position;

        @Schema(description = "나이")
        private Integer age;

        @Schema(description = "신청 메시지")
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "게스트 신청 응답")
    public static class ApplicationResponse {
        @Schema(description = "신청 UID")
        private String uid;

        @Schema(description = "모집 UID")
        private String recruitmentUid;

        @Schema(description = "신청자 UID")
        private String userUid;

        @Schema(description = "신청자 이름")
        private String userName;

        @Schema(description = "포지션")
        private String position;

        @Schema(description = "나이")
        private Integer age;

        @Schema(description = "신청 상태")
        private GuestApplication.ApplicationStatus status;

        @Schema(description = "결제 완료 여부")
        private Boolean paymentCompleted;

        @Schema(description = "신청일")
        private LocalDateTime createdDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "게스트 신청 처리 요청")
    public static class ProcessRequest {
        @NotBlank(message = "신청 UID는 필수입니다")
        @Schema(description = "신청 UID", required = true)
        private String applicationUid;

        @Schema(description = "승인 여부", required = true)
        private Boolean approved;
    }
}

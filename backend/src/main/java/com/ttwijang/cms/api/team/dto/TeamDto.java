package com.ttwijang.cms.api.team.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ttwijang.cms.entity.Team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TeamDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 생성 요청")
    public static class CreateRequest {
        @NotBlank(message = "팀 이름은 필수입니다")
        @Size(max = 100, message = "팀 이름은 100자를 초과할 수 없습니다")
        @Schema(description = "팀 이름", required = true)
        private String name;

        @NotBlank(message = "팀 코드는 필수입니다")
        @Size(max = 20, message = "팀 코드는 20자를 초과할 수 없습니다")
        @Schema(description = "팀 코드 (URL 슬러그)", required = true)
        private String teamCode;

        @Schema(description = "팀 로고 파일 UID")
        private String logoFileUid;

        @Schema(description = "팀 소개")
        private String description;

        @Schema(description = "후원 계좌 은행명")
        private String bankName;

        @Schema(description = "후원 계좌 번호")
        private String bankAccount;

        @Schema(description = "활동 요일 (비트마스크)")
        private Integer activeDays;

        @Schema(description = "활동 시간대 (비트마스크)")
        private Integer activeTimeSlots;

        @Schema(description = "성별 타입 (0: 남자, 1: 여자, 2: 혼성)")
        private Integer genderType;

        @Schema(description = "연령대 (비트마스크)")
        private Integer ageGroups;

        @Schema(description = "활동 지역 - 도/시")
        private String regionSido;

        @Schema(description = "활동 지역 - 시/군/구")
        private String regionSigungu;

        @Schema(description = "홈 구장명")
        private String homeStadium;

        @Schema(description = "홈 구장 주소")
        private String homeStadiumAddress;

        @Schema(description = "월 회비")
        private Integer monthlyFee;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 수정 요청")
    public static class UpdateRequest {
        @Schema(description = "팀 이름")
        private String name;

        @Schema(description = "팀 로고 파일 UID")
        private String logoFileUid;

        @Schema(description = "팀 소개")
        private String description;

        @Schema(description = "후원 계좌 은행명")
        private String bankName;

        @Schema(description = "후원 계좌 번호")
        private String bankAccount;

        @Schema(description = "환불 계좌 은행명")
        private String refundBankName;

        @Schema(description = "환불 계좌 번호")
        private String refundBankAccount;

        @Schema(description = "활동 요일 (비트마스크)")
        private Integer activeDays;

        @Schema(description = "활동 시간대 (비트마스크)")
        private Integer activeTimeSlots;

        @Schema(description = "성별 타입")
        private Integer genderType;

        @Schema(description = "연령대 (비트마스크)")
        private Integer ageGroups;

        @Schema(description = "활동 지역 - 도/시")
        private String regionSido;

        @Schema(description = "활동 지역 - 시/군/구")
        private String regionSigungu;

        @Schema(description = "홈 구장명")
        private String homeStadium;

        @Schema(description = "홈 구장 주소")
        private String homeStadiumAddress;

        @Schema(description = "월 회비")
        private Integer monthlyFee;

        @Schema(description = "팀 특징 태그 (JSON)")
        private String featureTags;

        @Schema(description = "회원 모집 중 여부")
        private Boolean recruitingMembers;

        @Schema(description = "회원 모집 추가사항")
        private String recruitmentDescription;

        @Schema(description = "팀 단체 사진 파일 UID")
        private String teamPhotoFileUid;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 목록 응답")
    public static class ListResponse {
        @Schema(description = "팀 UID")
        private String uid;

        @Schema(description = "팀 이름")
        private String name;

        @Schema(description = "팀 코드")
        private String teamCode;

        @Schema(description = "팀 로고 URL")
        private String logoUrl;

        @Schema(description = "매너 점수")
        private Double mannerScore;

        @Schema(description = "회원 수")
        private Integer memberCount;

        @Schema(description = "활동 지역")
        private String region;

        @Schema(description = "회원 모집 중 여부")
        private Boolean recruitingMembers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 상세 응답")
    public static class DetailResponse {
        @Schema(description = "팀 UID")
        private String uid;

        @Schema(description = "팀 이름")
        private String name;

        @Schema(description = "팀 코드")
        private String teamCode;

        @Schema(description = "팀 로고 URL")
        private String logoUrl;

        @Schema(description = "팀 소개")
        private String description;

        @Schema(description = "매너 점수")
        private Double mannerScore;

        @Schema(description = "회원 수")
        private Integer memberCount;

        @Schema(description = "활동 지역 - 도/시")
        private String regionSido;

        @Schema(description = "활동 지역 - 시/군/구")
        private String regionSigungu;

        @Schema(description = "홈 구장명")
        private String homeStadium;

        @Schema(description = "홈 구장 주소")
        private String homeStadiumAddress;

        @Schema(description = "활동 요일")
        private Integer activeDays;

        @Schema(description = "활동 시간대")
        private Integer activeTimeSlots;

        @Schema(description = "성별 타입")
        private Integer genderType;

        @Schema(description = "연령대")
        private Integer ageGroups;

        @Schema(description = "월 회비")
        private Integer monthlyFee;

        @Schema(description = "후원 계좌 은행명")
        private String bankName;

        @Schema(description = "후원 계좌 번호")
        private String bankAccount;

        @Schema(description = "환불 계좌 은행명")
        private String refundBankName;

        @Schema(description = "환불 계좌 번호")
        private String refundBankAccount;

        @Schema(description = "운영자 UID")
        private String ownerUid;

        @Schema(description = "운영자 이름")
        private String ownerName;

        @Schema(description = "구단주 UID")
        private String sponsorOwnerUid;

        @Schema(description = "구단주 이름")
        private String sponsorOwnerName;

        @Schema(description = "팀 특징 태그")
        private String featureTags;

        @Schema(description = "회원 모집 중 여부")
        private Boolean recruitingMembers;

        @Schema(description = "팀 상태")
        private Team.TeamStatus status;

        @Schema(description = "회원 모집 추가사항")
        private String recruitmentDescription;

        @Schema(description = "팀 단체 사진 파일 UID")
        private String teamPhotoFileUid;

        @Schema(description = "팀 단체 사진 URL")
        private String teamPhotoUrl;

        @Schema(description = "생성일")
        private LocalDateTime createdDate;

        @Schema(description = "팀원 목록")
        private List<TeamMemberDto.Response> members;

        @Schema(description = "리그 전적")
        private LeagueStatsDto leagueStats;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "리그 전적 정보")
    public static class LeagueStatsDto {
        @Schema(description = "리그 이름")
        private String leagueName;

        @Schema(description = "총 경기 수")
        private Integer totalPlayed;

        @Schema(description = "승")
        private Integer wins;

        @Schema(description = "무")
        private Integer draws;

        @Schema(description = "패")
        private Integer losses;

        @Schema(description = "승률")
        private Double winRate;
    }

    /**
     * 팀 가입/생성 가능 여부 응답 (BR-01, BR-02)
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 가입 상태 응답")
    public static class MembershipStatus {
        @Schema(description = "소속 팀 있음")
        private boolean hasTeam;

        @Schema(description = "가입 대기 중 신청 있음")
        private boolean hasPendingRequest;

        @Schema(description = "이미 팀 생성함")
        private boolean hasCreatedTeam;

        @Schema(description = "팀 가입 가능 여부")
        private boolean canJoinTeam;

        @Schema(description = "팀 생성 가능 여부")
        private boolean canCreateTeam;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 모집 설정 요청")
    public static class RecruitmentRequest {
        @Schema(description = "팀 특징 태그 (JSON 배열)")
        private String featureTags;

        @Schema(description = "활동 요일 (비트마스크)")
        private Integer activeDays;

        @Schema(description = "활동 시간대 (비트마스크)")
        private Integer activeTimeSlots;

        @Schema(description = "활동 지역 - 도/시")
        private String regionSido;

        @Schema(description = "활동 지역 - 시/군/구")
        private String regionSigungu;

        @Schema(description = "월 회비")
        private Integer monthlyFee;

        @Schema(description = "성별 타입 (0: 남자, 1: 여자, 2: 혼성)")
        private Integer genderType;

        @Schema(description = "연령대 (비트마스크)")
        private Integer ageGroups;

        @Schema(description = "팀 단체 사진 파일 UID")
        private String teamPhotoFileUid;

        @Schema(description = "회원 모집 추가사항")
        private String recruitmentDescription;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 모집 팀 목록 응답")
    public static class RecruitmentListResponse {
        @Schema(description = "팀 UID")
        private String uid;

        @Schema(description = "팀 이름")
        private String name;

        @Schema(description = "팀 코드")
        private String teamCode;

        @Schema(description = "팀 로고 URL")
        private String logoUrl;

        @Schema(description = "매너 점수")
        private Double mannerScore;

        @Schema(description = "회원 수")
        private Integer memberCount;

        @Schema(description = "활동 지역")
        private String region;

        @Schema(description = "성별 타입")
        private Integer genderType;

        @Schema(description = "연령대")
        private Integer ageGroups;

        @Schema(description = "활동 요일")
        private Integer activeDays;

        @Schema(description = "활동 시간대")
        private Integer activeTimeSlots;

        @Schema(description = "월 회비")
        private Integer monthlyFee;

        @Schema(description = "팀 특징 태그")
        private String featureTags;

        @Schema(description = "조회수")
        private Long viewCount;

        @Schema(description = "신청수")
        private Long applicationCount;
    }
}

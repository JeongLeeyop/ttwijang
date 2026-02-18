package com.ttwijang.cms.api.team.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.ttwijang.cms.entity.TeamMember;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TeamMemberDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 가입 신청 요청")
    public static class JoinRequest {
        @NotBlank(message = "팀 UID는 필수입니다")
        @Schema(description = "팀 UID", required = true)
        private String teamUid;

        @Schema(description = "포지션")
        private String position;

        @Schema(description = "지역")
        private String region;

        @Schema(description = "나이")
        private Integer age;

        @Schema(description = "실력/경력")
        private String experience;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀원 응답")
    public static class Response {
        @Schema(description = "멤버 UID")
        private String uid;

        @Schema(description = "사용자 UID")
        private String userUid;

        @Schema(description = "사용자 이름")
        private String userName;

        @Schema(description = "프로필 이미지 URL")
        private String profileImageUrl;

        @Schema(description = "역할")
        private TeamMember.MemberRole role;

        @Schema(description = "포지션")
        private String position;

        @Schema(description = "등번호")
        private Integer backNumber;

        @Schema(description = "가입 상태")
        private TeamMember.MemberStatus status;

        @Schema(description = "가입일")
        private LocalDateTime createdDate;

        @Schema(description = "성별 (0: 남, 1: 여)")
        private Integer gender;

        @Schema(description = "생년월일")
        private LocalDate birth;

        @Schema(description = "매너점수")
        private Integer mannerScore;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "가입 신청 처리 요청")
    public static class ProcessRequest {
        @NotBlank(message = "신청 UID는 필수입니다")
        @Schema(description = "신청 UID", required = true)
        private String memberUid;

        @Schema(description = "승인 여부", required = true)
        private Boolean approved;

        @Schema(description = "거절 사유")
        private String rejectReason;
    }
}

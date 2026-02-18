package com.ttwijang.cms.api.cash.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.ttwijang.cms.entity.CashTransaction;
import com.ttwijang.cms.entity.TeamSponsorship;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CashDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "캐시 충전 요청")
    public static class ChargeRequest {
        @NotNull(message = "충전 금액은 필수입니다")
        @Min(value = 1000, message = "최소 충전 금액은 1,000원입니다")
        @Schema(description = "충전 금액", required = true, minimum = "1000")
        private Integer amount;

        @Schema(description = "결제 수단 (CARD, BANK, KAKAO, NAVER)")
        private String paymentMethod;

        @Schema(description = "결제 참조 ID")
        private String paymentReferenceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "캐시 사용 요청")
    public static class UseRequest {
        @NotNull(message = "사용 금액은 필수입니다")
        @Min(value = 1, message = "최소 사용 금액은 1원입니다")
        @Schema(description = "사용 금액", required = true, minimum = "1")
        private Integer amount;

        @Schema(description = "사용 내역 설명")
        private String description;

        @Schema(description = "관련 참조 UID")
        private String referenceUid;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "캐시 지갑 응답")
    public static class WalletResponse {
        @Schema(description = "지갑 UID")
        private String uid;

        @Schema(description = "유저 UID")
        private String userUid;

        @Schema(description = "현재 잔액")
        private Integer balance;

        @Schema(description = "총 충전 금액")
        private Integer totalCharged;

        @Schema(description = "총 사용 금액")
        private Integer totalUsed;

        @Schema(description = "마지막 충전일")
        private LocalDateTime lastChargedDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "캐시 거래 내역 응답")
    public static class TransactionResponse {
        @Schema(description = "거래 UID")
        private String uid;

        @Schema(description = "지갑 UID")
        private String walletUid;

        @Schema(description = "거래 유형")
        private CashTransaction.TransactionType transactionType;

        @Schema(description = "금액")
        private Integer amount;

        @Schema(description = "거래 후 잔액")
        private Integer balanceAfter;

        @Schema(description = "설명")
        private String description;

        @Schema(description = "거래 일시")
        private LocalDateTime createdDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 후원 요청")
    public static class SponsorshipRequest {
        @NotNull(message = "팀 UID는 필수입니다")
        @Schema(description = "팀 UID", required = true)
        private String teamUid;

        @NotNull(message = "후원 유형은 필수입니다")
        @Schema(description = "후원 유형", required = true)
        private TeamSponsorship.SponsorshipType sponsorshipType;

        @Min(value = 1000, message = "최소 후원 금액은 1,000원입니다")
        @Schema(description = "후원 금액 (1회성/정기)")
        private Integer amount;

        @Schema(description = "후원 메시지")
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 후원 응답")
    public static class SponsorshipResponse {
        @Schema(description = "후원 UID")
        private String uid;

        @Schema(description = "팀 UID")
        private String teamUid;

        @Schema(description = "팀 이름")
        private String teamName;

        @Schema(description = "후원자 UID")
        private String sponsorUid;

        @Schema(description = "후원자 이름")
        private String sponsorName;

        @Schema(description = "후원 유형")
        private TeamSponsorship.SponsorshipType sponsorshipType;

        @Schema(description = "후원 금액")
        private Integer amount;

        @Schema(description = "총 후원 금액")
        private Integer totalAmount;

        @Schema(description = "후원 상태")
        private TeamSponsorship.SponsorshipStatus status;

        @Schema(description = "후원 메시지")
        private String message;

        @Schema(description = "후원 시작일")
        private LocalDateTime createdDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 후원 요약")
    public static class TeamSponsorshipSummary {
        @Schema(description = "팀 UID")
        private String teamUid;

        @Schema(description = "팀 이름")
        private String teamName;

        @Schema(description = "팀 오너 수")
        private Integer ownerCount;

        @Schema(description = "정기 후원자 수")
        private Integer regularSponsorCount;

        @Schema(description = "1회 후원자 수")
        private Integer oneTimeSponsorCount;

        @Schema(description = "총 후원 금액")
        private Long totalSponsorshipAmount;
    }
}

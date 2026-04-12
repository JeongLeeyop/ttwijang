package com.ttwijang.cms.api.settlement.dto;

import java.time.LocalDateTime;

import com.ttwijang.cms.entity.Settlement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SettlementDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "월별 팀 정산 집계")
    public static class MonthlySummary {
        private String teamUid;
        private String teamName;
        private String ownerUid;
        private String bankName;
        private String bankAccount;
        private Long totalAmount;
        private Long itemCount;
        private String period;
        private Boolean alreadySettled;
        private String settlementUid;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "건별 거래 내역")
    public static class ItemResponse {
        private String teamName;
        private String stadiumName;
        private String matchDate;
        private String userName;
        private Integer amount;
        private LocalDateTime createdDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "정산 생성 요청")
    public static class CreateRequest {
        private String teamUid;
        private String period; // "YYYY-MM"
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "정산 완료 요청")
    public static class CompleteRequest {
        private String adminNote;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "정산 설정")
    public static class ConfigResponse {
        private Integer settlementDays;
        private Boolean useTossPayout;
        private Integer autoSettleDay;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "정산 설정 수정 요청")
    public static class ConfigUpdateRequest {
        private Integer settlementDays;
        private Boolean useTossPayout;
        private Integer autoSettleDay;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "정산 이력 응답")
    public static class HistoryResponse {
        private String uid;
        private String teamUid;
        private String teamName;
        private String ownerUid;
        private String bankName;
        private String bankAccount;
        private Integer totalAmount;
        private Integer itemCount;
        private String period;
        private Settlement.SettlementStatus status;
        private LocalDateTime settledAt;
        private String adminNote;
        private LocalDateTime createdDate;
    }
}

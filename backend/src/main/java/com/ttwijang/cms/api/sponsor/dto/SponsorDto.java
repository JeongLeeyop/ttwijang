package com.ttwijang.cms.api.sponsor.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SponsorDto {

    // ── 구단주 금액 설정 ──────────────────────────────────

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeeResponse {
        private Integer amount;
        private LocalDateTime updatedDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeeRequest {
        private Integer amount;
    }

    // ── 팀별 후원 배너 ────────────────────────────────────

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamBannerResponse {
        private String uid;
        private String teamUid;
        private String teamName;
        private String imageUrl;
        private String description;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamBannerCreateRequest {
        private String teamUid;
        private String teamName;
        private String imageUrl;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamBannerUpdateRequest {
        private String teamUid;
        private String teamName;
        private String imageUrl;
        private String description;
    }
}

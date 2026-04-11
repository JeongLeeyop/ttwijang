package com.ttwijang.cms.api.region.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 지역 코드 DTO
 */
public class RegionCodeDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String uid;
        private String code;
        private String name;
        private String parentCode;
        private Integer level;
        private Integer sortOrder;
        private Boolean enabled;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SidoCreateRequest {
        private String code;
        private String name;
        private Integer sortOrder;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SigunguCreateRequest {
        private String code;
        private String name;
        private String parentCode;
        private Integer sortOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionLeagueSummary {
        private String uid;
        private String name;
        private String regionSigungu;
        private String season;
        private String status;
        private Integer maxTeams;
        private Integer currentTeams;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionTeamSummary {
        private String uid;
        private String name;
        private String regionSigungu;
        private String status;
        private Integer memberCount;
    }
}

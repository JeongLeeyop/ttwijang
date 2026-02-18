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
    }
}

package com.ttwijang.cms.api.banner.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ttwijang.cms.entity.Banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BannerDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private String uid;
        private String title;
        private String imageUrl;
        private String linkUrl;
        private Integer displayOrder;
        private Banner.BannerStatus status;
        private LocalDate startDate;
        private LocalDate endDate;
        private String regionSido;
        private String regionSigungu;
        private Banner.TargetPage targetPage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private String uid;
        private String title;
        private String imageUrl;
        private String linkUrl;
        private Integer displayOrder;
        private Banner.BannerStatus status;
        private LocalDate startDate;
        private LocalDate endDate;
        private String regionSido;
        private String regionSigungu;
        private Banner.TargetPage targetPage;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private String title;
        private String imageUrl;
        private String linkUrl;
        private Integer displayOrder;
        private Banner.BannerStatus status;
        private LocalDate startDate;
        private LocalDate endDate;
        private String regionSido;
        private String regionSigungu;
        private Banner.TargetPage targetPage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String title;
        private String imageUrl;
        private String linkUrl;
        private Integer displayOrder;
        private Banner.BannerStatus status;
        private LocalDate startDate;
        private LocalDate endDate;
        private String regionSido;
        private String regionSigungu;
        private Banner.TargetPage targetPage;
    }
}

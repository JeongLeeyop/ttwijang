package com.ttwijang.cms.api.coupon.dto;

import java.time.LocalDateTime;

import lombok.Data;

public class AdmCouponDto {
    @Data
    public static class list {
        private Integer idx;
        private String name;
        private String type;
        private boolean percentStatus;
        private Integer discountPercent;
        private Integer discountPrice;
        private Integer giveStandardPrice;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime expiredDate;
        private LocalDateTime createDate;
    }

    @Data
    public static class detail {
        private Integer idx;
        private String name;
        private String type;
        private boolean percentStatus;
        private Integer discountPercent;
        private Integer discountPrice;
        private Integer giveStandardPrice;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime expiredDate;
        private LocalDateTime createDate;
    }

    @Data
    public static class add {
        private String name;
        private String type;
        private boolean percentStatus;
        private Integer discountPercent;
        private Integer discountPrice;
        private Integer giveStandardPrice;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime expiredDate;
    }

    @Data
    public static class update {
        private String name;
        private String type;
        private boolean percentStatus;
        private Integer discountPercent;
        private Integer discountPrice;
        private Integer giveStandardPrice;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime expiredDate;
    }
}

package com.ttwijang.cms.api.coupon.dto;

import java.time.LocalDateTime;

import lombok.Data;

public class UserCouponDto {
    @Data
    public static class list {
        private Integer idx;
        private String name;
        private String type;
        private boolean percentStatus;
        private Integer discountPercent;
        private Integer discountPrice;
        private LocalDateTime expiredDate;
        private boolean downloadStatus;
        private LocalDateTime downloadDate;
        private boolean useStatus;
        private LocalDateTime useDate;
    }
}

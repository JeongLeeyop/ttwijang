package com.ttwijang.cms.api.shop.dto;

import lombok.Data;

public class ShopHolidayDto {
    @Data
    public static class list {
        private Boolean mon;
        private Boolean tue;
        private Boolean wed;
        private Boolean thu;
        private Boolean fri;
        private Boolean sat;
        private Boolean sun;
    }

    @Data
    public static class detail {
        private Boolean mon;
        private Boolean tue;
        private Boolean wed;
        private Boolean thu;
        private Boolean fri;
        private Boolean sat;
        private Boolean sun;
    }
}

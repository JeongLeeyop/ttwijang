package com.ttwijang.cms.api.shop.dto;

import lombok.Data;

public class AdmShopHolidayDto {
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

    @Data
    public static class save {
        private Boolean mon;
        private Boolean tue;
        private Boolean wed;
        private Boolean thu;
        private Boolean fri;
        private Boolean sat;
        private Boolean sun;
    }
}

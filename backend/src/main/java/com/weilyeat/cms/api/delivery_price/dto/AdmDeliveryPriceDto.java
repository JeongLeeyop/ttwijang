package com.weilyeat.cms.api.delivery_price.dto;

import java.time.LocalDateTime;

import lombok.Data;

public class AdmDeliveryPriceDto {
    @Data
    public static class save {
        private int amount;
    }

    @Data
    public static class detail {
        private int amount;
        private LocalDateTime updateDate;
    }
}

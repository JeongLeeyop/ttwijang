package com.ttwijang.cms.api.settle.dto;

import java.time.LocalDateTime;

import lombok.Data;

public class AdmSettleSettingDto {
    @Data
    public static class save {
        private int amount;
    }

    @Data
    public static class detail {
        private int amount;
        private LocalDateTime createDate;
    }
}

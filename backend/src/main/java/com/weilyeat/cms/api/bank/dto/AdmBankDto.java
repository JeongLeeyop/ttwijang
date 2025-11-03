package com.weilyeat.cms.api.bank.dto;

import lombok.Data;

public class AdmBankDto {
    @Data
    public static class list {
        private Integer idx;
        private String code;
        private String name;
    }
}

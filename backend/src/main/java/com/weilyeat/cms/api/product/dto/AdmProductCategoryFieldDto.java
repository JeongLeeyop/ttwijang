package com.weilyeat.cms.api.product.dto;

import lombok.Getter;
import lombok.Setter;

public class AdmProductCategoryFieldDto {
    @Getter
    @Setter
    public static class list {
        private Long idx;
        private String name;
        private int orderNumber;
    }
}

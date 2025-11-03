package com.ttwijang.cms.api.product.dto;

import lombok.Getter;
import lombok.Setter;

public class ProductCategoryFieldDto {
    @Getter
    @Setter
    public static class list {
        private Long idx;
        private String name;
        private int orderNumber;
    }
}

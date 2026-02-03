package com.ttwijang.cms.api.product.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ProductCategoryDto {
    @Getter
    @Setter
    public static class list {
        private Long idx;
        private String name;
        private int orderNumber;
        private List<AdmProductCategoryFieldDto.list> fields;
    }
}

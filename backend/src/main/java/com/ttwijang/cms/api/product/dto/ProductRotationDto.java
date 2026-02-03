package com.ttwijang.cms.api.product.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ProductRotationDto {
    
    @Getter
    @Setter
    public static class info {
        private Integer weekNum;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<ProductDto.list> products;
    }
}

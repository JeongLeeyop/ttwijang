package com.ttwijang.cms.api.product.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class AdmProductRotationDto {
    
    @Getter
    @Setter
    public static class update {
        List<Long> productsIdx;
    }

    @Getter
    @Setter
    public static class list {
        List<AdmProductDto.list> products;
        List<AdmProductDto.list> rotationProducts;
    }
}

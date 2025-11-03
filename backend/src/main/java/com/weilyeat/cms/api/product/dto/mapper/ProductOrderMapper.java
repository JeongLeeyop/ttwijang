package com.ttwijang.cms.api.product.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.product.dto.ProductOrderDto;
import com.ttwijang.cms.api.product.dto.ProductOrderGroupDto;
import com.ttwijang.cms.entity.ProductOrder;

@Mapper
public interface ProductOrderMapper {
    ProductOrderMapper INSTANCE = Mappers.getMapper(ProductOrderMapper.class);

    ProductOrderGroupDto.product entitiesToDtoNormal(ProductOrder entity);

    @Mapping(target = "calorie", source = "product.calorie")
    @Mapping(target = "carbohydrate", source = "product.carbohydrate")
    @Mapping(target = "protein", source = "product.protein")
    @Mapping(target = "fat", source = "product.fat")
    @Mapping(target = "sodium", source = "product.sodium")
    @Mapping(target = "sugar", source = "product.sugar")
    @Mapping(target = "saturatedfat", source = "product.saturatedfat")
    ProductOrderDto.list entityToListDto(ProductOrder entity);

    default List<ProductOrderGroupDto.product> entitiesToDto(List<ProductOrder> entities) {
        List<ProductOrderGroupDto.product> dtoList = new ArrayList<ProductOrderGroupDto.product>();
        for (ProductOrder entity : entities) {
            ProductOrderGroupDto.product dto = entitiesToDtoNormal(entity);
            dtoList.add(dto);
        }
        return dtoList;
    }
}

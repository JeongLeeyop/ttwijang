package com.weilyeat.cms.api.product.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.product.dto.ShopAdmProductOrderDto;
import com.weilyeat.cms.entity.ProductOrder;

@Mapper
public interface ShopAdmProductOrderMapper {
    ShopAdmProductOrderMapper INSTANCE = Mappers.getMapper(ShopAdmProductOrderMapper.class);

    List<ShopAdmProductOrderDto.list> entitiesToListDto(List<ProductOrder> entities);
    
    @Mapping(target = "pickupDate", source = "day.orderDate")
    @Mapping(target = "orderDate", source = "group.createDate")
    ShopAdmProductOrderDto.list entitiyToListDto(ProductOrder entity);

    @Mapping(target = "pickupDate", source = "day.orderDate")
    ShopAdmProductOrderDto.detail entitiyToDetailDto(ProductOrder entity);
}

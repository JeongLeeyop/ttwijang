package com.ttwijang.cms.api.product.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.product.dto.AdmProductOrderDto;
import com.ttwijang.cms.entity.ProductOrder;

@Mapper
public interface AdmProductOrderMapper {
    AdmProductOrderMapper INSTANCE = Mappers.getMapper(AdmProductOrderMapper.class);

    List<AdmProductOrderDto.list> entitiesToListDto(List<ProductOrder> entities);
    
    @Mapping(target = "pickupDate", source = "day.orderDate")
    @Mapping(target = "orderDate", source = "group.createDate")
    AdmProductOrderDto.list entitiyToListDto(ProductOrder entity);

    @Mapping(target = "pickupDate", source = "day.orderDate")
    AdmProductOrderDto.detailByDay entitiyToListDetailByDayDto(ProductOrder entity);

    @Mapping(target = "pickupDate", source = "day.orderDate")
    AdmProductOrderDto.detail entitiyToDetailDto(ProductOrder entity);
}

package com.weilyeat.cms.api.product.dto.mapper;

import java.util.List;

import com.weilyeat.cms.api.product.dto.ProductOrderGroupDto;
import com.weilyeat.cms.entity.ProductOrderGroup;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductOrderGroupMapper {
    ProductOrderGroupMapper INSTANCE = Mappers.getMapper(ProductOrderGroupMapper.class);

    List<ProductOrderGroupDto.list> entitiesToListDto(List<ProductOrderGroup> entity);
    ProductOrderGroupDto.list entitiyToListDto(ProductOrderGroup entity);
    ProductOrderGroupDto.detail entitiyToDetailDto(ProductOrderGroup entity);
}

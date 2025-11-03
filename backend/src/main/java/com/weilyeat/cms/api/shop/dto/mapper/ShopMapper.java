package com.ttwijang.cms.api.shop.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.shop.dto.ShopDto;
import com.ttwijang.cms.entity.Shop;
import com.ttwijang.cms.entity.ShopPickupTime;

@Mapper
public interface ShopMapper {
    ShopMapper INSTANCE = Mappers.getMapper(ShopMapper.class);

    ShopDto.detail entityToDetailDto(Shop entity);

    ShopDto.list entityToListDto(Shop entity);
}

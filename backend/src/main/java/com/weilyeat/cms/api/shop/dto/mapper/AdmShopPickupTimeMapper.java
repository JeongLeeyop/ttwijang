package com.ttwijang.cms.api.shop.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.shop.dto.AdmShopPickupTimeDto;
import com.ttwijang.cms.entity.ShopPickupTime;

@Mapper
public interface AdmShopPickupTimeMapper {
    AdmShopPickupTimeMapper INSTANCE = Mappers.getMapper(AdmShopPickupTimeMapper.class);

    ShopPickupTime saveDtoToEntity(AdmShopPickupTimeDto.save saveDto);
}

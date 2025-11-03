package com.weilyeat.cms.api.shop.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.shop.dto.AdmShopPickupTimeDto;
import com.weilyeat.cms.entity.ShopPickupTime;

@Mapper
public interface AdmShopPickupTimeMapper {
    AdmShopPickupTimeMapper INSTANCE = Mappers.getMapper(AdmShopPickupTimeMapper.class);

    ShopPickupTime saveDtoToEntity(AdmShopPickupTimeDto.save saveDto);
}

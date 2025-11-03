package com.ttwijang.cms.api.delivery_price.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.delivery_price.dto.AdmDeliveryPriceDto;
import com.ttwijang.cms.entity.DeliveryPrice;

@Mapper
public interface AdmDeliveryPriceMapper {
    AdmDeliveryPriceMapper INSTANCE = Mappers.getMapper(AdmDeliveryPriceMapper.class);

    AdmDeliveryPriceDto.detail entityToDetailDto(DeliveryPrice entity);

    DeliveryPrice saveDtoToEntity(AdmDeliveryPriceDto.save saveDto);
}

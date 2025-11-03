package com.weilyeat.cms.api.delivery_price.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.delivery_price.dto.AdmDeliveryPriceDto;
import com.weilyeat.cms.entity.DeliveryPrice;

@Mapper
public interface AdmDeliveryPriceMapper {
    AdmDeliveryPriceMapper INSTANCE = Mappers.getMapper(AdmDeliveryPriceMapper.class);

    AdmDeliveryPriceDto.detail entityToDetailDto(DeliveryPrice entity);

    DeliveryPrice saveDtoToEntity(AdmDeliveryPriceDto.save saveDto);
}

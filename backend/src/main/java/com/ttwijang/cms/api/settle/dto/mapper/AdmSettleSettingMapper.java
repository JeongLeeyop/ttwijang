package com.ttwijang.cms.api.settle.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.settle.dto.AdmSettleSettingDto;
import com.ttwijang.cms.entity.SettleSetting;

@Mapper
public interface AdmSettleSettingMapper {
    AdmSettleSettingMapper INSTANCE = Mappers.getMapper(AdmSettleSettingMapper.class);

    AdmSettleSettingDto.detail entityToDetailDto(SettleSetting entity);

    SettleSetting saveDtoToEntity(AdmSettleSettingDto.save saveDto);
}

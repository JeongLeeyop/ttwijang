package com.ttwijang.cms.api.settle.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.settle.dto.AdmSettleApplyDto;
import com.ttwijang.cms.entity.SettleApply;

@Mapper
public interface AdmSettleApplyMapper {
    AdmSettleApplyMapper INSTANCE = Mappers.getMapper(AdmSettleApplyMapper.class);

    AdmSettleApplyDto.list entityToListDtoNormal(SettleApply entity);

    @Mapping(target = "items", ignore = true)
    AdmSettleApplyDto.detail entityToDetailDto(SettleApply entity);

    default AdmSettleApplyDto.list entityToListDto(SettleApply entity) {
        AdmSettleApplyDto.list dto = entityToListDtoNormal(entity);
        dto.setTotalOrder(entity.getItems().size());
        return dto;
    }
}

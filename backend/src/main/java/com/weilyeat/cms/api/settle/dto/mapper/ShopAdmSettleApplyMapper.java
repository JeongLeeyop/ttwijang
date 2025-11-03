package com.weilyeat.cms.api.settle.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.settle.dto.ShopAdmSettleApplyDto;
import com.weilyeat.cms.entity.SettleApply;

@Mapper
public interface ShopAdmSettleApplyMapper {
    ShopAdmSettleApplyMapper INSTANCE = Mappers.getMapper(ShopAdmSettleApplyMapper.class);

    ShopAdmSettleApplyDto.list entityToListDto(SettleApply entity);

    @Mapping(target = "items", ignore = true)
    ShopAdmSettleApplyDto.detail entityToDetailDto(SettleApply entity);
}

package com.weilyeat.cms.api.product.dto.mapper;

import java.util.List;

import com.weilyeat.cms.api.product.dto.AdmProductOrderGroupDto;
import com.weilyeat.cms.entity.ProductOrderGroup;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdmProductOrderGroupMapper {
    AdmProductOrderGroupMapper INSTANCE = Mappers.getMapper(AdmProductOrderGroupMapper.class);

    AdmProductOrderGroupDto.list entityToListDto(ProductOrderGroup entity);
    
}

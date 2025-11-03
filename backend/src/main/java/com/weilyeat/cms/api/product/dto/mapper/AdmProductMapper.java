package com.weilyeat.cms.api.product.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

import com.weilyeat.cms.api.product.dto.AdmProductDto;
import com.weilyeat.cms.entity.Product;

@Mapper
public interface AdmProductMapper {
    AdmProductMapper INSTANCE = Mappers.getMapper(AdmProductMapper.class);

    AdmProductDto.list entityToListDto(Product entity);
    List<AdmProductDto.list> entitiesToListDto(List<Product> entity);

    AdmProductDto.detail entityToDetailDto(Product entity);

    Product addDtoToEntity(AdmProductDto.add addDto);

    Product updateDtoToEntity(AdmProductDto.update updateDto, @MappingTarget Product entity);
}

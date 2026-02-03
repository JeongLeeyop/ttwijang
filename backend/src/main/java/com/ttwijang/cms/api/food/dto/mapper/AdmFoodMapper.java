package com.ttwijang.cms.api.food.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.food.dto.AdmFoodDto;
import com.ttwijang.cms.entity.Food;

@Mapper
public interface AdmFoodMapper {
    AdmFoodMapper INSTANCE = Mappers.getMapper(AdmFoodMapper.class);

    AdmFoodDto.list entityToListDto(Food entity);

    AdmFoodDto.detail entityToDetailDto(Food entity);

    Food addDtoToEntity(AdmFoodDto.add addDto);

    Food updateDtoToEntity(AdmFoodDto.update updateDto, @MappingTarget Food entity);
}

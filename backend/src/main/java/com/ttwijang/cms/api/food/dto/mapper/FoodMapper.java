package com.ttwijang.cms.api.food.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.food.dto.FoodDto;
import com.ttwijang.cms.entity.Food;

@Mapper
public interface FoodMapper {
    FoodMapper INSTANCE = Mappers.getMapper(FoodMapper.class);

    FoodDto.list entityToListDto(Food entity);
}

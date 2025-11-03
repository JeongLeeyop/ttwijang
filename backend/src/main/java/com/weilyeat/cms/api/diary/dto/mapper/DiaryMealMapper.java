package com.ttwijang.cms.api.diary.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.diary.dto.DiaryDto;
import com.ttwijang.cms.api.diary.dto.DiaryMealDto;
import com.ttwijang.cms.entity.DiaryMeal;
import com.ttwijang.cms.entity.Product;
import com.ttwijang.cms.entity.ProductOrder;

@Mapper
public interface DiaryMealMapper {
    DiaryMealMapper INSTANCE = Mappers.getMapper(DiaryMealMapper.class);

    DiaryMeal addDtoToEntity(DiaryDto.mealItemAdd addDto);

    @Mapping(source = "weight", target = "amount")
    DiaryMeal addDtoByFoodToEntity(DiaryDto.mealItemAddByFood addDto);

    DiaryMealDto.list entityToListDto(DiaryMeal entity);

    @Mapping(target = "menuName", source = "name")
    @Mapping(target = "saturatedFattyAcids", source = "saturatedfat")
    @Mapping(target = "idx", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    DiaryMeal productToEntity(Product product);
}

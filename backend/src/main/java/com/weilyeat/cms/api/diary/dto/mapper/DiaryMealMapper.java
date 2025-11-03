package com.weilyeat.cms.api.diary.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.diary.dto.DiaryDto;
import com.weilyeat.cms.api.diary.dto.DiaryMealDto;
import com.weilyeat.cms.entity.DiaryMeal;
import com.weilyeat.cms.entity.Product;
import com.weilyeat.cms.entity.ProductOrder;

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

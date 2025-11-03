// package com.weilyeat.cms.entity.mapper;

// import java.util.List;

// import org.mapstruct.Mapper;
// import org.mapstruct.MappingTarget;
// import org.mapstruct.factory.Mappers;

// import com.weilyeat.cms.entity.Diary;
// import com.weilyeat.cms.entity.DiaryFood;
// import com.weilyeat.cms.entity.Food;
// import com.weilyeat.cms.entity.dto.DiaryDto;
// import com.weilyeat.cms.entity.dto.DiaryFoodDto;
// import com.weilyeat.cms.entity.dto.FoodDto;

// @Mapper
// public interface DiaryMapper {
    
//     DiaryMapper INSTANCE = Mappers.getMapper(DiaryMapper.class);

//     DiaryDto.Detail entityToDetailDto(Diary entity);
// 	List<DiaryDto.List> entityToListDto(List<Diary> entity);
//     Diary addDtoToEntity(DiaryDto.Add addDto);
// 	Diary updateDtoToEntity(DiaryDto.Update updateDto, @MappingTarget Diary diary);

    
        
//     DiaryFood diaryFoodAddDtoToEntity(DiaryFoodDto.Add addDto);
//     DiaryFoodDto.Detail entityToDiaryFoodDetailDto(DiaryFood entity);
//     List<DiaryFoodDto.Detail> entitiyToDiaryFoodDtoList(List<DiaryFood> entity);

//     Food foodAddDtoToEntity(FoodDto.Add addDto);
//     List<FoodDto.Detail> entityToFoodDetailDtoList(List<Food> entity);
//     FoodDto.Detail entityToFoodDetailDto(Food entity);

// }
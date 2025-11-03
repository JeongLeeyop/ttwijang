package com.ttwijang.cms.api.diary.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.diary.dto.DiaryDto;
import com.ttwijang.cms.entity.Diary;

@Mapper
public interface DiaryMapper {
    DiaryMapper INSTANCE = Mappers.getMapper(DiaryMapper.class);

    Diary saveDtoToEntity(DiaryDto.physicalConditionSave saveDto);

    Diary saveDtoToEntity(DiaryDto.physicalConditionSave saveDto, @MappingTarget Diary entity);

    Diary purposeSaveDtoToEntity(DiaryDto.purposePhysicalSave saveDto);

    Diary purposeSaveDtoToEntity(DiaryDto.purposePhysicalSave saveDto, @MappingTarget Diary entity);

    DiaryDto.physicalConditionDetail entityToPhysicalConditionDetail(Diary entity);
}

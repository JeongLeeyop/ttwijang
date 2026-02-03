package com.ttwijang.cms.api.mission.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.mission.dto.MissionCategoryDto;
import com.ttwijang.cms.entity.MissionCategory;

@Mapper
public interface MissionCategoryMapper {
    MissionCategoryMapper INSTANCE = Mappers.getMapper(MissionCategoryMapper.class);
    MissionCategoryDto.detail entityToDetailDto(MissionCategory entity);
}

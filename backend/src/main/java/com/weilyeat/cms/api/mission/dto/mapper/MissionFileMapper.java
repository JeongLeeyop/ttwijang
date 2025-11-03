package com.weilyeat.cms.api.mission.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.mission.dto.MissionDto;
import com.weilyeat.cms.api.mission.dto.MissionFileDto;
import com.weilyeat.cms.entity.Mission;
import com.weilyeat.cms.entity.MissionFile;

@Mapper
public interface MissionFileMapper {
    MissionFileMapper INSTANCE = Mappers.getMapper(MissionFileMapper.class);
    
    MissionFileDto.Detail entityToDetail(MissionFile missionFile);
    MissionFile detailDtoToEntity(MissionFileDto.Detail detailDto);
}

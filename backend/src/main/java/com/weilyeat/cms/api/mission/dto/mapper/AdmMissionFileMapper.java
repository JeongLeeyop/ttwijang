package com.weilyeat.cms.api.mission.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.mission.dto.MissionFileDto;
import com.weilyeat.cms.entity.MissionFile;

@Mapper
public interface AdmMissionFileMapper {
    AdmMissionFileMapper INSTANCE = Mappers.getMapper(AdmMissionFileMapper.class);
    
    MissionFileDto.Detail entityToDetail(MissionFile missionFile);
    MissionFile detailDtoToEntity(MissionFileDto.Detail detailDto);
}

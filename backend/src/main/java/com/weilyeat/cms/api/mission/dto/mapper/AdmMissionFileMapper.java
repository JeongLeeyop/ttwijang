package com.ttwijang.cms.api.mission.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.mission.dto.MissionFileDto;
import com.ttwijang.cms.entity.MissionFile;

@Mapper
public interface AdmMissionFileMapper {
    AdmMissionFileMapper INSTANCE = Mappers.getMapper(AdmMissionFileMapper.class);
    
    MissionFileDto.Detail entityToDetail(MissionFile missionFile);
    MissionFile detailDtoToEntity(MissionFileDto.Detail detailDto);
}

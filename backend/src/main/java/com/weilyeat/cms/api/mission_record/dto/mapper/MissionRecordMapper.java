package com.ttwijang.cms.api.mission_record.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.mission_record.dto.MissionRecordDto;
import com.ttwijang.cms.entity.MissionRecord;

@Mapper
public interface MissionRecordMapper {
    MissionRecordMapper INSTANCE = Mappers.getMapper(MissionRecordMapper.class);
    MissionRecordDto.detail entityToDetail(MissionRecord MissionRecord);
    MissionRecordDto.list entityToList(MissionRecord MissionRecord);
    MissionRecord detailDtoToEntity(MissionRecordDto.detail deatilDto);
    MissionRecord addDtoToEntity(MissionRecordDto.add addDto);
    MissionRecord updateDtoToEntity(MissionRecordDto.update updateDto, @MappingTarget MissionRecord entity);
}

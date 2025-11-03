package com.ttwijang.cms.api.mission.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.mission.dto.MissionUserDto;
import com.ttwijang.cms.entity.Mission;
import com.ttwijang.cms.entity.MissionUser;

@Mapper
public interface MissionUserMapper {
    MissionUserMapper INSTANCE = Mappers.getMapper(MissionUserMapper.class);
    MissionUser addDtoToEntity(MissionUserDto.add addDto);
    MissionUserDto.detail entityToDetail(MissionUser entity);
}

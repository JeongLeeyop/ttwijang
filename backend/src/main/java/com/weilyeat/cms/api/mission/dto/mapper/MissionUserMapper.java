package com.weilyeat.cms.api.mission.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.mission.dto.MissionUserDto;
import com.weilyeat.cms.entity.Mission;
import com.weilyeat.cms.entity.MissionUser;

@Mapper
public interface MissionUserMapper {
    MissionUserMapper INSTANCE = Mappers.getMapper(MissionUserMapper.class);
    MissionUser addDtoToEntity(MissionUserDto.add addDto);
    MissionUserDto.detail entityToDetail(MissionUser entity);
}

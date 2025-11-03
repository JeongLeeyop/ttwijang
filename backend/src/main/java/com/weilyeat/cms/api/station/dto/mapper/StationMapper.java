package com.ttwijang.cms.api.station.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.station.dto.StationDto;
import com.ttwijang.cms.entity.Station;

@Mapper
public interface StationMapper {
    StationMapper INSTANCE = Mappers.getMapper(StationMapper.class);

    StationDto.detail entityToDetailDto(Station entity);

    StationDto.list entityToListDto(Station entity);
}

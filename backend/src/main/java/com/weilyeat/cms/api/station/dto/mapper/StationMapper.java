package com.weilyeat.cms.api.station.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.station.dto.StationDto;
import com.weilyeat.cms.entity.Station;

@Mapper
public interface StationMapper {
    StationMapper INSTANCE = Mappers.getMapper(StationMapper.class);

    StationDto.detail entityToDetailDto(Station entity);

    StationDto.list entityToListDto(Station entity);
}

package com.weilyeat.cms.api.station.dto.mapper;

import java.time.LocalTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.station.dto.AdmStationDto;
import com.weilyeat.cms.entity.Station;

@Mapper
public interface AdmStationMapper {
    AdmStationMapper INSTANCE = Mappers.getMapper(AdmStationMapper.class);

    AdmStationDto.list entityToListDto(Station entity);

    AdmStationDto.detail entityToDetailDto(Station entity);

    Station addDtoToEntity(AdmStationDto.add addDto);

    Station updateDtoToEntity(AdmStationDto.update updateDto, @MappingTarget Station entity);
}

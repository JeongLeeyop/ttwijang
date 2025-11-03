package com.ttwijang.cms.api.point.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.point.dto.PointHistoryDto;
import com.ttwijang.cms.entity.PointHistory;

@Mapper
public interface PointHistoryMapper {
    PointHistoryMapper INSTANCE = Mappers.getMapper(PointHistoryMapper.class);

    PointHistoryDto.list entityToListDto(PointHistory entity);
}

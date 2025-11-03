package com.weilyeat.cms.api.point.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.point.dto.PointHistoryDto;
import com.weilyeat.cms.entity.PointHistory;

@Mapper
public interface PointHistoryMapper {
    PointHistoryMapper INSTANCE = Mappers.getMapper(PointHistoryMapper.class);

    PointHistoryDto.list entityToListDto(PointHistory entity);
}

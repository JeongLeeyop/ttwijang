package com.weilyeat.cms.api.push_alarm.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.push_alarm.dto.PushAlarmDto;
import com.weilyeat.cms.entity.PushAlarm;

@Mapper
public interface PushAlarmMapper {
    PushAlarmMapper INSTANCE = Mappers.getMapper(PushAlarmMapper.class);

    PushAlarmDto.Detail entityToDetailDto(PushAlarm entity);
}

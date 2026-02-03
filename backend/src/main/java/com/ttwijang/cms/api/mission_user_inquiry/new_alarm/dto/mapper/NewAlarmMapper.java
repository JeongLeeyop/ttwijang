package com.ttwijang.cms.api.mission_user_inquiry.new_alarm.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.mission_user_inquiry.new_alarm.dto.NewAlarmDto;
import com.ttwijang.cms.entity.NewAlarm;

@Mapper
public interface NewAlarmMapper {
    NewAlarmMapper INSTANCE = Mappers.getMapper(NewAlarmMapper.class);

}

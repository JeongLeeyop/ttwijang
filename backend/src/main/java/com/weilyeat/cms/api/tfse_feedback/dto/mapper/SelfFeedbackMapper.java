package com.ttwijang.cms.api.tfse_feedback.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.tfse_feedback.dto.SelfFeedbackDto;
import com.ttwijang.cms.entity.SelfFeedback;

@Mapper
public interface SelfFeedbackMapper {
    SelfFeedbackMapper INSTANCE = Mappers.getMapper(SelfFeedbackMapper.class);
    SelfFeedback addDtoToEntity(SelfFeedbackDto.add addDto);
    SelfFeedback addDtoToEntity(SelfFeedbackDto.add addDto, @MappingTarget SelfFeedback entity);
    SelfFeedbackDto.detail entityToDetailNormal(SelfFeedback entity);
    SelfFeedbackDto.list entityToListNormal(SelfFeedback entity);
    
    default SelfFeedbackDto.list entityToList(SelfFeedback entity) {
        SelfFeedbackDto.list dto = entityToListNormal(entity);
        // dto.setWriter(dto.getUser().getActualName().replaceAll("(?<=.{1}).","*"));
        dto.setWriter(dto.getUser().getActualName());
        dto.setUser(null);
        return dto;
    }
    
    default SelfFeedbackDto.detail entityToDetail(SelfFeedback entity) {
        SelfFeedbackDto.detail dto = entityToDetailNormal(entity);
        dto.setWriter(dto.getUser().getActualName());
        dto.setUser(null);
        return dto;
    }
}

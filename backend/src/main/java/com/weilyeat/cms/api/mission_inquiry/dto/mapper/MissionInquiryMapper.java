package com.ttwijang.cms.api.mission_inquiry.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.mission_inquiry.dto.MissionInquiryDto;
import com.ttwijang.cms.entity.MissionInquiry;

@Mapper
public interface MissionInquiryMapper {
    MissionInquiryMapper INSTANCE = Mappers.getMapper(MissionInquiryMapper.class);
    MissionInquiryDto.detail entityToDetail(MissionInquiry MissionInquiry);
    MissionInquiryDto.list entityToList(MissionInquiry MissionInquiry);
    MissionInquiry detailDtoToEntity(MissionInquiryDto.detail deatilDto);
    MissionInquiry addDtoToEntity(MissionInquiryDto.add addDto);
    MissionInquiry updateDtoToEntity(MissionInquiryDto.update updateDto, @MappingTarget MissionInquiry entity);
}

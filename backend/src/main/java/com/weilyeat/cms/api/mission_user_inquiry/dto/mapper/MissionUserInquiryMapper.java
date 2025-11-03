package com.weilyeat.cms.api.mission_user_inquiry.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.mission_user_inquiry.dto.MissionUserInquiryDto;
import com.weilyeat.cms.entity.MissionUserInquiry;

@Mapper
public interface MissionUserInquiryMapper {
    MissionUserInquiryMapper INSTANCE = Mappers.getMapper(MissionUserInquiryMapper.class);
    MissionUserInquiryDto.detail entityToDetail(MissionUserInquiry MissionUserInquiry);
    MissionUserInquiryDto.list entityToList(MissionUserInquiry MissionUserInquiry);
    MissionUserInquiry detailDtoToEntity(MissionUserInquiryDto.detail deatilDto);
    MissionUserInquiry addDtoToEntity(MissionUserInquiryDto.add addDto);
    MissionUserInquiry updateDtoToEntity(MissionUserInquiryDto.update updateDto, @MappingTarget MissionUserInquiry entity);
}

package com.weilyeat.cms.api.mission_inquiry.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.mission_inquiry.dto.MissionInquiryPageDto;
import com.weilyeat.cms.entity.MissionInquiryPage;

@Mapper
public interface MissionInquiryPageMapper {
    MissionInquiryPageMapper INSTANCE = Mappers.getMapper(MissionInquiryPageMapper.class);
    MissionInquiryPageDto.detail entityToDetail(MissionInquiryPage MissionInquiryPage);
    MissionInquiryPageDto.list entityToList(MissionInquiryPage MissionInquiryPage);
    MissionInquiryPage detailDtoToEntity(MissionInquiryPageDto.detail deatilDto);
    MissionInquiryPage addDtoToEntity(MissionInquiryPageDto.add addDto);
    MissionInquiryPage updateDtoToEntity(MissionInquiryPageDto.update updateDto, @MappingTarget MissionInquiryPage entity);
}

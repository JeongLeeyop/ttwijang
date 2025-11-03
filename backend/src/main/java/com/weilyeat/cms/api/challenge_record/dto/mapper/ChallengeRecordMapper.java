package com.ttwijang.cms.api.challenge_record.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.challenge_record.dto.ChallengeRecordDto;
import com.ttwijang.cms.entity.ChallengeRecord;

@Mapper
public interface ChallengeRecordMapper {
    ChallengeRecordMapper INSTANCE = Mappers.getMapper(ChallengeRecordMapper.class);
    ChallengeRecordDto.detail entityToDetail(ChallengeRecord ChallengeRecord);
    ChallengeRecordDto.list entityToList(ChallengeRecord ChallengeRecord);
    ChallengeRecord detailDtoToEntity(ChallengeRecordDto.detail deatilDto);
    ChallengeRecord addDtoToEntity(ChallengeRecordDto.add addDto);
    ChallengeRecord updateDtoToEntity(ChallengeRecordDto.update updateDto, @MappingTarget ChallengeRecord entity);
}

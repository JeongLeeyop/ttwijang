package com.ttwijang.cms.api.challenge.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.challenge.dto.ChallengeDto;
import com.ttwijang.cms.api.challenge.dto.ChallengeFileDto;
import com.ttwijang.cms.entity.Challenge;
import com.ttwijang.cms.entity.ChallengeFile;

@Mapper
public interface ChallengeFileMapper {
    ChallengeFileMapper INSTANCE = Mappers.getMapper(ChallengeFileMapper.class);
    
    ChallengeFileDto.Detail entityToDetail(ChallengeFile challengeFile);
    ChallengeFile detailDtoToEntity(ChallengeFileDto.Detail detailDto);
}

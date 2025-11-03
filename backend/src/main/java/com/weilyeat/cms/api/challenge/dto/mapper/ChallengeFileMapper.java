package com.weilyeat.cms.api.challenge.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.challenge.dto.ChallengeDto;
import com.weilyeat.cms.api.challenge.dto.ChallengeFileDto;
import com.weilyeat.cms.entity.Challenge;
import com.weilyeat.cms.entity.ChallengeFile;

@Mapper
public interface ChallengeFileMapper {
    ChallengeFileMapper INSTANCE = Mappers.getMapper(ChallengeFileMapper.class);
    
    ChallengeFileDto.Detail entityToDetail(ChallengeFile challengeFile);
    ChallengeFile detailDtoToEntity(ChallengeFileDto.Detail detailDto);
}

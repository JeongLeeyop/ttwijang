package com.weilyeat.cms.api.challenge.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.challenge.dto.ChallengeFileDto;
import com.weilyeat.cms.entity.ChallengeFile;

@Mapper
public interface AdmChallengeFileMapper {
    AdmChallengeFileMapper INSTANCE = Mappers.getMapper(AdmChallengeFileMapper.class);
    
    ChallengeFileDto.Detail entityToDetail(ChallengeFile challengeFile);
    ChallengeFile detailDtoToEntity(ChallengeFileDto.Detail detailDto);
}

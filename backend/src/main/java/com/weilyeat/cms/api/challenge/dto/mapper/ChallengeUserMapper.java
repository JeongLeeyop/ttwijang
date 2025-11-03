package com.weilyeat.cms.api.challenge.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.challenge.dto.ChallengeUserDto;
import com.weilyeat.cms.entity.Challenge;
import com.weilyeat.cms.entity.ChallengeUser;

@Mapper
public interface ChallengeUserMapper {
    ChallengeUserMapper INSTANCE = Mappers.getMapper(ChallengeUserMapper.class);
    ChallengeUser addDtoToEntity(ChallengeUserDto.add addDto);
    ChallengeUserDto.detail entityToDetail(ChallengeUser entity);
}

package com.ttwijang.cms.api.challenge.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.challenge.dto.ChallengeCategoryDto;
import com.ttwijang.cms.entity.ChallengeCategory;

@Mapper
public interface ChallengeCategoryMapper {
    ChallengeCategoryMapper INSTANCE = Mappers.getMapper(ChallengeCategoryMapper.class);
    ChallengeCategoryDto.detail entityToDetailDto(ChallengeCategory entity);
}

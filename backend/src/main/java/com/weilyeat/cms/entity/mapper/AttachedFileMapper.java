package com.ttwijang.cms.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.entity.AttachedFile;
import com.ttwijang.cms.entity.dto.AttachedFileDto;

@Mapper
public interface AttachedFileMapper {
    AttachedFileMapper INSTANCE = Mappers.getMapper(AttachedFileMapper.class);

    AttachedFileDto.Detail entityToDetailDto(AttachedFile entity);
}

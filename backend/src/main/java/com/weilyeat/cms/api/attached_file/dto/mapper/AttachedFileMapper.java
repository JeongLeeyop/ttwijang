package com.weilyeat.cms.api.attached_file.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.attached_file.dto.AttachedFileDto;
import com.weilyeat.cms.entity.AttachedFile;

@Mapper
public interface AttachedFileMapper {
    AttachedFileMapper INSTANCE = Mappers.getMapper(AttachedFileMapper.class);

    AttachedFileDto.detail entityToDetailDto(AttachedFile entity);

    AttachedFileDto.clientDetail entityToClientDetailDto(AttachedFile entity);
}

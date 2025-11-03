package com.ttwijang.cms.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.entity.Tag;
import com.ttwijang.cms.entity.dto.TagDto;

@Mapper
public interface TagMapper {
	TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

	TagDto entityToDto(Tag entity);
}

package com.weilyeat.cms.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.entity.Tag;
import com.weilyeat.cms.entity.dto.TagDto;

@Mapper
public interface TagMapper {
	TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

	TagDto entityToDto(Tag entity);
}

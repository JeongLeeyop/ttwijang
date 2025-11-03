package com.ttwijang.cms.api.comment.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.comment.dto.CommentDto;
import com.ttwijang.cms.entity.Comment;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "password", ignore = true)
    Comment addDtoToEntity(CommentDto.Add addDto);

    @Mapping(target = "password", ignore = true)
    Comment updateDtoToEntity(CommentDto.Update updateDto, @MappingTarget Comment entity);

    CommentDto.Detail entityToBasicDetailDto(Comment entity);

    default CommentDto.Detail entityToDetailDto(Comment entity) {
        CommentDto.Detail dto = entityToBasicDetailDto(entity);

        if (entity.getPost().getDataList().size() > 0) {
            dto.setPostTitle(entity.getPost().getDataList().get(0).getInputValue());
        }
        return dto;
    }
}

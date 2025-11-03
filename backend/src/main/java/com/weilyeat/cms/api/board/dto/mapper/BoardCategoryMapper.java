package com.ttwijang.cms.api.board.dto.mapper;

import java.util.ArrayList;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import com.ttwijang.cms.api.board.dto.BoardCategoryDto;
import com.ttwijang.cms.api.board.dto.BoardCategoryDto.Detail;
import com.ttwijang.cms.entity.BoardCategory;

@Mapper
public interface BoardCategoryMapper {
    BoardCategoryMapper INSTANCE = Mappers.getMapper(BoardCategoryMapper.class);

    Detail entityToDetailDto(BoardCategory entity);
    
    BoardCategory addDtoToEntityNormal(BoardCategoryDto.Add addDto);

    BoardCategory updateDtoToEntityNormal(BoardCategoryDto.Update updateDto, @MappingTarget BoardCategory entity);

    default BoardCategory addDtoToEntity(BoardCategoryDto.Add addDto) {
        BoardCategory entity = addDtoToEntityNormal(addDto);
        entity.setDepth(1);
        entity.setViewOrder(0);

        setChildren(entity);
        return entity;
    }

    default BoardCategory updateDtoToEntity(BoardCategoryDto.Update updateDto, BoardCategory entity) {
        BoardCategory updateEntity = updateDtoToEntityNormal(updateDto, entity);
        updateEntity.setUid(entity.getUid());
        updateEntity.setDepth(1);
        updateEntity.setViewOrder(0);

        setChildren(updateEntity);
        return updateEntity;
    }

    default void setChildren(BoardCategory parent) {
        int viewOrder = 0;
        for(BoardCategory child : parent.getChildren()) {
            if (StringUtils.hasText(parent.getUid())) child.setParentUid(parent.getUid());
            child.setDepth(parent.getDepth() + 1);
            child.setViewOrder(viewOrder);
            viewOrder++;
            setChildren(child);
        }
    }

}

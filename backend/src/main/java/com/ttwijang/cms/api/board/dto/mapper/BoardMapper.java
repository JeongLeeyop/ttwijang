package com.ttwijang.cms.api.board.dto.mapper;

import com.ttwijang.cms.api.board.dto.BoardDto;
import com.ttwijang.cms.api.board.dto.BoardDto.*;
import com.ttwijang.cms.entity.Board;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    Simple entityToSimpleDto(Board entity);

    Detail entityToDetailDto(Board entity);

    ClientDetail entityToClientDetail(Board entity);

    Board addDtoToEntity(Add addDto);

    Board updateDtoToEntity(BoardDto.Update updateDto, @MappingTarget Board entity);
}

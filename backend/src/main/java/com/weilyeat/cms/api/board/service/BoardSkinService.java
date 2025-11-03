package com.ttwijang.cms.api.board.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ttwijang.cms.api.board.dto.BoardSkinDto;
import com.ttwijang.cms.api.board.dto.mapper.BoardSkinMapper;
import com.ttwijang.cms.api.board.repository.BoardSkinRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

public interface BoardSkinService {
    List<BoardSkinDto.Detail> list();
}

@Service
@AllArgsConstructor
class BoardSkinServiceImpl implements BoardSkinService {
    private final BoardSkinRepository boardSkinRepository;

    @Override
    public List<BoardSkinDto.Detail> list() {
        return boardSkinRepository.findAll()
            .stream().map(entity -> BoardSkinMapper.INSTANCE.entityToDetailDto(entity))
            .collect(Collectors.toList());
    }
    
}

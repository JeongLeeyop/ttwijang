package com.ttwijang.cms.api.board.service;

import com.ttwijang.cms.api.board.dto.BoardDto.ClientDetail;
import com.ttwijang.cms.api.board.dto.mapper.BoardMapper;
import com.ttwijang.cms.api.board.exception.BoardNotFoundException;
import com.ttwijang.cms.api.board.repository.BoardRepository;
import com.ttwijang.cms.entity.Board;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

public interface ClientBoardService {
    ClientDetail get(String boardUid);
}

@Service
@AllArgsConstructor
class ClientBoardServiceImpl implements ClientBoardService {
    private final BoardRepository boardRepository;

    @Override
	public ClientDetail get(String boardUid) {
		Board board = boardRepository.findById(boardUid).orElseThrow(() -> new BoardNotFoundException());
		return BoardMapper.INSTANCE.entityToClientDetail(board);
	}
}

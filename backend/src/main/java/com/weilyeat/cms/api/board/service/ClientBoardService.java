package com.weilyeat.cms.api.board.service;

import com.weilyeat.cms.api.board.dto.BoardDto.ClientDetail;
import com.weilyeat.cms.api.board.dto.mapper.BoardMapper;
import com.weilyeat.cms.api.board.exception.BoardNotFoundException;
import com.weilyeat.cms.api.board.repository.BoardRepository;
import com.weilyeat.cms.entity.Board;

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

package com.ttwijang.cms.api.board.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.board.dto.BoardDto;
import com.ttwijang.cms.api.board.dto.mapper.BoardMapper;
import com.ttwijang.cms.api.board.dto.search.BoardSearch;
import com.ttwijang.cms.api.board.repository.BoardRepository;
import com.ttwijang.cms.api.post.repository.PostRepository;
import com.ttwijang.cms.entity.Board;

import lombok.AllArgsConstructor;

public interface BoardService {
	Page<BoardDto.Detail> list(BoardSearch boardSearch, Pageable pageable);

	List<BoardDto.Detail> listAll();

	void add(BoardDto.Add boardAddDto);

	void update(Board board, BoardDto.Update boardUpdateDto);

	void delete(Board board);
}

@Service
@AllArgsConstructor
class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;

	private final PostRepository postRepository;

	@Override
	public Page<BoardDto.Detail> list(BoardSearch boardSearch, Pageable pageable) {
		return boardRepository.findAll(boardSearch.search(), pageable)
				.map(board -> BoardMapper.INSTANCE.entityToDetailDto(board));
	}

	public List<BoardDto.Detail> listAll() {
		return boardRepository.findAllByOrderByCreateDate()
			.stream().map(board -> BoardMapper.INSTANCE.entityToDetailDto(board))
			.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void add(BoardDto.Add boardAddDto) {
		Board board = BoardMapper.INSTANCE.addDtoToEntity(boardAddDto);
		boardRepository.save(board);
	}

	@Transactional
	@Override
	public void update(Board board, BoardDto.Update boardUpdateDto) {
		BoardMapper.INSTANCE.updateDtoToEntity(boardUpdateDto, board);
		boardRepository.save(board);
	}

	@Transactional
	@Override
	public void delete(Board board) {
		postRepository.deleteByBoardUid(board.getUid());
		boardRepository.delete(board);
	}
}

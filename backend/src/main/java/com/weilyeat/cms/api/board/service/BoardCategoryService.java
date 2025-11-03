package com.ttwijang.cms.api.board.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.ttwijang.cms.api.board.dto.BoardCategoryDto;
import com.ttwijang.cms.api.board.dto.mapper.BoardCategoryMapper;
import com.ttwijang.cms.api.board.dto.search.BoardCategorySearch;
import com.ttwijang.cms.api.board.repository.BoardCategoryRepository;
import com.ttwijang.cms.api.post.repository.PostCategoryRepository;
import com.ttwijang.cms.entity.BoardCategory;
import com.ttwijang.cms.entity.PostCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

public interface BoardCategoryService {
	Page<BoardCategoryDto.Detail> list(BoardCategorySearch categorySearch, Pageable pageable);

	List<BoardCategoryDto.Detail> listAll();

	void add(BoardCategoryDto.Add addDto);

	void update(BoardCategory boardCategory, BoardCategoryDto.Update updateDto);

	void delete(BoardCategory boardCategory);
}

@Service
@AllArgsConstructor
class BoardCategoryServiceImpl implements BoardCategoryService {

	private final BoardCategoryRepository boardCategoryRepository;
	private final PostCategoryRepository postCategoryRepository;

	@Override
	public Page<BoardCategoryDto.Detail> list(BoardCategorySearch categorySearch, Pageable pageable) {
		return boardCategoryRepository.findAll(categorySearch.search(), pageable)
			.map(data -> BoardCategoryMapper.INSTANCE.entityToDetailDto(data));
	}

	@Override
	public List<BoardCategoryDto.Detail> listAll() {
		return boardCategoryRepository.findAllByDepthOrderByCreateDate(1)
			.stream().map(data -> BoardCategoryMapper.INSTANCE.entityToDetailDto(data))
			.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void add(BoardCategoryDto.Add addDto) {
		BoardCategory boardCategory = BoardCategoryMapper.INSTANCE.addDtoToEntity(addDto);
		boardCategoryRepository.save(boardCategory);
	}

	@Transactional
	@Override
	public void update(BoardCategory boardCategory, BoardCategoryDto.Update updateDto) {
		boardCategory = BoardCategoryMapper.INSTANCE.updateDtoToEntity(updateDto, boardCategory);
		boardCategoryRepository.save(boardCategory);

		// postCategory에 삭제된 카테고리 반영하기
		if(updateDto.getDeleteList() !=null && !updateDto.getDeleteList().isEmpty()){
			List<PostCategory> result = postCategoryRepository.findAllByCategoryUidIn(updateDto.getDeleteList());
			for (PostCategory item : result) {
				postCategoryRepository.delete(item);
			}
		}
	}

	@Transactional
	@Override
	public void delete(BoardCategory boardCategory) {
		boardCategoryRepository.delete(boardCategory);
	}
}

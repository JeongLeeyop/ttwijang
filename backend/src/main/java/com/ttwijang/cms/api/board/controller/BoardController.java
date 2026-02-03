package com.ttwijang.cms.api.board.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ttwijang.cms.api.attached_file.service.AttachedFileService;
import com.ttwijang.cms.api.board.dto.BoardDto;
import com.ttwijang.cms.api.board.dto.mapper.BoardMapper;
import com.ttwijang.cms.api.board.dto.search.BoardSearch;
import com.ttwijang.cms.api.board.exception.BoardNotFoundException;
import com.ttwijang.cms.api.board.service.BoardService;
import com.ttwijang.cms.entity.AttachedFile;
import com.ttwijang.cms.entity.Board;

import lombok.AllArgsConstructor;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BOARD')")
@RequestMapping("/api/board")
@AllArgsConstructor
public class BoardController {

	private final BoardService boardService;

	private final AttachedFileService attachedFileService;

	@GetMapping
	public ResponseEntity<Page<BoardDto.Detail>> list(
			@PageableDefault(size = 10, page = 0, direction = Direction.DESC, sort = {"createDate" }) Pageable pageable,
			BoardSearch boardSearch) {
        return ResponseEntity.ok(boardService.list(boardSearch, pageable));
	}

	@GetMapping("all/list")
	public ResponseEntity<List<BoardDto.Detail>> listAll() {
        return ResponseEntity.ok(boardService.listAll());
	}

	@GetMapping("/detail/{uid}")
	public ResponseEntity<BoardDto.Detail> get(@PathVariable("uid") Board board) {
		if (board == null)
			throw new BoardNotFoundException();
		return ResponseEntity.ok(BoardMapper.INSTANCE.entityToDetailDto(board));
	}

	@PostMapping
	public ResponseEntity add(@Valid @RequestBody BoardDto.Add addDto) {
		boardService.add(addDto);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/upload")
	public ResponseEntity<AttachedFile> fileUpload(MultipartFile file) {
		return ResponseEntity.ok(attachedFileService.save(file, "board"));
	}

	@PutMapping("{uid}")
	public ResponseEntity update(@PathVariable("uid") Board board, @Valid @RequestBody BoardDto.Update dto) {
		if (board == null) throw new BoardNotFoundException();
		boardService.update(board, dto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{uid}")
	public ResponseEntity delete(@PathVariable("uid") Board board) {
		if (board == null) throw new BoardNotFoundException();
		boardService.delete(board);
		return ResponseEntity.ok().build();
	}
}

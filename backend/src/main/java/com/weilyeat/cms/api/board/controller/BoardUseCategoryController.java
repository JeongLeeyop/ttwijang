package com.weilyeat.cms.api.board.controller;

import java.util.List;

import com.weilyeat.cms.api.board.repository.BoardUseCategoryRepository;
import com.weilyeat.cms.entity.BoardUseCategory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BOARD')")
@RequestMapping("/api/board-use-category")
@AllArgsConstructor
public class BoardUseCategoryController {

	private final BoardUseCategoryRepository boardUseCategoryRepository;
	
	@GetMapping("{boardUid}")
	public ResponseEntity<List<BoardUseCategory>> listAll(@PathVariable("boardUid") String boardUid) {
		return ResponseEntity.ok(boardUseCategoryRepository.findByBoardUid(boardUid));
	}
}

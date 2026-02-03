package com.ttwijang.cms.api.board.controller;

import java.util.List;

import com.ttwijang.cms.api.board.repository.BoardFieldRepository;
import com.ttwijang.cms.entity.BoardField;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BOARD')")
@RequestMapping("/api/board-field")
@AllArgsConstructor
public class BoardFieldController {
	
	private final BoardFieldRepository boardFieldRepository;
	
	@GetMapping("{boardUid}")
	public ResponseEntity<List<BoardField>> listAll(@PathVariable("boardUid") String boardUid) {
		return ResponseEntity.ok(boardFieldRepository.findByBoardUid(boardUid));
	}
}

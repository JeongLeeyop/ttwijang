package com.weilyeat.cms.api.board.controller;

import com.weilyeat.cms.api.board.dto.BoardDto;
import com.weilyeat.cms.api.board.service.ClientBoardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/board")
@AllArgsConstructor
public class ClientBoardController {
	
	private final ClientBoardService clientBoardService;
	
	@GetMapping("{uid}")
	public ResponseEntity<BoardDto.ClientDetail> get(@PathVariable("uid") String boardUid) {
		return ResponseEntity.ok(clientBoardService.get(boardUid));
	}
}

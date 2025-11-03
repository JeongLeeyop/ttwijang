package com.weilyeat.cms.api.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.board.dto.BoardSkinDto;
import com.weilyeat.cms.api.board.service.BoardSkinService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/board-skin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BOARD')")
@AllArgsConstructor
public class BoardSkinController {
    private final BoardSkinService boardSkinService;

    @GetMapping
    public ResponseEntity<List<BoardSkinDto.Detail>> list() {
        return  ResponseEntity.ok(boardSkinService.list());
    }
}

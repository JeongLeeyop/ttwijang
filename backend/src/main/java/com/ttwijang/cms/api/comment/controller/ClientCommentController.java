package com.ttwijang.cms.api.comment.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.comment.dto.CommentDto;
import com.ttwijang.cms.api.comment.dto.search.CommentSearch;
import com.ttwijang.cms.api.comment.service.CommentService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/comment")
@AllArgsConstructor
public class ClientCommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentDto.Detail>> list(
        @PageableDefault(size = 50, page = 0, direction = Direction.ASC, sort = {"createDate"}) Pageable pageable,
        CommentSearch commentSearch) {
        return ResponseEntity.ok(commentService.list(pageable, commentSearch));
    }

    @PostMapping
    public ResponseEntity add(
        @AuthenticationPrincipal SinghaUser authUser,
        @RequestBody @Valid CommentDto.Add addDto) {
        if (authUser == null || authUser.getUser() == null) {
            return ResponseEntity.status(401).build();
        }
        commentService.add(addDto, authUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{uid}")
    public ResponseEntity update(
        @AuthenticationPrincipal SinghaUser authUser,
        @PathVariable("uid") String uid,
        @RequestBody @Valid CommentDto.Update updateDto) {
        if (authUser == null || authUser.getUser() == null) {
            return ResponseEntity.status(401).build();
        }
        commentService.clientUpdate(uid, updateDto, authUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{uid}")
    public ResponseEntity delete(
        @AuthenticationPrincipal SinghaUser authUser,
        @PathVariable("uid") String uid) {
        if (authUser == null || authUser.getUser() == null) {
            return ResponseEntity.status(401).build();
        }
        commentService.clientDelete(uid, authUser);
        return ResponseEntity.ok().build();
    }
}

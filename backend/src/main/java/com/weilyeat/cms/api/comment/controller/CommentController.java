package com.weilyeat.cms.api.comment.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.comment.dto.CommentDto;
import com.weilyeat.cms.api.comment.dto.search.CommentSearch;
import com.weilyeat.cms.api.comment.service.CommentService;
import com.weilyeat.cms.entity.Comment;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comment")
// @PreAuthorize("hasRole('ROLE_ADMIN')")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LECTURE_ADMIN', 'ROLE_RESERVATION_ADMIN')")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentDto.Detail>> list(
        @PageableDefault(size = 10, page = 0, direction = Direction.DESC, sort = {"createDate" }) Pageable pageable,
        CommentSearch commentSearch) {
        return ResponseEntity.ok(commentService.list(pageable, commentSearch));
    }

    @PostMapping
    public ResponseEntity add(
        @AuthenticationPrincipal UserDetails userDetail,
        @RequestBody @Valid CommentDto.Add addDto) {
        commentService.add(addDto, userDetail);
        return ResponseEntity.ok().build();       
    }

    @PutMapping("{uid}")
    public ResponseEntity update(
        @PathVariable("uid") Comment comment,
        @RequestBody @Valid CommentDto.Update updateDto) {
        commentService.update(comment, updateDto);
        return ResponseEntity.ok().build();       
    }

    @DeleteMapping("{uid}")
    public ResponseEntity delete(@PathVariable("uid") Comment comment) {
        commentService.delete(comment);
        return ResponseEntity.ok().build();
    }
}

package com.weilyeat.cms.api.tfse_comment.controller;

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

import com.weilyeat.cms.api.tfse_comment.dto.TfseCommentDto;
import com.weilyeat.cms.api.tfse_comment.dto.search.TfseCommentSearch;
import com.weilyeat.cms.api.tfse_comment.service.TfseCommentService;
import com.weilyeat.cms.entity.TfseComment;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/tfse/comment")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BOARD')")
@AllArgsConstructor
public class TfseCommentController {
    private final TfseCommentService tfsecommentService;

    @GetMapping
    public ResponseEntity<Page<TfseCommentDto.Detail>> list(
        @PageableDefault(size = 10, page = 0, direction = Direction.ASC, sort = {"createDate" }) Pageable pageable,
        TfseCommentSearch tfseCommentSearch, @AuthenticationPrincipal UserDetails userDetail) {
        return ResponseEntity.ok(tfsecommentService.list(pageable, tfseCommentSearch, userDetail));
    }

    @PostMapping
    public ResponseEntity add(
        @AuthenticationPrincipal UserDetails userDetail,
        @RequestBody @Valid TfseCommentDto.Add addDto) {
        tfsecommentService.add(addDto, userDetail);
        return ResponseEntity.ok().build();       
    }

    @PutMapping("{uid}")
    public ResponseEntity update(
        @PathVariable("uid") TfseComment comment,
        @RequestBody @Valid TfseCommentDto.Update updateDto) {
        tfsecommentService.update(comment, updateDto);
        return ResponseEntity.ok().build();       
    }

    @DeleteMapping("{uid}")
    public ResponseEntity delete(@PathVariable("uid") TfseComment comment) {
        tfsecommentService.delete(comment);
        return ResponseEntity.ok().build();
    }
}

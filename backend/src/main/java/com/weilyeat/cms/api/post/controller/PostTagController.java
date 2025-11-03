package com.weilyeat.cms.api.post.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.post.dto.PostTagDto;
import com.weilyeat.cms.api.post.service.PostTagService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/post-tag")
@AllArgsConstructor
public class PostTagController {
    private final PostTagService postTagService;

    @GetMapping("best")
    public ResponseEntity<List<PostTagDto.best>> list() {
        return ResponseEntity.ok(postTagService.list());
    }
}

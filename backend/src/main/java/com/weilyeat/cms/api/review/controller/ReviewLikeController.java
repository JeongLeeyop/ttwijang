package com.ttwijang.cms.api.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.review.service.ReviewLikeService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/review/like")
@AllArgsConstructor
public class ReviewLikeController {
    private final ReviewLikeService reviewLikeService;

    @PostMapping("{idx}")
    public ResponseEntity like(@PathVariable("idx") Integer idx, @AuthenticationPrincipal SinghaUser authUser) {
        reviewLikeService.like(idx, authUser);
        return ResponseEntity.ok().build();
    }
}

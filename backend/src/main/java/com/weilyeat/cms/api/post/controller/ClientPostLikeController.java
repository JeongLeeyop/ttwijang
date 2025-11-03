package com.ttwijang.cms.api.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.post.service.ClientPostLikeService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/post-like")
@AllArgsConstructor
public class ClientPostLikeController {
    private final ClientPostLikeService clientPostLikeService;
    
    @PostMapping("{uid}")
    public ResponseEntity like(@PathVariable("uid") String uid, @AuthenticationPrincipal SinghaUser authUser) {
        clientPostLikeService.like(uid, authUser);
        return ResponseEntity.ok().build();
    }
}

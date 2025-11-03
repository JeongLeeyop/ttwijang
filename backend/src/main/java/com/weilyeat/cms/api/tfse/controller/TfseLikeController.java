package com.weilyeat.cms.api.tfse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.tfse.service.TfseLikeService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/tfse/like")
@AllArgsConstructor
public class TfseLikeController {
    private final TfseLikeService tfseLikeService;
    
    @PostMapping("{idx}")
    public ResponseEntity like(@PathVariable("idx") Long idx, @AuthenticationPrincipal SinghaUser authUser) {
        tfseLikeService.like(idx, authUser);
        return ResponseEntity.ok().build();
    }

}

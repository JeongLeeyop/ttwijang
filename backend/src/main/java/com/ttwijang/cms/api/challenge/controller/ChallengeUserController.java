package com.ttwijang.cms.api.challenge.controller;

import java.util.List;

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

import com.ttwijang.cms.api.challenge.dto.ChallengeDto;
import com.ttwijang.cms.api.challenge.dto.ChallengeSearch;
import com.ttwijang.cms.api.challenge.dto.ChallengeUserDto;
import com.ttwijang.cms.api.challenge.service.ChallengeService;
import com.ttwijang.cms.api.challenge.service.ChallengeUserService;
import com.ttwijang.cms.entity.Challenge;
import com.ttwijang.cms.entity.Post;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/challenge/user")
@AllArgsConstructor
public class ChallengeUserController {
    private final ChallengeUserService challengeUserService;

    @PostMapping("/join")
    public ResponseEntity add(@AuthenticationPrincipal SinghaUser authUser, @RequestBody ChallengeUserDto.add addDto) {
        challengeUserService.add(authUser, addDto);
        return ResponseEntity.ok().build();
    }
}

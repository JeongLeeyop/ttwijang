package com.weilyeat.cms.api.challenge.controller;

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

import com.weilyeat.cms.api.challenge.dto.ChallengeDto;
import com.weilyeat.cms.api.challenge.dto.ChallengeSearch;
import com.weilyeat.cms.api.challenge.service.ChallengeService;
import com.weilyeat.cms.entity.Challenge;
import com.weilyeat.cms.entity.Post;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/challenge")
@AllArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping("{uid}")
    public ResponseEntity<ChallengeDto.detail> detail(@AuthenticationPrincipal SinghaUser authUser,@PathVariable("uid") String uid) {
        return ResponseEntity.ok(challengeService.detail(authUser, uid));
    }

    @GetMapping
    public ResponseEntity<List<ChallengeDto.detail>> list(@AuthenticationPrincipal SinghaUser authUser, ChallengeSearch search) {
        return ResponseEntity.ok(challengeService.list(authUser, search));
    }
    
    @GetMapping("community")
    public ResponseEntity<Page<ChallengeDto.detail>> communityList(@AuthenticationPrincipal SinghaUser authUser, ChallengeSearch search, @PageableDefault(
					size=10,
					page=0,
					direction = Direction.DESC,
					sort = {"createDate"}) Pageable pageable) {
        return ResponseEntity.ok(challengeService.list(authUser, search, pageable));
    }

    @PostMapping
    public ResponseEntity add(@AuthenticationPrincipal SinghaUser authUser, @RequestBody ChallengeDto.add addDto) {
        challengeService.add(authUser, addDto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("{idx}")
    public ResponseEntity delete(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("idx") Challenge challenge) {
        challengeService.delete(authUser, challenge);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("{idx}")
    public ResponseEntity update(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("idx") Challenge challenge,@RequestBody ChallengeDto.update updateDto) {
        challengeService.update(authUser, challenge, updateDto);
        return ResponseEntity.ok().build();
    }
    
    // @PutMapping
    // public ResponseEntity updateSecretStatus(@AuthenticationPrincipal SinghaUser authUser, @RequestBody ChallengeDto.updateSecretStatus updateDto) {
    //     challengeService.updateSecretStatus(authUser, updateDto);
    //     return ResponseEntity.ok().build();
    // }
}

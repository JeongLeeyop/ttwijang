package com.weilyeat.cms.api.tfse_feedback.controller;

import javax.websocket.server.PathParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.tfse_feedback.dto.SelfFeedbackDto;
import com.weilyeat.cms.api.tfse_feedback.dto.SelfFeedbackSearch;
import com.weilyeat.cms.api.tfse_feedback.service.SelfFeedbackService;
import com.weilyeat.cms.entity.SelfFeedback;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/tfse/selfFeedback")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BOARD')")
@AllArgsConstructor
public class SelfFeedbackController {
    private final SelfFeedbackService selfFeedbackService;

    @GetMapping("list")
    public ResponseEntity<Page<SelfFeedbackDto.list>> list(@PageableDefault(size = 10, page = 0, direction = Direction.DESC, sort = {"selfFeedbackDate" }) Pageable pageable, @AuthenticationPrincipal SinghaUser authUser, SelfFeedbackSearch search) {
        return ResponseEntity.ok(selfFeedbackService.list(authUser, search, pageable));
    }

    @GetMapping
    public ResponseEntity<SelfFeedbackDto.detail> detail(@AuthenticationPrincipal SinghaUser authUser, SelfFeedbackSearch search) {
        return ResponseEntity.ok(selfFeedbackService.detail(authUser, search));
    }
    
    @GetMapping("{idx}")
    public ResponseEntity<SelfFeedbackDto.detail> detail(@PathVariable("idx") Long idx, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(selfFeedbackService.detail(idx));
    }

    @PostMapping
    public ResponseEntity add(@AuthenticationPrincipal SinghaUser authUser, @RequestBody SelfFeedbackDto.add addDto) {
        selfFeedbackService.add(authUser, addDto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("{idx}")
    public ResponseEntity delete(@PathVariable("idx") SelfFeedback entity, @AuthenticationPrincipal SinghaUser authUser) {
        selfFeedbackService.delete(entity);
        return ResponseEntity.ok().build();
    }
}

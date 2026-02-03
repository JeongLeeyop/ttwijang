package com.ttwijang.cms.api.tfse_feedback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.tfse.dto.TfseDto;
import com.ttwijang.cms.api.tfse.service.ClientTfseService;
import com.ttwijang.cms.api.tfse_feedback.dto.SelfFeedbackDto;
import com.ttwijang.cms.api.tfse_feedback.dto.SelfFeedbackSearch;
import com.ttwijang.cms.api.tfse_feedback.service.ClientSelfFeedbackService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/tfse/selfFeedback")
@AllArgsConstructor
public class ClientSelfFeedbackController {
    private final ClientSelfFeedbackService selfFeedbackService;

    @GetMapping
    public ResponseEntity<SelfFeedbackDto.detail> detail(@AuthenticationPrincipal SinghaUser authUser, SelfFeedbackSearch search) {
        return ResponseEntity.ok(selfFeedbackService.detail(authUser, search));
    }

    @PostMapping
    public ResponseEntity add(@AuthenticationPrincipal SinghaUser authUser, @RequestBody SelfFeedbackDto.add addDto) {
        selfFeedbackService.add(authUser, addDto);
        return ResponseEntity.ok().build();
    }
}

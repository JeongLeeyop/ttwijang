package com.ttwijang.cms.api.manner.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.manner.dto.MannerRatingDto;
import com.ttwijang.cms.api.manner.service.MannerRatingService;
import com.ttwijang.cms.oauth.SinghaUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Manner Rating", description = "매너 점수 평가 API")
@RestController
@RequestMapping("/api/manner-rating")
@RequiredArgsConstructor
public class MannerRatingController {

    private final MannerRatingService mannerRatingService;

    @Operation(summary = "팀 매너 점수 평가", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/team")
    public ResponseEntity<MannerRatingDto.RateResponse> rateTeam(
            @Valid @RequestBody MannerRatingDto.RateTeamRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(
                mannerRatingService.rateTeam(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "팀 매너 점수 조회")
    @GetMapping("/team/{teamUid}")
    public ResponseEntity<MannerRatingDto.TeamScoreResponse> getTeamScore(
            @PathVariable String teamUid) {
        return ResponseEntity.ok(mannerRatingService.getTeamScore(teamUid));
    }
}

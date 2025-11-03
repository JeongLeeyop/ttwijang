package com.weilyeat.cms.api.point.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.point.service.PointHistoryService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/point/history")
@AllArgsConstructor
public class PointHistoryController {
    private final PointHistoryService pointHistoryService;

    @GetMapping
    public ResponseEntity list(
        @AuthenticationPrincipal SinghaUser authUser,
        @PageableDefault(size = 10, page = 0, direction = Direction.DESC, sort = { "idx" }) Pageable pageable
    ) {
        return ResponseEntity.ok(pointHistoryService.list(authUser, pageable));
    }
}

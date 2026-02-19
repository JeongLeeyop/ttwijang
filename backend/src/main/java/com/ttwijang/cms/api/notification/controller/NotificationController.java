package com.ttwijang.cms.api.notification.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.notification.service.NotificationService;
import com.ttwijang.cms.entity.Notification;
import com.ttwijang.cms.oauth.SinghaUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Tag(name = "알림", description = "알림 API")
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 목록 조회")
    @GetMapping
    public ResponseEntity<Page<Notification>> getNotifications(
            @AuthenticationPrincipal SinghaUser userDetails,
            @PageableDefault(size = 20, sort = "createdDate", direction = Direction.DESC) Pageable pageable) {
        String userUid = userDetails.getUser().getUid();
        return ResponseEntity.ok(notificationService.getNotifications(userUid, pageable));
    }

    @Operation(summary = "읽지 않은 알림 개수 조회")
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal SinghaUser userDetails) {
        String userUid = userDetails.getUser().getUid();
        long count = notificationService.getUnreadCount(userUid);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @Operation(summary = "알림 읽음 처리")
    @PostMapping("/{uid}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable String uid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        String userUid = userDetails.getUser().getUid();
        notificationService.markAsRead(uid, userUid);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "전체 알림 읽음 처리")
    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @AuthenticationPrincipal SinghaUser userDetails) {
        String userUid = userDetails.getUser().getUid();
        notificationService.markAllAsRead(userUid);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "알림 삭제")
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable String uid,
            @AuthenticationPrincipal SinghaUser userDetails) {
        String userUid = userDetails.getUser().getUid();
        notificationService.deleteNotification(uid, userUid);
        return ResponseEntity.ok().build();
    }
}

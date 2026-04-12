package com.ttwijang.cms.api.notice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.notice.dto.NoticeDto;
import com.ttwijang.cms.api.notice.service.NoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Notice", description = "공지사항 API")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // ── 공개 API ──────────────────────────────────────────

    @Operation(summary = "공지사항 목록 조회")
    @GetMapping("/api/notice")
    public ResponseEntity<List<NoticeDto.Response>> getNoticeList() {
        return ResponseEntity.ok(noticeService.getNoticeList());
    }

    @Operation(summary = "공지사항 상세 조회")
    @GetMapping("/api/notice/{uid}")
    public ResponseEntity<NoticeDto.Response> getNotice(@PathVariable String uid) {
        return ResponseEntity.ok(noticeService.getNotice(uid));
    }

    // ── 관리자 API ────────────────────────────────────────

    @Operation(summary = "[관리자] 공지사항 등록", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/api/admin/notice")
    public ResponseEntity<NoticeDto.Response> createNotice(
            @Valid @RequestBody NoticeDto.CreateRequest request) {
        return ResponseEntity.ok(noticeService.createNotice(request));
    }

    @Operation(summary = "[관리자] 공지사항 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/api/admin/notice/{uid}")
    public ResponseEntity<NoticeDto.Response> updateNotice(
            @PathVariable String uid,
            @RequestBody NoticeDto.UpdateRequest request) {
        return ResponseEntity.ok(noticeService.updateNotice(uid, request));
    }

    @Operation(summary = "[관리자] 공지사항 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/api/admin/notice/{uid}")
    public ResponseEntity<Void> deleteNotice(@PathVariable String uid) {
        noticeService.deleteNotice(uid);
        return ResponseEntity.ok().build();
    }
}

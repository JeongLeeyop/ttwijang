package com.ttwijang.cms.api.faq.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.faq.dto.FaqDto;
import com.ttwijang.cms.api.faq.service.FaqService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "FAQ", description = "자주 묻는 질문 API")
@RestController
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    // ── 공개 API ──────────────────────────────────────────

    @Operation(summary = "FAQ 목록 조회")
    @GetMapping("/api/faq")
    public ResponseEntity<List<FaqDto.Response>> getFaqList() {
        return ResponseEntity.ok(faqService.getFaqList());
    }

    // ── 관리자 API ────────────────────────────────────────

    @Operation(summary = "[관리자] FAQ 등록", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/api/admin/faq")
    public ResponseEntity<FaqDto.Response> createFaq(
            @Valid @RequestBody FaqDto.CreateRequest request) {
        return ResponseEntity.ok(faqService.createFaq(request));
    }

    @Operation(summary = "[관리자] FAQ 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/api/admin/faq/{uid}")
    public ResponseEntity<FaqDto.Response> updateFaq(
            @PathVariable String uid,
            @RequestBody FaqDto.UpdateRequest request) {
        return ResponseEntity.ok(faqService.updateFaq(uid, request));
    }

    @Operation(summary = "[관리자] FAQ 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/api/admin/faq/{uid}")
    public ResponseEntity<Void> deleteFaq(@PathVariable String uid) {
        faqService.deleteFaq(uid);
        return ResponseEntity.ok().build();
    }
}

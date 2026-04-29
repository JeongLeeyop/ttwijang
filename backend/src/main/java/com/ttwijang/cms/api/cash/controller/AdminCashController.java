package com.ttwijang.cms.api.cash.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.cash.dto.CashDto;
import com.ttwijang.cms.api.cash.service.CashService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Admin-Cash", description = "관리자 포인트 관리 API")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/admin/cash")
@RequiredArgsConstructor
public class AdminCashController {

    private final CashService cashService;

    @Operation(summary = "유저 포인트 지급/차감", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/adjust")
    public ResponseEntity<CashDto.TransactionResponse> adjustPoint(
            @Valid @RequestBody CashDto.AdminAdjustRequest request) {
        return ResponseEntity.ok(cashService.adminAdjust(request));
    }

    @Operation(summary = "유저별 거래 내역 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/transactions/{userUid}")
    public ResponseEntity<Page<CashDto.TransactionResponse>> getUserTransactions(
            @PathVariable String userUid,
            @PageableDefault(direction = Direction.DESC, sort = "createdDate") Pageable pageable) {
        return ResponseEntity.ok(cashService.getTransactionsByUserUid(userUid, pageable));
    }

    @Operation(summary = "유저 지갑 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/wallet/{userUid}")
    public ResponseEntity<CashDto.WalletResponse> getUserWallet(
            @PathVariable String userUid) {
        return ResponseEntity.ok(cashService.getOrCreateWallet(userUid));
    }
}

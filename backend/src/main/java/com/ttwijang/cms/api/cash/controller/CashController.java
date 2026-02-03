package com.ttwijang.cms.api.cash.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.cash.dto.CashDto;
import com.ttwijang.cms.api.cash.service.CashService;
import com.ttwijang.cms.oauth.SinghaUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cash", description = "캐시/지갑 관리 API")
@RestController
@RequestMapping("/api/cash")
@RequiredArgsConstructor
public class CashController {

    private final CashService cashService;

    @Operation(summary = "내 지갑 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/wallet")
    public ResponseEntity<CashDto.WalletResponse> getWallet(
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(cashService.getOrCreateWallet(userDetails.getUser().getUid()));
    }

    @Operation(summary = "캐시 충전", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/charge")
    public ResponseEntity<CashDto.TransactionResponse> charge(
            @Valid @RequestBody CashDto.ChargeRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(cashService.charge(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "캐시 사용", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/use")
    public ResponseEntity<CashDto.TransactionResponse> use(
            @Valid @RequestBody CashDto.UseRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(cashService.use(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "거래 내역 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/transactions")
    public ResponseEntity<Page<CashDto.TransactionResponse>> getTransactions(
            @AuthenticationPrincipal SinghaUser userDetails,
            @PageableDefault(direction = Direction.DESC, sort = "createdDate") Pageable pageable) {
        return ResponseEntity.ok(cashService.getTransactions(userDetails.getUser().getUid(), pageable));
    }

    @Operation(summary = "기간별 거래 내역 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/transactions/range")
    public ResponseEntity<Page<CashDto.TransactionResponse>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @AuthenticationPrincipal SinghaUser userDetails,
            @PageableDefault(direction = Direction.DESC, sort = "createdDate") Pageable pageable) {
        return ResponseEntity.ok(cashService.getTransactionsByDateRange(userDetails.getUser().getUid(), startDate, endDate, pageable));
    }

    @Operation(summary = "팀 후원하기", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/sponsorship")
    public ResponseEntity<CashDto.SponsorshipResponse> sponsorTeam(
            @Valid @RequestBody CashDto.SponsorshipRequest request,
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(cashService.sponsorTeam(request, userDetails.getUser().getUid()));
    }

    @Operation(summary = "팀 후원 목록 조회")
    @GetMapping("/sponsorship/team/{teamUid}")
    public ResponseEntity<List<CashDto.SponsorshipResponse>> getTeamSponsorships(
            @PathVariable String teamUid) {
        return ResponseEntity.ok(cashService.getTeamSponsorships(teamUid));
    }

    @Operation(summary = "내 후원 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/sponsorship/my")
    public ResponseEntity<List<CashDto.SponsorshipResponse>> getMySponsorships(
            @AuthenticationPrincipal SinghaUser userDetails) {
        return ResponseEntity.ok(cashService.getMySponsorships(userDetails.getUser().getUid()));
    }

    @Operation(summary = "팀 후원 요약 조회")
    @GetMapping("/sponsorship/team/{teamUid}/summary")
    public ResponseEntity<CashDto.TeamSponsorshipSummary> getTeamSponsorshipSummary(
            @PathVariable String teamUid) {
        return ResponseEntity.ok(cashService.getTeamSponsorshipSummary(teamUid));
    }
}

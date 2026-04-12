package com.ttwijang.cms.api.settlement.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ttwijang.cms.api.settlement.dto.SettlementDto;
import com.ttwijang.cms.api.settlement.service.SettlementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/settlement")
@RequiredArgsConstructor
@Tag(name = "Settlement", description = "정산 관리 API")
public class SettlementController {

    private final SettlementService settlementService;

    // ─── 설정 ───────────────────────────────────────

    @GetMapping("/config")
    @Operation(summary = "정산 설정 조회")
    public ResponseEntity<SettlementDto.ConfigResponse> getConfig() {
        return ResponseEntity.ok(settlementService.getConfig());
    }

    @PutMapping("/config")
    @Operation(summary = "정산 설정 수정 (N일, 안내문)")
    public ResponseEntity<SettlementDto.ConfigResponse> updateConfig(
            @RequestBody SettlementDto.ConfigUpdateRequest request) {
        return ResponseEntity.ok(settlementService.updateConfig(request));
    }

    // ─── 월별 집계 ──────────────────────────────────

    @GetMapping("/monthly")
    @Operation(summary = "월별 팀별 정산 집계", description = "period: YYYY-MM 형식")
    public ResponseEntity<List<SettlementDto.MonthlySummary>> getMonthlySummary(
            @RequestParam String period) {
        return ResponseEntity.ok(settlementService.getMonthlySummary(period));
    }

    @GetMapping("/monthly/{teamUid}/items")
    @Operation(summary = "월별 특정 팀 건별 내역 (모달용)")
    public ResponseEntity<List<SettlementDto.ItemResponse>> getModalItems(
            @PathVariable String teamUid,
            @RequestParam String period) {
        return ResponseEntity.ok(settlementService.getModalItems(teamUid, period));
    }

    // ─── 건별 내역 탭 ────────────────────────────────

    @GetMapping("/items")
    @Operation(summary = "건별 거래 내역 조회 (탭)")
    public ResponseEntity<Page<SettlementDto.ItemResponse>> getDetailItems(
            @RequestParam(required = false) String teamName,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(settlementService.getDetailItems(teamName, startDate, endDate, pageable));
    }

    // ─── 정산 생성 / 완료 ────────────────────────────

    @PostMapping
    @Operation(summary = "정산 생성 (PENDING)")
    public ResponseEntity<SettlementDto.HistoryResponse> createSettlement(
            @RequestBody SettlementDto.CreateRequest request) {
        return ResponseEntity.ok(settlementService.createSettlement(request));
    }

    @PutMapping("/{uid}/complete")
    @Operation(summary = "정산 완료 처리 (Toss 페이아웃 실행)")
    public ResponseEntity<SettlementDto.HistoryResponse> completeSettlement(
            @PathVariable String uid,
            @RequestBody SettlementDto.CompleteRequest request) {
        return ResponseEntity.ok(settlementService.completeSettlement(uid, request));
    }

    // ─── 이력 ───────────────────────────────────────

    @GetMapping("/history")
    @Operation(summary = "정산 완료 이력 조회")
    public ResponseEntity<Page<SettlementDto.HistoryResponse>> getHistory(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(settlementService.getHistory(pageable));
    }
}

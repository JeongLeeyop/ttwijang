package com.ttwijang.cms.api.region.controller;

import com.ttwijang.cms.api.region.dto.RegionCodeDto;
import com.ttwijang.cms.api.region.service.RegionCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 지역 코드 Controller
 */
@Tag(name = "지역 코드", description = "지역 코드 관리 API")
@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionCodeController {

    private final RegionCodeService regionCodeService;

    /**
     * 시/도 목록 조회
     */
    @Operation(summary = "시/도 목록 조회", description = "전체 시/도 목록을 조회합니다.")
    @GetMapping("/sido")
    public ResponseEntity<List<RegionCodeDto.Response>> getSidoList() {
        return ResponseEntity.ok(regionCodeService.getSidoList());
    }

    /**
     * 시/군/구 목록 조회
     */
    @Operation(summary = "시/군/구 목록 조회", description = "특정 시/도의 시/군/구 목록을 조회합니다.")
    @GetMapping("/sigungu")
    public ResponseEntity<List<RegionCodeDto.Response>> getSigunguList(
            @RequestParam String parentCode
    ) {
        return ResponseEntity.ok(regionCodeService.getSigunguList(parentCode));
    }
}

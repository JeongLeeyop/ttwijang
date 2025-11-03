package com.weilyeat.cms.api.settle.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.settle.dto.AdmSettleSettingDto;
import com.weilyeat.cms.api.settle.service.AdmSettleSettingService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/settle/setting")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdmSettleSettingController {
    private final AdmSettleSettingService admSettleSettingService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_ADMIN')")
    public ResponseEntity<AdmSettleSettingDto.detail> detail() {
        return ResponseEntity.ok(admSettleSettingService.detail());
    }

    @PostMapping
    public ResponseEntity save(@RequestBody AdmSettleSettingDto.save saveDto) {
        admSettleSettingService.save(saveDto);
        return ResponseEntity.ok().build();
    }
    
}

package com.ttwijang.cms.api.bank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.bank.dto.AdmBankDto;
import com.ttwijang.cms.api.bank.service.AdmBankService;

import lombok.AllArgsConstructor;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/bank")
@AllArgsConstructor
public class AdmBankController {
    private final AdmBankService admBankService;

    @GetMapping
    public ResponseEntity<List<AdmBankDto.list>> list() {
        return ResponseEntity.ok(admBankService.list());
    }
}

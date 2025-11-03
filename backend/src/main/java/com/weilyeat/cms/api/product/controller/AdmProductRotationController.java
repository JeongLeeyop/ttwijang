package com.ttwijang.cms.api.product.controller;

import java.util.ArrayList;
import java.util.List;

import com.ttwijang.cms.api.product.dto.AdmProductDto;
import com.ttwijang.cms.api.product.dto.AdmProductRotationDto;
import com.ttwijang.cms.api.product.service.AdmProductRotationService;
import com.ttwijang.cms.api.product.service.AdmProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/product/rotation")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_ADMIN')")
public class AdmProductRotationController {
    
    private final AdmProductRotationService service;

    @GetMapping("{weekNum}")
    public ResponseEntity<AdmProductRotationDto.list> list(@PathVariable Integer weekNum) {
        
        return ResponseEntity.ok(service.get(weekNum));
    }
    
    @PostMapping("{weekNum}")
    public ResponseEntity update(@PathVariable Integer weekNum, @RequestBody AdmProductRotationDto.update dto) {
        service.update(weekNum, dto);
        return ResponseEntity.ok().build();

    }
}

package com.weilyeat.cms.api.delivery_price.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.delivery_price.dto.AdmDeliveryPriceDto;
import com.weilyeat.cms.api.delivery_price.service.AdmDeliveryPriceService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/deliveryPrice")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdmDeliveryPriceController {
    private final AdmDeliveryPriceService admdeliveryPriceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_ADMIN')")
    public ResponseEntity<AdmDeliveryPriceDto.detail> detail() {
        return ResponseEntity.ok(admdeliveryPriceService.detail());
    }

    @PostMapping
    public ResponseEntity save(@RequestBody AdmDeliveryPriceDto.save saveDto) {
        admdeliveryPriceService.save(saveDto);
        return ResponseEntity.ok().build();
    }
    
}

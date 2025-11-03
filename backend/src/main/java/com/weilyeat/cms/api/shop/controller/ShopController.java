package com.weilyeat.cms.api.shop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.shop.dto.AdmShopDto;
import com.weilyeat.cms.api.shop.dto.ShopDto;
import com.weilyeat.cms.api.shop.service.ShopService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client/shop")
public class ShopController {
    private final ShopService shopService;

    @GetMapping
    public ResponseEntity<List<ShopDto.list>> list() {
        return ResponseEntity.ok(shopService.list());
    }

    @GetMapping("{idx}")
    public ResponseEntity<ShopDto.detail> detail(@PathVariable("idx") Integer idx) {
        return ResponseEntity.ok(shopService.detail(idx));
    }

    @GetMapping("{idx}/pickup/time")
    public ResponseEntity<List<ShopDto.pickupTime>> getPickupTimes(@PathVariable("idx") Integer idx) {
        return ResponseEntity.ok(shopService.getPickupTimes(idx));
    }
}

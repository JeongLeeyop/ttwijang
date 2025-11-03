package com.weilyeat.cms.api.product.controller;

import com.weilyeat.cms.api.product.dto.ProductRotationDto;
import com.weilyeat.cms.api.product.service.ProductRotationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/product/rotation")
@AllArgsConstructor
public class ProductRotationController {
    
    private final ProductRotationService service;

    @GetMapping("{weekNum}")
    public ResponseEntity<ProductRotationDto.info> list(@PathVariable Integer weekNum) {
        return ResponseEntity.ok(service.list(weekNum));
    }
}

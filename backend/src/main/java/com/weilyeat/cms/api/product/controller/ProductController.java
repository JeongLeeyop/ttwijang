package com.ttwijang.cms.api.product.controller;

import com.ttwijang.cms.api.product.dto.AdmProductDto;
import com.ttwijang.cms.api.product.dto.ProductDto;
import com.ttwijang.cms.api.product.dto.ProductRotationDto;
import com.ttwijang.cms.api.product.dto.search.AdmProductSearch;
import com.ttwijang.cms.api.product.dto.search.ProductSearch;
import com.ttwijang.cms.api.product.service.ProductRotationService;
import com.ttwijang.cms.api.product.service.ProductService;
import com.ttwijang.cms.oauth.SinghaUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/product")
@AllArgsConstructor
public class ProductController {
    
    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDto.list>> list(
        @PageableDefault(direction = Direction.DESC, sort = { "createDate" }) Pageable pageable,
        @AuthenticationPrincipal SinghaUser authUser,
        ProductSearch search
    ) {
        return ResponseEntity.ok(service.list(pageable, search, authUser));
    }

    @GetMapping("{productIdx}")
    public ResponseEntity<ProductDto.detail> detail(@PathVariable Long productIdx) {
        return ResponseEntity.ok(service.detail(productIdx));
    }
}

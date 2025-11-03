package com.ttwijang.cms.api.product.controller;

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

import com.ttwijang.cms.api.product.dto.ProductPackageDto;
import com.ttwijang.cms.api.product.dto.search.ProductPackageSearch;
import com.ttwijang.cms.api.product.service.ProductPackageService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client/product/package")
public class ClientProductPackageController {
    private final ProductPackageService packageService;

    @GetMapping
    public ResponseEntity<Page<ProductPackageDto.list>> list(
        @PageableDefault(direction = Direction.DESC, sort = { "createDate" }) Pageable pageable,
        @AuthenticationPrincipal SinghaUser authUser,
        ProductPackageSearch search
    ) {
        return ResponseEntity.ok(packageService.list(pageable, search, authUser));
    }

    @GetMapping("{idx}")
    public ResponseEntity<ProductPackageDto.detail> detail(
        @PathVariable("idx") Long idx, 
        @AuthenticationPrincipal SinghaUser authUser
    ) {
        return ResponseEntity.ok(packageService.detail(idx, authUser));
    }
}

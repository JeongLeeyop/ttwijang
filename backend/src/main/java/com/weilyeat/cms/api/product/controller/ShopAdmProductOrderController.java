package com.ttwijang.cms.api.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.product.dto.AdmProductOrderDto;
import com.ttwijang.cms.api.product.dto.ShopAdmProductOrderDto;
import com.ttwijang.cms.api.product.repository.search.AdmProductOrderSearch;
import com.ttwijang.cms.api.product.service.ShopAdmProductOrderService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/manager/product/order")
@PreAuthorize("hasAnyRole('ROLE_SHOP_ADMIN')")
public class ShopAdmProductOrderController {
    private final ShopAdmProductOrderService shopAdmProductOrderService;

    @GetMapping
    public ResponseEntity<Page<AdmProductOrderDto.listByDay>> list(
        @AuthenticationPrincipal SinghaUser authUser,
        AdmProductOrderSearch search,
        @PageableDefault(
            size=10,
            page=0,
            direction = Direction.DESC,
            sort = {"idx"}) Pageable pageable
    ) {
        return ResponseEntity.ok(shopAdmProductOrderService.list(authUser, search, pageable));
    }

    
    @GetMapping("total/statistics")
    public ResponseEntity<AdmProductOrderDto.totalStatistics> getTotalStatistics(@AuthenticationPrincipal SinghaUser authUser, AdmProductOrderSearch search) {
        return ResponseEntity.ok(shopAdmProductOrderService.getTotalStatistics(authUser, search));
    }

}

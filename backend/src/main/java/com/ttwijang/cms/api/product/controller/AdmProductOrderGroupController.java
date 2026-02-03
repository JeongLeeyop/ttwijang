package com.ttwijang.cms.api.product.controller;

import com.ttwijang.cms.api.product.repository.search.AdmProductOrderGroupSearch;
import com.ttwijang.cms.api.product.service.AdmProductOrderGroupService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/product/order/group")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_ADMIN')")
public class AdmProductOrderGroupController {
    private final AdmProductOrderGroupService service; 

    @GetMapping
    public ResponseEntity list(
        AdmProductOrderGroupSearch search,
        @PageableDefault(
            size=10,
            page=0,
            direction = Direction.DESC,
            sort = {"createDate"}) Pageable pageable
    ) {
        return ResponseEntity.ok(service.list(search, pageable));
    }

    @GetMapping("item")
    public ResponseEntity listByItem() {
        return ResponseEntity.ok().build();
    }
}

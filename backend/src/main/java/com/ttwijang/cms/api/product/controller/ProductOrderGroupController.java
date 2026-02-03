package com.ttwijang.cms.api.product.controller;

import com.ttwijang.cms.api.product.dto.ProductOrderGroupDto;
import com.ttwijang.cms.api.product.repository.ProductOrderGroupRepository;
import com.ttwijang.cms.api.product.service.ProductOrderGroupService;
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

@AllArgsConstructor
@RequestMapping("/api/client/product/order/group")
@RestController
public class ProductOrderGroupController {

    private final ProductOrderGroupService orderGroupService;

    @GetMapping
    public ResponseEntity<Page<ProductOrderGroupDto.list>> list(
        @PageableDefault(
            size=10,
            page=0,
            direction = Direction.DESC,
            sort = {"createDate"}) Pageable pageable,
        @AuthenticationPrincipal SinghaUser authUser
        ) {
        return ResponseEntity.ok(orderGroupService.list(pageable, authUser));
    }

    @GetMapping("{groupId}")
    public ResponseEntity detail(@PathVariable Integer groupId, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(orderGroupService.detail(groupId, authUser));
    }
}

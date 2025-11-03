package com.ttwijang.cms.api.product.controller;

import java.net.URISyntaxException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.payment.dto.PaymentDto;
import com.ttwijang.cms.api.payment.dto.PaymentRequestDto;
import com.ttwijang.cms.api.payment.service.TossPaymentService;
import com.ttwijang.cms.api.product.dto.ProductOrderDto;
import com.ttwijang.cms.api.product.service.ProductOrderService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/client/product/order")
public class ProductOrderController {

    private final ProductOrderService service;
    private final TossPaymentService tossPaymentService;

    @GetMapping
    public ResponseEntity<Page<ProductOrderDto.list>> list(
        @PageableDefault(size = 10, page = 0, direction = Direction.DESC, sort = {"idx"}) Pageable pageable,
        @AuthenticationPrincipal SinghaUser authUser
    ) {
        return ResponseEntity.ok(service.list(pageable, authUser));
    }

    @GetMapping("remain/count")
    public ResponseEntity getRemainCount(@AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(service.getRemainCount(authUser));
    }
    
    @PostMapping
    public ResponseEntity<PaymentDto.createKey> order(@RequestBody ProductOrderDto.order data, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(service.order(data, authUser));
    }

    @PutMapping("refund/{orderGroupId}")
    public ResponseEntity refund(
        @PathVariable("orderGroupId") Integer orderGroupId,
        @AuthenticationPrincipal SinghaUser authUser,
        @RequestBody PaymentRequestDto.refund dto
    ) throws URISyntaxException {
        tossPaymentService.refund(orderGroupId, dto, authUser);
        return ResponseEntity.ok().build();
    }

    
    @PostMapping("shop/{idx}/count")
    public ResponseEntity<ProductOrderDto.orderCountList> getOrderCount(@RequestBody ProductOrderDto.orderCountList data, @AuthenticationPrincipal SinghaUser authUser) {   
        return ResponseEntity.ok(service.getOrderCount(data));
    }

}

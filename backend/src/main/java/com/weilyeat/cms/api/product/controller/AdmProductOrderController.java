package com.weilyeat.cms.api.product.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.product.dto.AdmProductOrderDto;
import com.weilyeat.cms.api.product.repository.search.AdmProductOrderSearch;
import com.weilyeat.cms.api.product.service.AdmProductOrderService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/product/order")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_ADMIN')")
public class AdmProductOrderController {
    private final AdmProductOrderService admProductOrderService;

    @GetMapping
    public ResponseEntity<Page<AdmProductOrderDto.list>> list(
        AdmProductOrderSearch search,
        @PageableDefault(
            size=10,
            page=0,
            direction = Direction.DESC,
            sort = {"idx"}) Pageable pageable
        ) {
        return ResponseEntity.ok(admProductOrderService.list(search, pageable));
    }
    
    @GetMapping("day")
    public ResponseEntity<Page<AdmProductOrderDto.listByDay>> listByDay(AdmProductOrderSearch search, @PageableDefault(size=10, page=0) Pageable pageable, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(admProductOrderService.listByDay(search, pageable,authUser));
    }

    @GetMapping("product")
    public ResponseEntity<Page<AdmProductOrderDto.listByProduct>> listByProduct(AdmProductOrderSearch search, @PageableDefault(size=10, page=0) Pageable pageable) {
        return ResponseEntity.ok(admProductOrderService.listByProduct(search, pageable));
    }

    @GetMapping("shop")
    public ResponseEntity<Page<AdmProductOrderDto.listByShop>> listByShop(AdmProductOrderSearch search, @PageableDefault(size=10, page=0) Pageable pageable) {
        return ResponseEntity.ok(admProductOrderService.listByShop(search, pageable));
    }
    
    @GetMapping("excel")
    public void excel(AdmProductOrderSearch search, Pageable pageable, HttpServletResponse response, @AuthenticationPrincipal SinghaUser authUser) throws IOException{
        admProductOrderService.excel(search, pageable, response, authUser);
    }

    @GetMapping("{idx}/detail")
    public ResponseEntity<AdmProductOrderDto.detail> detail(@PathVariable("idx") Integer idx) {
        return ResponseEntity.ok(admProductOrderService.detail(idx));
    }
    
    @GetMapping("product/{idx}/detail")
    public ResponseEntity<List<AdmProductOrderDto.detailByProduct>> detailByProduct(@PathVariable("idx") Integer idx, AdmProductOrderSearch search) {
        return ResponseEntity.ok(admProductOrderService.detailByProduct(idx, search));
    }

    @GetMapping("day/{idx}/detail")
    public ResponseEntity<List<AdmProductOrderDto.detailByDay>> detailByDay(@PathVariable("idx") Integer idx) {
        return ResponseEntity.ok(admProductOrderService.detailByDay(idx));
    }

    @GetMapping("shop/{idx}/detail")
    public ResponseEntity<List<AdmProductOrderDto.detailByShop>> detailByShop(@PathVariable("idx") Integer idx, AdmProductOrderSearch search) {
        return ResponseEntity.ok(admProductOrderService.detailByShop(idx, search));
    }
    
    @GetMapping("station/{idx}/detail")
    public ResponseEntity<List<AdmProductOrderDto.detailByShop>> detailByStation(@PathVariable("idx") Integer idx, AdmProductOrderSearch search) {
        return ResponseEntity.ok(admProductOrderService.detailByStation(idx, search));
    }

    @GetMapping("total/statistics")
    public ResponseEntity<AdmProductOrderDto.totalStatistics> getTotalStatistics(AdmProductOrderSearch search) {
        return ResponseEntity.ok(admProductOrderService.getTotalStatistics(search));
    }

    @PutMapping("{idx}")
    public ResponseEntity updateStatus(@PathVariable("idx") Integer idx, @RequestBody AdmProductOrderDto.updateStatus dto) {
        admProductOrderService.updateStatus(idx, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("day/{idx}")
    public ResponseEntity updateStatusByDay(@PathVariable("idx") Integer idx, @RequestBody AdmProductOrderDto.updateStatus dto) {
        admProductOrderService.updateStatusByDay(idx, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("receive/status")
    public ResponseEntity updateReceiveStatus(@RequestBody AdmProductOrderDto.updateReceiveStatus dto, AdmProductOrderSearch search) {
        admProductOrderService.updateReceiveStatus(dto, search);
        return ResponseEntity.ok().build();
    }
}

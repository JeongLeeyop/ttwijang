package com.weilyeat.cms.api.coupon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.coupon.dto.AdmCouponDto;
import com.weilyeat.cms.api.coupon.dto.search.AdmCouponSearch;
import com.weilyeat.cms.api.coupon.service.AdmCouponService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/admin/coupon")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdmCouponController {
    private final AdmCouponService admCouponService;

    @GetMapping
    public ResponseEntity<Page<AdmCouponDto.list>> list(AdmCouponSearch search, @PageableDefault(direction = Direction.DESC, sort = { "createDate" }) Pageable pageable) {
        return ResponseEntity.ok(admCouponService.list(search, pageable));
    }

    @GetMapping("{idx}")
    public ResponseEntity<AdmCouponDto.detail> detail(@PathVariable("idx") Integer idx) {
        return ResponseEntity.ok(admCouponService.detail(idx));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody AdmCouponDto.add addDto) {
        admCouponService.add(addDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{idx}")
    public ResponseEntity update(@PathVariable("idx") Integer idx, @RequestBody AdmCouponDto.update updateDto) {
        admCouponService.update(idx, updateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{idx}")
    public ResponseEntity delete(@PathVariable("idx") Integer idx) {
        admCouponService.delete(idx);
        return ResponseEntity.ok().build();
    }
}

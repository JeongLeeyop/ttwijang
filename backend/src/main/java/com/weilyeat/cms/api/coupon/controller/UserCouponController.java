package com.ttwijang.cms.api.coupon.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.coupon.dto.UserCouponDto;
import com.ttwijang.cms.api.coupon.dto.search.UserCouponSearch;
import com.ttwijang.cms.api.coupon.service.UserCouponService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/user/coupon")
@AllArgsConstructor
public class UserCouponController {
    private final UserCouponService userCouponService;

    @GetMapping
    public ResponseEntity<Page<UserCouponDto.list>> list(
        @AuthenticationPrincipal SinghaUser authUser,
        @PageableDefault(direction = Direction.DESC, sort = { "idx" }) Pageable pageable,
        UserCouponSearch search
    ) {
        return ResponseEntity.ok(userCouponService.list(authUser, pageable, search));
    }

    @GetMapping("usable")
    public ResponseEntity<List<UserCouponDto.list>> usableList(@AuthenticationPrincipal SinghaUser authUser, UserCouponSearch search) {
        return ResponseEntity.ok(userCouponService.usableList(authUser, search));
    }

    @PostMapping("{idx}")
    public ResponseEntity download(@PathVariable("idx") Integer idx, @AuthenticationPrincipal SinghaUser authUser) {
        userCouponService.download(idx, authUser);
        return ResponseEntity.ok().build();
    }
}

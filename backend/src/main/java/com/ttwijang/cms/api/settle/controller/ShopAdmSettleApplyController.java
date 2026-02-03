package com.ttwijang.cms.api.settle.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.settle.dto.ShopAdmSettleApplyDto;
import com.ttwijang.cms.api.settle.service.ShopAdmSettleApplyService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/manager/settle/apply")
@PreAuthorize("hasAnyRole('ROLE_SHOP_ADMIN')")
public class ShopAdmSettleApplyController {
    private final ShopAdmSettleApplyService shopAdmSettleApplyService;

    @GetMapping
    public ResponseEntity<Page<ShopAdmSettleApplyDto.list>> list(
        @PageableDefault(size = 10, page = 0, direction = Direction.DESC, sort = {"applyDate" }) Pageable pageable,
        @AuthenticationPrincipal SinghaUser authUser
    ) {
        return ResponseEntity.ok(shopAdmSettleApplyService.list(pageable, authUser));
    }

    @GetMapping("{idx}")
    public ResponseEntity<ShopAdmSettleApplyDto.detail> detail(@PathVariable("idx") Integer idx, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(shopAdmSettleApplyService.detail(idx, authUser));
    }

    @PostMapping
    public ResponseEntity apply(@RequestBody ShopAdmSettleApplyDto.apply applyDto, @AuthenticationPrincipal SinghaUser authUser) {
        shopAdmSettleApplyService.apply(applyDto, authUser);
        return ResponseEntity.ok().build();
    }

    /*
    @DeleteMapping("{idx}")
    public ResponseEntity cancel(@PathVariable("idx") Integer idx, @AuthenticationPrincipal SinghaUser authUser) {
        shopAdmSettleApplyService.cancel(idx, authUser);
        return ResponseEntity.ok().build();
    }
    */
}

package com.weilyeat.cms.api.product.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.product.dto.ProductPackageDto;
import com.weilyeat.cms.api.product.dto.search.ProductPackageSearch;
import com.weilyeat.cms.api.product.service.ProductPackageService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/product/package")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_ADMIN')")
public class ProductPackageController {
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

    @PostMapping
    public ResponseEntity<?> add(@RequestBody ProductPackageDto.add addDto) {
        packageService.add(addDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{idx}")
    public ResponseEntity<?> update(
        @PathVariable("idx") Long idx,
        @RequestBody ProductPackageDto.update updateDto,
        @AuthenticationPrincipal SinghaUser authUser
    ) {
        packageService.update(idx, updateDto, authUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{idx}")
    public ResponseEntity<?> delete(
        @PathVariable("idx") Long idx,
        @AuthenticationPrincipal SinghaUser authUser
    ) {
        packageService.delete(idx, authUser);
        return ResponseEntity.ok().build();
    }
}

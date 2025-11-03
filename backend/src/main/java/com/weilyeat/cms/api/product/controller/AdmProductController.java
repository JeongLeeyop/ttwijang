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
import org.springframework.web.multipart.MultipartFile;

import com.weilyeat.cms.api.attached_file.service.AttachedFileService;
import com.weilyeat.cms.api.product.dto.AdmProductDto;
import com.weilyeat.cms.api.product.dto.search.AdmProductSearch;
import com.weilyeat.cms.api.product.service.AdmProductService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/product")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_ADMIN')")
public class AdmProductController {
    private final AdmProductService productService;
    private final AttachedFileService attachedFileService;

    @GetMapping
    public ResponseEntity<Page<AdmProductDto.list>> list(
        @PageableDefault(direction = Direction.DESC, sort = { "createDate" }) Pageable pageable,
        @AuthenticationPrincipal SinghaUser authUser,
        AdmProductSearch search
    ) {
        return ResponseEntity.ok(productService.list(pageable, search, authUser));
    }

    @GetMapping("{idx}")
    public ResponseEntity<AdmProductDto.detail> detail(@PathVariable("idx") Long idx, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(productService.detail(idx, authUser));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody AdmProductDto.add addDto) {
        productService.add(addDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("upload")
    public ResponseEntity upload(MultipartFile file) {
        return ResponseEntity.ok(attachedFileService.save(file, "shop/item"));
    }

    @PutMapping("{idx}")
    public ResponseEntity update(
        @PathVariable("idx") Long idx,
        @RequestBody AdmProductDto.update updateDto,
        @AuthenticationPrincipal SinghaUser authUser
    ) {
        productService.update(idx, updateDto, authUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{idx}")
    public ResponseEntity update(
        @PathVariable("idx") Long idx,
        @AuthenticationPrincipal SinghaUser authUser
    ) {
        productService.delete(idx, authUser);
        return ResponseEntity.ok().build();
    }
}

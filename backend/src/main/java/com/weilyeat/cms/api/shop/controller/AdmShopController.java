package com.ttwijang.cms.api.shop.controller;

import java.util.List;

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

import com.ttwijang.cms.api.shop.dto.AdmShopDto;
import com.ttwijang.cms.api.shop.dto.search.AdmShopSearch;
import com.ttwijang.cms.api.shop.service.AdmShopService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/shop")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdmShopController {
    private final AdmShopService shopService;

    @GetMapping
    public ResponseEntity<Page<AdmShopDto.list>> list(
        @PageableDefault(direction = Direction.DESC, sort = { "createDate" }) Pageable pageable,
        AdmShopSearch search
    ) {
        return ResponseEntity.ok(shopService.list(pageable, search));
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<AdmShopDto.list>> listAll() {
        return ResponseEntity.ok(shopService.listAll());
    }

    @GetMapping("{idx}")
    public ResponseEntity<AdmShopDto.detail> detail(@PathVariable("idx") Integer idx) {
        return ResponseEntity.ok(shopService.detail(idx));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody AdmShopDto.add addDto) {
        shopService.add(addDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{idx}")
    public ResponseEntity update(@PathVariable("idx") Integer idx, @RequestBody AdmShopDto.update updateDto) {
        shopService.update(idx, updateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{idx}")
    public ResponseEntity withdraw(@PathVariable("idx") Integer idx) {
        shopService.withdraw(idx);
        return ResponseEntity.ok().build();
    }
}

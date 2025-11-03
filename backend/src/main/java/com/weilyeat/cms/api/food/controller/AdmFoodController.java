package com.ttwijang.cms.api.food.controller;

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

import com.ttwijang.cms.api.food.dto.AdmFoodDto;
import com.ttwijang.cms.api.food.dto.search.AdmFoodSearch;
import com.ttwijang.cms.api.food.service.AdmFoodService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/admin/food")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdmFoodController {
    private final AdmFoodService admFoodService;

    @GetMapping
    public ResponseEntity<Page<AdmFoodDto.list>> list(
        @PageableDefault(size = 10, page = 0, direction = Direction.DESC, sort = {"idx"}) Pageable pageable,
        AdmFoodSearch search
    ) {
        return ResponseEntity.ok(admFoodService.list(pageable, search));
    }

    @GetMapping("{idx}")
    public ResponseEntity<AdmFoodDto.detail> detail(@PathVariable("idx") Integer idx) {
        return ResponseEntity.ok(admFoodService.detail(idx));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody AdmFoodDto.add addDto) {
        admFoodService.add(addDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{idx}")
    public ResponseEntity update(@PathVariable("idx") Integer idx, @RequestBody AdmFoodDto.update updateDto) {
        admFoodService.update(idx, updateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{idx}")
    public ResponseEntity delete(@PathVariable("idx") Integer idx) {
        admFoodService.delete(idx);
        return ResponseEntity.ok().build();
    }
}

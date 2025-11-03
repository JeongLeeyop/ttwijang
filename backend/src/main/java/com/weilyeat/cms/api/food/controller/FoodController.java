package com.weilyeat.cms.api.food.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.api.food.dto.search.FoodSearch;
import com.weilyeat.cms.api.food.service.FoodService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/client/food")
public class FoodController {
    private final FoodService foodService;

    @GetMapping
    public ResponseEntity list(@PageableDefault(size = 10, page = 0) Pageable pageable, FoodSearch search) {
        return ResponseEntity.ok(foodService.list(search, pageable));
    }
}

package com.weilyeat.cms.api.food.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.food.dto.FoodDto;
import com.weilyeat.cms.api.food.dto.mapper.FoodMapper;
import com.weilyeat.cms.api.food.dto.search.FoodSearch;
import com.weilyeat.cms.api.food.repository.FoodRepository;

import lombok.AllArgsConstructor;

public interface FoodService {
    Page<FoodDto.list> list(FoodSearch search, Pageable pageable);
}

@Service
@AllArgsConstructor
class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;

    @Override
    public Page<FoodDto.list> list(FoodSearch search, Pageable pageable) {
        return foodRepository.findAll(search.search(), pageable).map(e -> FoodMapper.INSTANCE.entityToListDto(e));
    }
}

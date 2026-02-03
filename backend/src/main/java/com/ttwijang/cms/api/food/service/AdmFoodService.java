package com.ttwijang.cms.api.food.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.food.dto.AdmFoodDto;
import com.ttwijang.cms.api.food.dto.mapper.AdmFoodMapper;
import com.ttwijang.cms.api.food.dto.search.AdmFoodSearch;
import com.ttwijang.cms.api.food.repository.FoodRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Food;

import lombok.AllArgsConstructor;

public interface AdmFoodService {
    Page<AdmFoodDto.list> list(Pageable pageable, AdmFoodSearch search);
    AdmFoodDto.detail detail(Integer idx);
    void add(AdmFoodDto.add addDto);
    void update(Integer idx, AdmFoodDto.update updateDto);
    void delete(Integer idx);
}

@Service
@AllArgsConstructor
class AdmFoodServiceImpl implements AdmFoodService {
    private final FoodRepository foodRepository;

    @Override
    public Page<AdmFoodDto.list> list(Pageable pageable, AdmFoodSearch search) {
        return foodRepository.findAll(search.search(), pageable).map(e -> AdmFoodMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public AdmFoodDto.detail detail(Integer idx) {
        Food entity = foodRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.FOOD));
        return AdmFoodMapper.INSTANCE.entityToDetailDto(entity);
    }

    @Override
    public void add(AdmFoodDto.add addDto) {
        Food entity = AdmFoodMapper.INSTANCE.addDtoToEntity(addDto);
        entity.setInputType(2);
        entity.setRef("뛰장");
        foodRepository.save(entity);
    }

    @Override
    public void update(Integer idx, AdmFoodDto.update updateDto) {
        Food entity = foodRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.FOOD));
        entity = AdmFoodMapper.INSTANCE.updateDtoToEntity(updateDto, entity);
        foodRepository.save(entity);
    }

    @Override
    public void delete(Integer idx) {
        Food entity = foodRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.FOOD));
        entity.setDeleteStatus(true);
        foodRepository.save(entity);
    }
}

package com.weilyeat.cms.api.product.service;

import java.util.List;
import java.util.stream.Collectors;

import com.weilyeat.cms.api.product.dto.AdmProductRotationDto;
import com.weilyeat.cms.api.product.dto.mapper.AdmProductMapper;
import com.weilyeat.cms.api.product.repository.AdmProductRotationRepository;
import com.weilyeat.cms.entity.ProductRotation;
import com.weilyeat.cms.entity.ProductType;
import com.weilyeat.cms.api.product.repository.AdmProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public interface AdmProductRotationService {
    public void update(Integer weekNum, AdmProductRotationDto.update dto);
    public AdmProductRotationDto.list get(Integer weekNum);
}

@Slf4j
@AllArgsConstructor
@Service
class AdmProductRotationServiceImpl implements AdmProductRotationService {

    private final AdmProductRotationRepository repository;
    private final AdmProductRepository productRepository;

    @Transactional
    @Override
    public void update(Integer weekNum, AdmProductRotationDto.update dto) {
        /*
        if (dto.getProductsIdx() == null || dto.getProductsIdx().size() == 0) repository.deleteByWeekNum(weekNum);
        else repository.deleteByWeekNumAndProductIdxNotIn(weekNum, dto.getProductsIdx());
        */
        repository.deleteByWeekNum(weekNum);
        dto.getProductsIdx().stream().forEach(action -> {
            repository.save(new ProductRotation(weekNum, action));
        });
    }
    
    @Override
    public AdmProductRotationDto.list get(Integer weekNum) {
        AdmProductRotationDto.list listDto  = new AdmProductRotationDto.list();
        listDto.setProducts(AdmProductMapper.INSTANCE.entitiesToListDto(productRepository.findAllBySellStatusAndDeleteStatusAndProductType(true, false, ProductType.PICKUP)));
        List<ProductRotation> entities = repository.findAllByWeekNum(weekNum);
        listDto.setRotationProducts(entities.stream().map(e -> AdmProductMapper.INSTANCE.entityToListDto(e.getProducts())).collect(Collectors.toList()));
        return listDto;
    }
}

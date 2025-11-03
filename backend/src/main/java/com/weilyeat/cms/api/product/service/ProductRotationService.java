package com.weilyeat.cms.api.product.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.product.dto.ProductRotationDto;
import com.weilyeat.cms.api.product.dto.mapper.ProductMapper;
import com.weilyeat.cms.api.product.repository.ProductRepository;
import com.weilyeat.cms.api.product.repository.ProductRotationRepository;
import com.weilyeat.cms.entity.Product;
import com.weilyeat.cms.entity.ProductRotation;
import com.weilyeat.cms.util.DateUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public interface ProductRotationService {
    public ProductRotationDto.info list(Integer weekNum);
}
@Slf4j
@AllArgsConstructor
@Service
class ProductRotationServiceImpl implements ProductRotationService {

    private final ProductRotationRepository repository;
    private final ProductRepository productRepository;

    @Override
    public ProductRotationDto.info list(Integer weekNum) {
        ProductRotationDto.info dto = new ProductRotationDto.info();
        List<ProductRotation> productRotationEntities = repository.findByWeekNum(weekNum);
        List<Long> productIdx = productRotationEntities.stream().map(x -> x.getProductIdx()).collect(Collectors.toList());
        List<Product> productEntities = productRepository.findAllById(productIdx);

        dto.setProducts(ProductMapper.INSTANCE.entitiesToListDto(productEntities));
        dto.setWeekNum(weekNum);
        dto.setStartDate(DateUtil.startWeekDay(weekNum));
        dto.setEndDate(DateUtil.endWeekDay(weekNum));
        return dto;
    }
    
}

package com.ttwijang.cms.api.product.service;

import com.ttwijang.cms.api.product.dto.AdmProductDto;
import com.ttwijang.cms.api.product.dto.ProductDto;
import com.ttwijang.cms.api.product.dto.ProductDto.detail;
import com.ttwijang.cms.api.product.dto.mapper.AdmProductMapper;
import com.ttwijang.cms.api.product.dto.mapper.ProductMapper;
import com.ttwijang.cms.api.product.dto.search.AdmProductSearch;
import com.ttwijang.cms.api.product.dto.search.ProductSearch;
import com.ttwijang.cms.api.product.repository.ProductRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.entity.Product;
import com.ttwijang.cms.oauth.SinghaUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

public interface ProductService {
    Page<ProductDto.list> list(Pageable pageable, ProductSearch search, SinghaUser authUser);
    public ProductDto.detail detail(Long productIdx);
}

@AllArgsConstructor
@Service
class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public Page<ProductDto.list> list(Pageable pageable, ProductSearch search, SinghaUser authUser) {
        return repository.findAll(search.Search(), pageable).map(e -> ProductMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public ProductDto.detail detail(Long productIdx) {
        Product entity = repository.findById(productIdx).orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));
        return ProductMapper.INSTANCE.entityToDetailDto(entity);
    }

}

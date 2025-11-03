package com.weilyeat.cms.api.product.service;

import com.weilyeat.cms.api.product.dto.AdmProductDto;
import com.weilyeat.cms.api.product.dto.ProductDto;
import com.weilyeat.cms.api.product.dto.ProductDto.detail;
import com.weilyeat.cms.api.product.dto.mapper.AdmProductMapper;
import com.weilyeat.cms.api.product.dto.mapper.ProductMapper;
import com.weilyeat.cms.api.product.dto.search.AdmProductSearch;
import com.weilyeat.cms.api.product.dto.search.ProductSearch;
import com.weilyeat.cms.api.product.repository.ProductRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.entity.Product;
import com.weilyeat.cms.oauth.SinghaUser;

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

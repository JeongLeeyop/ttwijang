package com.weilyeat.cms.api.product.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

import com.weilyeat.cms.api.product.dto.AdmProductDto;
import com.weilyeat.cms.api.product.dto.ProductDto;
import com.weilyeat.cms.entity.Product;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto.detail entityToDetailDto(Product entity);
    ProductDto.list entityToListDto(Product entity);
    List<ProductDto.list> entitiesToListDto(List<Product> entity);
}

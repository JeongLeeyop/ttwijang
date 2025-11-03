package com.ttwijang.cms.api.product.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.product.dto.ProductPackageDto;
import com.ttwijang.cms.entity.ProductPackage;
import com.ttwijang.cms.entity.ProductPackageItem;

@Mapper
public interface ProductPackageMapper {
    ProductPackageMapper INSTANCE = Mappers.getMapper(ProductPackageMapper.class);

    ProductPackageDto.list entityToListDto(ProductPackage entity);
    
    @Mapping(target = "packageItems", ignore = true)
    ProductPackageDto.detail entityToDetailDto(ProductPackage entity);
    
    @Mapping(target = "productType", ignore = true)
    @Mapping(target = "packageItems", ignore = true)
    @Mapping(target = "idx", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "createUserUid", ignore = true)
    @Mapping(target = "modifyDate", ignore = true)
    @Mapping(target = "modifyUserUid", ignore = true)
    @Mapping(target = "deleteStatus", ignore = true)
    ProductPackage addDtoToEntity(ProductPackageDto.add addDto);
    
    @Mapping(target = "packageItems", ignore = true)
    @Mapping(target = "idx", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "createUserUid", ignore = true)
    @Mapping(target = "modifyDate", ignore = true)
    @Mapping(target = "modifyUserUid", ignore = true)
    @Mapping(target = "deleteStatus", ignore = true)
    @Mapping(target = "productType", ignore = true)
    ProductPackage updateDtoToEntity(ProductPackageDto.update updateDto, @MappingTarget ProductPackage entity);
    
    // PackageItem 매핑
    default ProductPackageDto.PackageItem packageItemToDto(ProductPackageItem item) {
        if (item == null) {
            return null;
        }
        
        ProductPackageDto.PackageItem dto = new ProductPackageDto.PackageItem();
        dto.setIdx(item.getIdx());
        dto.setProductIdx(item.getProductIdx());
        
        if (item.getProduct() != null) {
            dto.setProductName(item.getProduct().getName());
            dto.setProductThumbUid(item.getProduct().getThumbUid());
            dto.setProductPrice(item.getProduct().getPrice());
        }
        
        return dto;
    }
    
    default List<ProductPackageDto.PackageItem> packageItemsToDtos(List<ProductPackageItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
            .map(this::packageItemToDto)
            .collect(Collectors.toList());
    }
}

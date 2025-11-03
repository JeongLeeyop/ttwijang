package com.weilyeat.cms.api.product.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.product.dto.ProductPackageDto;
import com.weilyeat.cms.api.product.dto.mapper.ProductPackageMapper;
import com.weilyeat.cms.api.product.dto.search.ProductPackageSearch;
import com.weilyeat.cms.api.product.repository.ProductPackageItemRepository;
import com.weilyeat.cms.api.product.repository.ProductPackageRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.ProductPackage;
import com.weilyeat.cms.entity.ProductPackageItem;
import com.weilyeat.cms.entity.ProductType;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ProductPackageService {
    Page<ProductPackageDto.list> list(Pageable pageable, ProductPackageSearch search, SinghaUser authUser);
    ProductPackageDto.detail detail(Long idx, SinghaUser authUser);
    void add(ProductPackageDto.add addDto);
    void update(Long idx, ProductPackageDto.update updateDto, SinghaUser authUser);
    void delete(Long idx, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class ProductPackageServiceImpl implements ProductPackageService {
    private final ProductPackageRepository packageRepository;
    private final ProductPackageItemRepository packageItemRepository;

    @Override
    public Page<ProductPackageDto.list> list(Pageable pageable, ProductPackageSearch search, SinghaUser authUser) {
        return packageRepository.findAll(search.search(), pageable)
            .map(e -> ProductPackageMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public ProductPackageDto.detail detail(Long idx, SinghaUser authUser) {
        ProductPackage entity = packageRepository.findById(idx)
            .orElseThrow(() -> new NotFoundException(NotFound.SHOP_ITEM));
        
        ProductPackageDto.detail dto = ProductPackageMapper.INSTANCE.entityToDetailDto(entity);
        dto.setPackageItems(ProductPackageMapper.INSTANCE.packageItemsToDtos(entity.getPackageItems()));
        
        return dto;
    }

    @Transactional
    @Override
    public void add(ProductPackageDto.add addDto) {
        ProductPackage entity = ProductPackageMapper.INSTANCE.addDtoToEntity(addDto);
        entity.setProductType(ProductType.valueOf(addDto.getProductType()));
        entity.setDeleteStatus(false);
        
        // 패키지 저장
        ProductPackage savedPackage = packageRepository.save(entity);
        
        // 선택된 상품들을 패키지 아이템으로 추가
        if (addDto.getProductIdxList() != null && !addDto.getProductIdxList().isEmpty()) {
            List<ProductPackageItem> items = addDto.getProductIdxList().stream()
                .map(productIdx -> {
                    ProductPackageItem item = new ProductPackageItem();
                    item.setPackageIdx(savedPackage.getIdx());
                    item.setProductIdx(productIdx);
                    return item;
                })
                .collect(Collectors.toList());
            
            packageItemRepository.saveAll(items);
        }
    }

    @Transactional
    @Override
    public void update(Long idx, ProductPackageDto.update updateDto, SinghaUser authUser) {
        ProductPackage entity = packageRepository.findById(idx)
            .orElseThrow(() -> new NotFoundException(NotFound.SHOP_ITEM));
        
        entity = ProductPackageMapper.INSTANCE.updateDtoToEntity(updateDto, entity);
        entity.setModifyDate(LocalDateTime.now());
        
        // 기존 패키지 아이템 삭제
        packageItemRepository.deleteByPackageIdx(idx);
        
        // 새로운 패키지 아이템 추가
        if (updateDto.getProductIdxList() != null && !updateDto.getProductIdxList().isEmpty()) {
            List<ProductPackageItem> items = updateDto.getProductIdxList().stream()
                .map(productIdx -> {
                    ProductPackageItem item = new ProductPackageItem();
                    item.setPackageIdx(idx);
                    item.setProductIdx(productIdx);
                    return item;
                })
                .collect(Collectors.toList());
            
            packageItemRepository.saveAll(items);
        }
        
        packageRepository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Long idx, SinghaUser authUser) {
        ProductPackage entity = packageRepository.findById(idx)
            .orElseThrow(() -> new NotFoundException(NotFound.SHOP_ITEM));
        
        entity.setDeleteStatus(true);
        packageRepository.save(entity);
    }
}

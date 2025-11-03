package com.weilyeat.cms.api.product.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.product.dto.AdmProductDto;
import com.weilyeat.cms.api.product.dto.mapper.AdmProductMapper;
import com.weilyeat.cms.api.product.dto.search.AdmProductSearch;
import com.weilyeat.cms.api.product.repository.AdmProductRepository;
import com.weilyeat.cms.api.product.repository.ProductRotationRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Product;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface AdmProductService {
    Page<AdmProductDto.list> list(Pageable pageable, AdmProductSearch search, SinghaUser authUser);
    AdmProductDto.detail detail(Long idx, SinghaUser authUser);
    void add(AdmProductDto.add addDto);
    void update(Long idx, AdmProductDto.update updateDto, SinghaUser authUser);
    void delete(Long idx, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class AdmShopItemServiceImpl implements AdmProductService {
    private final AdmProductRepository productRepository;
    private final ProductRotationRepository productRotationRepository;

    @Override
    public Page<AdmProductDto.list> list(Pageable pageable, AdmProductSearch search, SinghaUser authUser) {
        return productRepository.findAll(search.shopAdminSearch(), pageable).map(e -> AdmProductMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public AdmProductDto.detail detail(Long idx, SinghaUser authUser) {
        User user = authUser.getUser();
        
        Product entity = productRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SHOP_ITEM));
        // if (!entity.getShopIdx().equals(user.getShopIdx())) throw new BadRequestException("잘못된 접근입니다.");

        return AdmProductMapper.INSTANCE.entityToDetailDto(entity);
    }

    @Override
    public void add(AdmProductDto.add addDto) {
        Product entity = AdmProductMapper.INSTANCE.addDtoToEntity(addDto);
        productRepository.save(entity);        
    }

    @Override
    public void update(Long idx, AdmProductDto.update updateDto, SinghaUser authUser) {
        User user = authUser.getUser();
        
        Product entity = productRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SHOP_ITEM));
        // if (!entity.getShopIdx().equals(user.getShopIdx())) throw new BadRequestException("잘못된 접근입니다.");

        entity = AdmProductMapper.INSTANCE.updateDtoToEntity(updateDto, entity);
        entity.setModifyDate(LocalDateTime.now());
        entity.setModifyUserUid(user.getUid());
        productRepository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Long idx, SinghaUser authUser) {
        User user = authUser.getUser();
        
        Product entity = productRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.SHOP_ITEM));
        entity.setDeleteStatus(true);
        productRepository.save(entity);

        productRotationRepository.deleteByProductIdx(idx);
    }
}

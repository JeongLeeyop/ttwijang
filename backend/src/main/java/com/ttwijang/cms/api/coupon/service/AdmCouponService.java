package com.ttwijang.cms.api.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.coupon.dto.AdmCouponDto;
import com.ttwijang.cms.api.coupon.dto.mapper.AdmCouponMapper;
import com.ttwijang.cms.api.coupon.dto.search.AdmCouponSearch;
import com.ttwijang.cms.api.coupon.repository.CouponRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Coupon;

import lombok.AllArgsConstructor;

public interface AdmCouponService {
    Page<AdmCouponDto.list> list(AdmCouponSearch search, Pageable pageable);
    AdmCouponDto.detail detail(Integer idx);
    void add(AdmCouponDto.add addDto);
    void update(Integer idx, AdmCouponDto.update updateDto);
    void delete(Integer idx);
}

@Service
@AllArgsConstructor
class AdmCouponServiceImpl implements AdmCouponService {
    private final CouponRepository couponRepository;

    @Override
    public Page<AdmCouponDto.list> list(AdmCouponSearch search, Pageable pageable) {
        return couponRepository.findAll(search.search(), pageable).map(e -> AdmCouponMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public AdmCouponDto.detail detail(Integer idx) {
        Coupon entity = couponRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.COUPON));
        return AdmCouponMapper.INSTANCE.entityToDetailDto(entity);
    }

    @Override
    public void add(AdmCouponDto.add addDto) {
        Coupon entity = AdmCouponMapper.INSTANCE.addDtoToEntity(addDto);
        couponRepository.save(entity);
    }

    @Override
    public void update(Integer idx, AdmCouponDto.update updateDto) {
        Coupon entity = couponRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.COUPON));
        entity = AdmCouponMapper.INSTANCE.updateDtoToEntity(updateDto, entity);
        couponRepository.save(entity);
    }

    @Override
    public void delete(Integer idx) {
        Coupon entity = couponRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.COUPON));
        entity.setDeleteStatus(true);
        couponRepository.save(entity);
    }
}

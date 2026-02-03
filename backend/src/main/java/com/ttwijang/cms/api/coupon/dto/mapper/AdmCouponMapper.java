package com.ttwijang.cms.api.coupon.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.coupon.dto.AdmCouponDto;
import com.ttwijang.cms.entity.Coupon;

@Mapper
public interface AdmCouponMapper {
    AdmCouponMapper INSTANCE = Mappers.getMapper(AdmCouponMapper.class);

    AdmCouponDto.list entityToListDto(Coupon entity);

    AdmCouponDto.detail entityToDetailDto(Coupon entity);

    Coupon addDtoToEntity(AdmCouponDto.add addDto);

    Coupon updateDtoToEntity(AdmCouponDto.update updateDto, @MappingTarget Coupon entity);
}

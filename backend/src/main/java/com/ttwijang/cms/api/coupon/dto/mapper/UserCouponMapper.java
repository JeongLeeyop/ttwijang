package com.ttwijang.cms.api.coupon.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.coupon.dto.UserCouponDto;
import com.ttwijang.cms.entity.UserCoupon;

@Mapper
public interface UserCouponMapper {
    UserCouponMapper INSTANCE = Mappers.getMapper(UserCouponMapper.class);

    UserCouponDto.list entityToListDto(UserCoupon entity);
}

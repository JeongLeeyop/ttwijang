package com.weilyeat.cms.api.payment.dto.mapper;

import com.weilyeat.cms.api.payment.dto.PaymentResultDto;
import com.weilyeat.cms.entity.PaymentResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentResultMapper {
    PaymentResultMapper INSTANCE = Mappers.getMapper(PaymentResultMapper.class);

    PaymentResult dtoToEntity(PaymentResultDto.success dto);
}

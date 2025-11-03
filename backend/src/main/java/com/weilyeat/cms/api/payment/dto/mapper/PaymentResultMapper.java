package com.ttwijang.cms.api.payment.dto.mapper;

import com.ttwijang.cms.api.payment.dto.PaymentResultDto;
import com.ttwijang.cms.entity.PaymentResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentResultMapper {
    PaymentResultMapper INSTANCE = Mappers.getMapper(PaymentResultMapper.class);

    PaymentResult dtoToEntity(PaymentResultDto.success dto);
}

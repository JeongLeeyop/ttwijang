package com.ttwijang.cms.api.payment.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentRequestMapper {
    PaymentRequestMapper INSTANCE = Mappers.getMapper(PaymentRequestMapper.class);
}

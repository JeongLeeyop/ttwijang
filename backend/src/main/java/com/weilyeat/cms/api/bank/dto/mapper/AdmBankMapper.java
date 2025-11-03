package com.ttwijang.cms.api.bank.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.bank.dto.AdmBankDto;
import com.ttwijang.cms.entity.Bank;

@Mapper
public interface AdmBankMapper {
    AdmBankMapper INSTANCE = Mappers.getMapper(AdmBankMapper.class);

    AdmBankDto.list entityToListDto(Bank entity);
}

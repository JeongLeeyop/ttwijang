package com.weilyeat.cms.entity.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.entity.Account;
import com.weilyeat.cms.entity.dto.AccountDto;

@Mapper
public interface AccountMapper {
    
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto.Detail entityToDetailDto(Account entity);
    
    Account addDtoToEntity(AccountDto.Add addDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account updateDtoToEntity(AccountDto.Add updateDto, @MappingTarget Account entity);
    
    AccountDto.Dest entityToDestDto(Account entity);
    Account destDtoToEntity(AccountDto.Dest destDto);

    List<AccountDto.Detail> entityListToDetailDtoList(List<Account> entityList);
}
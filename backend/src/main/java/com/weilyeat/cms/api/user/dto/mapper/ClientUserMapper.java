package com.weilyeat.cms.api.user.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.user.dto.ClientUserDto;
import com.weilyeat.cms.entity.User;

@Mapper
public interface ClientUserMapper {
    ClientUserMapper INSTANCE = Mappers.getMapper(ClientUserMapper.class);

    User joinDtoToEntity(ClientUserDto.join dto, @MappingTarget User entity);

    ClientUserDto.info entityToInfoDto(User entity);

    User updateDtoToEntity(ClientUserDto.update dto, @MappingTarget User entity);
}

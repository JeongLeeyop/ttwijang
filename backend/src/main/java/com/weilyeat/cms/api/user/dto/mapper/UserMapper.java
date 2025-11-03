package com.weilyeat.cms.api.user.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.user.dto.UserDto;
import com.weilyeat.cms.entity.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto.Page entityToPageDto(User entity);
    List<UserDto.Simple> entityToSimple(List<User> users);

    UserDto.Detail entityToDetailDto(User entity);
    List<UserDto.Detail> entityToDetailDto(List<User> entity);

    @Mapping(target = "shopName", source = "shop.name")
    UserDto.manager entityToManagerDto(User entity);

    @Mapping(target = "roles", ignore = true)
    User joinDtoToEntity(UserDto.JoinAdmin addDto);
    
    @Mapping(target = "roles", ignore = true)
    User joinDtoToEntity(UserDto.JoinSoicalUser addDto);
    // @Mapping(target = "roles", ignore = true)
    // User joinDtoToEntity(UserDto.JoinClient addDto);
    
    @Mapping(target = "roles", ignore = true)
    User updateDtoToEntity(UserDto.Update updateDto, @MappingTarget User entity);

    User addManagerDtoToEntity(UserDto.addManager dto);

    User updateManagerDtoToEntity(UserDto.updateManager dto, @MappingTarget User entity);
}

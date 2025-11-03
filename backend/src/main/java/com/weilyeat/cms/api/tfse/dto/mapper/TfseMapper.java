package com.weilyeat.cms.api.tfse.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.tfse.dto.TfseDto;
import com.weilyeat.cms.entity.Tfse;

@Mapper
public interface TfseMapper {
    TfseMapper INSTANCE = Mappers.getMapper(TfseMapper.class);
    TfseDto.detail entityToDetail(Tfse tfse);
    TfseDto.list entityToList(Tfse tfse);
    Tfse detailDtoToEntity(TfseDto.detail deatilDto);
    Tfse addDtoToEntity(TfseDto.add addDto);
    Tfse updateDtoToEntity(TfseDto.update updateDto, @MappingTarget Tfse entity);

    default TfseDto.detail entityToAdmDetail(Tfse tfse){
        TfseDto.detail dto = entityToDetail(tfse);
        dto.setWriter(dto.getUser().getActualName());
        dto.setUser(null);
        return dto;
    }
    default TfseDto.list entityToAdmList(Tfse tfse){
        TfseDto.list dto = entityToList(tfse);
        dto.setWriter(dto.getUser().getActualName());
        dto.setUser(null);
        return dto;
    }

}

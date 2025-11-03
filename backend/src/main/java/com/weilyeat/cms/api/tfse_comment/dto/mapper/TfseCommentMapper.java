package com.weilyeat.cms.api.tfse_comment.dto.mapper;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.weilyeat.cms.api.tfse_comment.dto.TfseCommentDto;
import com.weilyeat.cms.entity.TfseComment;

@Mapper
public interface TfseCommentMapper {
    TfseCommentMapper INSTANCE = Mappers.getMapper(TfseCommentMapper.class);

    // @Mapping(target = "password", ignore = true)
    TfseComment addDtoToEntity(TfseCommentDto.Add addDto);

    // @Mapping(target = "password", ignore = true)
    TfseComment updateDtoToEntity(TfseCommentDto.Update updateDto, @MappingTarget TfseComment entity);

    TfseCommentDto.Detail entityToBasicDetailDto(TfseComment entity);

    default TfseCommentDto.Detail entityToDetailDto(TfseComment entity, String userUid) {
        TfseCommentDto.Detail dto = entityToBasicDetailDto(entity);
        if(dto.getUserUid().equals(userUid)){
            dto.setHasAuthority(true);
        } else dto.setHasAuthority(false);

        dto.getChildren().stream().forEach(x -> {
            if(x.getUserUid().equals(userUid)){
                x.setHasAuthority(true);
            } else x.setHasAuthority(false);
        });
        return dto;
    }
    
    default TfseCommentDto.Detail entityToAdmDetailDto(TfseComment entity, String userUid) {
        TfseCommentDto.Detail dto = entityToBasicDetailDto(entity);
        if(dto.getUserUid().equals(userUid)){
            dto.setHasAuthority(true);
        } else dto.setHasAuthority(false);

        dto.setWriter(entity.getUser().getActualName());
        dto.setUser(null);

        dto.getChildren().stream().forEach(x -> {
            if(x.getUserUid().equals(userUid)){
                x.setHasAuthority(true);
            } else x.setHasAuthority(false);
            x.setWriter(x.getUser().getActualName());
            x.setUser(null);
        });
        return dto;
    }
}

package com.ttwijang.cms.api.review.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ttwijang.cms.api.review.dto.ReviewDto;
import com.ttwijang.cms.entity.Review;

@Mapper
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    ReviewDto.list entityToListDtoNormal(Review entity);

    ReviewDto.detail entityToDetailDto(Review entity);

    Review addDtoToEntity(ReviewDto.add addDto);

    Review updateDtoToEntity(ReviewDto.update updateDto, @MappingTarget Review entity);
    
    default ReviewDto.list entityToListDto(Review entity) {
        ReviewDto.list dto = entityToListDtoNormal(entity);
        StringBuilder sb = new StringBuilder(entity.getUser().getActualName());
        for (int i = 1; i < entity.getUser().getActualName().length(); i++) {
            sb.setCharAt(i, '*');
        }
        dto.setUserName(sb.toString());
        return dto;
    }
}

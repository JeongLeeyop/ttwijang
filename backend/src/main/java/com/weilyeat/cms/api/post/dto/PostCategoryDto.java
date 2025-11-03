package com.weilyeat.cms.api.post.dto;

import javax.validation.constraints.NotBlank;

import com.weilyeat.cms.api.board.dto.BoardCategoryDto;

import lombok.Data;

public class PostCategoryDto {
    @Data
    public static class Detail {
        private String categoryUid;
        private BoardCategoryDto.Detail category;
    }

    @Data
    public static class Save {
        private String uid;
        @NotBlank(message = "카테고리 고유값을 입력하세요.")
        private String categoryUid;
    }
}

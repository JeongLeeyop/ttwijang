package com.weilyeat.cms.api.board.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

public class BoardUseCategoryDto {
    @Data
    public static class Detail {
        private String categoryUid;
        private BoardCategoryDto.Detail category;
    }
    
    @Data
    public static class Save {
        @NotBlank(message = "카테고리 고유값을 입력하세요.")
        private String categoryUid;
    }
  
}

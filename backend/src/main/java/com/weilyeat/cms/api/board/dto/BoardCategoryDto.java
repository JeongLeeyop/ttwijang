package com.weilyeat.cms.api.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

public class BoardCategoryDto {

    @Data
    public static class Detail {
        private String uid;
        private String name;
        private String descript;
        private int depth;
        private LocalDateTime createDate;
        private List<BoardCategoryDto.Detail> children;
    }

    @Data
    public static class Add {
        // 카테고리 이름
        @NotBlank(message = "카테고리 이름을 입력하세요.")
        private String name;
        
        // 카테고리 설명
        private String descript; 
        
        // 하위 데이터 리스트
        private List<BoardCategoryDto.children> children = new ArrayList<BoardCategoryDto.children>();
        
    }

    @Data
    public static class Update {
        private String uid;
        
        // 카테고리 이름
        @NotBlank(message = "카테고리 이름을 입력하세요.")
        private String name; 
        
        // 카테고리 설명
        private String descript; 
        
        // 하위 데이터 리스트
        private List<BoardCategoryDto.children> children = new ArrayList<BoardCategoryDto.children>();
        
        private List<String> deleteList = new ArrayList<String>();
    }

    @Data
    public static class children {

        private String uid;
        // 카테고리 이름
        @NotBlank(message = "카테고리 이름을 입력하세요.")
        private String name; 
        
        // 카테고리 설명
        private String descript; 
        
        // 하위 데이터 리스트
        private List<BoardCategoryDto.children> children = new ArrayList<BoardCategoryDto.children>();
    }
}

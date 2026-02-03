package com.ttwijang.cms.api.post.dto;

import javax.validation.constraints.NotBlank;


import lombok.Data;

public class PostDataDto {
    @Data
    public static class Detail {
        private String uid;
        private String fieldUid;
        private String inputValue;
        private Detail field;
    }

    @Data
    public static class Save {
        private String uid;
        @NotBlank(message = "게시판 필드 고유값을 입력하세요.")
        private String fieldUid;
        private String inputValue;
    }
}

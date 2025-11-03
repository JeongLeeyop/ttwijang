package com.ttwijang.cms.api.comment.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Data;

public class CommentDto {
    @Data
    public static class Detail {
        private String uid;
        private String userUid;
        private String writer;
        private String contents;
        private String postUid;
        private int depth;
        private int viewOrder;
        private String postTitle;
        private boolean hide;
        private LocalDateTime createDate;
    }

    @Data
    public static class Add {
        @NotBlank(message = "게시글 고유값을 입력하세요.")
        private String postUid;
        private String parentUid;
        private String writer;
        private String password;
        private String contents;
        private boolean hide;
    }

    @Data
    public static class Update {
        private String writer;
        private String password;
        private String contents;
        private boolean hide;
    }
}

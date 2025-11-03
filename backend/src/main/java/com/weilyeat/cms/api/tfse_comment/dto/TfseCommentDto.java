package com.weilyeat.cms.api.tfse_comment.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.mapstruct.Mapping;

import com.weilyeat.cms.api.user.dto.UserDto;

import lombok.Data;

public class TfseCommentDto {
    @Data
    public static class Detail {
        private String uid;
        private Long tfseIdx;
        private String userUid;
        private String writer;
        private String contents;
        private int depth;
        private int viewOrder;
        private boolean hide;
        private LocalDateTime createDate;
        private List<Children> children;
        private boolean hasAuthority;
        private UserDto.Simple user;
    }

    @Data
    public static class Children {
        private String uid;
        private Long tfseIdx;
        private String parentUid;
        private String userUid;
        private String writer;
        private String contents;
        private int depth;
        private int viewOrder;
        private boolean hide;
        private LocalDateTime createDate;
        private boolean hasAuthority;
        private UserDto.Simple user;
    }

    @Data
    public static class Add {
        // @NotBlank(message = "게시글 고유값을 입력하세요.")
        private Long tfseIdx;
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

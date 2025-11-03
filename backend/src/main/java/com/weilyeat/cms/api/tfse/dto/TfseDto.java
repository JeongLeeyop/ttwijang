package com.ttwijang.cms.api.tfse.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.ttwijang.cms.api.tfse_comment.dto.TfseCommentDto;
import com.ttwijang.cms.api.user.dto.UserDto;

import lombok.Data;

public class TfseDto {
    
    @Data
    public static class list {
        private Long idx;
        private String userUid;
        private String writer;
        private LocalDateTime tfseDate;
        private String foodText;
        private String emotionText;
        private Float satietyScore;
        private boolean secretStatus;
        private Integer likeCount;
        private boolean likeStatus;
        private LocalDateTime createDate;
        private Integer commentCount;
        private UserDto.Simple user;
    }
    
    @Data
    public static class detail {
        private Long idx;
        private String userUid;
        private String writer;
        private LocalDateTime tfseDate;
        private String foodText;
        private String emotionText;
        private Float satietyScore;
        private boolean secretStatus;
        private Integer likeCount;
        private boolean likeStatus;
        private LocalDateTime createDate;
        private Integer commentCount;
        private List<TfseCommentDto.Detail> commentList;
        private UserDto.Simple user;
    }

    @Data
    public static class update {
        private Long idx;
        private String userUid;
        private LocalDateTime tfseDate;
        private String foodText;
        private String emotionText;
        private Float satietyScore;
        private boolean secretStatus;
        private LocalDateTime createDate;
    }
    
    @Data
    public static class updateSecretStatus {
        private Long idx;
        private boolean secretStatus;
    }

    @Data
    public static class add {
        private Long idx;
        private String userUid;
        private String writer;
        private LocalDateTime tfseDate;
        private String foodText;
        private String emotionText;
        private Float satietyScore;
        private boolean secretStatus;
        private LocalDateTime createDate;
    }

}

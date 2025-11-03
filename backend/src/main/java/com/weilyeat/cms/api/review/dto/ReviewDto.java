package com.weilyeat.cms.api.review.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class ReviewDto {
    @Data
    public static class list {
        private Integer idx;
        private String content;
        private int score;
        private String createDate;
        private String userName;
        private int likeCount;
        private boolean likeStatus;
        private String thumbUid;
    }

    @Data
    public static class detail {
        private Integer idx;
        private String content;
        private int score;
        private LocalDateTime createDate;
        private List<ReviewPhotoDto.list> photoes = new ArrayList<ReviewPhotoDto.list>();
    }

    @Data
    public static class add {
        private Integer orderGroupIdx;
        private String content;
        private int score;
        private List<ReviewPhotoDto.add> photoes = new ArrayList<ReviewPhotoDto.add>();
    }

    @Data
    public static class update {
        private String content;
        private int score;
        private List<ReviewPhotoDto.add> photoes = new ArrayList<ReviewPhotoDto.add>();
    }
}

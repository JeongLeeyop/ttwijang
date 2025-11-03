package com.weilyeat.cms.api.review.dto;

import lombok.Data;

public class ReviewPhotoDto {
    @Data
    public static class list {
        private Integer idx;
        private String fileUid;
    }

    @Data
    public static class add {
        private String fileUid;
    }
}

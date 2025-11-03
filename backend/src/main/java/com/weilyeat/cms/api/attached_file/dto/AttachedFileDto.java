package com.weilyeat.cms.api.attached_file.dto;

import java.time.LocalDateTime;

import lombok.Data;

public class AttachedFileDto {
    @Data
    public static class detail {
        private String uid;
        private String originalName;
        private long fileSize;
        private String fileType;
        private String url;
        private LocalDateTime createDate;
    }

    @Data
    public static class clientDetail {
        private String uid;
        private String originalName;
        private long fileSize;
        private String fileType;
    }
}

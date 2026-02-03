package com.ttwijang.cms.entity.dto;

import lombok.Data;

public class PostFileDto {
    @Data
    public static class List {
        private Long id;
        private Long postId;
        private AttachedFileDto.Detail file;
    }
    
    @Data
    public static class Add {
        private String fileUid;
    }
    
    @Data
    public static class Update {
        private Long id;
        private String fileUid;
    }
}



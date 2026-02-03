package com.ttwijang.cms.api.post.dto;

import lombok.Data;

public class PostTagDto {
    @Data
    public static class list {
        private Integer idx;
        private String tag;
    }

    @Data
    public static class save {
        private Integer idx;
        private String tag;
    }

    @Data
    public static class best {
        private String tag;
    }
}

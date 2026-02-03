package com.ttwijang.cms.api.mission_user_inquiry.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class MissionUserInquiryDto {

    @Data
    public static class list {
        private Integer idx;
        private String userUid;
        private Integer inquiryIdx;
        private String answer;
        private String type;
        private LocalDateTime createDate;
    }

    @Data
    public static class detail {
        private String userUid;
        private Integer inquiryIdx;
        private String answer;
        private String type;
    }

    @Data
    public static class add {
        private String userUid;
        private Integer inquiryIdx;
        private Integer page;
        private String answer;
        private String type;
    }

    @Data
    public static class update {
        private Integer idx;
        private String userUid;
        private Integer inquiryIdx;
        private String answer;
        private String type;
    }
}

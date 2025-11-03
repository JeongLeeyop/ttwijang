package com.weilyeat.cms.api.mission_inquiry.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

public class MissionInquiryOptionDto {
    @Data
    public static class list {
        private Integer idx;
        private Integer inquiryIdx;
        private String name;
        private int viewOrder;
        private LocalDateTime createDate;
    }

    @Data
    public static class detail { // 영양상태 정보
        private Integer idx;
        private Integer inquiryIdx;
        private String name;
        private int viewOrder;
        private LocalDateTime createDate;
    }

    @Data
    public static class update {
        private Integer idx;
        private Integer inquiryIdx;
        private String name;
        private int viewOrder;
    }

    @Data
    public static class add {
        private Integer inquiryIdx;
        private String name;
        private int viewOrder;
    }

}

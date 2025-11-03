package com.ttwijang.cms.api.mission_inquiry.dto;

import java.util.List;

import lombok.Data;

public class MissionInquiryPageDto {
    @Data
    public static class list {
        private Integer idx;
        private Integer pageNum;
        private String title;
        private boolean useStatus;
        private List<MissionInquiryDto.list> inquiries;
    }

    @Data
    public static class detail { // 영양상태 정보
        private Integer idx;
        private Integer pageNum;
        private String title;
        private boolean useStatus;
        private List<MissionInquiryDto.detail> inquiries;
    }

    @Data
    public static class update {
        private Integer idx;
        private Integer pageNum;
        private String title;
        private boolean useStatus;
        private List<MissionInquiryDto.update> inquiries;
    }

    @Data
    public static class add {
        private Integer idx;
        private Integer pageNum;
        private String title;
        private boolean useStatus;
        private List<MissionInquiryDto.add> inquiries;
    }
}

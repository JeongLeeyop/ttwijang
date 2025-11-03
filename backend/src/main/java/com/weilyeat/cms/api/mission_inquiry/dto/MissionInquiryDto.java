package com.ttwijang.cms.api.mission_inquiry.dto;

import java.util.List;

import lombok.Data;

public class MissionInquiryDto {
    @Data
    public static class list {
        private Integer idx;
        private Integer pageNum;
        private String question;
        private String type;
        private int viewOrder;
        private boolean useStatus;
        private String options;
        // private List<MissionInquiryOptionDto.list> options;
    }

    @Data
    public static class detail { // 영양상태 정보
        private Integer idx;
        private Integer pageNum;
        private String question;
        private String type;
        private int viewOrder;
        private boolean useStatus;
        private String options;
        // private List<MissionInquiryOptionDto.detail> options;
    }

    @Data
    public static class update {
        private Integer idx;
        private Integer pageNum;
        private String question;
        private String type;
        private int viewOrder;
        private boolean useStatus;
        private String options;
        // private List<MissionInquiryOptionDto.update> options;
    }

    @Data
    public static class add {
        private Integer idx;
        private Integer pageNum;
        private String question;
        private String type;
        private int viewOrder;
        private boolean useStatus;
        private String options;
        // private List<MissionInquiryOptionDto.add> options;
    }

}

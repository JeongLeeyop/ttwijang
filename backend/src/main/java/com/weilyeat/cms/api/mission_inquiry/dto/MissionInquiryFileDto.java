package com.weilyeat.cms.api.mission_inquiry.dto;

import com.weilyeat.cms.api.attached_file.dto.AttachedFileDto;

import lombok.Data;

public class MissionInquiryFileDto {
    @Data
    public static class Detail {
        private String fileUid;
        private int viewOrder;
        private AttachedFileDto.detail file;
    }

    @Data
    public static class ClientDetail {
        private String fileUid;
        private int viewOrder;
        private AttachedFileDto.clientDetail file;
    }

    @Data
    public static class Save {
        private String fileUid;
    }
    
}

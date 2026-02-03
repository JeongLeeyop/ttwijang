package com.ttwijang.cms.api.challenge_record.dto;

import com.ttwijang.cms.api.attached_file.dto.AttachedFileDto;

import lombok.Data;

public class ChallengeRecordFileDto {
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

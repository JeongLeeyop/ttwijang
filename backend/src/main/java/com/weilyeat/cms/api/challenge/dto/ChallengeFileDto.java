package com.weilyeat.cms.api.challenge.dto;

import com.weilyeat.cms.api.attached_file.dto.AttachedFileDto;
import com.weilyeat.cms.entity.ChallengeFileType;

import lombok.Data;

public class ChallengeFileDto {
    @Data
    public static class Detail {
        private String fileUid;
        private int viewOrder;
        private ChallengeFileType type;
        private AttachedFileDto.detail file;
    }

    @Data
    public static class ClientDetail {
        private String fileUid;
        private int viewOrder;
        private ChallengeFileType type;
        private AttachedFileDto.clientDetail file;
    }

    @Data
    public static class Save {
        private ChallengeFileType type;
        private String fileUid;
    }
    
}

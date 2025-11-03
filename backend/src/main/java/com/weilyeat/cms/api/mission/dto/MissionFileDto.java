package com.weilyeat.cms.api.mission.dto;

import com.weilyeat.cms.api.attached_file.dto.AttachedFileDto;
import com.weilyeat.cms.entity.MissionFileType;

import lombok.Data;

public class MissionFileDto {
    @Data
    public static class Detail {
        private String fileUid;
        private int viewOrder;
        private MissionFileType type;
        private AttachedFileDto.detail file;
    }

    @Data
    public static class ClientDetail {
        private String fileUid;
        private int viewOrder;
        private MissionFileType type;
        private AttachedFileDto.clientDetail file;
    }

    @Data
    public static class Save {
        private MissionFileType type;
        private String fileUid;
    }
    
}

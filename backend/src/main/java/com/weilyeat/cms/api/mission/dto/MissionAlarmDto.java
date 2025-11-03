package com.ttwijang.cms.api.mission.dto;

import lombok.Data;

public class MissionAlarmDto {
    @Data
    public static class Detail {
        private String uid;
        private String missionUid;
        private int mon;
        private int tue;
        private int wed;
        private int thu;
        private int fri;
        private int sat;
        private int sun;
        private String alarmTime;
    }

    @Data
    public static class Add {
        private String missionUid;
        private int mon;
        private int tue;
        private int wed;
        private int thu;
        private int fri;
        private int sat;
        private int sun;
        private String alarmTime;
    }
    
}

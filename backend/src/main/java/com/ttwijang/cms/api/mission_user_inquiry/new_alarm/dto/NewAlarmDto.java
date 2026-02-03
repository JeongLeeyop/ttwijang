package com.ttwijang.cms.api.mission_user_inquiry.new_alarm.dto;

import lombok.Data;

public class NewAlarmDto {
    @Data
    public static class Add {
        private String postUid;
        private int type;
    }

    @Data
    public static class Check {
        private String userUid;
        private String postUid;
    }

    @Data
    public static class Count {
        private Integer total;
        private Integer noticeCnt;
        private Integer qnaCnt;
    }
}

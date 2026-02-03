package com.ttwijang.cms.api.push_alarm.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ttwijang.cms.entity.User;

import lombok.Data;

public class PushAlarmDto {
    @Data
    public static class Add {
        private String userUid;
        private String title;
        private String content;
        private String link;
		private List<User> userUidList = new ArrayList<User>();
    }

    @Data
    public static class Detail {
        private Integer id;
        private String title;
        private String content;
        private String link;
        private LocalDateTime createDate;
    }
}

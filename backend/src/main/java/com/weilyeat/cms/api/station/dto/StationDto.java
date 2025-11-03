package com.weilyeat.cms.api.station.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

public class StationDto {
    @Data
    public static class list {
       private Integer idx;
        private String name;
        private String postCode;
        private String address;
        private String addressDetail;
        private String lat;
        private String lon;
        private LocalDateTime createdDate;
        private boolean useStatus;
    }

    @Data
    public static class detail {
        private Integer idx;
        private String name;
        private String postCode;
        private String address;
        private String addressDetail;
        private String lat;
        private String lon;
        private LocalDateTime createdDate;
        private boolean useStatus;
    }
}

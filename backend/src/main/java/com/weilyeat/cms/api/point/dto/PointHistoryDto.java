package com.weilyeat.cms.api.point.dto;

import java.time.LocalDateTime;

import lombok.Data;

public class PointHistoryDto {
    @Data
    public static class list {
        private Integer idx;
        private Integer point;
        private Integer remainPoint;
        private String reason;
        private LocalDateTime createDate;
    }
}

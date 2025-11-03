package com.weilyeat.cms.api.challenge.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class ChallengeCategoryDto {

    @Data
    public static class list {
        private String uid; // 아이디
        private String name; // 챌린지명
        private String descript; // 내용
        private int viewOrder; //순서
        private LocalDateTime createDate; // 생성일
    }

    @Data
    public static class detail { // 영양상태 정보
        private String uid; // 아이디
        private String name; // 챌린지명
        private String descript; // 내용
        private int viewOrder; //순서
        private LocalDateTime createDate; // 생성일
    }

    @Data
    public static class update {
        private String uid; // 아이디
        private String name; // 챌린지명
        private String descript; // 내용
        private int viewOrder; //순서
        private LocalDateTime createDate; // 생성일
    }

    @Data
    public static class add {
        private String uid; // 아이디
        private String name; // 챌린지명
        private String descript; // 내용
        private int viewOrder; //순서
        private LocalDateTime createDate; // 생성일
    }

}

package com.weilyeat.cms.api.mission_record.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class MissionRecordDto {

    @Data
    public static class list {
        private String uid;
        private String writer;
        private String missionUid;
        private String title; // 챌린지명
        private String content; // 내용
        private boolean hiddenStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일
		private List<MissionRecordFileDto.Save> fileList = new ArrayList<MissionRecordFileDto.Save>(); // 이미지
    }

    @Data
    public static class detail { // 영양상태 정보
        private String uid;
        private String writer;
        private String missionUid;
        private String title; // 챌린지명
        private String content; // 내용
        private boolean hiddenStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일
		private List<MissionRecordFileDto.Detail> fileList = new ArrayList<MissionRecordFileDto.Detail>(); // 이미지
    }

    @Data
    public static class update {
        private String uid;
        private String writer;
        private String missionUid;
        private String title; // 챌린지명
        private String content; // 내용
        private boolean hiddenStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일
		private List<MissionRecordFileDto.Save> fileList = new ArrayList<MissionRecordFileDto.Save>(); // 이미지
    }

    @Data
    public static class add {
        private String writer;
        private String missionUid;
        private String title; // 챌린지명
        private String content; // 내용
        private boolean hiddenStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일
		private List<MissionRecordFileDto.Save> fileList = new ArrayList<MissionRecordFileDto.Save>(); // 이미지
    }

}

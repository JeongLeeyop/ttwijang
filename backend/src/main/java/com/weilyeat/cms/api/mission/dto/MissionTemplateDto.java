package com.weilyeat.cms.api.mission.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class MissionTemplateDto {

    @Data
    public static class list {
        private String uid; // 아이디
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String authMethodInfo; // 인증방식
        private boolean useStatus; // 숨김여부
        private String createDate; // 생성일
		private List<MissionFileDto.Detail> mainImageList = new ArrayList<MissionFileDto.Detail>(); // 메인 이미지 
		private List<MissionFileDto.Detail> sampleImageList = new ArrayList<MissionFileDto.Detail>(); // 예시 이미지
    }

    @Data
    public static class detail { // 영양상태 정보
        private String uid; // 아이디
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String authMethodInfo; // 인증방식
        private boolean useStatus; // 숨김여부
        private String createDate; // 생성일
		private List<MissionFileDto.Detail> mainImageList = new ArrayList<MissionFileDto.Detail>(); // 메인 이미지 
		private List<MissionFileDto.Detail> sampleImageList = new ArrayList<MissionFileDto.Detail>(); // 예시 이미지
        private MissionCategoryDto.detail missionCategory; // 카테고리
        private List<MissionDto.list> recordList = new ArrayList<MissionDto.list>(); // 기록
    }

    @Data
    public static class update {
        private String uid; // 아이디
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String authMethodInfo; // 인증방식
        private boolean useStatus; // 숨김여부
		private List<MissionFileDto.Detail> mainImageList = new ArrayList<MissionFileDto.Detail>(); // 메인 이미지 
		private List<MissionFileDto.Detail> sampleImageList = new ArrayList<MissionFileDto.Detail>(); // 예시 이미지
    }

    @Data
    public static class add {
        private String uid; // 아이디
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String authMethodInfo; // 인증방식
        private boolean useStatus; // 숨김여부
		private List<MissionFileDto.Detail> mainImageList = new ArrayList<MissionFileDto.Detail>(); // 메인 이미지 
		private List<MissionFileDto.Detail> sampleImageList = new ArrayList<MissionFileDto.Detail>(); // 예시 이미지
    }
}

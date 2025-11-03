package com.ttwijang.cms.api.challenge_record.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class ChallengeRecordDto {

    @Data
    public static class list {
        private String uid;
        private String writer;
        private String challengeUid;
        private String title; // 챌린지명
        private String content; // 내용
        private boolean hiddenStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일
		private List<ChallengeRecordFileDto.Save> fileList = new ArrayList<ChallengeRecordFileDto.Save>(); // 이미지
    }

    @Data
    public static class detail { // 영양상태 정보
        private String uid;
        private String writer;
        private String challengeUid;
        private String title; // 챌린지명
        private String content; // 내용
        private boolean hiddenStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일
		private List<ChallengeRecordFileDto.Detail> fileList = new ArrayList<ChallengeRecordFileDto.Detail>(); // 이미지
    }

    @Data
    public static class update {
        private String uid;
        private String writer;
        private String challengeUid;
        private String title; // 챌린지명
        private String content; // 내용
        private boolean hiddenStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일
		private List<ChallengeRecordFileDto.Save> fileList = new ArrayList<ChallengeRecordFileDto.Save>(); // 이미지
    }

    @Data
    public static class add {
        private String writer;
        private String challengeUid;
        private String title; // 챌린지명
        private String content; // 내용
        private boolean hiddenStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일
		private List<ChallengeRecordFileDto.Save> fileList = new ArrayList<ChallengeRecordFileDto.Save>(); // 이미지
    }

}

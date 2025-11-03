package com.weilyeat.cms.api.challenge.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.weilyeat.cms.api.challenge_record.dto.ChallengeRecordDto;

import lombok.Data;

public class ChallengeDto {

    @Data
    public static class list {
        private String uid; // 아이디
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String authMethodInfo; // 인증방식
        private Integer dailyReward; // 적립금
        private LocalDateTime startDate; // 시작일
        private LocalDateTime endDate; // 종료일
        private Integer totalDay; // 일수
        private boolean useStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일

        private int ParticipantCnt;
		private boolean userJoinStatus;
		private boolean todayWriteStatus;
		private List<ChallengeFileDto.Detail> mainImageList = new ArrayList<ChallengeFileDto.Detail>(); // 메인 이미지 
		private List<ChallengeFileDto.Detail> sampleImageList = new ArrayList<ChallengeFileDto.Detail>(); // 예시 이미지
    }

    @Data
    public static class detail { // 영양상태 정보
        private String uid; // 아이디
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String authMethodInfo; // 인증방식
        private Integer dailyReward; // 적립금
        private LocalDateTime startDate; // 시작일
        private LocalDateTime endDate; // 종료일
        private Integer totalDay; // 일수
        private boolean useStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일

        private int ParticipantCnt;
		private boolean userJoinStatus;
		private boolean todayWriteStatus;
        private ChallengeCategoryDto.detail challengeCategory; // 카테고리
		private ChallengeUserDto.detail challengeUser = new ChallengeUserDto.detail(); // 유저
        private List<ChallengeRecordDto.list> recordList = new ArrayList<ChallengeRecordDto.list>(); // 기록
        private List<ChallengeFileDto.Detail> mainImageList = new ArrayList<ChallengeFileDto.Detail>(); // 메인 이미지 
		private List<ChallengeFileDto.Detail> sampleImageList = new ArrayList<ChallengeFileDto.Detail>(); // 예시 이미지
    }

    @Data
    public static class update {
        private String uid; // 아이디
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String authMethodInfo; // 인증방식
        private Integer dailyReward; // 적립금
        private LocalDateTime startDate; // 시작일
        private LocalDateTime endDate; // 종료일
        private Integer totalDay; // 일수
        private boolean useStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일
        private List<ChallengeFileDto.Save> mainImageList = new ArrayList<ChallengeFileDto.Save>(); // 메인 이미지 
		private List<ChallengeFileDto.Save> sampleImageList = new ArrayList<ChallengeFileDto.Save>(); // 예시 이미지
    }

    @Data
    public static class add {
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String authMethodInfo; // 인증방식
        private Integer dailyReward; // 적립금
        private LocalDateTime startDate; // 시작일
        private LocalDateTime endDate; // 종료일
        private Integer totalDay; // 일수
        private boolean useStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private List<ChallengeFileDto.Save> mainImageList = new ArrayList<ChallengeFileDto.Save>(); // 메인 이미지 
		private List<ChallengeFileDto.Save> sampleImageList = new ArrayList<ChallengeFileDto.Save>(); // 예시 이미지
    }

}

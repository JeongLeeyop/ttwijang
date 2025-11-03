package com.weilyeat.cms.api.mission.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.weilyeat.cms.api.mission_record.dto.MissionRecordDto;

import lombok.Data;

public class MissionDto {

    @Data
    public static class list {
        private String uid; // 아이디
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String templateUid; // 템플릿 아이디
        private String authMethodInfo; // 인증방식
        private Integer dailyReward; // 적립금
        private LocalDate startDate; // 시작일
        private LocalDate endDate; // 종료일
        private Integer totalDay; // 일수
        private Double achieveEffect; // 감량효과
        private boolean useStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일

        private boolean alarmStatus;
        private boolean mon;
        private boolean tue;
        private boolean wed;
        private boolean thu;
        private boolean fri;
        private boolean sat;
        private boolean sun;
        private String alarmTime;
        private String alarmTitle;
	    private String alarmMessage;

        private int participantCnt;
		private boolean userJoinStatus;
		private int todayWriteStatus;
		private List<MissionFileDto.Detail> mainImageList = new ArrayList<MissionFileDto.Detail>(); // 메인 이미지 
		private List<MissionFileDto.Detail> sampleImageList = new ArrayList<MissionFileDto.Detail>(); // 예시 이미지
    }

    @Data
    public static class detail { // 영양상태 정보
        private String uid; // 아이디
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String templateUid; // 템플릿 아이디
        private String authMethodInfo; // 인증방식
        private Integer dailyReward; // 적립금
        private LocalDate startDate; // 시작일
        private LocalDate endDate; // 종료일
        private Integer totalDay; // 일수
        private Double achieveEffect; // 감량효과
        private boolean useStatus; // 숨김여부                          
        private boolean deleteStatus; // 삭제여부
        private boolean dueOverFlag; // 종료 여부
        private LocalDateTime createDate; // 생성일

        private Integer status; // 챌린지 상태 (0: 실패, 1: 성공)

        private boolean alarmStatus;
        private boolean mon;
        private boolean tue;
        private boolean wed;
        private boolean thu;
        private boolean fri;
        private boolean sat;
        private boolean sun;
        private String alarmTime;
        private String alarmTitle;
	    private String alarmMessage;

        private int participantCnt;
		private boolean userJoinStatus;
		private int todayWriteStatus;
        private MissionCategoryDto.detail missionCategory; // 카테고리
		private MissionUserDto.detail missionUser = new MissionUserDto.detail(); // 유저
		private List<MissionUserDto.detail> missionUserList = new ArrayList<>(); // 유저
        private List<MissionRecordDto.list> recordList = new ArrayList<MissionRecordDto.list>(); // 기록
        private List<MissionFileDto.Detail> mainImageList = new ArrayList<MissionFileDto.Detail>(); // 메인 이미지 
		private List<MissionFileDto.Detail> sampleImageList = new ArrayList<MissionFileDto.Detail>(); // 예시 이미지
    }

    @Data
    public static class update {
        private String uid; // 아이디
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String templateUid; // 템플릿 아이디
        private String authMethodInfo; // 인증방식
        private Integer dailyReward; // 적립금
        private LocalDate startDate; // 시작일
        private LocalDate endDate; // 종료일
        private Integer totalDay; // 일수
        private Double achieveEffect; // 감량효과
        private boolean useStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private LocalDateTime createDate; // 생성일

        private boolean alarmStatus;
	    private boolean mon;
	    private boolean tue;
        private boolean wed;
        private boolean thu;
        private boolean fri;
        private boolean sat;
        private boolean sun;
	    private String alarmTime;
        private String alarmTitle;
	    private String alarmMessage;

        private String userUid; // 유저 UID

        private List<MissionFileDto.Save> mainImageList = new ArrayList<MissionFileDto.Save>(); // 메인 이미지 
		private List<MissionFileDto.Save> sampleImageList = new ArrayList<MissionFileDto.Save>(); // 예시 이미지
    }

    @Data
    public static class add {
        private String title; // 챌린지명
        private String content; // 내용
        private String categoryUid; // 내용
        private String templateUid; // 템플릿 아이디
        private String authMethodInfo; // 인증방식
        private Integer dailyReward; // 적립금
        private LocalDate startDate; // 시작일
        private LocalDate endDate; // 종료일
        private Integer totalDay; // 일수
        private Double achieveEffect; // 감량효과
        private boolean useStatus; // 숨김여부
        private boolean deleteStatus; // 삭제여부
        private String userUid; // 유저 UID

        private boolean alarmStatus;
        private boolean mon;
        private boolean tue;
        private boolean wed;
        private boolean thu;
        private boolean fri;
        private boolean sat;
        private boolean sun;
        private String alarmTime;
        private String alarmTitle;
	    private String alarmMessage;

        private List<MissionFileDto.Save> mainImageList = new ArrayList<MissionFileDto.Save>(); // 메인 이미지 
		private List<MissionFileDto.Save> sampleImageList = new ArrayList<MissionFileDto.Save>(); // 예시 이미지
    }

    @Data
    public static class summary {
        private int completeCnt; // 완료된 미션 개수
        private double successRate; // 미션 성공률 (%)
        private double totalWeightLoss; // 총 감량 효과 (kg)
    }

}

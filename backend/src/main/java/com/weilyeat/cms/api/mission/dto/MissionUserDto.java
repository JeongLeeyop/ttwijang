package com.weilyeat.cms.api.mission.dto;

import java.time.LocalDateTime;

import com.weilyeat.cms.api.user.dto.UserDto;

import lombok.Data;

public class MissionUserDto {

    @Data
    public static class list {
    private String uid;
	private String missionUid; // 챌린지 아이디
	private String userUid; // 카테고리 아이디
    private LocalDateTime startDate;
	private LocalDateTime dueDate; // 종료일
    private Integer RecordCnt; // 인증 횟수
    private boolean abandonStatus; // 포기여부
    }

    @Data
    public static class detail { // 영양상태 정보
       private String uid;
	private String missionUid; // 챌린지 아이디
	private String userUid; // 카테고리 아이디
    private LocalDateTime startDate;
	private LocalDateTime dueDate; // 종료일
    private String actualName; // 유저 이름
    private Integer RecordCnt; // 인증 횟수
    private Integer todayWriteStatus; // 오늘 인증 횟수
    private boolean approveStatus; // 승인여부
    private boolean abandonStatus; // 포기여부
    // private UserDto.Detail user; // 유저 정보
    }

    @Data
    public static class update {
        private String uid;
	private String missionUid; // 챌린지 아이디
	private String userUid; // 카테고리 아이디
    private LocalDateTime startDate;
	private LocalDateTime dueDate; // 종료일
    private boolean approveStatus; // 승인여부
    }

    @Data
    public static class add {
	private String missionUid; // 챌린지 아이디
	private String userUid; // 카테고리 아이디
	private LocalDateTime dueDate; // 종료일
    private boolean approveStatus; // 승인여부
    }
}

package com.weilyeat.cms.utils;

import java.time.LocalDate;

import com.weilyeat.cms.api.mission.dto.MissionDto;
import com.weilyeat.cms.entity.Mission;

/**
 * 미션 상태 계산 유틸리티 클래스
 */
public class MissionStatusCalculator {
    
    /**
     * 미션 상태 열거형
     */
    public enum MissionStatus {
        SUCCESS(1, "성공"),
        FAILURE(0, "실패"), 
        IN_PROGRESS(2, "진행중"),
        WAITING(3, "시작전");
        
        private final int code;
        private final String description;
        
        MissionStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public int getCode() { return code; }
        public String getDescription() { return description; }
    }
    
    /**
     * 미션 상태 계산
     * @param recordCount 사용자의 실제 기록 수
     * @param totalDay 미션 총 일수
     * @param startDate 미션 시작일
     * @param endDate 미션 종료일
     * @return 미션 상태
     */
    public static MissionStatus calculateStatus(int recordCount, int totalDay, LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        
        if (today.isBefore(startDate)) {
            return MissionStatus.WAITING; // 시작전
        } else if (recordCount >= totalDay) {
            return MissionStatus.SUCCESS; // 성공 (목표 달성)
        } else if (endDate.isBefore(today)) {
            return MissionStatus.FAILURE; // 실패 (기간 종료)
        } else {
            return MissionStatus.IN_PROGRESS; // 진행중
        }
    }
    
    /**
     * 미션 성공률 계산
     * @param recordCount 실제 기록 수
     * @param totalDay 총 일수
     * @return 성공률 (0.0 ~ 100.0)
     */
    public static double calculateSuccessRate(int recordCount, int totalDay) {
        if (totalDay <= 0) return 0.0;
        return Math.min(100.0, (double) recordCount / totalDay * 100.0);
    }
    
    /**
     * 기한 초과 여부 판단
     * @param endDate 종료일
     * @return 기한 초과 여부
     */
    public static boolean isDueOver(LocalDate endDate) {
        return endDate.isBefore(LocalDate.now());
    }
    
    /**
     * 오늘 작성 상태 계산
     * @param todayRecordCount 오늘 기록 수
     * @return 1: 작성함, 0: 작성안함
     */
    public static int calculateTodayWriteStatus(int todayRecordCount) {
        return todayRecordCount > 0 ? 1 : 0;
    }
    
    /**
     * 미션 진행률 계산
     * @param recordCount 현재 기록 수
     * @param totalDay 총 일수
     * @return 진행률 (0.0 ~ 100.0)
     */
    public static double calculateProgress(int recordCount, int totalDay) {
        if (totalDay <= 0) return 0.0;
        return Math.min(100.0, (double) recordCount / totalDay * 100.0);
    }
    
    /**
     * 미션 상태 정보를 한번에 계산하여 DTO에 설정
     * @param dto 설정할 DTO
     * @param recordCount 기록 수
     * @param todayRecordCount 오늘 기록 수
     * @param mission 미션 엔티티
     */
    public static void setMissionStatus(MissionDto.detail dto, int recordCount, int todayRecordCount, Mission mission) {
        // 상태 계산
        MissionStatus status = calculateStatus(recordCount, mission.getTotalDay(), mission.getStartDate(), mission.getEndDate());
        dto.setStatus(status.getCode());
        
        // 기한 초과 여부
        dto.setDueOverFlag(isDueOver(mission.getEndDate()));
        
        // 오늘 작성 상태
        dto.setTodayWriteStatus(calculateTodayWriteStatus(todayRecordCount));
        
        // 참여 횟수
        dto.setParticipantCnt(recordCount);
    }
}

package com.ttwijang.cms.entity;

import java.time.LocalDate;

/**
 * Mission 엔티티 확장 - 비즈니스 로직 메서드
 */
public class MissionCalculations {
    
    /**
     * 미션 상태 계산
     */
    public static class StatusInfo {
        private final int status;
        private final boolean dueOverFlag;
        private final double successRate;
        private final double progress;
        
        public StatusInfo(int status, boolean dueOverFlag, double successRate, double progress) {
            this.status = status;
            this.dueOverFlag = dueOverFlag;
            this.successRate = successRate;
            this.progress = progress;
        }
        
        public int getStatus() { return status; }
        public boolean isDueOverFlag() { return dueOverFlag; }
        public double getSuccessRate() { return successRate; }
        public double getProgress() { return progress; }
    }
    
    /**
     * 미션의 전체 상태 정보를 계산
     */
    public static StatusInfo calculateMissionStatus(Mission mission, int recordCount) {
        // 상태 계산
        int status;
        if (recordCount >= mission.getTotalDay()) {
            status = 1; // 성공
        } else if (mission.getEndDate().isBefore(LocalDate.now())) {
            status = 0; // 실패
        } else {
            status = 2; // 진행중
        }
        
        // 기한 초과 여부
        boolean dueOverFlag = mission.getEndDate().isBefore(LocalDate.now());
        
        // 성공률 계산
        double successRate = mission.getTotalDay() > 0 ? 
            Math.min(100.0, (double) recordCount / mission.getTotalDay() * 100.0) : 0.0;
        
        // 진행률 계산
        double progress = mission.getTotalDay() > 0 ? 
            Math.min(100.0, (double) recordCount / mission.getTotalDay() * 100.0) : 0.0;
        
        return new StatusInfo(status, dueOverFlag, successRate, progress);
    }
    
    /**
     * 미션 성공 여부만 간단히 확인
     */
    public static boolean isMissionSuccessful(Mission mission, int recordCount) {
        return recordCount >= mission.getTotalDay();
    }
    
    /**
     * 미션 진행중 여부 확인
     */
    public static boolean isMissionInProgress(Mission mission) {
        LocalDate now = LocalDate.now();
        return !mission.getEndDate().isBefore(now) && !mission.getStartDate().isAfter(now);
    }
}

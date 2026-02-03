package com.ttwijang.cms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.mission.dto.MissionDto;
import com.ttwijang.cms.api.mission.repository.MissionUserRepository;
import com.ttwijang.cms.api.mission_record.repository.MissionRecordRepository;
import com.ttwijang.cms.entity.Mission;
import com.ttwijang.cms.entity.MissionUser;
import com.ttwijang.cms.utils.MissionStatusCalculator;

/**
 * 미션 상태 계산 전용 서비스
 */
@Service
public class MissionStatusService {
    
    @Autowired
    private MissionRecordRepository missionRecordRepository;
    
    @Autowired
    private MissionUserRepository missionUserRepository;
    
    /**
     * 미션 상태 정보를 계산하여 DTO에 설정
     */
    public void enrichMissionStatus(MissionDto.detail dto, String userUid, Mission mission) {
        // 미션 사용자 정보 조회 (포기 여부 확인용)
        MissionUser missionUser = missionUserRepository.findByUserUidAndMissionUid(userUid, mission.getUid());
        
        // 포기한 미션인지 확인
        if (missionUser != null && missionUser.isAbandonStatus()) {
            // 포기한 미션은 실패(0) 상태로 설정
            dto.setStatus(0); // 실패
            dto.setDueOverFlag(true);
            dto.setTodayWriteStatus(0);
            dto.setParticipantCnt(0);
            return;
        }
        
        // 기록 수 조회
        int todayRecordCount = missionRecordRepository.countTodayMission(userUid, mission.getUid());
        int totalRecordCount = missionRecordRepository.countMissionByUserUid(userUid, mission.getUid());
        
        // 디버깅 로그 추가
/*         System.out.println("Mission UID: " + mission.getUid() + 
                         ", User UID: " + userUid + 
                         ", TotalRecordCount: " + totalRecordCount + 
                         ", TotalDay: " + mission.getTotalDay() + 
                         ", EndDate: " + mission.getEndDate() +
                         ", Today: " + LocalDate.now());
         */

        // 상태 계산 및 설정
        MissionStatusCalculator.MissionStatus status = MissionStatusCalculator.calculateStatus(
            totalRecordCount, mission.getTotalDay(), mission.getStartDate(), mission.getEndDate());
        
        // System.out.println("Calculated Status: " + status + " (code: " + status.getCode() + ")");
        
        dto.setStatus(status.getCode());
        dto.setDueOverFlag(MissionStatusCalculator.isDueOver(mission.getEndDate()));
        dto.setTodayWriteStatus(MissionStatusCalculator.calculateTodayWriteStatus(todayRecordCount));
        dto.setParticipantCnt(totalRecordCount);
    }
    
    /**
     * 여러 미션의 상태를 한번에 계산
     */
    public void enrichMissionListStatus(List<MissionDto.detail> dtoList, String userUid) {
        for (MissionDto.detail dto : dtoList) {
            // Mission 객체를 다시 조회하지 않고 DTO의 정보만으로 계산
            int todayRecordCount = missionRecordRepository.countTodayMission(userUid, dto.getUid());
            int totalRecordCount = missionRecordRepository.countMissionByUserUid(userUid, dto.getUid());
            
            MissionStatusCalculator.MissionStatus status = MissionStatusCalculator.calculateStatus(
                totalRecordCount, dto.getTotalDay(), dto.getStartDate(), dto.getEndDate());
            
            dto.setStatus(status.getCode());
            dto.setDueOverFlag(MissionStatusCalculator.isDueOver(dto.getEndDate()));
            dto.setTodayWriteStatus(MissionStatusCalculator.calculateTodayWriteStatus(todayRecordCount));
            dto.setParticipantCnt(totalRecordCount);
        }
    }
    
    /**
     * 간단한 성공 여부만 확인
     */
    public boolean isMissionSuccessful(String missionUid, String userUid, int totalDay) {
        int recordCount = missionRecordRepository.countMissionByUserUid(userUid, missionUid);
        return recordCount >= totalDay;
    }
    
    /**
     * 미션 성공률 계산
     */
    public double calculateSuccessRate(String missionUid, String userUid, int totalDay) {
        int recordCount = missionRecordRepository.countMissionByUserUid(userUid, missionUid);
        return MissionStatusCalculator.calculateSuccessRate(recordCount, totalDay);
    }
}

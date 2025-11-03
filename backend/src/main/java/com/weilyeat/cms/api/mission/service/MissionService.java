package com.weilyeat.cms.api.mission.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.mission.dto.MissionDto;
import com.weilyeat.cms.api.mission.dto.MissionSearch;
import com.weilyeat.cms.api.mission.dto.mapper.MissionMapper;
import com.weilyeat.cms.api.mission.dto.mapper.MissionUserMapper;
import com.weilyeat.cms.api.mission.repository.MissionRepository;
import com.weilyeat.cms.api.mission.repository.MissionUserRepository;
import com.weilyeat.cms.api.mission_record.repository.MissionRecordRepository;
import com.weilyeat.cms.api.point.repository.PointHistoryRepository;
import com.weilyeat.cms.api.point.service.PointHistoryService;
import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.api.user.repository.UserFcmTokenRepository;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Mission;
import com.weilyeat.cms.entity.MissionUser;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.fcm.service.PushNotificationService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface MissionService {
    MissionDto.detail detail(SinghaUser authUser, String idx);
    List<MissionDto.detail> list(SinghaUser authUser, MissionSearch search);
    Page<MissionDto.detail> list(SinghaUser authUser, MissionSearch search, Pageable pageable);
    void add(SinghaUser authUser, MissionDto.add addDto );
    void update(SinghaUser authUser, Mission Mission, MissionDto.update updateDto );
    void delete(SinghaUser authUser, Mission Mission);
    MissionDto.summary getSummary(SinghaUser authUser);
    void joinMultipleMissions(SinghaUser authUser, List<String> missionUids);
    void abandonMission(SinghaUser authUser, String missionUid);
}

@Service
@AllArgsConstructor
class MissionServiceImpl implements MissionService {
    
    private final MissionRepository missionRepository;
    private final MissionUserRepository missionUserRepository;
    private final MissionRecordRepository missionRecordRepository;
    private final UserRepository userRepository;
    
    @Autowired
    PushAlarmService pushAlarmService;
    
    @Autowired
    PushNotificationService pushNotificationService;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    PointHistoryService pointHistoryService;
    
    @Autowired
    UserFcmTokenRepository userFcmTokenRepository;

    @Autowired
    com.weilyeat.cms.service.MissionAlarmService missionAlarmService;
    
    @Autowired
    com.weilyeat.cms.service.MissionStatusService missionStatusService;

    @Override
    public MissionDto.detail detail(SinghaUser authUser, String uid) {
        User user = userRepository.findById(authUser.getUser().getUid())
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        
        Mission mission = missionRepository.findById(uid)
                .orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
                
        MissionDto.detail dto = MissionMapper.INSTANCE.entityToDetail(mission);
        
        // 사용자 참여 상태 확인
        MissionUser result = missionUserRepository.findByUserUidAndMissionUid(authUser.getUser().getUid(), uid);
        if (result != null) { 
            dto.setUserJoinStatus(true);
            dto.setMissionUser(MissionUserMapper.INSTANCE.entityToDetail(result));
        } else {
            dto.setUserJoinStatus(false);
        }
        
        // 미션 상태 서비스를 사용하여 상태 계산 및 설정
        missionStatusService.enrichMissionStatus(dto, user.getUid(), mission);
        
        return dto;
    }

    @Override
    public List<MissionDto.detail> list(SinghaUser authUser, MissionSearch search) {
        search.setUserUid(authUser.getUser().getUid());
        String userUid = authUser.getUser().getUid();
        
        List<Mission> missions;
        
        // status에 따라 적절한 쿼리 메서드 사용
        if ("current".equals(search.getStatus())) {
            missions = missionRepository.findCurrentMissions(userUid);
            System.out.println("Found " + missions.size() + " current missions for user " + userUid);
        } else if ("past".equals(search.getStatus())) {
            missions = missionRepository.findPastMissions(userUid);
            System.out.println("Found " + missions.size() + " past missions for user " + userUid);
        } else {
            // 기본 조건 사용
            missions = missionRepository.findAll(search.search());
        }
        
        return missions.stream().map(entity -> MissionMapper.INSTANCE.entityToDetailMyFlag(entity, search)).collect(Collectors.toList());
    }
    
    @Override
    public Page<MissionDto.detail> list(SinghaUser authUser, MissionSearch search, Pageable pageable) {
        Page<Mission> missionPage = missionRepository.findAll(search.search(), pageable);
        
        return missionPage.map(mission -> {
            MissionDto.detail dto = MissionMapper.INSTANCE.entityToDetail(mission);
            
            // 미션에 연결된 첫 번째 사용자의 상태 정보를 계산
            if (mission.getMissionUserList() != null && !mission.getMissionUserList().isEmpty()) {
                MissionUser firstUser = mission.getMissionUserList().get(0);
                if (firstUser != null && firstUser.getUserUid() != null) {
                    // 해당 사용자의 미션 상태 정보를 계산하여 DTO에 설정
                    missionStatusService.enrichMissionStatus(dto, firstUser.getUserUid(), mission);
                }
            } else {
                // 사용자가 없는 경우 미션 날짜를 기준으로 상태 판단
                LocalDate today = LocalDate.now();
                int status;
                
                if (today.isBefore(mission.getStartDate())) {
                    // 아직 시작 전 - 대기중
                    status = 3; // 대기중 (새로운 상태 코드)
                } else if (today.isAfter(mission.getEndDate())) {
                    // 이미 종료됨 - 완료
                    status = 0; // 완료 (종료됨)
                } else {
                    // 진행중
                    status = 2; // 진행중
                }
                
                dto.setStatus(status);
                dto.setDueOverFlag(today.isAfter(mission.getEndDate()));
                dto.setTodayWriteStatus(0);
                dto.setParticipantCnt(0);
            }
            
            return dto;
        });
    }

    @Transactional
    @Override
    public void add(SinghaUser authUser, MissionDto.add addDto) {
        Mission entity = null;
        entity = MissionMapper.INSTANCE.addDtoToEntity(addDto);
       
        // User user = authUser.getUser();
        // if (!StringUtils.hasText(addDto.getWriter())){
        //     StringBuilder sb = new StringBuilder(user.getActualName());
        //     for (int i = 1; i < user.getActualName().length(); i++) {
        //         sb.setCharAt(i, '*');
        //     }
        //     entity.setWriter(sb.toString());
        // }
        Mission savedMission = missionRepository.save(entity);
        
        // 알람이 설정되어 있다면 스케줄링
        if (savedMission.isAlarmStatus()) {
            missionAlarmService.scheduleAlarm(savedMission);
        }
    }
    
    @Override
    public void update(SinghaUser authUser, Mission Mission, MissionDto.update updateDto) {
        Mission = MissionMapper.INSTANCE.updateDtoToEntity(updateDto, Mission);
        Mission updatedMission = missionRepository.save(Mission);
        
        // 기존 알람 취소
        missionAlarmService.cancelAlarm(updatedMission.getUid());
        
        // 알람이 설정되어 있다면 새로 스케줄링
        if (updatedMission.isAlarmStatus()) {
            missionAlarmService.scheduleAlarm(updatedMission);
        }
    }
    
    @Override
    public void delete(SinghaUser authUser, Mission Mission) {
        // 알람 스케줄 취소
        missionAlarmService.cancelAlarm(Mission.getUid());
        missionRepository.delete(Mission);
    }

    @Override
    public MissionDto.summary getSummary(SinghaUser authUser) {
        String userUid = authUser.getUser().getUid();
        
        MissionSearch approvedSearch = new MissionSearch();
        approvedSearch.setUserUid(userUid);
        approvedSearch.setMyFlag(true);
        approvedSearch.setStatus("approved"); // 승인된 미션만 조회
        List<Mission> approvedMissions = missionRepository.findAll(approvedSearch.search());
        
        // 미션 성공률, 감량 효과 및 완료 개수 계산
        int totalRecordCount = 0;
        int totalMaxRecordCount = 0;
        double totalWeightLoss = 0.0;
        int completedMissionCount = 0; // 성공적으로 완료한 미션 개수
        
        for (Mission mission : approvedMissions) {
            // 해당 미션의 실제 기록 수
            int actualRecordCount = missionRecordRepository.countMissionByUserUid(userUid, mission.getUid());
            
            // 해당 미션의 최대 기록 수 (총 일수)
            int maxRecordCount = mission.getTotalDay();
            
            totalRecordCount += actualRecordCount;
            totalMaxRecordCount += maxRecordCount;
            
            // 목표를 달성한 경우 완료로 카운트 (종료 여부와 관계없이)
            if (actualRecordCount >= maxRecordCount) {
                completedMissionCount++;
            }
            // 미션 성과 계산 (각 미션별 감량효과 × 개별 성공률)
            if (maxRecordCount > 0) {
                double individualSuccessRate = (double) actualRecordCount / maxRecordCount;
                double missionWeightLoss = mission.getAchieveEffect() * individualSuccessRate;
                totalWeightLoss += missionWeightLoss;
            }
        }
        
        // 전체 성공률 계산
        double overallSuccessRate = totalMaxRecordCount > 0 ? 
            ((double) totalRecordCount / totalMaxRecordCount) * 100 : 0.0;
        
        // DTO 생성 및 반환
        MissionDto.summary summary = new MissionDto.summary();
        summary.setCompleteCnt(completedMissionCount);
        summary.setSuccessRate(overallSuccessRate);
        summary.setTotalWeightLoss(totalWeightLoss);
        
        return summary;
    }

    @Transactional
    @Override
    public void joinMultipleMissions(SinghaUser authUser, List<String> missionUids) {
        String userUid = authUser.getUser().getUid();
        
        for (String missionUid : missionUids) {
            // 미션 존재 여부 확인
            Mission mission = missionRepository.findById(missionUid)
                    .orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
            
            // 기존 MissionUser 확인
            MissionUser existingMissionUser = missionUserRepository.findByUserUidAndMissionUid(userUid, missionUid);
            
            if (existingMissionUser != null) {
                // 이미 존재하는 MissionUser가 있다면 승인 상태만 업데이트
                if (!existingMissionUser.isApproveStatus()) {
                    existingMissionUser.setApproveStatus(true);
                    missionUserRepository.save(existingMissionUser);
                }
                // 이미 승인된 상태라면 건너뛰기
            }
            
            // 미션 알람이 설정되어 있다면 스케줄링
            if (mission.isAlarmStatus()) {
                missionAlarmService.scheduleAlarm(mission);
            }
        }
    }

    @Transactional
    @Override
    public void abandonMission(SinghaUser authUser, String missionUid) {
        String userUid = authUser.getUser().getUid();
        
        // 미션 존재 여부 확인
        missionRepository.findById(missionUid)
                .orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
        
        // 사용자의 미션 참여 정보 확인
        MissionUser missionUser = missionUserRepository.findByUserUidAndMissionUid(userUid, missionUid);
        
        if (missionUser == null) {
            throw new NotFoundException(NotFound.CHALLENGE);
        }
        
        // 미션 포기 상태로 변경 (abandonStatus를 true로 설정)
        missionUser.setAbandonStatus(true);
        missionUserRepository.save(missionUser);
        
        // 해당 사용자의 미션 기록도 삭제 (선택사항)
        // missionRecordRepository.deleteByUserUidAndMissionUid(userUid, missionUid);
        
        // 알람 스케줄 취소 (해당 사용자만 대상)
        try {
            // 개별 사용자 알람 취소는 추후 구현
            // missionAlarmService.cancelUserMissionAlarm(missionUid, userUid);
        } catch (Exception e) {
            // 알람 취소 실패는 로그만 남기고 진행
            System.err.println("Failed to cancel alarm for user " + userUid + " mission " + missionUid + ": " + e.getMessage());
        }
    }

    // @Override
    // public void updateSecretStatus(SinghaUser authUser, MissionDto.updateSecretStatus updateDto){
    //     Optional<Mission> optional = missionRepository.findById(updateDto.getIdx());
    //     if (optional.isPresent()) {
    //         Mission entity = optional.get();
    //         if(authUser.getUser().getUid().equals(entity.getUserUid())){
    //             entity.setSecretStatus(updateDto.isSecretStatus());
    //             missionRepository.save(entity);
    //         }else throw new BadRequestException(BadRequest.NOT_MINE);
    //     } else throw new NotFoundException(NotFound.Mission);
    // }
    
}

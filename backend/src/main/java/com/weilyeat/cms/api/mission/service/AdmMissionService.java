package com.weilyeat.cms.api.mission.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.mission.dto.MissionDto;
import com.weilyeat.cms.api.mission.dto.MissionSearch;
import com.weilyeat.cms.api.mission.dto.mapper.AdmMissionMapper;
import com.weilyeat.cms.api.mission.dto.mapper.MissionMapper;
import com.weilyeat.cms.api.mission.repository.AdmMissionRepository;
import com.weilyeat.cms.api.mission.repository.MissionUserRepository;
import com.weilyeat.cms.api.mission_record.repository.MissionRecordFileRepository;
import com.weilyeat.cms.api.mission_record.repository.MissionRecordRepository;
import com.weilyeat.cms.api.mission_user_inquiry.repository.MissionUserInquiryRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Mission;
import com.weilyeat.cms.entity.MissionUser;
import com.weilyeat.cms.oauth.SinghaUser;
import com.weilyeat.cms.service.MissionAlarmService;
import com.weilyeat.cms.service.MissionStatusService;
import com.weilyeat.cms.fcm.firebase.FCMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AdmMissionService {
    MissionDto.detail detail(SinghaUser authUser, String idx);
    List<MissionDto.detail> list(SinghaUser authUser, MissionSearch search);
    Page<MissionDto.detail> list(SinghaUser authUser, MissionSearch search, Pageable pageable);
    void add(SinghaUser authUser, MissionDto.add addDto );
    void update(SinghaUser authUser, Mission Mission, MissionDto.update updateDto );
    void delete(SinghaUser authUser, Mission Mission);
}

@Service
class AdmMissionServiceImpl implements AdmMissionService {

    private static final Logger logger = LoggerFactory.getLogger(AdmMissionServiceImpl.class);
    
    private final AdmMissionRepository admMissionRepository;
    private final MissionUserRepository missionUserRepository;
    private final MissionRecordRepository missionRecordRepository;
    private final MissionRecordFileRepository missionRecordFileRepository;
    private final MissionAlarmService missionAlarmService;
    private final MissionStatusService missionStatusService;
    private final FCMService fcmService;

    public AdmMissionServiceImpl(
            AdmMissionRepository admMissionRepository,
            MissionUserRepository missionUserRepository,
            MissionRecordRepository missionRecordRepository,
            MissionRecordFileRepository missionRecordFileRepository,
            MissionAlarmService missionAlarmService,
            MissionStatusService missionStatusService,
            FCMService fcmService) {
        this.admMissionRepository = admMissionRepository;
        this.missionUserRepository = missionUserRepository;
        this.missionRecordRepository = missionRecordRepository;
        this.missionRecordFileRepository = missionRecordFileRepository;
        this.missionAlarmService = missionAlarmService;
        this.missionStatusService = missionStatusService;
        this.fcmService = fcmService;
    }

    @Override
    public MissionDto.detail detail(SinghaUser authUser, String uid) {
        // search.setUserUid(authUser.getUser().getUid());
        Optional<Mission> optional = admMissionRepository.findById(uid);
        optional.orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
        MissionDto.detail dto = MissionMapper.INSTANCE.entityToDetail(optional.get());
        return dto;
    }

    @Override
    public List<MissionDto.detail> list(SinghaUser authUser, MissionSearch search) {
        System.out.println("AdmMissionService.list(List) called");
        List<Mission> missions = admMissionRepository.findAll(search.search());
        
        return missions.stream().map(mission -> {
            MissionDto.detail dto = MissionMapper.INSTANCE.entityToDetail(mission);
            
            // 미션에 연결된 첫 번째 사용자의 상태 정보를 계산
            if (mission.getMissionUserList() != null && !mission.getMissionUserList().isEmpty()) {
                MissionUser firstUser = mission.getMissionUserList().get(0);
                if (firstUser != null && firstUser.getUserUid() != null) {
                    // 해당 사용자의 미션 상태 정보를 계산하여 DTO에 설정
                    missionStatusService.enrichMissionStatus(dto, firstUser.getUserUid(), mission);
                }
            } else {
                // 사용자가 없는 경우 기본값 설정
                dto.setStatus(2); // 진행중으로 기본 설정
                dto.setDueOverFlag(false);
                dto.setTodayWriteStatus(0);
                dto.setParticipantCnt(0);
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    @Override
    public Page<MissionDto.detail> list(SinghaUser authUser, MissionSearch search, Pageable pageable) {
        Page<Mission> missionPage = admMissionRepository.findAll(search.search(), pageable);
        
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
        if (addDto.getUserUid() == null || addDto.getUserUid().trim().isEmpty()) {
            throw new IllegalArgumentException("사용자 UID가 필요합니다.");
        }
        
        Mission entity = MissionMapper.INSTANCE.addDtoToEntity(addDto);
        Mission savedMission = admMissionRepository.save(entity);

        // MissionUser 생성
        MissionUser userEntity = new MissionUser();
        userEntity.setUserUid(addDto.getUserUid());
        userEntity.setMissionUid(savedMission.getUid());
        userEntity.setApproveStatus(false); // 기본값 설정
        userEntity.setAbandonStatus(false); // 포기하지 않은 상태로 초기화
        missionUserRepository.save(userEntity);

        // 알람 스케줄링
        if (savedMission.isAlarmStatus()) {
            try {
                missionAlarmService.scheduleAlarm(savedMission);
                logger.info("미션 알람 스케줄링 완료 - Mission: " + savedMission.getUid());
            } catch (RuntimeException e) {
                logger.error("알람 스케줄링 실패 - Mission: " + savedMission.getUid() + ", Error: " + e.getMessage(), e);
            }
        }
        
        // 미션 부여 푸시 알람 전송
        try {
            fcmService.sendMissionAlarm(
                addDto.getUserUid(), 
                "미션 부여 알림", 
                "미션이 부여되었습니다. 앱에서 미션을 확인하세요"
            );
            logger.info("미션 부여 알림 전송 완료 - Mission: " + savedMission.getUid() + ", User: " + addDto.getUserUid());
        } catch (Exception e) {
            logger.error("미션 부여 알림 전송 실패 - Mission: " + savedMission.getUid() + ", User: " + addDto.getUserUid() + ", Error: " + e.getMessage(), e);
        }
        
        logger.info("미션 생성 완료 - Mission: " + savedMission.getUid() + ", User: " + addDto.getUserUid());
    }
    
    @Transactional
    @Override
    public void update(SinghaUser authUser, Mission mission, MissionDto.update updateDto) {
        if (mission == null) {
            throw new IllegalArgumentException("미션 정보가 필요합니다.");
        }
        
        Mission updatedMission = AdmMissionMapper.INSTANCE.updateDtoToEntity(updateDto, mission);
        updatedMission = admMissionRepository.save(updatedMission);

        // 기존 알람 취소
        missionAlarmService.cancelAlarm(updatedMission.getUid());
        
        // 알람이 설정되어 있다면 새로 스케줄링 (중복 취소 방지를 위해 skipCancel=true)
        if (updatedMission.isAlarmStatus()) {
            try {
                missionAlarmService.scheduleAlarm(updatedMission, true);
                logger.info("미션 알람 재스케줄링 완료 - Mission: " + updatedMission.getUid());
            } catch (RuntimeException e) {
                logger.error("알람 재스케줄링 실패 - Mission: " + updatedMission.getUid() + ", Error: " + e.getMessage(), e);
                // 필요시 알람 상태를 false로 변경할 수도 있음
                // updatedMission.setAlarmStatus(false);
                // admMissionRepository.save(updatedMission);
            }
        }
        
        logger.info("미션 업데이트 완료 - Mission: " + updatedMission.getUid());
    }
    
    @Transactional
    @Override
    public void delete(SinghaUser authUser, Mission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("미션 정보가 필요합니다.");
        }
        
        // 알람 스케줄 취소
        missionAlarmService.cancelAlarm(mission.getUid());
        
        // 미션 삭제 (순서 중요: 파일 -> 레코드 -> 사용자 -> 미션)
        missionRecordFileRepository.deleteByMissionUid(mission.getUid()); // 파일 먼저 삭제
        missionRecordRepository.deleteByMissionUid(mission.getUid());     // 그 다음 레코드 삭제
        missionUserRepository.deleteByMissionUid(mission.getUid());

        admMissionRepository.delete(mission);
        
        logger.info("미션 삭제 완료 - Mission: " + mission.getUid());
    }
}

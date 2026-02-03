package com.ttwijang.cms.api.mission_record.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.mission.repository.MissionRepository;
import com.ttwijang.cms.api.mission_record.dto.MissionRecordDto;
import com.ttwijang.cms.api.mission_record.dto.MissionRecordSearch;
import com.ttwijang.cms.api.mission_record.dto.mapper.MissionRecordMapper;
import com.ttwijang.cms.api.mission_record.exception.RecordAlreadyAddException;
import com.ttwijang.cms.api.mission_record.repository.MissionRecordRepository;
import com.ttwijang.cms.api.point.repository.PointHistoryRepository;
import com.ttwijang.cms.api.point.service.PointHistoryService;
import com.ttwijang.cms.api.push_alarm.dto.PushAlarmDto;
import com.ttwijang.cms.api.push_alarm.service.PushAlarmService;
import com.ttwijang.cms.api.user.dto.UserFcmToken;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Mission;
import com.ttwijang.cms.entity.MissionRecord;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.fcm.model.PushNotificationRequest;
import com.ttwijang.cms.fcm.service.PushNotificationService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface MissionRecordService {
    MissionRecordDto.detail detail(String uid);
    List<MissionRecordDto.list> List(MissionRecordSearch search, SinghaUser authUser);
    MissionRecordDto.detail add(MissionRecordDto.add addDto, SinghaUser authUser);
	MissionRecordDto.detail update(MissionRecord entity, MissionRecordDto.update updateDto, SinghaUser authUser);
	void delete(String uid, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class MissionRecordServiceImpl implements MissionRecordService {
    private final MissionRepository missionRepository;
    private final MissionRecordRepository missionRecordRepository;
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

    @Override
    public MissionRecordDto.detail detail(String uid) {
        return MissionRecordMapper.INSTANCE.entityToDetail(missionRecordRepository.findById(uid).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE_RECORD)));
    }

    @Override
    public List<MissionRecordDto.list> List(MissionRecordSearch search, SinghaUser authUser) {
        if(search.isMyFlag()){
            User user = authUser.getUser();
            if(user != null) search.setUserUid(user.getUid());
        }
        return missionRecordRepository.findAll(search.search()).stream().map(entity -> MissionRecordMapper.INSTANCE.entityToList(entity)).collect(Collectors.toList());
    }

    @Transactional
        @Override
        public MissionRecordDto.detail add(MissionRecordDto.add addDto, SinghaUser authUser) {
            missionRepository.findById(addDto.getMissionUid()).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
            int cnt = missionRecordRepository.countTodayMission(authUser.getUser().getUid(), addDto.getMissionUid());
            if(cnt > 0) throw new RecordAlreadyAddException();
            MissionRecord missionRecord = MissionRecordMapper.INSTANCE.addDtoToEntity(addDto);
            Mission mission = missionRepository.findById(missionRecord.getMissionUid()).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));

            if (authUser != null) {
                missionRecord.setUserUid(authUser.getUser().getUid());
            }

                StringBuilder sb = new StringBuilder(authUser.getUser().getActualName());
                for (int i = 1; i < authUser.getUser().getActualName().length(); i++) {
                    sb.setCharAt(i, '*');
                }
                missionRecord.setWriter(sb.toString());

                // 미션 기록 저장 후 완료 여부 확인
                MissionRecord savedRecord = missionRecordRepository.save(missionRecord);
                
                // 미션 완료 여부 확인 및 보상 지급
                checkMissionCompletionAndReward(authUser.getUser(), mission);
                
                return MissionRecordMapper.INSTANCE.entityToDetail(savedRecord);


        }

        @Transactional
        @Override
        public MissionRecordDto.detail update(MissionRecord missionRecord, MissionRecordDto.update updateDto, SinghaUser authUser) {
            missionRecord = MissionRecordMapper.INSTANCE.updateDtoToEntity(updateDto, missionRecord);
            missionRecord = missionRecordRepository.save(missionRecord);
            return MissionRecordMapper.INSTANCE.entityToDetail(missionRecord);
        }

        @Transactional
        @Override
        public void delete(String uid, SinghaUser authUser) {
            Optional<MissionRecord> optional = missionRecordRepository.findById(uid);
            optional.orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE_RECORD));
            MissionRecord missionRecord = optional.get();
            
            // missionRecord.setDeleteStatus(true);
            // missionRecordRepository.save(missionRecord);
            missionRecordRepository.delete(missionRecord);
        }

        private void checkMissionCompletionAndReward(User user, Mission mission) {
            // 현재 사용자의 해당 미션 기록 수 확인
            int currentRecordCount = missionRecordRepository.countMissionByUserUid(user.getUid(), mission.getUid());
            
            // 미션 완료 기준일수와 비교
            if (currentRecordCount >= mission.getTotalDay()) {
                // 미션 완료! 총 보상 지급
                saveMissionCompletionReward(user, mission);
            }
        }
        
        private void saveMissionCompletionReward(User user, Mission mission) {
            StringBuilder sb = new StringBuilder();
            sb.append("미션 완료 보상 : " + mission.getTitle());
            
            // 이미 해당 미션 완료 보상을 받았는지 확인
            // int count = pointHistoryRepository.countTodayByUserUidAndTfse(user.getUid(), sb.toString());
            
            Integer totalReward = mission.getDailyReward();
            if (totalReward == null) totalReward = 0;
            
            // 미션 완료 보상을 아직 받지 않았고 보상이 설정되어 있다면
            if (totalReward > 0) {
                // 총 보상 = dailyReward (이제 완료 보상의 의미)
                pointHistoryService.addPoint(totalReward, sb.toString(), user.getUid());
                
                // 미션 완료 푸시 알림 전송
                if (totalReward > 0) {
                    sendMissionCompletionPushAlarm(user, mission, totalReward);
                }
            }
        }
    
    
        private void sendMissionCompletionPushAlarm(User user, Mission mission, Integer totalReward) {
            String title = "🎉 미션 완료! 축하합니다!";
            String content = "'" + mission.getTitle() + "' 미션을 완료하여 " + totalReward + "포인트를 획득했습니다!";
            String link = "/missionDetail/" + mission.getUid();
    
            UserFcmToken fcmToken = userFcmTokenRepository.findById(user.getUid()).orElse(null);
    
            // 토큰이 있으면 FCM 알림 전송
            if(fcmToken != null) {
                PushNotificationRequest pushRequest = new PushNotificationRequest(title, content, link, fcmToken.getToken(), null);
                pushNotificationService.sendPushNotificationToToken(pushRequest);
            }
    
            // 푸시알람 저장
            PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
            pushAlarmDto.setUserUid(user.getUid());
            pushAlarmDto.setTitle(title);
            pushAlarmDto.setContent(content);
            pushAlarmDto.setLink(link);
            pushAlarmDto.setUserUidList(null);
            pushAlarmService.add(pushAlarmDto);
        }
}

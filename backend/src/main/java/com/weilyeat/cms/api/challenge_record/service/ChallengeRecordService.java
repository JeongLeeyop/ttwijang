package com.ttwijang.cms.api.challenge_record.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.challenge.repository.ChallengeRepository;
import com.ttwijang.cms.api.challenge_record.dto.ChallengeRecordDto;
import com.ttwijang.cms.api.challenge_record.dto.ChallengeRecordSearch;
import com.ttwijang.cms.api.challenge_record.dto.mapper.ChallengeRecordMapper;
import com.ttwijang.cms.api.challenge_record.exception.RecordAlreadyAddException;
import com.ttwijang.cms.api.challenge_record.repository.ChallengeRecordRepository;
import com.ttwijang.cms.api.point.repository.PointHistoryRepository;
import com.ttwijang.cms.api.point.service.PointHistoryService;
import com.ttwijang.cms.api.push_alarm.dto.PushAlarmDto;
import com.ttwijang.cms.api.push_alarm.service.PushAlarmService;
import com.ttwijang.cms.api.user.dto.UserFcmToken;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Challenge;
import com.ttwijang.cms.entity.ChallengeRecord;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.fcm.model.PushNotificationRequest;
import com.ttwijang.cms.fcm.service.PushNotificationService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ChallengeRecordService {
    ChallengeRecordDto.detail detail(String uid);
    List<ChallengeRecordDto.list> List(ChallengeRecordSearch search, SinghaUser authUser);
    ChallengeRecordDto.detail add(ChallengeRecordDto.add addDto, SinghaUser authUser);
	ChallengeRecordDto.detail update(ChallengeRecord entity, ChallengeRecordDto.update updateDto, SinghaUser authUser);
	void delete(String uid, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class ChallengeRecordServiceImpl implements ChallengeRecordService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
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
    public ChallengeRecordDto.detail detail(String uid) {
        return ChallengeRecordMapper.INSTANCE.entityToDetail(challengeRecordRepository.findById(uid).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE_RECORD)));
    }

    @Override
    public List<ChallengeRecordDto.list> List(ChallengeRecordSearch search, SinghaUser authUser) {
        if(search.isMyFlag()){
            User user = authUser.getUser();
            if(user != null) search.setUserUid(user.getUid());
        }
        return challengeRecordRepository.findAll(search.search()).stream().map(entity -> ChallengeRecordMapper.INSTANCE.entityToList(entity)).collect(Collectors.toList());
    }

    @Transactional
        @Override
        public ChallengeRecordDto.detail add(ChallengeRecordDto.add addDto, SinghaUser authUser) {
            challengeRepository.findById(addDto.getChallengeUid()).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
            int cnt = challengeRecordRepository.countTodayChallenge(authUser.getUser().getUid(), addDto.getChallengeUid());
            if(cnt > 0) throw new RecordAlreadyAddException();
            ChallengeRecord challengeRecord = ChallengeRecordMapper.INSTANCE.addDtoToEntity(addDto);
            Challenge challenge = challengeRepository.findById(challengeRecord.getChallengeUid()).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));

            if (authUser != null) {
                challengeRecord.setUserUid(authUser.getUser().getUid());
            }

                StringBuilder sb = new StringBuilder(authUser.getUser().getActualName());
                for (int i = 1; i < authUser.getUser().getActualName().length(); i++) {
                    sb.setCharAt(i, '*');
                }
                challengeRecord.setWriter(sb.toString());

                    if(savePoint(authUser.getUser(), challenge)){
                        sendPushAlarm(authUser.getUser(), challenge);
                    }

            return ChallengeRecordMapper.INSTANCE.entityToDetail(challengeRecordRepository.save(challengeRecord));
        }

        @Transactional
        @Override
        public ChallengeRecordDto.detail update(ChallengeRecord challengeRecord, ChallengeRecordDto.update updateDto, SinghaUser authUser) {
            Challenge challenge = challengeRepository.findById(challengeRecord.getChallengeUid()).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
            challengeRecord = ChallengeRecordMapper.INSTANCE.updateDtoToEntity(updateDto, challengeRecord);
            challengeRecord = challengeRecordRepository.save(challengeRecord);
            return ChallengeRecordMapper.INSTANCE.entityToDetail(challengeRecord);
        }

        @Transactional
        @Override
        public void delete(String uid, SinghaUser authUser) {
            Optional<ChallengeRecord> optional = challengeRecordRepository.findById(uid);
            optional.orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE_RECORD));
            ChallengeRecord challengeRecord = optional.get();

            Challenge challenge = challengeRepository.findById(challengeRecord.getChallengeUid()).orElseThrow(() ->  new NotFoundException(NotFound.CHALLENGE_RECORD));
            
            // challengeRecord.setDeleteStatus(true);
            // challengeRecordRepository.save(challengeRecord);
            challengeRecordRepository.delete(challengeRecord);
        }

        private boolean savePoint(User user, Challenge c) {
            StringBuilder sb = new StringBuilder();
            sb.append("챌린지 기록 : " + c.getTitle());
            int count = pointHistoryRepository.countTodayByUserUidAndTfse(user.getUid(), sb.toString());
            if (count < 1) {
                Integer reward = c.getDailyReward();
                if(reward == null) reward = 0;
                pointHistoryService.addPoint(reward, sb.toString(), user.getUid());
                return true;
            }
            return false;
        }
    
    
        private void sendPushAlarm(User user, Object obj) {
    
            String content = null;
            String link = null;
    
            Challenge entity = (Challenge) obj;
            content = "챌린지 기록으로 "+entity.getDailyReward()+"포인트가 적립되셨습니다.";
            link = "/challengeDetail/"+entity.getUid();
    
            String title = "금일의 챌린지 기록 보상이 도착했습니다.";
    
            UserFcmToken fcmToken = userFcmTokenRepository.findById(user.getUid()).orElse(null);
    
            // 토큰이 있으면 FCM 알림 전송
            if(fcmToken!=null){
                PushNotificationRequest pushRequest = new PushNotificationRequest(title,content,link,fcmToken.getToken(),null);
                pushNotificationService.sendPushNotificationToToken(pushRequest);
            }
    
            // 푸쉬알람 저장
            PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
            pushAlarmDto.setUserUid(user.getUid());
            pushAlarmDto.setTitle(title);
            pushAlarmDto.setContent(content);
            pushAlarmDto.setLink(link);
            pushAlarmDto.setUserUidList(null);
            pushAlarmService.add(pushAlarmDto);
        }
}

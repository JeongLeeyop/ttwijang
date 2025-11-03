package com.weilyeat.cms.api.challenge.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.challenge.dto.ChallengeDto;
import com.weilyeat.cms.api.challenge.dto.ChallengeSearch;
import com.weilyeat.cms.api.challenge.dto.mapper.AdmChallengeMapper;
import com.weilyeat.cms.api.challenge.dto.mapper.ChallengeMapper;
import com.weilyeat.cms.api.challenge.repository.AdmChallengeRepository;
import com.weilyeat.cms.api.challenge.repository.ChallengeRepository;
import com.weilyeat.cms.api.point.repository.PointHistoryRepository;
import com.weilyeat.cms.api.point.service.PointHistoryService;
import com.weilyeat.cms.api.push_alarm.dto.PushAlarmDto;
import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.api.user.dto.UserFcmToken;
import com.weilyeat.cms.api.user.repository.UserFcmTokenRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Challenge;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.fcm.model.PushNotificationRequest;
import com.weilyeat.cms.fcm.service.PushNotificationService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface AdmChallengeService {
    ChallengeDto.detail detail(SinghaUser authUser, String idx);
    List<ChallengeDto.detail> list(SinghaUser authUser, ChallengeSearch search);
    Page<ChallengeDto.detail> list(SinghaUser authUser, ChallengeSearch search, Pageable pageable);
    void add(SinghaUser authUser, ChallengeDto.add addDto );
    void update(SinghaUser authUser, Challenge Challenge, ChallengeDto.update updateDto );
    void delete(SinghaUser authUser, Challenge Challenge);
}

@Service
@AllArgsConstructor
class AdmChallengeServiceImpl implements AdmChallengeService {
    
    private final AdmChallengeRepository admChallengeRepository;
    
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
    public ChallengeDto.detail detail(SinghaUser authUser, String uid) {
        // search.setUserUid(authUser.getUser().getUid());
        Optional<Challenge> optional = admChallengeRepository.findById(uid);
        optional.orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
        ChallengeDto.detail dto = ChallengeMapper.INSTANCE.entityToDetail(optional.get());
        return dto;
    }

    @Override
    public List<ChallengeDto.detail> list(SinghaUser authUser, ChallengeSearch search) {
        return admChallengeRepository.findAll(search.search()).stream().map(entity -> ChallengeMapper.INSTANCE.entityToDetail(entity)).collect(Collectors.toList());
    }
    
    @Override
    public Page<ChallengeDto.detail> list(SinghaUser authUser, ChallengeSearch search, Pageable pageable) {
        return admChallengeRepository.findAll(search.search(), pageable).map(entity -> ChallengeMapper.INSTANCE.entityToDetail(entity));
    }

    @Transactional
    @Override
    public void add(SinghaUser authUser, ChallengeDto.add addDto) {
        Challenge entity = null;
        User user = authUser.getUser();
        entity = ChallengeMapper.INSTANCE.addDtoToEntity(addDto);
       
        // if (!StringUtils.hasText(addDto.getWriter())){
        //     StringBuilder sb = new StringBuilder(user.getActualName());
        //     for (int i = 1; i < user.getActualName().length(); i++) {
        //         sb.setCharAt(i, '*');
        //     }
        //     entity.setWriter(sb.toString());
        // }
        admChallengeRepository.save(entity);
    }
    
    @Override
    public void update(SinghaUser authUser, Challenge Challenge, ChallengeDto.update updateDto) {
        Challenge = AdmChallengeMapper.INSTANCE.updateDtoToEntity(updateDto, Challenge);
		admChallengeRepository.save(Challenge);
    }
    
    @Override
    public void delete(SinghaUser authUser, Challenge Challenge) {
		admChallengeRepository.delete(Challenge);
    }

    // @Override
    // public void updateSecretStatus(SinghaUser authUser, ChallengeDto.updateSecretStatus updateDto){
    //     Optional<Challenge> optional = admChallengeRepository.findById(updateDto.getIdx());
    //     if (optional.isPresent()) {
    //         Challenge entity = optional.get();
    //         if(authUser.getUser().getUid().equals(entity.getUserUid())){
    //             entity.setSecretStatus(updateDto.isSecretStatus());
    //             admChallengeRepository.save(entity);
    //         }else throw new BadRequestException(BadRequest.NOT_MINE);
    //     } else throw new NotFoundException(NotFound.Challenge);
    // }
    
    private void sendPushAlarm(User user, Object obj) {

        String content = null;
        String link = null;

        ChallengeDto.add addDto = (ChallengeDto.add) obj;
        content = "신규 챌린지가 등록되었습니다.";
        link = "/Challenge/?tfseDate=";

        String title = "지금 바로 웨일리잇 앱에서";

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

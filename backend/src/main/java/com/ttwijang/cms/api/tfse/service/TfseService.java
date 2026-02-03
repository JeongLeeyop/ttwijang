package com.ttwijang.cms.api.tfse.service;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ttwijang.cms.api.point.repository.PointHistoryRepository;
import com.ttwijang.cms.api.point.service.PointHistoryService;
import com.ttwijang.cms.api.push_alarm.dto.PushAlarmDto;
import com.ttwijang.cms.api.push_alarm.service.PushAlarmService;
import com.ttwijang.cms.api.tfse.dto.TfseDto;
import com.ttwijang.cms.api.tfse.dto.TfseSearch;
import com.ttwijang.cms.api.tfse.dto.mapper.TfseMapper;
import com.ttwijang.cms.api.tfse.repository.TfseLikeRepository;
import com.ttwijang.cms.api.tfse.repository.TfseRepository;
import com.ttwijang.cms.api.user.dto.UserFcmToken;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.common.exception.BadRequestException;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.BadRequest;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Tfse;
import com.ttwijang.cms.entity.TfseLike;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.fcm.model.PushNotificationRequest;
import com.ttwijang.cms.fcm.service.PushNotificationService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface TfseService {
    TfseDto.detail detail(SinghaUser authUser, Long idx);
    Page<TfseDto.list> list(SinghaUser authUser, TfseSearch search, Pageable pageable);
    void add(SinghaUser authUser, TfseDto.add addDto );
    void update(SinghaUser authUser, Tfse tfse, TfseDto.update updateDto );
    void delete(SinghaUser authUser, Tfse tfse);
    void updateSecretStatus(SinghaUser authUser, TfseDto.updateSecretStatus updateDto);
}

@Service
@AllArgsConstructor
class TfseServiceImpl implements TfseService {
    private final TfseRepository tfseRepository;
    
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
    TfseLikeRepository tfseLikeRepository;

    @Override
    public TfseDto.detail detail(SinghaUser authUser, Long idx) {
        // search.setUserUid(authUser.getUser().getUid());
        Optional<Tfse> optional = tfseRepository.findById(idx);
        optional.orElseThrow(() -> new NotFoundException(NotFound.TFSE));
        TfseDto.detail dto = TfseMapper.INSTANCE.entityToAdmDetail(optional.get());
        
        if (authUser != null) {
            Optional<TfseLike> optional2 = tfseLikeRepository.findByTfseIdxAndUserUid(idx, authUser.getUser().getUid());
            dto.setLikeStatus(optional2.isPresent());
        }
        return dto;
    }

    @Override
    public Page<TfseDto.list> list(SinghaUser authUser, TfseSearch search, Pageable pageable) {
        return tfseRepository.findAll(search.search(), pageable).map(entity -> TfseMapper.INSTANCE.entityToAdmList(entity));
    }

    @Transactional
    @Override
    public void add(SinghaUser authUser, TfseDto.add addDto) {
        Tfse entity = null;
        User user = authUser.getUser();
        entity = TfseMapper.INSTANCE.addDtoToEntity(addDto);
        entity.setUserUid(user.getUid());
       
        if (!StringUtils.hasText(addDto.getWriter())){
            StringBuilder sb = new StringBuilder(user.getActualName());
            for (int i = 1; i < user.getActualName().length(); i++) {
                sb.setCharAt(i, '*');
            }
            entity.setWriter(sb.toString());
        }
        tfseRepository.save(entity);

        if (addDto.getTfseDate().toLocalDate().equals(LocalDate.now())) {
            if(savePoint(authUser.getUser())){
                sendPushAlarm(authUser.getUser(), addDto);
            }
        }
    }
    
    @Override
    public void update(SinghaUser authUser, Tfse tfse, TfseDto.update updateDto) {
        tfse = TfseMapper.INSTANCE.updateDtoToEntity(updateDto, tfse);
		tfseRepository.save(tfse);
    }
    
    @Override
    public void delete(SinghaUser authUser, Tfse tfse) {
		tfseRepository.delete(tfse);
    }

    @Override
    public void updateSecretStatus(SinghaUser authUser, TfseDto.updateSecretStatus updateDto){
        Optional<Tfse> optional = tfseRepository.findById(updateDto.getIdx());
        if (optional.isPresent()) {
            Tfse entity = optional.get();
            if(authUser.getUser().getUid().equals(entity.getUserUid())){
                entity.setSecretStatus(updateDto.isSecretStatus());
                tfseRepository.save(entity);
            }else throw new BadRequestException(BadRequest.NOT_MINE);
        } else throw new NotFoundException(NotFound.TFSE);
    }
    

    private boolean savePoint(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("TFSE일지 작성");
        int count = pointHistoryRepository.countTodayByUserUidAndTfse(user.getUid(), sb.toString());
        if (count < 1) {
            pointHistoryService.addPoint(10, sb.toString(), user.getUid());
            return true;
        }
        return false;
    }


    private void sendPushAlarm(User user, Object obj) {

        String content = null;
        String link = null;

        TfseDto.add addDto = (TfseDto.add) obj;
        content = "TFSE일지 작성으로 10포인트가 적립되셨습니다.";
        link = "/tfse/?tfseDate="+addDto.getTfseDate().toLocalDate();

        String title = "금일의 TFSE일지 작성 보상이 도착했습니다.";

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

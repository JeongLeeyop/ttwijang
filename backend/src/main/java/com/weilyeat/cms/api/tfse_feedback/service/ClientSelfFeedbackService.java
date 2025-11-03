package com.weilyeat.cms.api.tfse_feedback.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.api.tfse.dto.TfseDto;
import com.weilyeat.cms.api.tfse_feedback.dto.SelfFeedbackDto;
import com.weilyeat.cms.api.tfse_feedback.dto.SelfFeedbackSearch;
import com.weilyeat.cms.api.tfse_feedback.dto.mapper.SelfFeedbackMapper;
import com.weilyeat.cms.api.tfse_feedback.repository.SelfFeedbackRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.SelfFeedback;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.fcm.service.PushNotificationService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ClientSelfFeedbackService {
    SelfFeedbackDto.detail detail(SinghaUser authUser, SelfFeedbackSearch search);
    void add(SinghaUser authUser, SelfFeedbackDto.add addDto );
}

@Service
@AllArgsConstructor
class ClientSelfFeedbackServiceImpl implements ClientSelfFeedbackService {
    private final SelfFeedbackRepository selfFeedbackRepository;
    
    @Autowired
    PushAlarmService pushAlarmService;
    
    @Autowired
    PushNotificationService pushNotificationService;
    
    @Override
    public SelfFeedbackDto.detail detail(SinghaUser authUser, SelfFeedbackSearch search) {
        search.setUserUid(authUser.getUser().getUid());
        return SelfFeedbackMapper.INSTANCE.entityToDetail(selfFeedbackRepository.findOne(search.search()).orElse(null));
    } 

    @Override
    public void add(SinghaUser authUser, SelfFeedbackDto.add addDto) {
        User user = authUser.getUser();
        SelfFeedback entity = null;
        Optional<SelfFeedback> optional = selfFeedbackRepository.findByUserUidAndSelfFeedbackDate(user.getUid(), addDto.getSelfFeedbackDate());
        if (optional.isPresent()) {
            entity = SelfFeedbackMapper.INSTANCE.addDtoToEntity(addDto, optional.get());
        } else {
            entity = SelfFeedbackMapper.INSTANCE.addDtoToEntity(addDto);
        }
        System.out.println(entity);
        entity.setUserUid(user.getUid());
        selfFeedbackRepository.save(entity);
    }
}

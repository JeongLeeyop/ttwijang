package com.weilyeat.cms.api.tfse_feedback.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.api.tfse_feedback.dto.SelfFeedbackDto;
import com.weilyeat.cms.api.tfse_feedback.dto.SelfFeedbackSearch;
import com.weilyeat.cms.api.tfse_feedback.dto.mapper.SelfFeedbackMapper;
import com.weilyeat.cms.api.tfse_feedback.repository.SelfFeedbackRepository;
import com.weilyeat.cms.entity.SelfFeedback;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.fcm.service.PushNotificationService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface SelfFeedbackService {
    Page<SelfFeedbackDto.list> list(SinghaUser authUser, SelfFeedbackSearch search, Pageable pageable);
    SelfFeedbackDto.detail detail(SinghaUser authUser, SelfFeedbackSearch search);
    SelfFeedbackDto.detail detail(Long idx);
    void add(SinghaUser authUser, SelfFeedbackDto.add addDto );
    void delete(SelfFeedback entity);
}

@Service
@AllArgsConstructor
class SelfFeedbackServiceImpl implements SelfFeedbackService {
    private final SelfFeedbackRepository selfFeedbackRepository;
    
    @Autowired
    PushAlarmService pushAlarmService;
    
    @Autowired
    PushNotificationService pushNotificationService;
    
    @Override
    public Page<SelfFeedbackDto.list> list(SinghaUser authUser, SelfFeedbackSearch search, Pageable pageable) {
        return selfFeedbackRepository.findAll(search.search(), pageable).map(data -> SelfFeedbackMapper.INSTANCE.entityToList(data));
    } 

    @Override
    public SelfFeedbackDto.detail detail(SinghaUser authUser, SelfFeedbackSearch search) {
        search.setUserUid(authUser.getUser().getUid());
        return SelfFeedbackMapper.INSTANCE.entityToDetail(selfFeedbackRepository.findOne(search.search()).orElse(null));
    } 
    
    @Override
    public SelfFeedbackDto.detail detail(Long idx) {
        return SelfFeedbackMapper.INSTANCE.entityToDetail(selfFeedbackRepository.findById(idx).orElse(null));
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

    @Override
    public void delete(SelfFeedback entity) {
        selfFeedbackRepository.delete(entity);
    }
}

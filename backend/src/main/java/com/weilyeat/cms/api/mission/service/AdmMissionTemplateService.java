package com.weilyeat.cms.api.mission.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.mission.dto.MissionTemplateDto;
import com.weilyeat.cms.api.mission.dto.MissionTemplateSearch;
import com.weilyeat.cms.api.mission.dto.mapper.MissionTemplateMapper;
import com.weilyeat.cms.api.mission.repository.AdmMissionTemplateRepository;
import com.weilyeat.cms.api.point.repository.PointHistoryRepository;
import com.weilyeat.cms.api.point.service.PointHistoryService;
import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.api.user.repository.UserFcmTokenRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.MissionTemplate;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.fcm.service.PushNotificationService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface AdmMissionTemplateService {
    MissionTemplateDto.detail detail(SinghaUser authUser, String idx);
    List<MissionTemplateDto.detail> list(SinghaUser authUser, MissionTemplateSearch search);
    Page<MissionTemplateDto.detail> list(SinghaUser authUser, MissionTemplateSearch search, Pageable pageable);
    void add(SinghaUser authUser, MissionTemplateDto.add addDto );
    void update(SinghaUser authUser, MissionTemplate Mission, MissionTemplateDto.update updateDto );
    void delete(SinghaUser authUser, MissionTemplate Mission);
}

@Service
@AllArgsConstructor
class AdmMissionTemplateServiceImpl implements AdmMissionTemplateService {
    
    private final AdmMissionTemplateRepository admMissionTemplateRepository;
    
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
    public MissionTemplateDto.detail detail(SinghaUser authUser, String uid) {
        Optional<MissionTemplate> optional = admMissionTemplateRepository.findById(uid);
        optional.orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
        MissionTemplateDto.detail dto = MissionTemplateMapper.INSTANCE.entityToDetail(optional.get());
        return dto;
    }

    @Override
    public List<MissionTemplateDto.detail> list(SinghaUser authUser, MissionTemplateSearch search) {
        return admMissionTemplateRepository.findAll(search.search()).stream().map(entity -> MissionTemplateMapper.INSTANCE.entityToDetail(entity)).collect(Collectors.toList());
    }
    
    @Override
    public Page<MissionTemplateDto.detail> list(SinghaUser authUser, MissionTemplateSearch search, Pageable pageable) {
        Page<MissionTemplateDto.detail> a = admMissionTemplateRepository.findAll(search.search(), pageable).map(entity -> MissionTemplateMapper.INSTANCE.entityToDetail(entity));
        return a;
    }

    @Transactional
    @Override
    public void add(SinghaUser authUser, MissionTemplateDto.add addDto) {
        MissionTemplate entity = null;
        User user = authUser.getUser();
        entity = MissionTemplateMapper.INSTANCE.addDtoToEntity(addDto);
        admMissionTemplateRepository.save(entity);
    }
    
    @Override
    public void update(SinghaUser authUser, MissionTemplate Mission, MissionTemplateDto.update updateDto) {
        Mission = MissionTemplateMapper.INSTANCE.updateDtoToEntity(updateDto, Mission);
		admMissionTemplateRepository.save(Mission);
    }
    
    @Override
    public void delete(SinghaUser authUser, MissionTemplate Mission) {
		admMissionTemplateRepository.delete(Mission);
    }
}

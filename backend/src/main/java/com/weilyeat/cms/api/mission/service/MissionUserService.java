package com.ttwijang.cms.api.mission.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.mission.dto.MissionUserDto;
import com.ttwijang.cms.api.mission.dto.mapper.MissionUserMapper;
import com.ttwijang.cms.api.mission.exception.UserAlreadyJoinException;
import com.ttwijang.cms.api.mission.repository.MissionRepository;
import com.ttwijang.cms.api.mission.repository.MissionUserRepository;
import com.ttwijang.cms.api.point.repository.PointHistoryRepository;
import com.ttwijang.cms.api.point.service.PointHistoryService;
import com.ttwijang.cms.api.push_alarm.service.PushAlarmService;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Mission;
import com.ttwijang.cms.entity.MissionUser;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.fcm.service.PushNotificationService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface MissionUserService {
    void add(SinghaUser authUser, MissionUserDto.add addDto );
}

@Service
@AllArgsConstructor
class MissionUserServiceImpl implements MissionUserService {
    
    private final MissionUserRepository missionUserRepository;
    private final MissionRepository missionRepository;
    
    @Transactional
    @Override
    public void add(SinghaUser authUser, MissionUserDto.add addDto) {
        User user = authUser.getUser();
        if(user == null) throw new NotFoundException(NotFound.USER);
        
        MissionUser entity = missionUserRepository.findByUserUidAndMissionUid(user.getUid(), addDto.getMissionUid());
        
        if(entity == null) {
            // 새로운 참여자 생성
            entity = new MissionUser();
            entity.setUserUid(user.getUid());
            entity.setMissionUid(addDto.getMissionUid());
            entity.setApproveStatus(true);
        } else if(entity.isApproveStatus()) {
            throw new UserAlreadyJoinException();
        } else {
            entity.setApproveStatus(true);
        }
        
        missionUserRepository.save(entity);
    }
}

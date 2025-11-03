package com.ttwijang.cms.api.challenge.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.challenge.dto.ChallengeUserDto;
import com.ttwijang.cms.api.challenge.dto.mapper.ChallengeUserMapper;
import com.ttwijang.cms.api.challenge.exception.UserAlreadyJoinException;
import com.ttwijang.cms.api.challenge.repository.ChallengeRepository;
import com.ttwijang.cms.api.challenge.repository.ChallengeUserRepository;
import com.ttwijang.cms.api.point.repository.PointHistoryRepository;
import com.ttwijang.cms.api.point.service.PointHistoryService;
import com.ttwijang.cms.api.push_alarm.service.PushAlarmService;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Challenge;
import com.ttwijang.cms.entity.ChallengeUser;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.fcm.service.PushNotificationService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ChallengeUserService {
    void add(SinghaUser authUser, ChallengeUserDto.add addDto );
}

@Service
@AllArgsConstructor
class ChallengeUserServiceImpl implements ChallengeUserService {
    
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeRepository challengeRepository;
    
    @Transactional
    @Override
    public void add(SinghaUser authUser, ChallengeUserDto.add addDto) {
        ChallengeUser entity = null;
        User user = authUser.getUser();
        if(user == null) new NotFoundException(NotFound.USER);
        int cnt = challengeUserRepository.countByUserUidAndChallengeUid(user.getUid(), addDto.getChallengeUid());
        if(cnt > 0) throw new UserAlreadyJoinException();
        addDto.setUserUid(user.getUid());
        Challenge challenge = challengeRepository.findById(addDto.getChallengeUid()).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
        Integer totalDay = challenge.getTotalDay();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDateTime = now.plus(totalDay, ChronoUnit.DAYS);
        addDto.setDueDate(futureDateTime);
        entity = ChallengeUserMapper.INSTANCE.addDtoToEntity(addDto);
        challengeUserRepository.save(entity);
    }
}

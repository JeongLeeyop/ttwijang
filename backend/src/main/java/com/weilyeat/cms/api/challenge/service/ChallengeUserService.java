package com.weilyeat.cms.api.challenge.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.challenge.dto.ChallengeUserDto;
import com.weilyeat.cms.api.challenge.dto.mapper.ChallengeUserMapper;
import com.weilyeat.cms.api.challenge.exception.UserAlreadyJoinException;
import com.weilyeat.cms.api.challenge.repository.ChallengeRepository;
import com.weilyeat.cms.api.challenge.repository.ChallengeUserRepository;
import com.weilyeat.cms.api.point.repository.PointHistoryRepository;
import com.weilyeat.cms.api.point.service.PointHistoryService;
import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.api.user.repository.UserFcmTokenRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Challenge;
import com.weilyeat.cms.entity.ChallengeUser;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.fcm.service.PushNotificationService;
import com.weilyeat.cms.oauth.SinghaUser;

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

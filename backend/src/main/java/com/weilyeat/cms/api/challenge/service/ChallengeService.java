package com.ttwijang.cms.api.challenge.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.challenge.dto.ChallengeDto;
import com.ttwijang.cms.api.challenge.dto.ChallengeSearch;
import com.ttwijang.cms.api.challenge.dto.mapper.ChallengeMapper;
import com.ttwijang.cms.api.challenge.dto.mapper.ChallengeUserMapper;
import com.ttwijang.cms.api.challenge.repository.ChallengeRepository;
import com.ttwijang.cms.api.challenge.repository.ChallengeUserRepository;
import com.ttwijang.cms.api.challenge_record.repository.ChallengeRecordRepository;
import com.ttwijang.cms.api.point.repository.PointHistoryRepository;
import com.ttwijang.cms.api.point.service.PointHistoryService;
import com.ttwijang.cms.api.push_alarm.dto.PushAlarmDto;
import com.ttwijang.cms.api.push_alarm.service.PushAlarmService;
import com.ttwijang.cms.api.user.dto.UserFcmToken;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Challenge;
import com.ttwijang.cms.entity.ChallengeUser;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.fcm.model.PushNotificationRequest;
import com.ttwijang.cms.fcm.service.PushNotificationService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface ChallengeService {
    ChallengeDto.detail detail(SinghaUser authUser, String idx);
    List<ChallengeDto.detail> list(SinghaUser authUser, ChallengeSearch search);
    Page<ChallengeDto.detail> list(SinghaUser authUser, ChallengeSearch search, Pageable pageable);
    void add(SinghaUser authUser, ChallengeDto.add addDto );
    void update(SinghaUser authUser, Challenge Challenge, ChallengeDto.update updateDto );
    void delete(SinghaUser authUser, Challenge Challenge);
}

@Service
@AllArgsConstructor
class ChallengeServiceImpl implements ChallengeService {
    
    private final ChallengeRepository challengeRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final UserRepository userRepository;
    
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
        User user = userRepository.findById(authUser.getUser().getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));
        Optional<Challenge> optional = challengeRepository.findById(uid);
        optional.orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
        ChallengeDto.detail dto = ChallengeMapper.INSTANCE.entityToDetail(optional.get());
        ChallengeUser result = challengeUserRepository.findByUserUidAndChallengeUid(authUser.getUser().getUid(), uid);
        if (result != null) { 
            dto.setUserJoinStatus(true);
            dto.setChallengeUser(ChallengeUserMapper.INSTANCE.entityToDetail(result));
        } else dto.setUserJoinStatus(false);
        
        int cnt2 = challengeRecordRepository.countTodayChallenge(user.getUid(), uid);
        if(cnt2 > 0) dto.setTodayWriteStatus(true);
        else dto.setTodayWriteStatus(false);
        
        return dto;
    }

    @Override
    public List<ChallengeDto.detail> list(SinghaUser authUser, ChallengeSearch search) {
        search.setUserUid(authUser.getUser().getUid());
        return challengeRepository.findAll(search.search()).stream().map(entity -> ChallengeMapper.INSTANCE.entityToDetailMyFlag(entity, search)).collect(Collectors.toList());
    }
    
    @Override
    public Page<ChallengeDto.detail> list(SinghaUser authUser, ChallengeSearch search, Pageable pageable) {
        return challengeRepository.findAll(search.search(), pageable).map(entity -> ChallengeMapper.INSTANCE.entityToDetail(entity));
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
        challengeRepository.save(entity);
    }
    
    @Override
    public void update(SinghaUser authUser, Challenge Challenge, ChallengeDto.update updateDto) {
        Challenge = ChallengeMapper.INSTANCE.updateDtoToEntity(updateDto, Challenge);
		challengeRepository.save(Challenge);
    }
    
    @Override
    public void delete(SinghaUser authUser, Challenge Challenge) {
		challengeRepository.delete(Challenge);
    }

    // @Override
    // public void updateSecretStatus(SinghaUser authUser, ChallengeDto.updateSecretStatus updateDto){
    //     Optional<Challenge> optional = challengeRepository.findById(updateDto.getIdx());
    //     if (optional.isPresent()) {
    //         Challenge entity = optional.get();
    //         if(authUser.getUser().getUid().equals(entity.getUserUid())){
    //             entity.setSecretStatus(updateDto.isSecretStatus());
    //             challengeRepository.save(entity);
    //         }else throw new BadRequestException(BadRequest.NOT_MINE);
    //     } else throw new NotFoundException(NotFound.Challenge);
    // }
    
}

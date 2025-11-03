package com.weilyeat.cms.api.mission_user_inquiry.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.mission_user_inquiry.repository.MissionUserInquiryRepository;
import com.weilyeat.cms.api.mission.repository.MissionUserRepository;
import com.weilyeat.cms.api.mission_user_inquiry.dto.MissionUserInquiryDto;
import com.weilyeat.cms.api.mission_user_inquiry.dto.MissionUserInquirySearch;
import com.weilyeat.cms.api.mission_user_inquiry.dto.mapper.MissionUserInquiryMapper;
import com.weilyeat.cms.api.mission_user_inquiry.exception.InquiryAlreadyAddException;
import com.weilyeat.cms.api.point.repository.PointHistoryRepository;
import com.weilyeat.cms.api.point.service.PointHistoryService;
import com.weilyeat.cms.api.push_alarm.dto.PushAlarmDto;
import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.api.user.dto.UserFcmToken;
import com.weilyeat.cms.api.user.repository.UserFcmTokenRepository;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Mission;
import com.weilyeat.cms.entity.MissionUser;
import com.weilyeat.cms.entity.MissionUserInquiry;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.fcm.model.PushNotificationRequest;
import com.weilyeat.cms.fcm.service.PushNotificationService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface MissionUserInquiryService {
    List<MissionUserInquiryDto.detail> detail(String userUid);
    List<MissionUserInquiryDto.list> List(MissionUserInquirySearch search, SinghaUser authUser);
    void add(List<MissionUserInquiryDto.add> addDto, SinghaUser authUser);
	MissionUserInquiryDto.detail update(MissionUserInquiry entity, MissionUserInquiryDto.update updateDto, SinghaUser authUser);
	void delete(Integer idx, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class MissionUserInquiryServiceImpl implements MissionUserInquiryService {
    private final MissionUserRepository missionUserRepository;
    private final MissionUserInquiryRepository missionUserInquiryRepository;
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
    UserRepository userRepository;

    @Override
    public List<MissionUserInquiryDto.detail> detail(String userUid) {
        return missionUserInquiryRepository.findByUserUid(userUid).stream().map(entity -> MissionUserInquiryMapper.INSTANCE.entityToDetail(entity)).collect(Collectors.toList());
    }

    @Override
    public List<MissionUserInquiryDto.list> List(MissionUserInquirySearch search, SinghaUser authUser) {
        if(search.isMyFlag()){
            User user = authUser.getUser();
            if(user != null) search.setUserUid(user.getUid());
        }
        return missionUserInquiryRepository.findAll(search.search()).stream().map(entity -> MissionUserInquiryMapper.INSTANCE.entityToList(entity)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void add(List<MissionUserInquiryDto.add> addDto, SinghaUser authUser) {
        User user = userRepository.findById(authUser.getUser().getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));

        // 기존 문의를 삭제
        missionUserInquiryRepository.deleteAllByUserUid(user.getUid());

        // addDto를 Mapper를 통해 enity로 변환하고 userUid 설정
        List<MissionUserInquiry> result = addDto.stream()
            .map(MissionUserInquiryMapper.INSTANCE::addDtoToEntity)
            .peek(entity -> entity.setUserUid(user.getUid()))
            .collect(Collectors.toList());

        // MissionUserInquiryRepository에 저장
        missionUserInquiryRepository.saveAll(result);

        user.setMissionInquiryStatus(true);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public MissionUserInquiryDto.detail update(MissionUserInquiry missionUserInquiry, MissionUserInquiryDto.update updateDto, SinghaUser authUser) {
        // MissionUser missionUser = missionUserRepository.findById(missionUserInquiry.getMissionUserUid()).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
        missionUserInquiry = MissionUserInquiryMapper.INSTANCE.updateDtoToEntity(updateDto, missionUserInquiry);
        missionUserInquiry = missionUserInquiryRepository.save(missionUserInquiry);
        return MissionUserInquiryMapper.INSTANCE.entityToDetail(missionUserInquiry);
    }

    @Transactional
    @Override
    public void delete(Integer idx, SinghaUser authUser) {
        Optional<MissionUserInquiry> optional = missionUserInquiryRepository.findById(idx);
        optional.orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE_RECORD));
        MissionUserInquiry missionUserInquiry = optional.get();

        // MissionUser missionUser = missionUserRepository.findById(missionUserInquiry.getMissionUserUid()).orElseThrow(() ->  new NotFoundException(NotFound.CHALLENGE_RECORD));
        
        // missionUserInquiry.setDeleteStatus(true);
        // missionUserInquiryRepository.save(missionUserInquiry);
        missionUserInquiryRepository.delete(missionUserInquiry);
    }
}

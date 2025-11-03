package com.weilyeat.cms.api.mission_inquiry.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.mission.repository.MissionRepository;
import com.weilyeat.cms.api.mission_inquiry.dto.MissionInquiryDto;
import com.weilyeat.cms.api.mission_inquiry.dto.MissionInquiryPageDto;
import com.weilyeat.cms.api.mission_inquiry.dto.MissionInquirySearch;
import com.weilyeat.cms.api.mission_inquiry.dto.mapper.MissionInquiryMapper;
import com.weilyeat.cms.api.mission_inquiry.dto.mapper.MissionInquiryPageMapper;
import com.weilyeat.cms.api.mission_inquiry.exception.InquiryAlreadyAddException;
import com.weilyeat.cms.api.mission_inquiry.repository.MissionInquiryPageRepository;
import com.weilyeat.cms.api.mission_inquiry.repository.MissionInquiryRepository;
import com.weilyeat.cms.api.point.repository.PointHistoryRepository;
import com.weilyeat.cms.api.point.service.PointHistoryService;
import com.weilyeat.cms.api.push_alarm.dto.PushAlarmDto;
import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.api.user.dto.UserFcmToken;
import com.weilyeat.cms.api.user.repository.UserFcmTokenRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.Mission;
import com.weilyeat.cms.entity.MissionInquiry;
import com.weilyeat.cms.entity.MissionInquiryPage;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.fcm.model.PushNotificationRequest;
import com.weilyeat.cms.fcm.service.PushNotificationService;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface MissionInquiryService {
    MissionInquiryDto.detail detail(Integer idx);
    List<MissionInquiryDto.list> listPage(MissionInquirySearch search, SinghaUser authUser);
    List<MissionInquiryPageDto.list> List(SinghaUser authUser);
    MissionInquiryPageDto.list List(SinghaUser authUser, Integer pageNum);
    MissionInquiryDto.detail add(MissionInquiryDto.add addDto, SinghaUser authUser);
	MissionInquiryDto.detail update(MissionInquiry entity, MissionInquiryDto.update updateDto, SinghaUser authUser);
	MissionInquiryPageDto.detail updatePage(MissionInquiryPage entity, MissionInquiryPageDto.update updateDto, SinghaUser authUser);
	void delete(Integer idx, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class MissionInquiryServiceImpl implements MissionInquiryService {
    private final MissionRepository missionRepository;
    private final MissionInquiryRepository missionInquiryRepository;
    private final MissionInquiryPageRepository missionInquiryPageRepository;
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
    public MissionInquiryDto.detail detail(Integer idx) {
        return MissionInquiryMapper.INSTANCE.entityToDetail(missionInquiryRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE_RECORD)));
    }

    @Override
    public List<MissionInquiryDto.list> listPage(MissionInquirySearch search, SinghaUser authUser) {
        if(search.isMyFlag()){
            User user = authUser.getUser();
            if(user != null) search.setUserUid(user.getUid());
        }
        return missionInquiryRepository.findAll(search.search()).stream().map(entity -> MissionInquiryMapper.INSTANCE.entityToList(entity)).collect(Collectors.toList());
    }


    @Override
    public List<MissionInquiryPageDto.list> List(SinghaUser authUser) {
        return missionInquiryPageRepository.findAll().stream().map(entity -> MissionInquiryPageMapper.INSTANCE.entityToList(entity)).collect(Collectors.toList());
    }
    
    @Override
    public MissionInquiryPageDto.list List(SinghaUser authUser, Integer pageNum) {
        MissionInquiryPage page = missionInquiryPageRepository.findByPageNumAndUseStatusIsTrue(pageNum)
            .orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE_RECORD));
        
        // MissionInquiry 리스트에서 useStatus가 true인 것만 필터링
        if (page.getInquiries() != null) {
            page.setInquiries(page.getInquiries().stream()
                .filter(inquiry -> inquiry.isUseStatus())
                .collect(Collectors.toList()));
        }
        
        return MissionInquiryPageMapper.INSTANCE.entityToList(page);
    }

    @Transactional
        @Override
        public MissionInquiryDto.detail add(MissionInquiryDto.add addDto, SinghaUser authUser) {
            MissionInquiry missionInquiry = MissionInquiryMapper.INSTANCE.addDtoToEntity(addDto);

            int  viewOrder = 0;
            Optional<MissionInquiry> optional = missionInquiryRepository.findTopByOrderByViewOrderDesc();
            if (optional.isPresent()) viewOrder = optional.get().getViewOrder() + 1;
            missionInquiry.setViewOrder(viewOrder);
            return MissionInquiryMapper.INSTANCE.entityToDetail(missionInquiryRepository.save(missionInquiry));
        }

        @Transactional
        @Override
        public MissionInquiryDto.detail update(MissionInquiry missionInquiry, MissionInquiryDto.update updateDto, SinghaUser authUser) {
            // Mission mission = missionRepository.findById(missionInquiry.getMissionUid()).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
            missionInquiry = MissionInquiryMapper.INSTANCE.updateDtoToEntity(updateDto, missionInquiry);
            missionInquiry = missionInquiryRepository.save(missionInquiry);
            return MissionInquiryMapper.INSTANCE.entityToDetail(missionInquiry);
        }

        
        @Transactional
        @Override
        public MissionInquiryPageDto.detail updatePage(MissionInquiryPage missionInquiryPage, MissionInquiryPageDto.update updateDto, SinghaUser authUser) {
            // Mission mission = missionRepository.findById(missionInquiry.getMissionUid()).orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE));
            missionInquiryPage = MissionInquiryPageMapper.INSTANCE.updateDtoToEntity(updateDto, missionInquiryPage);
            missionInquiryPage = missionInquiryPageRepository.save(missionInquiryPage);
            return MissionInquiryPageMapper.INSTANCE.entityToDetail(missionInquiryPage);
        }

        @Transactional
        @Override
        public void delete(Integer idx, SinghaUser authUser) {
            Optional<MissionInquiry> optional = missionInquiryRepository.findById(idx);
            optional.orElseThrow(() -> new NotFoundException(NotFound.CHALLENGE_RECORD));
            MissionInquiry missionInquiry = optional.get();

            // Mission mission = missionRepository.findById(missionInquiry.getMissionUid()).orElseThrow(() ->  new NotFoundException(NotFound.CHALLENGE_RECORD));
            
            // missionInquiry.setDeleteStatus(true);
            // missionInquiryRepository.save(missionInquiry);
            missionInquiryRepository.delete(missionInquiry);
        }
}

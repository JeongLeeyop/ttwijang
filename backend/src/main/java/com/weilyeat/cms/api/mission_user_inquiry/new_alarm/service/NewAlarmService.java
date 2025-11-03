package com.weilyeat.cms.api.mission_user_inquiry.new_alarm.service;

import java.util.ArrayList;
import java.util.List;

// import com.weilyeat.cms.config.WebUtilConfig;
import com.weilyeat.cms.entity.NewAlarm;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.api.mission_user_inquiry.new_alarm.dto.NewAlarmDto;
import com.weilyeat.cms.api.mission_user_inquiry.new_alarm.dto.mapper.NewAlarmMapper;
import com.weilyeat.cms.api.mission_user_inquiry.new_alarm.dto.search.NewAlarmSearch;
import com.weilyeat.cms.api.mission_user_inquiry.new_alarm.exception.NewAlarmAccessException;
import com.weilyeat.cms.api.mission_user_inquiry.new_alarm.repository.NewAlarmRepository;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.oauth.SinghaUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

public interface NewAlarmService {
    Integer count(SinghaUser authUser);
    void add(NewAlarmDto.Add addDto);
    void check(SinghaUser authUser, String postUid);
}

@Service
@AllArgsConstructor
class NewAlarmServiceImpl implements NewAlarmService {
    private final NewAlarmRepository newAlarmRepository;
    private final UserRepository userRepository;

    @Override
    public Integer count(SinghaUser authUser) {
        if (authUser != null) {
            return (int) newAlarmRepository.count(authUser.getUser().getUid());
        }
        return null;
    }

    @Override
    public void add(NewAlarmDto.Add item) {
        if(item.getPostUid() != null){
            List<User> userList = userRepository.findAll();
            for(User user : userList){
                NewAlarm data = new NewAlarm();
                data.setPostUid(item.getPostUid());
                data.setType(item.getType());
                data.setCheckStatus(false);
                data.setUserUid(user.getUid());
                newAlarmRepository.save(data);
            }
        } 
    }

    @Override
    public void check(SinghaUser authUser, String postUid) {
       newAlarmRepository.check(authUser.getUser().getUid(),postUid);        
    }
}
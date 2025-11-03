package com.ttwijang.cms.api.mission_user_inquiry.new_alarm.service;

import java.util.ArrayList;
import java.util.List;

// import com.ttwijang.cms.config.WebUtilConfig;
import com.ttwijang.cms.entity.NewAlarm;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.api.mission_user_inquiry.new_alarm.dto.NewAlarmDto;
import com.ttwijang.cms.api.mission_user_inquiry.new_alarm.dto.mapper.NewAlarmMapper;
import com.ttwijang.cms.api.mission_user_inquiry.new_alarm.dto.search.NewAlarmSearch;
import com.ttwijang.cms.api.mission_user_inquiry.new_alarm.exception.NewAlarmAccessException;
import com.ttwijang.cms.api.mission_user_inquiry.new_alarm.repository.NewAlarmRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.oauth.SinghaUser;

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
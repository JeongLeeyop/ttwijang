package com.weilyeat.cms.api.push_alarm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// import com.weilyeat.cms.api.post.model.dto.PostDto;
import com.weilyeat.cms.api.push_alarm.dto.PushAlarmDto;
import com.weilyeat.cms.api.push_alarm.dto.mapper.PushAlarmMapper;
import com.weilyeat.cms.api.push_alarm.dto.search.PushAlarmSearch;
import com.weilyeat.cms.api.push_alarm.exception.PushAlarmAccessException;
import com.weilyeat.cms.api.push_alarm.repository.PushAlarmRepository;
// import com.weilyeat.cms.config.WebUtilConfig;
import com.weilyeat.cms.entity.PushAlarm;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface PushAlarmService {
    Page<PushAlarmDto.Detail> list(SinghaUser authUser, Pageable pageable);
    Integer count(SinghaUser authUser);
    void add(PushAlarmDto.Add addDto);
    void delete(SinghaUser authUser, PushAlarm pushAlarm);
}

@Service
@AllArgsConstructor
class PushAlarmServiceImpl implements PushAlarmService {
    private final PushAlarmRepository pushAlarmRepository;

    @Override
    public Page<PushAlarmDto.Detail> list(SinghaUser authUser, Pageable pageable) {
        PushAlarmSearch pushAlarmSearch = new PushAlarmSearch();
        if (authUser != null) {
            pushAlarmSearch.setUserUid(authUser.getUser().getUid());
            return pushAlarmRepository.findAll(pushAlarmSearch.search(), pageable).map(e -> PushAlarmMapper.INSTANCE.entityToDetailDto(e));
        }
        return null;
    }

    @Override
    public Integer count(SinghaUser authUser) {
        PushAlarmSearch pushAlarmSearch = new PushAlarmSearch();
        if (authUser != null) {
            pushAlarmSearch.setUserUid(authUser.getUser().getUid());
            return (int) pushAlarmRepository.count(pushAlarmSearch.search());
        }
        return null;
    }

    @Override
    public void add(PushAlarmDto.Add item) {
        
        if(item.getUserUidList() != null){
            List<PushAlarm> datas = new ArrayList<PushAlarm>();
            item.getUserUidList().forEach(uid -> {
                PushAlarm data = new PushAlarm();
                data.setTitle(item.getTitle());
                data.setContent(item.getContent());
                data.setLink(item.getLink());
                data.setUserUid(uid.getUid());
                datas.add(data);
            });
            pushAlarmRepository.saveAll(datas);    
        } else {
            PushAlarm data = new PushAlarm();
            data.setTitle(item.getTitle());
            data.setContent(item.getContent());
            data.setLink(item.getLink());
            data.setUserUid(item.getUserUid());
            pushAlarmRepository.save(data);
        }
    }

    @Override
    public void delete(SinghaUser authUser, PushAlarm pushAlarm) {
        if (pushAlarm.getUserUid().equals(authUser.getUser().getUid())) pushAlarmRepository.delete(pushAlarm);        
        else throw new PushAlarmAccessException();
    }
}
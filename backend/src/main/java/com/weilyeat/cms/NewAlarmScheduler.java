package com.ttwijang.cms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ttwijang.cms.api.mission_user_inquiry.new_alarm.repository.NewAlarmRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableAsync
public class NewAlarmScheduler {
    
    @Autowired
    private NewAlarmRepository newAlarmRepository;

    @Async
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")  
    public void deleteOldAlarm() {
        int cnt = newAlarmRepository.deleteOldAlarm();
        log.info("일주일이 지난 newAlarm 목록을 삭제하였습니다. ("+cnt+")개 삭제됨");
    }
}

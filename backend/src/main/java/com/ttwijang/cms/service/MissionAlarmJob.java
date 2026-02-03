package com.ttwijang.cms.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MissionAlarmJob implements Job {

    @Autowired
    private MissionAlarmService missionAlarmService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime now = LocalDateTime.now();
        String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));

        // MissionAlarmService의 트랜잭션 메서드를 통해 알람 처리
        missionAlarmService.processAlarms(currentTime);
    }
}

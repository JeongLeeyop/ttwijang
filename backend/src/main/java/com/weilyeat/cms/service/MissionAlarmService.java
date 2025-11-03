package com.ttwijang.cms.service;

import java.util.List;

import javax.transaction.Transactional;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.CronScheduleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttwijang.cms.entity.Mission;
import com.ttwijang.cms.entity.MissionUser;
import com.ttwijang.cms.api.mission.repository.MissionRepository;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.api.push_alarm.dto.PushAlarmDto;
import com.ttwijang.cms.api.push_alarm.service.PushAlarmService;
import com.ttwijang.cms.fcm.firebase.FCMService;

@Service
public class MissionAlarmService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private FCMService fcmService;

    @Autowired
    private UserFcmTokenRepository userFcmTokenRepository;

    @Autowired
    private PushAlarmService pushAlarmService;

    public void scheduleAlarm(Mission mission) {
        scheduleAlarm(mission, false);
    }
    
    public void scheduleAlarm(Mission mission, boolean skipCancel) {
        if (mission == null) {
            throw new IllegalArgumentException("Mission cannot be null");
        }
        
        if (!mission.isAlarmStatus() || mission.getAlarmTime() == null || mission.getAlarmTime().trim().isEmpty()) {
            return;
        }

        try {
            // 크론 표현식 검증 추가
            String cronExpression = createCronExpression(mission);
            if (!isValidCronExpression(cronExpression)) {
                throw new IllegalArgumentException("잘못된 크론 표현식: " + cronExpression);
            }
            
            // 기존 스케줄이 있다면 삭제 (skipCancel이 false일 때만)
            if (!skipCancel) {
                cancelAlarm(mission.getUid());
            }

            // Job 생성
            JobDetail jobDetail = JobBuilder.newJob(MissionAlarmJob.class)
                    .withIdentity("mission-alarm-" + mission.getUid())
                    .usingJobData("missionUid", mission.getUid())
                    .usingJobData("userUid", mission.getUserUid() != null ? mission.getUserUid() : "")
                    .usingJobData("alarmTitle", mission.getAlarmTitle() != null ? mission.getAlarmTitle() : "미션 알림")
                    .usingJobData("alarmMessage", mission.getAlarmMessage() != null ? mission.getAlarmMessage() : "미션을 수행할 시간입니다!")
                    .build();

            // Trigger 생성
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("mission-alarm-trigger-" + mission.getUid())
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();

            // 스케줄 등록
            scheduler.scheduleJob(jobDetail, trigger);
            System.out.println("미션 알람 스케줄 등록 완료 - Mission: " + mission.getUid() + ", Cron: " + cronExpression);
        } catch (SchedulerException e) {
            System.out.println("미션 알람 스케줄링 실패 - Mission: " + mission.getUid() + ", Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("알람 스케줄링에 실패했습니다.", e);
        }
    }

    public void cancelAlarm(String missionUid) {
        if (missionUid == null || missionUid.trim().isEmpty()) {
            System.out.println("취소할 미션 UID가 유효하지 않습니다.");
            return;
        }
        
        try {
            JobKey jobKey = JobKey.jobKey("mission-alarm-" + missionUid);
            boolean deleted = scheduler.deleteJob(jobKey);
            if (deleted) {
                System.out.println("미션 알람 스케줄 취소 완료 - Mission: " + missionUid);
            } else {
                System.out.println("취소할 미션 알람 스케줄을 찾을 수 없습니다 - Mission: " + missionUid);
            }
        } catch (SchedulerException e) {
            System.out.println("미션 알람 스케줄 취소 실패 - Mission: " + missionUid + ", Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String createCronExpression(Mission mission) {
        String alarmTime = mission.getAlarmTime();
        if (alarmTime == null || !alarmTime.contains(":")) {
            throw new IllegalArgumentException("잘못된 알람 시간 형식: " + alarmTime);
        }
        
        String[] timeParts = alarmTime.split(":");
        if (timeParts.length != 2) {
            throw new IllegalArgumentException("알람 시간은 HH:mm 형식이어야 합니다: " + alarmTime);
        }
        
        try {
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            
            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                throw new IllegalArgumentException("잘못된 시간 범위: " + alarmTime);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("숫자가 아닌 시간 형식: " + alarmTime);
        }
        
        String minute = timeParts[1];
        String hour = timeParts[0];
        
        StringBuilder dayOfWeek = new StringBuilder();
        if (mission.isSun()) dayOfWeek.append("1,");
        if (mission.isMon()) dayOfWeek.append("2,");
        if (mission.isTue()) dayOfWeek.append("3,");
        if (mission.isWed()) dayOfWeek.append("4,");
        if (mission.isThu()) dayOfWeek.append("5,");
        if (mission.isFri()) dayOfWeek.append("6,");
        if (mission.isSat()) dayOfWeek.append("7,");
        
        if (dayOfWeek.length() > 0) {
            dayOfWeek.setLength(dayOfWeek.length() - 1); // 마지막 콤마 제거
            // 특정 요일이 선택된 경우: 초 분 시 ? 월 요일
            return String.format("0 %s %s ? * %s", minute, hour, dayOfWeek.toString());
        } else {
            // 모든 요일인 경우: 초 분 시 * 월 ?
            return String.format("0 %s %s * * ?", minute, hour);
        }
    }

    private boolean isValidCronExpression(String cronExpression) {
        try {
            CronScheduleBuilder.cronSchedule(cronExpression);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void processAlarms(String currentTime) {
        try {
            // JOIN FETCH로 missionUserList를 함께 로드
            List<Mission> missions = missionRepository.findMissionsForAlarmWithUsers(currentTime);

            for (Mission mission : missions) {
                try {
                    // 미션 사용자 리스트 검사
                    if (mission.getMissionUserList() == null || mission.getMissionUserList().isEmpty()) {
                        System.out.println("미션에 등록된 사용자가 없습니다. Mission UID: " + mission.getUid());
                        continue;
                    }
                    
                    // 모든 미션 사용자에게 알람 전송
                    for (MissionUser missionUser : mission.getMissionUserList()) {
                        String userUid = missionUser.getUserUid();
                        if (userUid == null || userUid.trim().isEmpty()) {
                            System.out.println("미션의 사용자 UID가 유효하지 않습니다. Mission UID: " + mission.getUid());
                            continue;
                        }
                        
                        try {
                            // FCM 알람 전송
                            String alarmTitle = mission.getAlarmTitle() != null ? mission.getAlarmTitle() : "미션 알림";
                            String alarmMessage = mission.getAlarmMessage() != null ? mission.getAlarmMessage() : "미션을 수행할 시간입니다!";
                            
                            fcmService.sendMissionAlarm(
                                userUid,
                                alarmTitle,
                                alarmMessage
                            );
                            
                            // FCM 전송 성공 시에만 푸시 알람을 DB에 저장
                            PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
                            pushAlarmDto.setUserUid(userUid);
                            pushAlarmDto.setTitle(alarmTitle);
                            pushAlarmDto.setContent(alarmMessage);
                            pushAlarmDto.setLink("/mission?fromPush=true&showDrawer=true");
                            pushAlarmDto.setUserUidList(null);
                            pushAlarmService.add(pushAlarmDto);
                            
                            System.out.println("미션 알람 전송 완료 - User: " + userUid + ", Mission: " + mission.getUid());
                        } catch (Exception fcmException) {
                            // FCM 전송 실패 처리 (토큰 만료, UNREGISTERED 등)
                            String errorMessage = fcmException.getMessage();
                            if (errorMessage != null && (errorMessage.contains("UNREGISTERED") || 
                                                        errorMessage.contains("Requested entity was not found") ||
                                                        errorMessage.contains("NOT_FOUND"))) {
                                System.out.println("FCM 토큰이 유효하지 않습니다 - User: " + userUid + 
                                                 ", Mission: " + mission.getUid() + 
                                                 ", 사용자가 앱을 삭제했거나 토큰이 만료되었을 수 있습니다.");
                                
                                // 무효한 토큰을 DB에서 삭제
                                try {
                                    userFcmTokenRepository.deleteByUserUid(userUid);
                                    System.out.println("무효한 FCM 토큰 삭제 완료 - User: " + userUid);
                                } catch (Exception deleteException) {
                                    System.out.println("FCM 토큰 삭제 중 오류 발생 - User: " + userUid + 
                                                     ", Error: " + deleteException.getMessage());
                                }
                            } else {
                                System.out.println("FCM 전송 실패 - User: " + userUid + 
                                                 ", Mission: " + mission.getUid() + 
                                                 ", Error: " + errorMessage);
                            }
                            
                            // FCM 전송 실패 시에는 푸시 알람 저장하지 않음 (FCMService에서 이미 처리됨)
                            System.out.println("FCM 전송 실패로 푸시 알람은 FCMService에서 처리됨 - User: " + userUid);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("개별 미션 알람 전송 중 오류 발생 - Mission UID: " + mission.getUid() + ", Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("미션 알람 배치 처리 중 전체 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

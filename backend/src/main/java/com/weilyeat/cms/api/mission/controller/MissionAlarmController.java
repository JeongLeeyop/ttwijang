package com.weilyeat.cms.api.mission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weilyeat.cms.entity.Mission;
import com.weilyeat.cms.api.mission.repository.MissionRepository;
import com.weilyeat.cms.service.MissionAlarmService;

@RestController
@RequestMapping("/api/mission/alarm")
public class MissionAlarmController {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionAlarmService missionAlarmService;

    @PostMapping("/schedule/{missionUid}")
    public ResponseEntity<String> scheduleAlarm(@PathVariable String missionUid) {
        try {
            Mission mission = missionRepository.findById(missionUid).orElse(null);
            if (mission == null) {
                return ResponseEntity.badRequest().body("Mission not found");
            }

            if (!mission.isAlarmStatus()) {
                return ResponseEntity.badRequest().body("Alarm is not enabled for this mission");
            }

            missionAlarmService.scheduleAlarm(mission);
            return ResponseEntity.ok("Alarm scheduled successfully for mission: " + missionUid);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error scheduling alarm: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{missionUid}")
    public ResponseEntity<String> cancelAlarm(@PathVariable String missionUid) {
        try {
            missionAlarmService.cancelAlarm(missionUid);
            return ResponseEntity.ok("Alarm cancelled successfully for mission: " + missionUid);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error cancelling alarm: " + e.getMessage());
        }
    }
}

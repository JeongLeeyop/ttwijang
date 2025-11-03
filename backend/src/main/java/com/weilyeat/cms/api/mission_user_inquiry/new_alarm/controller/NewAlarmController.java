package com.ttwijang.cms.api.mission_user_inquiry.new_alarm.controller;

import com.ttwijang.cms.api.mission_user_inquiry.new_alarm.dto.NewAlarmDto;
import com.ttwijang.cms.api.mission_user_inquiry.new_alarm.service.NewAlarmService;
import com.ttwijang.cms.entity.NewAlarm;
import com.ttwijang.cms.oauth.SinghaUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/newAlarm")
@AllArgsConstructor
public class NewAlarmController {
    private final NewAlarmService newAlarmService;

    @GetMapping("count")
    public ResponseEntity<Integer> count(@AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(newAlarmService.count(authUser));
    }

    @PutMapping("check/{id}")
    public ResponseEntity check(@PathVariable("id") String postUid, @AuthenticationPrincipal SinghaUser authUser) {
        newAlarmService.check(authUser, postUid);
        return ResponseEntity.ok().build();
    }
}

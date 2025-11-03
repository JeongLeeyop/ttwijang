package com.weilyeat.cms.api.push_alarm.controller;

import com.weilyeat.cms.api.push_alarm.dto.PushAlarmDto;
import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.entity.PushAlarm;
import com.weilyeat.cms.oauth.SinghaUser;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/push-alarm")
@AllArgsConstructor
public class PushAlarmController {
    private final PushAlarmService pushAlarmService;

    @GetMapping
    public ResponseEntity<Page<PushAlarmDto.Detail>> list(
        @AuthenticationPrincipal SinghaUser authUser,
        @PageableDefault(
            size=10,
            page=0)
        @SortDefault.SortDefaults({
            @SortDefault(sort = "createDate", direction = Sort.Direction.DESC)
        }) Pageable pageable) {
        return ResponseEntity.ok(pushAlarmService.list(authUser, pageable));
    }

    @GetMapping("count")
    public ResponseEntity<Integer> count(@AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(pushAlarmService.count(authUser));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") PushAlarm pushAlarm, @AuthenticationPrincipal SinghaUser authUser) {
        pushAlarmService.delete(authUser, pushAlarm);
        return ResponseEntity.ok().build();
    }
}

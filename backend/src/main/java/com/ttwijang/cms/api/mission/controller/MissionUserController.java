package com.ttwijang.cms.api.mission.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.mission.dto.MissionDto;
import com.ttwijang.cms.api.mission.dto.MissionSearch;
import com.ttwijang.cms.api.mission.dto.MissionUserDto;
import com.ttwijang.cms.api.mission.service.MissionService;
import com.ttwijang.cms.api.mission.service.MissionUserService;
import com.ttwijang.cms.entity.Mission;
import com.ttwijang.cms.entity.Post;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/mission/user")
@AllArgsConstructor
public class MissionUserController {
    private final MissionUserService missionUserService;

    @PostMapping("/join")
    public ResponseEntity add(@AuthenticationPrincipal SinghaUser authUser, @RequestBody MissionUserDto.add addDto) {
        missionUserService.add(authUser, addDto);
        return ResponseEntity.ok().build();
    }
}

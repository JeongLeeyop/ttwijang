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
import com.ttwijang.cms.api.mission.service.MissionService;
import com.ttwijang.cms.entity.Mission;
import com.ttwijang.cms.entity.Post;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/mission")
@AllArgsConstructor
public class MissionController {
    private final MissionService missionService;

    @GetMapping("{uid}")
    public ResponseEntity<MissionDto.detail> detail(@AuthenticationPrincipal SinghaUser authUser,@PathVariable("uid") String uid) {
        return ResponseEntity.ok(missionService.detail(authUser, uid));
    }

    @GetMapping
    public ResponseEntity<List<MissionDto.detail>> list(@AuthenticationPrincipal SinghaUser authUser, MissionSearch search) {
        return ResponseEntity.ok(missionService.list(authUser, search));
    }

    @GetMapping("summary")
    public ResponseEntity<MissionDto.summary> summary(@AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(missionService.getSummary(authUser));
    }
    
    @GetMapping("community")
    public ResponseEntity<Page<MissionDto.detail>> communityList(@AuthenticationPrincipal SinghaUser authUser, MissionSearch search, @PageableDefault(
					size=10,
					page=0,
					direction = Direction.DESC,
					sort = {"createDate"}) Pageable pageable) {
        return ResponseEntity.ok(missionService.list(authUser, search, pageable));
    }

    @PostMapping
    public ResponseEntity add(@AuthenticationPrincipal SinghaUser authUser, @RequestBody MissionDto.add addDto) {
        missionService.add(authUser, addDto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("{idx}")
    public ResponseEntity delete(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("idx") Mission mission) {
        missionService.delete(authUser, mission);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("{idx}")
    public ResponseEntity update(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("idx") Mission mission,@RequestBody MissionDto.update updateDto) {
        missionService.update(authUser, mission, updateDto);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("join/multiple")
    public ResponseEntity<?> joinMultipleMissions(@AuthenticationPrincipal SinghaUser authUser, @RequestBody List<String> missionUids) {
        missionService.joinMultipleMissions(authUser, missionUids);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("abandon/{missionUid}")
    public ResponseEntity<?> abandonMission(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("missionUid") String missionUid) {
        missionService.abandonMission(authUser, missionUid);
        return ResponseEntity.ok().build();
    }
    
    // @PutMapping
    // public ResponseEntity updateSecretStatus(@AuthenticationPrincipal SinghaUser authUser, @RequestBody MissionDto.updateSecretStatus updateDto) {
    //     missionService.updateSecretStatus(authUser, updateDto);
    //     return ResponseEntity.ok().build();
    // }
}

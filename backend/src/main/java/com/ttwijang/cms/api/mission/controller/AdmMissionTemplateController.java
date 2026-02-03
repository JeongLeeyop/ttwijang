package com.ttwijang.cms.api.mission.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ttwijang.cms.api.attached_file.dto.AttachedFileDto;
import com.ttwijang.cms.api.attached_file.dto.mapper.AttachedFileMapper;
import com.ttwijang.cms.api.attached_file.service.AttachedFileService;
import com.ttwijang.cms.api.mission.dto.MissionTemplateDto;
import com.ttwijang.cms.api.mission.dto.MissionTemplateSearch;
import com.ttwijang.cms.api.mission.service.AdmMissionTemplateService;
import com.ttwijang.cms.entity.Mission;
import com.ttwijang.cms.entity.MissionTemplate;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/mission/template")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@AllArgsConstructor
public class AdmMissionTemplateController {
    private final AdmMissionTemplateService admMissionTemplateService;
    private final AttachedFileService attachedFileService;

    @GetMapping("{uid}")
    public ResponseEntity<MissionTemplateDto.detail> detail(@AuthenticationPrincipal SinghaUser authUser,@PathVariable("uid") String uid) {
        return ResponseEntity.ok(admMissionTemplateService.detail(authUser, uid));
    }

    @GetMapping
    public ResponseEntity<Page<MissionTemplateDto.detail>> list(@AuthenticationPrincipal SinghaUser authUser, MissionTemplateSearch search, @PageableDefault(direction = Direction.DESC, sort = { "createDate" }) Pageable pageable) {
        return ResponseEntity.ok(admMissionTemplateService.list(authUser, search, pageable));
    }
    
    @PostMapping
    public ResponseEntity add(@AuthenticationPrincipal SinghaUser authUser, @RequestBody MissionTemplateDto.add addDto) {
        admMissionTemplateService.add(authUser, addDto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("{uid}")
    public ResponseEntity delete(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("uid") MissionTemplate mission) {
        admMissionTemplateService.delete(authUser, mission);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("{uid}")
    public ResponseEntity update(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("uid") MissionTemplate mission,@RequestBody MissionTemplateDto.update updateDto) {
        admMissionTemplateService.update(authUser, mission, updateDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
	@PostMapping("/upload")
	public ResponseEntity<AttachedFileDto.detail> fileUpload(MultipartFile file) {
		return ResponseEntity.ok(AttachedFileMapper.INSTANCE.entityToDetailDto(attachedFileService.save(file, "mission")));
	}
}

package com.weilyeat.cms.api.mission_record.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.weilyeat.cms.api.attached_file.dto.AttachedFileDto;
import com.weilyeat.cms.api.attached_file.dto.mapper.AttachedFileMapper;
import com.weilyeat.cms.api.attached_file.service.AttachedFileService;
import com.weilyeat.cms.api.mission_record.dto.MissionRecordDto;
import com.weilyeat.cms.api.mission_record.dto.MissionRecordSearch;
import com.weilyeat.cms.api.mission_record.service.MissionRecordService;
import com.weilyeat.cms.entity.MissionRecord;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/mission/record")
@AllArgsConstructor
public class MissionRecordController {
    private final MissionRecordService missionRecordService;
    private final AttachedFileService attachedFileService;
    
    @GetMapping("{uid}")
    public ResponseEntity<MissionRecordDto.detail> detail(@PathVariable("uid") String uid, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(missionRecordService.detail(uid));
    }

	@GetMapping
    public ResponseEntity<List<MissionRecordDto.list>> List(@AuthenticationPrincipal SinghaUser authUser, MissionRecordSearch search) {
        return ResponseEntity.ok(missionRecordService.List(search, authUser));
    }

    @PostMapping
	public ResponseEntity add(
			@AuthenticationPrincipal SinghaUser authUser,
			@Valid @RequestBody MissionRecordDto.add postAddDto) {
		return ResponseEntity.ok(missionRecordService.add(postAddDto, authUser));
	}

    @PutMapping("{uid}")
	public ResponseEntity update(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("uid") MissionRecord entity,
			@Valid @RequestBody MissionRecordDto.update updateDto) {
		return ResponseEntity.ok(missionRecordService.update(entity, updateDto, authUser));
	}

    @DeleteMapping("{uid}")
	public ResponseEntity delete(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("uid") String uid) {
		missionRecordService.delete(uid, authUser);
		return ResponseEntity.ok().build();
	}
 
    @PreAuthorize("permitAll()")
	@PostMapping("/upload")
	public ResponseEntity<AttachedFileDto.detail> fileUpload(MultipartFile file) {
		return ResponseEntity.ok(AttachedFileMapper.INSTANCE.entityToDetailDto(attachedFileService.save(file, "missionRecord")));
	}
}

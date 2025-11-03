package com.ttwijang.cms.api.mission_inquiry.controller;

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

import com.ttwijang.cms.api.attached_file.dto.AttachedFileDto;
import com.ttwijang.cms.api.attached_file.dto.mapper.AttachedFileMapper;
import com.ttwijang.cms.api.attached_file.service.AttachedFileService;
import com.ttwijang.cms.api.mission_inquiry.dto.MissionInquiryDto;
import com.ttwijang.cms.api.mission_inquiry.dto.MissionInquiryPageDto;
import com.ttwijang.cms.api.mission_inquiry.dto.MissionInquirySearch;
import com.ttwijang.cms.api.mission_inquiry.service.MissionInquiryService;
import com.ttwijang.cms.entity.MissionInquiry;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/mission/inquiry")
@AllArgsConstructor
public class MissionInquiryController {
    private final MissionInquiryService missionInquiryService;
    private final AttachedFileService attachedFileService;
    
    @GetMapping("{idx}")
    public ResponseEntity<MissionInquiryDto.detail> detail(@PathVariable("idx") Integer idx, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(missionInquiryService.detail(idx));
    }

	@GetMapping("/page/{pageNum}")
    public ResponseEntity<MissionInquiryPageDto.list> List(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("pageNum") Integer pageNum) {
        return ResponseEntity.ok(missionInquiryService.List(authUser, pageNum));
    }

	@GetMapping
    public ResponseEntity<List<MissionInquiryDto.list>> listPage(@AuthenticationPrincipal SinghaUser authUser, MissionInquirySearch search) {
        return ResponseEntity.ok(missionInquiryService.listPage(search, authUser));
    }

    @PostMapping
	public ResponseEntity add(
			@AuthenticationPrincipal SinghaUser authUser,
			@Valid @RequestBody MissionInquiryDto.add postAddDto) {
		return ResponseEntity.ok(missionInquiryService.add(postAddDto, authUser));
	}

    @PutMapping("{idx}")
	public ResponseEntity update(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("idx") MissionInquiry entity,
			@Valid @RequestBody MissionInquiryDto.update updateDto) {
		return ResponseEntity.ok(missionInquiryService.update(entity, updateDto, authUser));
	}

    @DeleteMapping("{idx}")
	public ResponseEntity delete(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("idx") Integer idx) {
		missionInquiryService.delete(idx, authUser);
		return ResponseEntity.ok().build();
	}
 
    @PreAuthorize("permitAll()")
	@PostMapping("/upload")
	public ResponseEntity<AttachedFileDto.detail> fileUpload(MultipartFile file) {
		return ResponseEntity.ok(AttachedFileMapper.INSTANCE.entityToDetailDto(attachedFileService.save(file, "missionInquiry")));
	}
}

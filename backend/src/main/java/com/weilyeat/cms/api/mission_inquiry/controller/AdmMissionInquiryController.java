package com.weilyeat.cms.api.mission_inquiry.controller;

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
import com.weilyeat.cms.api.mission_inquiry.dto.MissionInquiryDto;
import com.weilyeat.cms.api.mission_inquiry.dto.MissionInquiryPageDto;
import com.weilyeat.cms.api.mission_inquiry.dto.MissionInquirySearch;
import com.weilyeat.cms.api.mission_inquiry.service.MissionInquiryService;
import com.weilyeat.cms.entity.MissionInquiry;
import com.weilyeat.cms.entity.MissionInquiryPage;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/mission/inquiry")
@AllArgsConstructor
public class AdmMissionInquiryController {
    private final MissionInquiryService missionInquiryService;
    private final AttachedFileService attachedFileService;
    
    @GetMapping("{idx}")
    public ResponseEntity<MissionInquiryDto.detail> detail(@PathVariable("idx") Integer idx, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(missionInquiryService.detail(idx));
    }

	@GetMapping
    public ResponseEntity<List<MissionInquiryPageDto.list>> List(@AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(missionInquiryService.List(authUser));
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

	
    @PutMapping("/page/{idx}")
	public ResponseEntity updatePage(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("idx") MissionInquiryPage entity,
			@Valid @RequestBody MissionInquiryPageDto.update updateDto) {
		return ResponseEntity.ok(missionInquiryService.updatePage(entity, updateDto, authUser));
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

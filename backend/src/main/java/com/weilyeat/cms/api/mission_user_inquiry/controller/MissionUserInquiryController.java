package com.weilyeat.cms.api.mission_user_inquiry.controller;

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
import com.weilyeat.cms.api.mission_user_inquiry.dto.MissionUserInquiryDto;
import com.weilyeat.cms.api.mission_user_inquiry.dto.MissionUserInquirySearch;
import com.weilyeat.cms.api.mission_user_inquiry.service.MissionUserInquiryService;
import com.weilyeat.cms.entity.MissionUserInquiry;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/mission/user/inquiry")
@AllArgsConstructor
public class MissionUserInquiryController {
    private final MissionUserInquiryService missionUserInquiryService;
    private final AttachedFileService attachedFileService;
    
    @GetMapping("{userUid}")
    public ResponseEntity<List<MissionUserInquiryDto.detail>> detail(@PathVariable("userUid") String userUid, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(missionUserInquiryService.detail(userUid));
    }

	@GetMapping
    public ResponseEntity<List<MissionUserInquiryDto.list>> List(@AuthenticationPrincipal SinghaUser authUser, MissionUserInquirySearch search) {
        return ResponseEntity.ok(missionUserInquiryService.List(search, authUser));
    }

    @PostMapping
	public ResponseEntity add(
			@AuthenticationPrincipal SinghaUser authUser,
			@Valid @RequestBody List<MissionUserInquiryDto.add> postAddDto) {
		missionUserInquiryService.add(postAddDto, authUser);
		return ResponseEntity.ok().build();
	}

    @PutMapping("{idx}")
	public ResponseEntity update(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("idx") MissionUserInquiry entity,
			@Valid @RequestBody MissionUserInquiryDto.update updateDto) {
		return ResponseEntity.ok(missionUserInquiryService.update(entity, updateDto, authUser));
	}

    @DeleteMapping("{idx}")
	public ResponseEntity delete(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("idx") Integer idx) {
		missionUserInquiryService.delete(idx, authUser);
		return ResponseEntity.ok().build();
	}
}

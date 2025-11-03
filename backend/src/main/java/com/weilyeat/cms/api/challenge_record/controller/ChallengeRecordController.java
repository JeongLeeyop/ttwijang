package com.weilyeat.cms.api.challenge_record.controller;

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
import com.weilyeat.cms.api.challenge_record.dto.ChallengeRecordDto;
import com.weilyeat.cms.api.challenge_record.dto.ChallengeRecordSearch;
import com.weilyeat.cms.api.challenge_record.service.ChallengeRecordService;
import com.weilyeat.cms.entity.ChallengeRecord;
import com.weilyeat.cms.entity.Post;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/challenge/record")
@AllArgsConstructor
public class ChallengeRecordController {
    private final ChallengeRecordService challengeRecordService;
    private final AttachedFileService attachedFileService;
    
    @GetMapping("{uid}")
    public ResponseEntity<ChallengeRecordDto.detail> detail(@PathVariable("uid") String uid, @AuthenticationPrincipal SinghaUser authUser) {
        return ResponseEntity.ok(challengeRecordService.detail(uid));
    }

	@GetMapping
    public ResponseEntity<List<ChallengeRecordDto.list>> List(@AuthenticationPrincipal SinghaUser authUser, ChallengeRecordSearch search) {
        return ResponseEntity.ok(challengeRecordService.List(search, authUser));
    }

    @PostMapping
	public ResponseEntity add(
			@AuthenticationPrincipal SinghaUser authUser,
			@Valid @RequestBody ChallengeRecordDto.add postAddDto) {
		return ResponseEntity.ok(challengeRecordService.add(postAddDto, authUser));
	}

    @PutMapping("{uid}")
	public ResponseEntity update(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("uid") ChallengeRecord entity,
			@Valid @RequestBody ChallengeRecordDto.update updateDto) {
		return ResponseEntity.ok(challengeRecordService.update(entity, updateDto, authUser));
	}

    @DeleteMapping("{uid}")
	public ResponseEntity delete(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("uid") String uid) {
		challengeRecordService.delete(uid, authUser);
		return ResponseEntity.ok().build();
	}
 
    @PreAuthorize("permitAll()")
	@PostMapping("/upload")
	public ResponseEntity<AttachedFileDto.detail> fileUpload(MultipartFile file) {
		return ResponseEntity.ok(AttachedFileMapper.INSTANCE.entityToDetailDto(attachedFileService.save(file, "challengeRecord")));
	}
}

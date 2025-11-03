package com.ttwijang.cms.api.challenge.controller;

import java.util.List;

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
import com.ttwijang.cms.api.challenge.dto.ChallengeDto;
import com.ttwijang.cms.api.challenge.dto.ChallengeSearch;
import com.ttwijang.cms.api.challenge.service.AdmChallengeService;
import com.ttwijang.cms.api.challenge.service.ChallengeService;
import com.ttwijang.cms.entity.Challenge;
import com.ttwijang.cms.entity.Post;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/challenge")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@AllArgsConstructor
public class AdmChallengeController {
    private final AdmChallengeService admChallengeService;
    private final AttachedFileService attachedFileService;

    @GetMapping("{uid}")
    public ResponseEntity<ChallengeDto.detail> detail(@AuthenticationPrincipal SinghaUser authUser,@PathVariable("uid") String uid) {
        return ResponseEntity.ok(admChallengeService.detail(authUser, uid));
    }

    @GetMapping
    public ResponseEntity<Page<ChallengeDto.detail>> list(@AuthenticationPrincipal SinghaUser authUser, ChallengeSearch search, @PageableDefault(direction = Direction.DESC, sort = { "createDate" }) Pageable pageable) {
        return ResponseEntity.ok(admChallengeService.list(authUser, search, pageable));
    }
    
    @PostMapping
    public ResponseEntity add(@AuthenticationPrincipal SinghaUser authUser, @RequestBody ChallengeDto.add addDto) {
        admChallengeService.add(authUser, addDto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("{uid}")
    public ResponseEntity delete(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("uid") Challenge challenge) {
        admChallengeService.delete(authUser, challenge);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("{uid}")
    public ResponseEntity update(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("uid") Challenge challenge,@RequestBody ChallengeDto.update updateDto) {
        admChallengeService.update(authUser, challenge, updateDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
	@PostMapping("/upload")
	public ResponseEntity<AttachedFileDto.detail> fileUpload(MultipartFile file) {
		return ResponseEntity.ok(AttachedFileMapper.INSTANCE.entityToDetailDto(attachedFileService.save(file, "challenge")));
	}
    
    // @PutMapping
    // public ResponseEntity updateSecretStatus(@AuthenticationPrincipal SinghaUser authUser, @RequestBody ChallengeDto.updateSecretStatus updateDto) {
    //     admChallengeService.updateSecretStatus(authUser, updateDto);
    //     return ResponseEntity.ok().build();
    // }
}

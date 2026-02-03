package com.ttwijang.cms.api.tfse.controller;

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

import com.ttwijang.cms.api.tfse.dto.TfseDto;
import com.ttwijang.cms.api.tfse.dto.TfseSearch;
import com.ttwijang.cms.api.tfse.service.TfseService;
import com.ttwijang.cms.entity.Tfse;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BOARD')")
@RequestMapping("/api/tfse")
@AllArgsConstructor
public class TfseController {
    private final TfseService TfseService;

    @GetMapping("{idx}/view")
    public ResponseEntity<TfseDto.detail> detail(@AuthenticationPrincipal SinghaUser authUser,@PathVariable("idx") Long idx) {
        return ResponseEntity.ok(TfseService.detail(authUser, idx));
    }
    
    @GetMapping
    public ResponseEntity<Page<TfseDto.list>> list(@AuthenticationPrincipal SinghaUser authUser, TfseSearch search, @PageableDefault(
					size=10,
					page=0,
					direction = Direction.DESC,
					sort = {"createDate"}) Pageable pageable) {
        return ResponseEntity.ok(TfseService.list(authUser, search, pageable));
    }

    @PostMapping
    public ResponseEntity add(@AuthenticationPrincipal SinghaUser authUser, @RequestBody TfseDto.add addDto) {
        TfseService.add(authUser, addDto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("{idx}")
    public ResponseEntity delete(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("idx") Tfse tfse) {
        TfseService.delete(authUser, tfse);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("{idx}")
    public ResponseEntity update(@AuthenticationPrincipal SinghaUser authUser, @PathVariable("idx") Tfse tfse,@RequestBody TfseDto.update updateDto) {
        TfseService.update(authUser, tfse, updateDto);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping
    public ResponseEntity updateSecretStatus(@AuthenticationPrincipal SinghaUser authUser, @RequestBody TfseDto.updateSecretStatus updateDto) {
        TfseService.updateSecretStatus(authUser, updateDto);
        return ResponseEntity.ok().build();
    }
}

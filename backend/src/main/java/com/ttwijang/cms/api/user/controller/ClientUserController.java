package com.ttwijang.cms.api.user.controller;

import javax.validation.Valid;

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

import com.ttwijang.cms.api.user.dto.ClientUserDto;
import com.ttwijang.cms.api.user.service.ClientUserService;
import com.ttwijang.cms.api.user.service.UserService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/user")
@AllArgsConstructor
public class ClientUserController {
	
	private final UserService userService;
	private final ClientUserService clientUserService;

	@GetMapping("info")
	public ResponseEntity<ClientUserDto.info> info(@AuthenticationPrincipal SinghaUser authUser) {
		return ResponseEntity.ok(clientUserService.info(authUser));
	}
	
	@GetMapping("/id-check/{userId}")
    public ResponseEntity<Boolean> userIdCheck(
    		@PathVariable("userId") String userId) {
		boolean isExistId = userService.userIdCheck(userId);
        return ResponseEntity.ok(isExistId);
    }
	
	@PostMapping("join")
	public ResponseEntity join(@Valid @RequestBody ClientUserDto.join dto, @AuthenticationPrincipal SinghaUser authUser) {
		clientUserService.join(dto, authUser);
		return ResponseEntity.ok().build();
	}

	@PutMapping("info")
	public ResponseEntity updateInfo(@RequestBody ClientUserDto.update updateDto, @AuthenticationPrincipal SinghaUser authUser) {
		clientUserService.updateInfo(updateDto, authUser);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("withdraw")
	public ResponseEntity withdraw(@RequestBody ClientUserDto.withdraw dto, @AuthenticationPrincipal SinghaUser authUser) {
		clientUserService.withdraw(dto, authUser);
		return ResponseEntity.ok().build();
	}
}

package com.ttwijang.cms.api.user.controller;

import java.util.HashMap;
import java.util.Map;

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
	public ResponseEntity join(@Valid @RequestBody ClientUserDto.join dto) {
		clientUserService.join(dto);
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

	/**
	 * 휴대폰 번호 중복 확인
	 */
	@GetMapping("/phone-check/{phoneNumber}")
	public ResponseEntity<Map<String, Object>> phoneNumberCheck(@PathVariable("phoneNumber") String phoneNumber) {
		boolean isDuplicate = clientUserService.checkPhoneNumberDuplicate(phoneNumber);
		Map<String, Object> response = new HashMap<>();
		response.put("isDuplicate", isDuplicate);
		response.put("message", isDuplicate ? "이미 해당 휴대폰 번호로 가입된 계정이 존재합니다." : "사용 가능한 휴대폰 번호입니다.");
		return ResponseEntity.ok(response);
	}

	/**
	 * 아이디(이메일) 찾기
	 */
	@PostMapping("/find-email")
	public ResponseEntity<Map<String, String>> findEmail(@RequestBody Map<String, String> request) {
		String phoneNumber = request.get("phoneNumber");
		String email = clientUserService.findEmailByPhoneNumber(phoneNumber);
		
		Map<String, String> response = new HashMap<>();
		response.put("email", email);
		return ResponseEntity.ok(response);
	}

	/**
	 * 비밀번호 재설정 요청
	 */
	@PostMapping("/password/reset-request")
	public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String phoneNumber = request.get("phoneNumber");
		
		String resetToken = clientUserService.requestPasswordReset(email, phoneNumber);
		
		Map<String, String> response = new HashMap<>();
		response.put("resetToken", resetToken);
		response.put("message", "비밀번호 재설정이 가능합니다.");
		return ResponseEntity.ok(response);
	}

	/**
	 * 비밀번호 재설정
	 */
	@PostMapping("/password/reset")
	public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
		String resetToken = request.get("resetToken");
		String newPassword = request.get("newPassword");
		
		clientUserService.resetPassword(resetToken, newPassword);
		
		Map<String, String> response = new HashMap<>();
		response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
		return ResponseEntity.ok(response);
	}
}

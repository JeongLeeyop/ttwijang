package com.ttwijang.cms.controller;

import com.ttwijang.cms.common.ApiResponse;
import com.ttwijang.cms.dto.sms.SmsRequest;
import com.ttwijang.cms.dto.sms.SmsResponse;
import com.ttwijang.cms.dto.sms.SmsVerifyRequest;
import com.ttwijang.cms.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * SMS 인증 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/client/sms")
@RequiredArgsConstructor
@Tag(name = "SMS 인증", description = "SMS 인증번호 전송 및 검증 API")
public class SmsController {
    
    private final SmsService smsService;
    
    /**
     * 인증번호 전송
     */
    @PostMapping("/send")
    @Operation(summary = "인증번호 전송", description = "휴대폰 번호로 6자리 인증번호를 전송합니다.")
    public ResponseEntity<ApiResponse<SmsResponse>> sendVerificationCode(
            @Valid @RequestBody SmsRequest request) {
        
        log.info("인증번호 전송 요청 - 전화번호: {}", request.getPhoneNumber());
        
        try {
            SmsResponse response = smsService.sendVerificationCode(request.getPhoneNumber());
            
            log.info("SMS 응답 - resultCode: {}, message: {}", 
                     response.getResultCode(), response.getMessage());
            
            // 전송 성공 여부 확인 (resultCode가 1 이상이거나, testmode에서 successCnt > 0)
            boolean isSuccess = (response.getResultCode() != null && response.getResultCode() >= 1) ||
                               (response.getSuccessCnt() != null && response.getSuccessCnt() > 0);
            
            if (isSuccess) {
                return ResponseEntity.ok(ApiResponse.success(response, "인증번호가 전송되었습니다."));
            } else {
                String errorMsg = response.getMessage() != null ? response.getMessage() : "인증번호 전송에 실패했습니다.";
                log.warn("SMS 전송 실패 - resultCode: {}, message: {}", 
                         response.getResultCode(), errorMsg);
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMsg));
            }
            
        } catch (Exception e) {
            log.error("인증번호 전송 중 오류 발생", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("인증번호 전송 중 오류가 발생했습니다."));
        }
    }
    
    /**
     * 인증번호 검증
     */
    @PostMapping("/verify")
    @Operation(summary = "인증번호 검증", description = "전송된 인증번호가 올바른지 검증합니다.")
    public ResponseEntity<ApiResponse<Boolean>> verifyCode(
            @Valid @RequestBody SmsVerifyRequest request) {
        
        log.info("인증번호 검증 요청 - 전화번호: {}, 인증번호: {}", 
                 request.getPhoneNumber(), request.getVerificationCode());
        
        try {
            boolean isValid = smsService.verifyCode(
                request.getPhoneNumber(), 
                request.getVerificationCode()
            );
            
            if (isValid) {
                return ResponseEntity.ok(ApiResponse.success(true, "인증이 완료되었습니다."));
            } else {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("인증번호가 일치하지 않거나 만료되었습니다."));
            }
            
        } catch (Exception e) {
            log.error("인증번호 검증 중 오류 발생", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("인증번호 검증 중 오류가 발생했습니다."));
        }
    }
}

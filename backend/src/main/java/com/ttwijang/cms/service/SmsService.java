package com.ttwijang.cms.service;

import com.ttwijang.cms.config.SmsConfig;
import com.ttwijang.cms.dto.sms.SmsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * SMS 전송 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {
    
    private final SmsConfig smsConfig;
    private final StringRedisTemplate redisTemplate;
    private final WebClient webClient;
    
    private static final String VERIFICATION_CODE_PREFIX = "SMS_VERIFY:";
    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final int VERIFICATION_CODE_EXPIRATION_MINUTES = 3;
    private static final SecureRandom RANDOM = new SecureRandom();
    
    /**
     * 인증번호 SMS 전송
     */
    public SmsResponse sendVerificationCode(String phoneNumber) {
        // 하이픈 제거
        String cleanPhoneNumber = phoneNumber.replaceAll("-", "");
        
        // 인증번호 생성 (6자리)
        String verificationCode = generateVerificationCode();
        
        // Redis에 인증번호 저장 (3분 유효)
        String redisKey = VERIFICATION_CODE_PREFIX + cleanPhoneNumber;
        redisTemplate.opsForValue().set(
            redisKey, 
            verificationCode, 
            VERIFICATION_CODE_EXPIRATION_MINUTES, 
            TimeUnit.MINUTES
        );
        
        log.info("인증번호 생성 - 전화번호: {}, 인증번호: {}, Redis Key: {}", 
                 cleanPhoneNumber, verificationCode, redisKey);
        
        // SMS 메시지 내용
        String message = String.format(
            "[뛰장] 인증번호는 [%s]입니다. %d분 이내에 입력해주세요.",
            verificationCode,
            VERIFICATION_CODE_EXPIRATION_MINUTES
        );
        
        // 알리고 SMS 전송
        return sendSms(cleanPhoneNumber, message);
    }
    
    /**
     * 인증번호 검증
     */
    public boolean verifyCode(String phoneNumber, String code) {
        // 하이픈 제거
        String cleanPhoneNumber = phoneNumber.replaceAll("-", "");
        
        // Redis에서 인증번호 조회
        String redisKey = VERIFICATION_CODE_PREFIX + cleanPhoneNumber;
        String savedCode = redisTemplate.opsForValue().get(redisKey);
        
        log.info("인증번호 검증 - 전화번호: {}, 입력코드: {}, 저장코드: {}", 
                 cleanPhoneNumber, code, savedCode);
        
        // 인증번호 일치 확인
        if (savedCode != null && savedCode.equals(code)) {
            // 인증 성공 시 Redis에서 삭제
            redisTemplate.delete(redisKey);
            log.info("인증번호 검증 성공 - 전화번호: {}", cleanPhoneNumber);
            return true;
        }
        
        log.warn("인증번호 검증 실패 - 전화번호: {}", cleanPhoneNumber);
        return false;
    }
    
    /**
     * 알리고 SMS API 호출
     */
    private SmsResponse sendSms(String receiver, String message) {
        try {
            // 요청 파라미터 구성
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("key", smsConfig.getApiKey());
            formData.add("user_id", smsConfig.getUserId());
            formData.add("sender", smsConfig.getSender());
            formData.add("receiver", receiver);
            formData.add("msg", message);
            formData.add("msg_type", "SMS");
            formData.add("testmode_yn", smsConfig.getTestmode());
            
            log.info("SMS 전송 요청 - receiver: {}, sender: {}, testmode: {}", 
                     receiver, smsConfig.getSender(), smsConfig.getTestmode());
            log.debug("SMS 전송 파라미터 - formData: {}", formData);
            
            // 알리고 API 호출 (JSON 또는 String 응답 처리)
            String responseBody = webClient.post()
                .uri(smsConfig.getApiUrl() + "/send/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
            
            log.info("SMS 전송 원본 응답: {}", responseBody);
            
            // HTML 응답인 경우 (에러)
            if (responseBody != null && responseBody.trim().startsWith("<")) {
                log.error("SMS API 호출 실패 - HTML 응답 수신: {}", 
                          responseBody.substring(0, Math.min(200, responseBody.length())));
                return SmsResponse.builder()
                    .resultCode(-998)
                    .message("SMS API 호출 실패: 잘못된 요청 파라미터")
                    .build();
            }
            
            // JSON 응답 파싱
            if (responseBody != null && responseBody.contains("{")) {
                // 간단한 JSON 파싱 (Jackson 사용 가능하면 ObjectMapper 사용)
                SmsResponse response = parseJsonResponse(responseBody);
                
                if (response != null) {
                    log.info("SMS 전송 응답 - resultCode: {}, message: {}, msgId: {}", 
                             response.getResultCode(), response.getMessage(), response.getMsgId());
                    
                    if (response.getResultCode() != null && response.getResultCode() < 0) {
                        log.error("SMS 전송 실패 - resultCode: {}, message: {}", 
                                  response.getResultCode(), response.getMessage());
                    }
                    return response;
                }
            }
            
            // 파싱 실패
            return SmsResponse.builder()
                .resultCode(-997)
                .message("SMS API 응답 파싱 실패")
                .build();
            
        } catch (Exception e) {
            log.error("SMS 전송 중 오류 발생 - receiver: {}, error: {}", receiver, e.getMessage(), e);
            return SmsResponse.builder()
                .resultCode(-999)
                .message("SMS 전송 중 오류가 발생했습니다: " + e.getMessage())
                .build();
        }
    }
    
    /**
     * JSON 응답 간단 파싱
     */
    private SmsResponse parseJsonResponse(String json) {
        try {
            // 간단한 정규식 파싱 (production에서는 ObjectMapper 사용 권장)
            Integer resultCode = extractIntValue(json, "result_code");
            String message = extractStringValue(json, "message");
            String msgId = extractStringValue(json, "msg_id");
            Integer successCnt = extractIntValue(json, "success_cnt");
            Integer errorCnt = extractIntValue(json, "error_cnt");
            String msgType = extractStringValue(json, "msg_type");
            
            return SmsResponse.builder()
                .resultCode(resultCode)
                .message(message)
                .msgId(msgId)
                .successCnt(successCnt)
                .errorCnt(errorCnt)
                .msgType(msgType)
                .build();
        } catch (Exception e) {
            log.error("JSON 파싱 실패: {}", e.getMessage());
            return null;
        }
    }
    
    private Integer extractIntValue(String json, String key) {
        try {
            String pattern = "\"" + key + "\"\\s*:\\s*(-?\\d+)";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        } catch (Exception e) {
            log.debug("Int 값 추출 실패 - key: {}", key);
        }
        return null;
    }
    
    private String extractStringValue(String json, String key) {
        try {
            String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(json);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            log.debug("String 값 추출 실패 - key: {}", key);
        }
        return null;
    }
    
    /**
     * 6자리 인증번호 생성
     */
    private String generateVerificationCode() {
        int code = RANDOM.nextInt(1000000); // 0 ~ 999999
        return String.format("%06d", code); // 6자리로 포맷 (앞자리 0 포함)
    }
}

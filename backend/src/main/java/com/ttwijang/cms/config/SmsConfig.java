package com.ttwijang.cms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SMS 알리고 API 설정
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "sms.aligo")
public class SmsConfig {
    
    /**
     * 알리고 API URL
     */
    private String apiUrl;
    
    /**
     * 알리고 API Key
     */
    private String apiKey;
    
    /**
     * 알리고 사용자 ID
     */
    private String userId;
    
    /**
     * 발신번호
     */
    private String sender;
    
    /**
     * 테스트 모드 (Y/N)
     */
    private String testmode;
}

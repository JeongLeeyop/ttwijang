package com.weilyeat.cms.oauth.soical.naver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class NaverApi {

    private static final String HOST = "https://nid.naver.com";

    @Value("${spring.config.naver-client-id}")
    private String naverClientId;

    @Value("${spring.config.naver-client-secret}")
    private String naverClientSecret;

    @Value("${spring.profiles}")
    private String profile;
    

    public ResponseEntity me(String access_token) {
        RestTemplate template = new RestTemplate();

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<HashMap<String, Object>> responseType =
                new ParameterizedTypeReference<HashMap<String, Object>>() {};

        return template.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, entity, responseType);
    }
    
    public ResponseEntity token(String code) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(HOST+"/oauth2.0/token")
            .queryParam("code", code)
            .queryParam("grant_type", "authorization_code")
            .queryParam("client_id", naverClientId)
            .queryParam("client_secret", naverClientSecret)
            // .queryParam("redirect_uri", profile.equals("development") ? "http://leeyop.unids.kr/oauth/token" : "https://weilyeat.co.kr/oauth/token")
            .queryParam("redirect_uri", profile.equals("development") ? "http://localhost:3000/oauth/token" : "https://weilyeat.co.kr/oauth/token")
            .queryParam("state", "state");
        String url = builder.toUriString();
        RestTemplate template = new RestTemplate();
        
        HttpHeaders headers  = new HttpHeaders();
        HttpEntity entity = new HttpEntity<>(headers); 
        
        ParameterizedTypeReference<HashMap<String, Object>> responseType =
        new ParameterizedTypeReference<HashMap<String, Object>>() {};

        // ResponseEntity<HashMap<String, Object>> response = template.exchange(url, HttpMethod.POST, entity, responseType);
        return template.exchange(url, HttpMethod.POST, entity, responseType);
        // return access(response.getBody().get("access_token").toString());
    }

    public ResponseEntity access(String access) {
        RestTemplate template = new RestTemplate();
        
        String url = profile.equals("development") ? "http://localhost:8080/oauth/token" : "http://localhost:8080/oauth/token";
        // String url = profile.equals("development") ? "http://leeyop.unids.kr:8080/oauth/token" : "http://weilyeat.co.kr/oauth/token";
        
        HttpHeaders headers  = new HttpHeaders();
        headers.add("authorization", "Basic c2luZ2hhX29hdXRoOnNpbmdoYXNjcmVjdCFAIyQ=");
        headers.add("x-auth-token", "Naver "+access);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity entity = new HttpEntity<>(headers);
        
        ParameterizedTypeReference<HashMap<String, Object>> responseType =
        new ParameterizedTypeReference<HashMap<String, Object>>() {};

        // ResponseEntity<HashMap<String, Object>> response = me(access);
        // String userInfo = response.getBody().get("response").toString();
        // String userInfo = response.getBody();

        ResponseEntity<HashMap<String, Object>> response2 = template.exchange(url, HttpMethod.POST, entity, responseType);
        // HashMap<String, Object> responseBody = response2.getBody();
        // responseBody.put("userInfo", userInfo);
        return response2;
    }
}

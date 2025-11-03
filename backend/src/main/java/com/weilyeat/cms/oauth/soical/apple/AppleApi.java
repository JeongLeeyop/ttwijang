package com.weilyeat.cms.oauth.soical.apple;

import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.weilyeat.cms.common.exception.BadRequestException;
import com.weilyeat.cms.common.exception.code.BadRequest;
import com.weilyeat.cms.oauth.soical.apple.dto.AppleLoginInfo;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AppleApi {
    @Value("${spring.profiles}")
    private String profile;

    public static final String TEAM_ID = "PDTCUY63XW";
	// public static final String REDIRECT_URL = "https://127.0.0.1/api/apple/login";
    public static final String CLIENT_ID = "unids.weilyeat";
	public static final String KEY_ID = "Y75R25GR55";
    public static final String AUTH_URL = "https://appleid.apple.com";
	public static final String KEY_PATH = "lib/AuthKey_Y75R25GR55.p8";

    public ResponseEntity<HashMap<String, Object>> me(String access_token) {
        RestTemplate template = new RestTemplate();

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<HashMap<String, Object>> responseType =
                new ParameterizedTypeReference<HashMap<String, Object>>() {};
        return template.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, entity, responseType);
    }
    
    public ResponseEntity token(AppleLoginInfo dto) {
        String[] check = dto.getIdToken().split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(check[1]));
    
        JSONObject jsonObject = new JSONObject(payload);
        
        long currentTime = System.currentTimeMillis() / 1000;
        String aud = jsonObject.getString("aud");
        long exp = jsonObject.getLong("exp");

        if (!aud.equals(CLIENT_ID)) throw new BadRequestException(BadRequest.NOT_VALID_ID_TOKEN);
        if (currentTime > exp) throw new BadRequestException(BadRequest.EXPIRED_ID_TOKEN);
        /*
        PrivateKey privateKey = null;
        JcaPEMKeyConverter converter = null;

        try {
            ClassPathResource resource = new ClassPathResource(KEY_PATH);
            byte[] resourceBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            Reader pemReader = new StringReader(new String(resourceBytes));
            PEMParser pemParser = new PEMParser(pemReader);
            converter = new JcaPEMKeyConverter();
            privateKey = (PrivateKey) converter.getPrivateKey((PrivateKeyInfo) pemParser.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }

        long currentTime = System.currentTimeMillis();

        
        JwtBuilder jwtBuilder = Jwts.builder()
                .setHeaderParam("alg", "ES256")
                .setHeaderParam("kid", KEY_ID)
                .setIssuer(TEAM_ID)
                .setSubject(CLIENT_ID)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + 60 * 5 * 1000))  // 5분 후 만료
                .setAudience("https://appleid.apple.com")
                .signWith(privateKey, SignatureAlgorithm.ES256);

        String clientSecret = jwtBuilder.compact();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(AUTH_URL+"/auth/token")
            .queryParam("client_id", CLIENT_ID)
            .queryParam("client_secret", clientSecret)
            .queryParam("code", code)
            .queryParam("grant_type", "authorization_code")
            .queryParam("redirect_uri", profile.equals("development") ? "http://localhost:3000/oauth/token" : "https://weilyeat.go.kr/oauth/token");
        String url = builder.toUriString();
        
        RestTemplate template = new RestTemplate();
        
        HttpHeaders headers  = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity entity = new HttpEntity<>(headers);

        ParameterizedTypeReference<HashMap<String, Object>> responseType =
        new ParameterizedTypeReference<HashMap<String, Object>>() {};
        
        ResponseEntity<HashMap<String, Object>> response = template.exchange(url, HttpMethod.POST, entity, responseType);
        */
        return access(dto.getIdToken());
    }

    public ResponseEntity access(String access) {
        RestTemplate template = new RestTemplate();
        
        String url = profile.equals("development") ? "http://localhost:8080/oauth/token" : "https://weilyeat.co.kr/oauth/token";
        // String url = profile.equals("development") ? "ttp://leeyop.unids.kr:8080/oauth/token" : "https://weilyeat.co.kr/oauth/token";
        
        HttpHeaders headers  = new HttpHeaders();
        headers.add("authorization", "Basic c2luZ2hhX29hdXRoOnNpbmdoYXNjcmVjdCFAIyQ=");
        headers.add("x-auth-token", "Apple "+ access);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity entity = new HttpEntity<>(headers);
        
        ParameterizedTypeReference<HashMap<String, Object>> responseType =
        new ParameterizedTypeReference<HashMap<String, Object>>() {};
        
        return template.exchange(url, HttpMethod.POST, entity, responseType);

    }

    private byte[] readPrivateKey(String keyPath) {
        Resource resource = new ClassPathResource(keyPath);
        byte[] content = null;
        try (FileReader keyReader = new FileReader(resource.getFile());
             PemReader pemReader = new PemReader(keyReader)) {
            {
                PemObject pemObject = pemReader.readPemObject();
                content = pemObject.getContent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}

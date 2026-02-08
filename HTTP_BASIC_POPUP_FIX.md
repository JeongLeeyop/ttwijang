# 로그인 HTTP Basic 팝업 문제 해결 가이드

## 문제 상황
로그인 버튼을 누르면 Spring Security 기본 HTTP Basic Authentication 팝업창이 표시되는 문제

## 원인 분석

### 1. HTTP Basic Authentication 활성화
Spring Security가 기본적으로 HTTP Basic Authentication을 활성화하여 모든 요청에 대해 인증 팝업을 표시

### 2. OAuth 엔드포인트 미허용
`/oauth/token` 엔드포인트가 인증 필요로 설정되어 있어 로그인 시도 시 팝업 발생

### 3. Client Secret 불일치
- 프론트엔드: `singhascrect!@#$`
- 백엔드 DB: `secret` (초기 설정)

## 해결 방법

### ✅ 1. SecurityConfig.java 수정
HTTP Basic Authentication 비활성화 및 OAuth 엔드포인트 허용

```java
@Override
public void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()  // ✅ HTTP Basic 비활성화
        .csrf().disable()       // ✅ CSRF 비활성화
        .authorizeRequests()
        .antMatchers("/oauth/**").permitAll()  // ✅ OAuth 엔드포인트 허용
        .antMatchers("/api/client/**").permitAll()
        .antMatchers("/swagger-ui/**").permitAll()
        .anyRequest().permitAll();
}
```

### ✅ 2. ResourceServerConfig.java 수정
OAuth 엔드포인트 허용 및 HTTP Basic 비활성화

```java
@Override
public void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()  // ✅ HTTP Basic 비활성화
        .csrf().disable()       // ✅ CSRF 비활성화
        .headers().frameOptions().disable()
        .and()
        .authorizeRequests()
        .antMatchers("/oauth/**").permitAll()  // ✅ 모든 OAuth 엔드포인트 허용
        .antMatchers("/api/client/**").permitAll()
        .antMatchers("/swagger-ui/**").permitAll()
        .anyRequest().authenticated();
}
```

### ✅ 3. AuthServerConfig.java 수정
Client Secret 인증 개선

```java
@Autowired
private PasswordEncoder passwordEncoder;

@Override
public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
    configurer.jdbc(dataSourceOAuth).passwordEncoder(passwordEncoder);
}

@Override
public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()")
        .allowFormAuthenticationForClients();  // ✅ Form 기반 인증 허용
}
```

### ✅ 4. PasswordEncoder 수정
NoOp과 BCrypt 모두 지원

```java
@Bean
public PasswordEncoder passwordEncoder() {
    // {noop}, {bcrypt} 등 다양한 인코딩 방식 지원
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```

### ✅ 5. OAuth Client Secret 동기화
DB 마이그레이션 파일에서 프론트엔드와 일치하도록 수정

```sql
INSERT INTO oauth_client_details (
    client_id,
    client_secret,
    ...
) VALUES (
    'singha_oauth',
    '{noop}singhascrect!@#$',  -- ✅ 프론트엔드와 동일한 값
    ...
);
```

## 테스트 방법

### 1. 애플리케이션 재시작
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 2. 브라우저에서 테스트
1. 프론트엔드 접속: http://localhost:3000
2. 로그인 버튼 클릭
3. ✅ HTTP Basic 팝업이 나타나지 않고 일반 로그인 폼만 표시되어야 함

### 3. Swagger UI에서 테스트
1. http://localhost:8080/swagger-ui.html 접속
2. `/oauth/token` 엔드포인트 찾기
3. "Try it out" 클릭
4. Parameters 입력:
   ```
   grant_type: password
   username: user@example.com
   password: yourpassword
   ```
5. Headers (Authorization 추가):
   ```
   Authorization: Basic c2luZ2hhX29hdXRoOnNpbmdoYXNjcmVjdCFAIyQ=
   ```
6. Execute 클릭
7. ✅ 200 OK 응답 및 토큰 발급 확인

### 4. cURL로 테스트
```bash
curl -X POST http://localhost:8080/oauth/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic c2luZ2hhX29hdXRoOnNpbmdoYXNjcmVjdCFAIyQ=" \
  -d "grant_type=password" \
  -d "username=user@example.com" \
  -d "password=yourpassword"
```

예상 응답:
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "bearer",
  "refresh_token": "...",
  "expires_in": 86400,
  "scope": "read write",
  "actualName": "홍길동",
  "status": true
}
```

## Authorization Header 생성 방법

프론트엔드에서 사용하는 Basic Authorization Header는 다음과 같이 생성됩니다:

```javascript
// client_id:client_secret를 Base64 인코딩
const credentials = 'singha_oauth:singhascrect!@#$';
const encoded = btoa(credentials);
const authHeader = `Basic ${encoded}`;
// 결과: "Basic c2luZ2hhX29hdXRoOnNpbmdoYXNjcmVjdCFAIyQ="
```

## 프론트엔드 코드 확인

[client/src/api/oauth.ts](client/src/api/oauth.ts):
```typescript
// OAuth2 Basic Authorization Header
const OAUTH_AUTH_HEADER = 'Basic c2luZ2hhX29hdXRoOnNpbmdoYXNjcmVjdCFAIyQ=';

export const emailLogin = (email: string, password: string) => {
  const data = qs.stringify({
    grant_type: 'password',
    username: email,
    password,
  });

  return axios({
    url: '/oauth/token',
    method: 'post',
    headers: {
      Authorization: OAUTH_AUTH_HEADER,
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    data,
  });
};
```

## 트러블슈팅

### 문제: 여전히 HTTP Basic 팝업이 나타남
**해결:**
1. 브라우저 캐시 삭제
2. 애플리케이션 완전 재시작
3. SecurityConfig와 ResourceServerConfig가 제대로 수정되었는지 확인

### 문제: "Invalid client"
**해결:**
```sql
-- oauth_client_details 테이블 확인
SELECT client_id, client_secret FROM oauth_client_details;

-- client_secret 업데이트
UPDATE oauth_client_details 
SET client_secret = '{noop}singhascrect!@#$' 
WHERE client_id = 'singha_oauth';
```

### 문제: "Unauthorized" (401)
**해결:**
- Authorization 헤더가 올바른지 확인
- client_id와 client_secret이 일치하는지 확인
- `/oauth/token` 엔드포인트가 `permitAll()`로 설정되었는지 확인

### 문제: CORS 에러
**해결:**
프론트엔드 개발 서버의 프록시 설정 확인 ([client/vue.config.js](client/vue.config.js))

## 보안 고려사항

### 운영 환경 설정

1. **Client Secret 강화**
   ```sql
   -- BCrypt로 암호화된 강력한 secret 사용
   UPDATE oauth_client_details 
   SET client_secret = '{bcrypt}$2a$10$...' 
   WHERE client_id = 'singha_oauth';
   ```

2. **HTTPS 사용**
   - 운영 환경에서는 반드시 HTTPS 사용
   - HTTP Basic Auth는 Base64 인코딩만 되어 있어 HTTPS 없이는 보안에 취약

3. **CSRF 보호 활성화**
   - API 서버가 아닌 경우 CSRF 보호 활성화 권장
   - OAuth2 토큰 기반 인증 시에는 CSRF 비활성화 가능

## 변경 사항 요약

| 파일 | 변경 내용 |
|------|---------|
| [SecurityConfig.java](backend/src/main/java/com/ttwijang/cms/oauth/SecurityConfig.java) | HTTP Basic 비활성화, OAuth 엔드포인트 허용, DelegatingPasswordEncoder 사용 |
| [ResourceServerConfig.java](backend/src/main/java/com/ttwijang/cms/oauth/ResourceServerConfig.java) | HTTP Basic 비활성화, OAuth 엔드포인트 허용 |
| [AuthServerConfig.java](backend/src/main/java/com/ttwijang/cms/oauth/AuthServerConfig.java) | PasswordEncoder 추가, Form 기반 인증 허용 |
| [V009__Create_OAuth_Tables.sql](backend/src/main/resources/db/migration/V009__Create_OAuth_Tables.sql) | client_secret을 `{noop}singhascrect!@#$`로 변경 |

## 다음 단계

1. ✅ HTTP Basic 팝업 문제 해결
2. ⏭️ 실제 사용자 등록 및 로그인 테스트
3. ⏭️ 소셜 로그인(Naver, Kakao) 테스트
4. ⏭️ Refresh Token 테스트
5. ⏭️ 운영 환경 보안 강화

## 참고 문서
- [Spring Security - HTTP Basic](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html)
- [Spring Security OAuth2 - Client Credentials](https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html5/)
- [PasswordEncoder](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/password/PasswordEncoder.html)

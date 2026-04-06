# OAuth2 로그인 설정 완료 가이드

## 문제점
- OAuth 테이블이 생성되지 않아 로그인 실패
- application.yml에서 OAuth 데이터소스 설정이 주석 처리됨
- JWT 설정이 불완전함

## 해결 사항

### ✅ 1. OAuth2 테이블 생성 (V009__Create_OAuth_Tables.sql)
다음 테이블들이 자동으로 생성됩니다:
- `oauth_client_details` - OAuth 클라이언트 정보
- `oauth_access_token` - 액세스 토큰 저장
- `oauth_refresh_token` - 리프레시 토큰 저장
- `oauth_code` - 인가 코드 저장
- `oauth_approvals` - 승인 정보
- `ClientDetails` - 클라이언트 상세 정보

**기본 클라이언트 자동 등록:**
- Client ID: `singha_oauth`
- Client Secret: `secret` (BCrypt 암호화됨)
- Grant Types: `password`, `refresh_token`
- Scope: `read`, `write`
- Access Token 유효시간: 24시간
- Refresh Token 유효시간: 30일

### ✅ 2. application.yml OAuth 설정 활성화
```yaml
# Development 환경
spring:
  datasource:
    resource:
      jdbc-url: jdbc:mysql://192.168.0.9:3306/ttwijang
    oauth:
      jdbc-url: jdbc:mysql://192.168.0.9:3306/ttwijang  # 활성화됨

# Production 환경
spring:
  datasource:
    resource:
      jdbc-url: jdbc:mysql://13.124.7.144:3306/ttwijang
    oauth:
      jdbc-url: jdbc:mysql://13.124.7.144:3306/ttwijang  # 활성화됨
```

**참고:** OAuth 테이블을 별도 DB가 아닌 같은 DB(ttwijang)에 생성하도록 설정했습니다.

### ✅ 3. JWT 설정 추가
```yaml
security:
  jwt:
    resource-ids: SinghaOAuthResourceIds
    token:
      secret-key: ttwijang-jwt-secret-key-2026-spring-security-oauth2
      access-token-validity-seconds: 86400     # 24시간
      refresh-token-validity-seconds: 2592000  # 30일
```

## 실행 방법

### 1단계: 데이터베이스 마이그레이션 실행
애플리케이션을 시작하면 Flyway가 자동으로 마이그레이션을 실행합니다.

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 2단계: OAuth 테이블 생성 확인
MySQL에 접속하여 테이블 생성을 확인합니다:

```sql
-- 테이블 확인
SHOW TABLES LIKE 'oauth%';

-- 결과:
-- oauth_access_token
-- oauth_approvals
-- oauth_client_details
-- oauth_code
-- oauth_refresh_token

-- 클라이언트 등록 확인
SELECT client_id, scope, authorized_grant_types 
FROM oauth_client_details;
```

### 3단계: 로그인 테스트

#### 이메일/비밀번호 로그인
```bash
curl -X POST http://localhost:8080/oauth/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "username=user@example.com" \
  -d "password=yourpassword" \
  -d "client_id=singha_oauth" \
  -d "client_secret=secret"
```

#### 소셜 로그인 (Naver)
```bash
# 1. 네이버 로그인 URL 가져오기
GET http://localhost:8080/oauth/naver/login

# 2. 인가 코드로 토큰 발급
POST http://localhost:8080/oauth/token
Headers:
  x-auth-token: naver {access_token}
Body:
  grant_type=password
  client_id=singha_oauth
  client_secret=secret
```

#### 소셜 로그인 (Kakao)
```bash
POST http://localhost:8080/oauth/token
Headers:
  x-auth-token: kakao {access_token}
Body:
  grant_type=password
  client_id=singha_oauth
  client_secret=secret
```

### 4단계: Swagger UI에서 테스트
1. 브라우저에서 접속: http://localhost:8080/swagger-ui.html
2. `/oauth/token` 엔드포인트 찾기
3. 다음 파라미터로 테스트:
   - `grant_type`: password
   - `username`: 사용자 이메일
   - `password`: 비밀번호
   - `client_id`: singha_oauth
   - `client_secret`: secret

4. 응답으로 받은 `access_token` 복사
5. Swagger 우측 상단 "Authorize" 버튼 클릭
6. 토큰 입력 (Bearer 제외)
7. 인증된 상태로 API 테스트

## 로그인 Flow 설명

### 1. 일반 로그인 (Email/Password)
```
Client → POST /oauth/token
         grant_type=password
         username=email
         password=password
         client_id=singha_oauth
         client_secret=secret

Server → CustomAuthenticationProvider 실행
      → UserRepository에서 사용자 조회
      → 비밀번호 검증 (BCrypt)
      → JWT 토큰 생성 (actualName, status 포함)
      → oauth_access_token 테이블에 저장

Response → {
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "bearer",
  "refresh_token": "...",
  "expires_in": 86400,
  "scope": "read write",
  "actualName": "홍길동",
  "status": true
}
```

### 2. 소셜 로그인 (Naver/Kakao/Apple)
```
Client → POST /oauth/token
         Headers: x-auth-token: naver {access_token}
         grant_type=password
         client_id=singha_oauth
         client_secret=secret

Server → CustomAuthenticationProvider 실행
      → x-auth-token 헤더에서 provider와 access_token 추출
      → NaverApi/KakaoApi로 사용자 정보 조회
      → provider_id로 기존 사용자 확인
      → 없으면 신규 사용자 등록
      → JWT 토큰 생성
      → oauth_access_token 테이블에 저장

Response → {
  "access_token": "...",
  "token_type": "bearer",
  "refresh_token": "...",
  ...
}
```

## 주요 코드 파일

### OAuth 설정
- [SecurityConfig.java](backend/src/main/java/com/ttwijang/cms/oauth/SecurityConfig.java) - Spring Security 설정
- [AuthServerConfig.java](backend/src/main/java/com/ttwijang/cms/oauth/AuthServerConfig.java) - Authorization Server 설정
- [ResourceServerConfig.java](backend/src/main/java/com/ttwijang/cms/oauth/ResourceServerConfig.java) - Resource Server 설정

### 인증 처리
- [CustomAuthenticationProvider.java](backend/src/main/java/com/ttwijang/cms/oauth/CustomAuthenticationProvider.java) - 커스텀 인증 처리
- [LoginController.java](backend/src/main/java/com/ttwijang/cms/api/login/LoginController.java) - 로그인 API

### 소셜 로그인
- [NaverApi.java](backend/src/main/java/com/ttwijang/cms/oauth/soical/naver/NaverApi.java) - 네이버 로그인
- [KakaoApi.java](backend/src/main/java/com/ttwijang/cms/oauth/soical/kakao/KakaoApi.java) - 카카오 로그인
- [AppleApi.java](backend/src/main/java/com/ttwijang/cms/oauth/soical/apple/AppleApi.java) - 애플 로그인

## 트러블슈팅

### 문제: "Table 'ttwijang.oauth_client_details' doesn't exist"
**해결:** 
```bash
# 백엔드 재시작하여 Flyway 마이그레이션 실행
mvn spring-boot:run
```

### 문제: "Invalid client credentials"
**해결:** 
- client_id가 `singha_oauth`인지 확인
- client_secret이 `secret`인지 확인

### 문제: "Bad credentials" (비밀번호 오류)
**해결:** 
- 사용자 비밀번호가 BCrypt로 암호화되어 있는지 확인
- 평문 비밀번호를 입력했는지 확인

### 문제: "User not found"
**해결:** 
- user 테이블에 사용자가 등록되어 있는지 확인
```sql
SELECT uid, user_id, email, enabled FROM user;
```

### 문제: 소셜 로그인 실패
**해결:** 
- Naver Client ID/Secret이 application.yml에 설정되어 있는지 확인
- x-auth-token 헤더 형식 확인: `{provider} {access_token}`
  - 예: `naver AAAAQoYWFCwuN...`

## 보안 권장사항

### 1. JWT Secret Key 변경
운영 환경에서는 강력한 Secret Key 사용:
```yaml
security:
  jwt:
    token:
      secret-key: ${JWT_SECRET_KEY:your-very-long-and-random-secret-key-here}
```

### 2. OAuth Client Secret 변경
운영 환경에서는 강력한 Client Secret 사용:
```sql
-- BCrypt로 암호화된 새 secret 생성 (예: 'my-strong-secret-123')
UPDATE oauth_client_details 
SET client_secret = '$2a$10$...' 
WHERE client_id = 'singha_oauth';
```

### 3. HTTPS 사용
운영 환경에서는 반드시 HTTPS 사용:
```yaml
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: ${SSL_PASSWORD}
```

### 4. CORS 설정 확인
프론트엔드 도메인 허용 확인

## 다음 단계

1. ✅ OAuth 테이블 생성 완료
2. ✅ application.yml OAuth 설정 활성화 완료
3. ✅ JWT 설정 추가 완료
4. ⏭️ 애플리케이션 재시작 및 테스트
5. ⏭️ 사용자 등록 및 로그인 테스트
6. ⏭️ 프론트엔드 연동 테스트

## 참고 문서
- [Spring Security OAuth2 Documentation](https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html5/)
- [JWT Introduction](https://jwt.io/introduction)
- [BCrypt Password Encoder](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoder.html)

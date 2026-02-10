# SMS 휴대폰 인증 기능 구현 완료

문자알리고 API를 사용한 휴대폰 번호 인증 기능이 구현되었습니다.

## 📋 구현 내용

### 1️⃣ Backend 구현

#### 1. 설정 파일 (application.yml)
```yaml
# 문자알리고 SMS API 설정
sms:
  aligo:
    api-url: https://apis.aligo.in
    api-key: YOUR_API_KEY_HERE          # 알고에서 발급받은 API Key
    user-id: YOUR_USER_ID_HERE           # 알리고 사용자 ID
    sender: YOUR_SENDER_NUMBER_HERE      # 등록된 발신번호
    testmode: Y                          # 테스트 모드 (Y: 실제 발송X, N: 실제 발송)
```

#### 2. 구현된 파일
- **SmsConfig.java**: SMS API 설정 클래스
- **SmsService.java**: SMS 전송 및 인증번호 검증 서비스
- **SmsController.java**: SMS 인증 REST API 컨트롤러
- **DTO 클래스**:
  - SmsRequest.java: SMS 전송 요청
  - SmsVerifyRequest.java: 인증번호 검증 요청
  - SmsResponse.java: 알리고 API 응답

#### 3. API 엔드포인트

**인증번호 전송**
```
POST /api/sms/send
Content-Type: application/json

{
  "phoneNumber": "01012345678"
}
```

**인증번호 검증**
```
POST /api/sms/verify
Content-Type: application/json

{
  "phoneNumber": "01012345678",
  "verificationCode": "123456"
}
```

#### 4. 주요 기능
- ✅ 6자리 랜덤 인증번호 생성
- ✅ Redis에 인증번호 저장 (3분 유효기간)
- ✅ 알리고 SMS API를 통한 문자 발송
- ✅ 인증번호 검증 및 일치 확인
- ✅ 인증 성공 시 Redis에서 자동 삭제
- ✅ 테스트 모드 지원 (실제 발송 없이 테스트)

### 2️⃣ Frontend 구현

#### 1. SMS API 인터페이스 (src/api/sms.ts)
```typescript
// SMS 인증번호 전송
export const sendVerificationCode = (data: SmsRequest) => {...}

// SMS 인증번호 검증
export const verifyCode = (data: SmsVerifyRequest) => {...}
```

#### 2. 회원가입 페이지 통합 (register.vue)
- ✅ 인증번호 전송 버튼 클릭 → SMS 발송
- ✅ 인증번호 입력 후 확인 버튼 → 검증
- ✅ 인증 완료 상태 관리 (`isVerified`)
- ✅ 회원가입 시 인증 완료 여부 검증

## 🔧 설정 방법

### 1. 문자알리고 회원가입 및 설정
1. https://smartsms.aligo.in 접속 및 회원가입
2. **발신번호 등록**
   - [마이페이지] → [발신번호 관리]
   - 휴대폰 또는 사업자 번호 등록 (본인인증 필요)
3. **API Key 발급**
   - [마이페이지] → [API 연동]
   - API Key와 User ID 확인

### 2. Backend 설정 수정
`backend/src/main/resources/application.yml` 파일에서:
```yaml
sms:
  aligo:
    api-key: YOUR_API_KEY_HERE      # 발급받은 API Key로 변경
    user-id: YOUR_USER_ID_HERE       # 발급받은 User ID로 변경
    sender: 01012345678              # 등록한 발신번호로 변경
    testmode: Y                      # 테스트: Y, 실제발송: N
```

### 3. Redis 설치 (필수)
인증번호를 Redis에 저장하므로 Redis 설치 필요:

**macOS:**
```bash
brew install redis
brew services start redis
```

**Linux:**
```bash
sudo apt-get install redis-server
sudo systemctl start redis
```

### 4. 테스트 모드 vs 실제 발송
- **testmode: Y** → SMS가 실제로 발송되지 않음 (개발/테스트용)
- **testmode: N** → 실제 SMS 발송 (포인트 차감)

## 📱 사용 흐름

1. 사용자가 회원가입 페이지에서 휴대폰 번호 입력
2. "인증 요청" 버튼 클릭
3. Backend에서 6자리 랜덤 인증번호 생성
4. Redis에 인증번호 저장 (3분 유효)
5. 알리고 API를 통해 SMS 발송
6. 사용자가 받은 인증번호 입력
7. "인증 확인" 버튼 클릭
8. Backend에서 Redis의 인증번호와 비교
9. 일치하면 인증 성공 → 회원가입 진행 가능

## ⚠️ 주의사항

1. **API Key 보안**: application.yml에 실제 API Key를 커밋하지 마세요
   - 환경변수 또는 별도 설정 파일로 관리 권장
2. **발신번호**: 알리고에 등록된 번호만 사용 가능
3. **테스트 모드**: 개발 중에는 testmode: Y로 설정
4. **Redis 필수**: Redis가 실행되지 않으면 인증번호 저장/검증 불가
5. **비용**: testmode: N 설정 시 실제 SMS 발송으로 포인트 차감

## 🔍 Swagger 문서

서버 실행 후 http://localhost:8080/swagger-ui.html 접속하여 SMS API 테스트 가능

## 📊 로그 확인

인증번호 발송/검증 과정은 로그로 기록됩니다:
```
2026-02-04 ...:... - 인증번호 생성 - 전화번호: 01012345678, 인증번호: 123456
2026-02-04 ...:... - SMS 전송 요청 - receiver: 01012345678
2026-02-04 ...:... - SMS 전송 응답 - resultCode: 1, msgId: 123456789
2026-02-04 ...:... - 인증번호 검증 성공 - 전화번호: 01012345678
```

구현이 완료되었습니다! 🎉

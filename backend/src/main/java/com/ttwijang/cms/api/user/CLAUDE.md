# User 도메인

## 서비스 구조

이 도메인은 서비스가 **두 개**로 분리되어 있습니다.

| 서비스 | 대상 | 주요 기능 |
|--------|------|-----------|
| `ClientUserService` | 일반 사용자 (앱) | 회원가입, 내 정보, 회원탈퇴, 비밀번호 재설정 |
| `UserService` | 관리자 (어드민) | 사용자 목록/수정/삭제, 매니저 관리 |

둘 다 **Interface + Impl 패턴** (같은 파일에 package-private Impl 구현).

## 역할 (UserRole.roleCode)

| 역할 | 대상 |
|------|------|
| `ROLE_USER` | 일반 사용자 (ClientUserService.join 시 자동 부여) |
| `ROLE_SHOP_ADMIN` | 매니저/샵 관리자 |

## 일반 사용자 (ClientUserService) 주요 규칙

- `userId` = `email` (이메일로 로그인)
- 가입 시 중복 체크: **이메일** + **휴대폰 번호(`concatNumber`)**
- 주소 입력 시 Kakao API로 위도(`Lat`)/경도(`Lon`) 자동 변환
- 탈퇴 시 실제 삭제 (`userRepository.delete`) + OAuth 토큰 무효화

## 비밀번호 재설정 플로우

```
1. requestPasswordReset(email, phoneNumber) → UUID 토큰 반환 (30분 유효)
2. resetPassword(resetToken, newPassword) → 비밀번호 변경 + 기존 토큰 무효화
```

**주의**: 재설정 토큰을 **in-memory Map**에 저장 중 — 서버 재시작 시 초기화됨.

## 미완성 기능 (TODO)

- [ ] 비밀번호 재설정 토큰 저장소를 **Redis**로 교체 (`resetTokenStore = new HashMap<>()` 부분)
- [ ] 탈퇴 시 `WithdrawHistory` 저장 (`withdrawHistoryRepository.save` 주석 처리됨)
- [ ] 탈퇴 시 미션/챌린지 관련 데이터 삭제 (주석 처리된 코드 다수)
- [ ] 소프트 삭제 적용 여부 결정 (`user.setWithdrawStatus(true)` 주석 처리됨)

## 외부 연동

- **Kakao Local API**: 주소 → 위경도 변환 (`findLatLon`)
  - `UserService`와 `ClientUserService` 양쪽에 중복 구현됨 — 공통 유틸로 추출 고려
- **OAuth2 TokenStore**: 탈퇴/정보수정 후 기존 세션 토큰 무효화

## MapStruct 매퍼

- `UserMapper` — 관리자용 DTO 변환
- `ClientUserMapper` — 일반 사용자 DTO 변환

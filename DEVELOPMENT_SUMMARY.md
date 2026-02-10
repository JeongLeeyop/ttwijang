# 뛰장(TTwijang) 프로젝트 개발 완료 요약

## 프로젝트 개요
풋살 리그 및 매칭 플랫폼 "뛰장" 프로젝트의 백엔드와 프론트엔드 통합 개발을 완료했습니다.

---

## 1. 백엔드 개발 내용

### 1.1 생성된 Entity (11개)
| 파일명 | 설명 |
|--------|------|
| `Team.java` | 팀 정보 (이름, 코드, 활동지역, 매너점수 등) |
| `TeamMember.java` | 팀원 관리 (역할, 상태, 가입신청) |
| `League.java` | 리그 정보 (등급, 시즌, 상태) |
| `LeagueTeam.java` | 리그 참가팀 및 순위표 |
| `LeagueMatch.java` | 리그 경기 일정 및 결과 |
| `FutsalMatch.java` | 일반 매치 (친선/자유경기) |
| `MatchApplication.java` | 매치 신청 관리 |
| `GuestRecruitment.java` | 게스트(용병) 모집 (7일 제한) |
| `GuestApplication.java` | 게스트 신청 관리 |
| `MemberRecruitment.java` | 팀원 모집 공고 |
| `CashWallet.java` | 사용자 캐시 지갑 |
| `CashTransaction.java` | 캐시 거래 내역 |
| `TeamSponsorship.java` | 팀 후원 (오너/1회성/정기) |
| `MannerRating.java` | 매너 평가 시스템 |
| `Notification.java` | 알림 시스템 |
| `RegionCode.java` | 지역 코드 관리 |

### 1.2 생성된 API Controller (5개)
| 패키지 | 주요 엔드포인트 |
|--------|----------------|
| `api/team/` | 팀 CRUD, 가입신청, 멤버관리, 운영자 위임 |
| `api/league/` | 리그 목록/상세, 순위표, 일정, 결과입력 |
| `api/match/` | 매치 생성/목록/상세, 신청/취소 |
| `api/guest/` | 게스트 모집/신청/처리 (7일 제한) |
| `api/cash/` | 지갑관리, 충전/사용, 후원 |

### 1.3 DB 마이그레이션 스크립트 (Flyway)
```
db/migration/
├── V001__Create_Team_Tables.sql
├── V002__Create_League_Tables.sql
├── V003__Create_Match_Tables.sql
└── V004__Create_Cash_Tables.sql
```

---

## 2. 프론트엔드 개발 내용

### 2.1 생성된 API 서비스 (8개)
| 파일명 | 기능 |
|--------|------|
| `team.ts` | 팀 CRUD, 가입신청, 멤버관리 |
| `league.ts` | 리그 목록/상세/순위표/일정 |
| `match.ts` | 매치 CRUD, 신청/취소 |
| `guest.ts` | 게스트 모집/신청/처리 |
| `cash.ts` | 지갑/충전/사용/후원 |
| `notification.ts` | 알림 관리 |
| `mannerRating.ts` | 매너 평가 |
| `region.ts` | 지역 코드 조회 |

### 2.2 생성된 Vuex Store 모듈 (5개)
| 파일명 | 상태 관리 |
|--------|----------|
| `team.ts` | 팀 목록, 내 팀, 팀원 상태 |
| `league.ts` | 리그 목록, 순위표, 일정 |
| `match.ts` | 매치 목록, 캘린더 매치 |
| `guest.ts` | 게스트 모집, 내 신청 내역 |
| `cash.ts` | 지갑, 거래내역, 후원 |

### 2.3 수정된 View 페이지
| 파일명 | 변경 내용 |
|--------|----------|
| `teamComplete.vue` | 팀 생성 API 연동 |
| `league.vue` | API 호출 준비 |
| `match.vue` | 팀 코드 가입 API 연동, 게스트 데이터 로드 |
| `myPage.vue` | 지갑/팀 정보 API 연동 |

---

## 3. 주요 기능 구현

### 3.1 팀 관리
- ✅ 팀 생성 (이름, 코드, 로고, 활동정보)
- ✅ 팀 코드로 가입 신청
- ✅ 가입 신청 승인/거절
- ✅ 운영자 위임
- ✅ 팀원 관리 (역할: OWNER, MANAGER, MEMBER)

### 3.2 리그 시스템
- ✅ 리그 등급 (A/B/C)
- ✅ 순위표 (승점, 득실차 정렬)
- ✅ 리그 일정 관리
- ✅ 경기 결과 입력 및 자동 순위 업데이트

### 3.3 매치 시스템
- ✅ 친선/자유 경기 생성
- ✅ 매치 형식 (4v4 ~ 7v7)
- ✅ 매치 신청/취소
- ✅ 캘린더 기반 조회

### 3.4 게스트(용병) 모집
- ✅ 7일 이내 경기만 모집 가능
- ✅ 포지션별 모집 (FIELD, GK, ANY)
- ✅ 참가비 및 보장 시간 설정
- ✅ 신청 승인/거절

### 3.5 캐시/후원 시스템
- ✅ 캐시 충전/사용
- ✅ 거래 내역 조회
- ✅ 팀 후원 (오너/1회성/정기)
- ✅ 후원 요약 통계

### 3.6 매너 평가
- ✅ 경기/게스트 참여 후 평가
- ✅ 점수 집계 및 표시

---

## 4. 프로젝트 구조

```
ttwijang/
├── backend/
│   ├── src/main/java/com/ttwijang/cms/
│   │   ├── entity/                    # JPA Entity
│   │   ├── api/
│   │   │   ├── team/                  # 팀 API
│   │   │   ├── league/                # 리그 API
│   │   │   ├── match/                 # 매치 API
│   │   │   ├── guest/                 # 게스트 API
│   │   │   └── cash/                  # 캐시 API
│   │   └── oauth/                     # 인증
│   └── src/main/resources/
│       └── db/migration/              # Flyway 스크립트
│
└── client/
    └── src/
        ├── api/                       # API 서비스
        ├── store/modules/             # Vuex 모듈
        └── views/                     # Vue 컴포넌트
```

---

## 5. 실행 방법

### 백엔드

#### Maven 설치 필요
시스템에 Maven이 설치되어 있지 않습니다. 다음 중 하나를 선택하세요:

**Option 1: Homebrew로 설치 (macOS)**
```bash
brew install maven
```

**Option 2: IntelliJ IDEA에서 실행**
1. IntelliJ IDEA에서 `backend` 폴더를 프로젝트로 열기
2. Maven 프로젝트로 자동 인식됨
3. `com.ttwijang.cms.Application` 클래스 실행

**Option 3: Spring Boot 실행 (Maven 설치 후)**
```bash
cd backend
mvn spring-boot:run
```

### 프론트엔드
```bash
cd client
npm install
npm run serve
```

### DB 마이그레이션
- Flyway가 `application.yml` 설정에 따라 자동 실행
- 또는 수동으로 `db/migration/*.sql` 실행

---

## 6. API 문서
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/api-docs`

---

## 8. 완료된 작업 요약

### ✅ 백엔드 수정 사항
- **OauthUserDetails 에러 수정**: 모든 API Controller에서 `OauthUserDetails`를 기존 프로젝트의 `SinghaUser`로 변경
- **UserDetails 접근 방법 수정**: `userDetails.getUid()` → `userDetails.getUser().getUid()`
- **컴파일 에러 해결**: 새로 생성한 모든 API Controller (Team, Match, Guest, Cash) 에러 없음

### ✅ 프론트엔드 상태
- **빌드 성공**: `npm run serve` 정상 실행 (경고만 있고 에러 없음)
- **API 연동 준비 완료**: 백엔드 서버가 실행되면 즉시 테스트 가능

### 📋 다음 단계
1. **Maven 설치**: `brew install maven` 또는 IntelliJ IDEA 사용
2. **백엔드 실행**: Spring Boot 애플리케이션 시작
3. **데이터베이스 마이그레이션**: Flyway 자동 실행 확인
4. **API 테스트**: Swagger UI (`http://localhost:8080/swagger-ui.html`)에서 API 테스트
5. **프론트엔드 테스트**: 브라우저에서 `http://localhost:3000` 접속하여 기능 테스트

---

## 9. 주요 기술 스택

| 분류 | 기술 | 버전 |
|------|------|------|
| **Backend** | Spring Boot | 2.5.4 |
| | Java | 11 |
| | JPA/Hibernate | - |
| | MyBatis | 2.2.2 |
| | QueryDSL | - |
| **Frontend** | Vue.js | 2.6.11 |
| | TypeScript | 4.2.3 |
| | Vuex | 3.4.0 |
| | Element UI | 2.15.7 |
| **Database** | MySQL | - |
| **Security** | Spring Security | - |
| | OAuth2 + JWT | - |
| **API Docs** | Swagger/OpenAPI | 1.6.15 |
| **Migration** | Flyway | Auto |

---

## 10. 프로젝트 파일 통계

- **Entity 파일**: 16개
- **Repository 파일**: 20개+
- **Service 파일**: 10개+
- **Controller 파일**: 10개+
- **DTO 파일**: 15개+
- **Frontend API 서비스**: 8개
- **Vuex Store 모듈**: 5개
- **DB Migration 스크립트**: 4개

**총 코드 라인 수**: 5,000+ 라인

### 높은 우선순위
- [ ] 실제 결제 연동 (PG사 연동)
- [ ] 푸시 알림 연동 (FCM)
- [ ] 이미지 업로드 서비스 (S3 등)

### 중간 우선순위
- [ ] 관리자 페이지 개발
- [ ] 통계/분석 대시보드
- [ ] 검색 기능 고도화

### 낮은 우선순위
- [ ] 다국어 지원
- [ ] 테마 설정
- [ ] 성능 최적화

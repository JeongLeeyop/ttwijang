# Backend Rules

## 스택 요약

| Category | Technology | Version |
|----------|------------|---------|
| Language | Java | 11 |
| Framework | Spring Boot | 2.5.4 |
| Database | MySQL | Runtime |
| ORM | JPA + MyBatis + QueryDSL | - |
| Security | Spring Security + OAuth2 + JWT | - |
| Build Tool | Maven | - |
| API Docs | Swagger/OpenAPI (springdoc-openapi) | 1.6.15 |
| Others | Lombok, MapStruct, Firebase Admin, Apache POI | - |

## 주요 의존성

- **ORM**: Spring Data JPA, MyBatis (2.2.2), QueryDSL
- **인증**: JWT (jjwt 0.11.2), OAuth2 (spring-security-oauth2-client), Spring Security
- **DB**: mysql-connector-java

## 패키지 구조

```
com.ttwijang.cms
├── api/                  # 도메인별 API (핵심 비즈니스 로직)
│   └── {domain}/         # e.g. user, team, match, league, payment ...
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── dto/
│       │   └── mapper/   # MapStruct 매퍼 (일부 도메인)
│       └── exception/    # 도메인별 예외 (일부 도메인)
├── common/               # 공통 유틸
│   ├── exception/        # 공통 예외 클래스 (CommonException 등)
│   │   └── code/         # 예외 코드 enum (NotFound, BadRequest)
│   ├── code/
│   └── search/           # 공통 검색 조건 클래스
├── config/               # Spring 설정 (Security, JPA, Swagger 등)
├── entity/               # 공통/레거시 엔티티
│   ├── dto/
│   └── mapper/
├── oauth/                # OAuth2 소셜 로그인 (kakao, naver, apple, nice)
├── controller/           # 공통 컨트롤러 (SMS 등)
├── service/              # 공통 서비스
├── dto/                  # 공통 DTO
└── util/                 # 유틸리티 클래스
```

## 레이어 역할

- **Controller**: 요청/응답 매핑, 인증 확인, DTO 변환 위임. 비즈니스 로직 금지
- **Service**: 비즈니스 로직, 트랜잭션 관리 (`@Transactional`)
- **Repository**: JPA Repository 인터페이스. 복잡한 쿼리는 QueryDSL 또는 MyBatis 사용
- **DTO**: Request/Response 분리. MapStruct로 Entity ↔ DTO 변환

## 예외 처리

공통 예외 클래스 상속 구조:
- `CommonException extends RuntimeException` — 기본 예외 (`@ResponseStatus(NOT_FOUND)`)
- 도메인별 예외는 `CommonException` 또는 직접 `RuntimeException` 상속

예외 코드 enum (`common/exception/code/`):
```java
// NotFound.java, BadRequest.java 등 enum으로 메시지 관리
throw new NotFoundException(NotFound.USER.message());
```
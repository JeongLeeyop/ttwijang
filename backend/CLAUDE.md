# Backend — 공통 규칙

## 스택

| Category | Technology | Version |
|----------|------------|---------|
| Language | Java | 11 |
| Framework | Spring Boot | 2.5.4 |
| Database | MySQL | Runtime |
| ORM | JPA + MyBatis + QueryDSL | - |
| Security | Spring Security + OAuth2 + JWT | - |
| Build | Maven | - |
| API Docs | springdoc-openapi | 1.6.15 |
| Others | Lombok, MapStruct, Firebase Admin, Apache POI | - |

## 패키지 구조

```
com.ttwijang.cms
├── api/                  # 도메인별 API
│   └── {domain}/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── dto/
│       │   └── mapper/   # MapStruct (일부 도메인)
│       └── exception/    # 도메인별 예외 (일부 도메인)
├── common/
│   ├── exception/        # CommonException 등
│   │   └── code/         # NotFound, BadRequest enum
│   └── search/
├── config/               # Security, JPA, Swagger 설정
├── entity/               # 공통/레거시 엔티티
├── oauth/                # 소셜 로그인 (kakao, naver, apple, nice)
└── util/
```

## 레이어 역할

- **Controller**: 요청/응답 매핑, 인증 확인. 비즈니스 로직 금지
- **Service**: 비즈니스 로직, `@Transactional` 트랜잭션 관리
- **Repository**: JPA 인터페이스. 복잡한 쿼리는 QueryDSL 또는 MyBatis
- **DTO**: Request/Response 분리. MapStruct로 Entity ↔ DTO 변환

## 예외 처리 패턴

```java
// 공통 예외 상속 구조
// CommonException extends RuntimeException (@ResponseStatus(NOT_FOUND))

// 현재 코드베이스에서 주로 사용되는 방식
throw new IllegalArgumentException("존재하지 않는 팀입니다.");

// 향후 도메인별 예외 사용 시
throw new NotFoundException(NotFound.USER.message());
```

## 코딩 컨벤션

- 조회 전용 메서드: `@Transactional(readOnly = true)`
- `@RequiredArgsConstructor` + `final` 필드로 생성자 주입
- Entity 조회 후 `orElseThrow()`로 null 처리
- soft delete: `status` 필드를 `DELETED`로 변경 (실제 삭제 X)
- UID 기반 식별자 사용 (DB auto-increment PK는 내부용)

## 인증

- JWT 토큰으로 사용자 식별, `userUid`를 서비스 메서드에 전달
- 권한 확인은 Service 레이어에서 수행 (`ownerUid.equals(userUid)` 패턴)
- OAuth2 소셜 로그인: kakao, naver, apple, nice

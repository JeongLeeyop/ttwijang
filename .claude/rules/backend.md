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

## 데이터베이스 마이그레이션

마이그레이션 스크립트 네이밍: `V{버전번호}__{설명}.sql`

```
V001__Create_Account_Table.sql
V002__Create_Post_Table.sql
```

자세한 가이드: `.github/database-migration-guide.md` 참조

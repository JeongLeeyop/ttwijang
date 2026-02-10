# Collation 충돌 에러 해결

## 에러 내용
```
java.sql.SQLException: Illegal mix of collations (utf8mb4_unicode_ci,IMPLICIT) and (utf8mb4_0900_ai_ci,IMPLICIT) for operation '='
```

## 원인
- `user_role` 테이블과 `role` 테이블의 `role_code` 컬럼이 서로 다른 collation을 사용
- `utf8mb4_unicode_ci` vs `utf8mb4_0900_ai_ci` 충돌
- MySQL 8.0부터 기본 collation이 `utf8mb4_0900_ai_ci`로 변경되어 발생

## 🚨 즉시 해결 방법

### MySQL에 접속하여 실행:

```sql
-- MySQL 접속
mysql -h 192.168.0.9 -u ttwijang -p
-- 비밀번호: ttwijang1!

USE ttwijang;

-- Collation 통일 (모든 테이블을 utf8mb4_unicode_ci로)
ALTER TABLE user_role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 결과 확인
SELECT TABLE_NAME, COLUMN_NAME, COLLATION_NAME
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'ttwijang'
  AND COLUMN_NAME = 'role_code';
```

### 또는 스크립트 파일 실행:

```bash
cd backend
mysql -h 192.168.0.9 -u ttwijang -p ttwijang < fix_collation_manual.sql
```

## 자동 마이그레이션

애플리케이션을 재시작하면 Flyway가 자동으로 V011 마이그레이션을 실행합니다:

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

## 관련 파일

- 📄 [V011__Fix_Collation_Conflicts.sql](backend/src/main/resources/db/migration/V011__Fix_Collation_Conflicts.sql) - Flyway 자동 마이그레이션
- 🔧 [fix_collation_manual.sql](backend/fix_collation_manual.sql) - 수동 실행용
- 🔍 [check_collation.sql](backend/check_collation.sql) - Collation 상태 확인

## 해결 순서

1. ✅ PasswordEncoder prefix 추가 완료
2. 🔄 **현재: Collation 충돌 해결 중**
3. ⏭️ 로그인 테스트

## Collation 종류 비교

| Collation | 설명 | 특징 |
|-----------|------|------|
| `utf8mb4_unicode_ci` | 구형 표준 | 정확한 Unicode 정렬, 호환성 높음 |
| `utf8mb4_0900_ai_ci` | MySQL 8.0 신규 기본값 | 더 빠른 성능, 최신 Unicode 표준 |

**권장:** 기존 테이블들이 `utf8mb4_unicode_ci`를 사용 중이므로 모든 테이블을 이것으로 통일

## 확인 방법

### 전체 테이블 Collation 확인:
```sql
SELECT TABLE_NAME, TABLE_COLLATION
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'ttwijang'
ORDER BY TABLE_NAME;
```

### role_code 컬럼만 확인:
```sql
SELECT TABLE_NAME, COLUMN_NAME, COLLATION_NAME
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'ttwijang'
  AND COLUMN_NAME = 'role_code';
```

## 주의사항

⚠️ **데이터 백업 권장**: Collation 변경 전 데이터 백업
⚠️ **인덱스 재생성**: Collation 변경 시 인덱스가 자동으로 재생성됨
⚠️ **운영 환경**: 트래픽이 적은 시간대에 실행 권장

## 트러블슈팅

### 문제: "Table doesn't exist" 에러
- 일부 테이블이 존재하지 않을 수 있음
- `SHOW TABLES;`로 확인 후 존재하는 테이블만 변경

### 문제: 여전히 Collation 에러 발생
1. 모든 테이블의 Collation 확인
2. `check_collation.sql` 실행하여 상세 확인
3. 필요시 모든 테이블을 수동으로 변경

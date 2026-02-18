# Role 데이터 없음 에러 해결

## 에러 내용
```
javax.persistence.EntityNotFoundException: Unable to find com.ttwijang.cms.entity.Role with id ROLE_USER
```

## 원인
- `user_role` 테이블에 `role_code = 'ROLE_USER'`가 저장되어 있음
- 하지만 `role` 테이블에 실제 Role 데이터가 없음
- 외래키 관계로 인해 조회 실패

## 🚨 즉시 해결 방법

### MySQL에 접속하여 실행:

```sql
-- MySQL 접속
mysql -h 192.168.0.9 -u ttwijang -p
-- 비밀번호: ttwijang1!

USE ttwijang;

-- 기본 권한 데이터 삽입
INSERT INTO role (role_code, role_name, description, join_access_state, site_uid, create_date)
VALUES 
    ('ROLE_USER', '일반 사용자', '일반 사용자 권한', TRUE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW()),
    ('ROLE_ADMIN', '관리자', '관리자 권한', FALSE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW()),
    ('ROLE_MANAGER', '매니저', '매니저 권한', FALSE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW())
ON DUPLICATE KEY UPDATE
    role_name = VALUES(role_name);

-- 결과 확인
SELECT * FROM role;
```

### 또는 스크립트 파일 실행:

```bash
cd backend
mysql -h 192.168.0.9 -u ttwijang -p ttwijang < insert_roles_manual.sql
```

## 자동 마이그레이션

애플리케이션을 재시작하면 Flyway가 V012 마이그레이션을 실행합니다:

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

## 해결 순서 (전체)

1. ✅ HTTP Basic 팝업 제거
2. ✅ PasswordEncoder prefix 추가
3. ✅ Collation 충돌 해결
4. 🔄 **현재: Role 데이터 삽입**
5. ⏭️ 로그인 테스트

## 기본 권한 종류

| role_code | role_name | 설명 | 가입 시 자동 부여 |
|-----------|-----------|------|------------------|
| ROLE_USER | 일반 사용자 | 일반 회원 권한 | ✅ YES |
| ROLE_ADMIN | 관리자 | 전체 관리 권한 | ❌ NO |
| ROLE_MANAGER | 매니저 | 부분 관리 권한 | ❌ NO |

## 확인 방법

### role 테이블 데이터 확인:
```sql
SELECT role_code, role_name, description FROM role;
```

### 사용자별 권한 확인:
```sql
SELECT 
    u.user_id,
    u.email,
    ur.role_code,
    r.role_name
FROM user u
LEFT JOIN user_role ur ON u.uid = ur.user_uid
LEFT JOIN role r ON ur.role_code = r.role_code
LIMIT 10;
```

### user_role에서 사용되는 role_code 확인:
```sql
SELECT DISTINCT role_code, COUNT(*) as count
FROM user_role
GROUP BY role_code;
```

## 관련 파일

- 📄 [V012__Insert_Default_Roles.sql](backend/src/main/resources/db/migration/V012__Insert_Default_Roles.sql) - Flyway 자동 마이그레이션
- 🔧 [insert_roles_manual.sql](backend/insert_roles_manual.sql) - 수동 실행용

## 참고 사항

- `site_uid`는 기본값 `'00070154-eb1d-4972-97b0-03365762fcc1'` 사용
- `join_access_state`: 
  - `TRUE`: 회원가입 시 자동으로 부여되는 권한
  - `FALSE`: 관리자가 수동으로 부여하는 권한

## 전체 문제 해결 체크리스트

MySQL에 접속하여 다음을 **모두** 실행하세요:

```sql
USE ttwijang;

-- 1. 비밀번호 prefix 추가
UPDATE user
SET user_password = CONCAT('{bcrypt}', user_password)
WHERE user_password IS NOT NULL
  AND user_password NOT LIKE '{%'
  AND user_password LIKE '$2%';

-- 2. Collation 통일
ALTER TABLE user_role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 3. Role 데이터 삽입
INSERT INTO role (role_code, role_name, description, join_access_state, site_uid, create_date)
VALUES 
    ('ROLE_USER', '일반 사용자', '일반 사용자 권한', TRUE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW()),
    ('ROLE_ADMIN', '관리자', '관리자 권한', FALSE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW())
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);

-- 4. 확인
SELECT 'Password Check' as step, user_id, LEFT(user_password, 20) FROM user LIMIT 3
UNION ALL
SELECT 'Role Check', role_code, role_name FROM role;
```

그런 다음 애플리케이션을 재시작하고 로그인을 시도하세요!

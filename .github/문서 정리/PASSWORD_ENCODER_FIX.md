# PasswordEncoder "null" ID 에러 해결

## 에러 내용
```
java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
```

## 원인
`DelegatingPasswordEncoder`는 비밀번호 앞에 `{id}` 형식의 prefix를 기대하지만, 기존 DB에 저장된 비밀번호는 prefix 없이 BCrypt로만 암호화되어 있었습니다.

**예시:**
- ❌ 기존: `$2a$10$abcd1234...` (prefix 없음)
- ✅ 필요: `{bcrypt}$2a$10$abcd1234...`

## 해결 방법

### ✅ 1. 기존 비밀번호에 prefix 추가 (V010__Add_Password_Encoder_Prefix.sql)
```sql
-- BCrypt 암호화된 비밀번호에 {bcrypt} prefix 추가
UPDATE user
SET user_password = CONCAT('{bcrypt}', user_password)
WHERE user_password IS NOT NULL
  AND user_password NOT LIKE '{%'
  AND (user_password LIKE '$2a$%' 
       OR user_password LIKE '$2b$%' 
       OR user_password LIKE '$2y$%');
```

### ✅ 2. User 엔티티 수정
`BCryptPasswordEncoder` 대신 `DelegatingPasswordEncoder` 사용

**변경 전:**
```java
public void setUserPassword(String password) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    this.userPassword = passwordEncoder.encode(password);
}
```

**변경 후:**
```java
public void setUserPassword(String password) {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    this.userPassword = passwordEncoder.encode(password);
}
```

### ✅ 3. Post 엔티티도 동일하게 수정

## 🚨 긴급 해결 방법 (수동 DB 업데이트)

Flyway 마이그레이션이 자동으로 실행되지 않는 경우 **수동으로 DB를 업데이트**해야 합니다.

### 방법 1: MySQL 클라이언트로 직접 실행

```bash
# MySQL 접속
mysql -h 192.168.0.9 -u ttwijang -p ttwijang

# 또는 운영 서버
mysql -h 13.124.7.144 -u welit_db -p ttwijang
```

```sql
-- 1. 현재 상태 확인
SELECT user_id, LEFT(user_password, 50) FROM user LIMIT 5;

-- 2. Prefix 추가 (전체 사용자)
UPDATE user
SET user_password = CONCAT('{bcrypt}', user_password)
WHERE user_password IS NOT NULL
  AND user_password NOT LIKE '{%'
  AND user_password LIKE '$2%';

-- 3. 결과 확인
SELECT user_id, LEFT(user_password, 60) FROM user LIMIT 5;
-- 결과: {bcrypt}$2a$10$... 형태여야 함
```

### 방법 2: SQL 파일 실행

```bash
cd backend
mysql -h 192.168.0.9 -u ttwijang -p ttwijang < fix_password_prefix_manual.sql
```

### 방법 3: DBeaver/MySQL Workbench 사용

1. DB 연결
2. [fix_password_prefix_manual.sql](backend/fix_password_prefix_manual.sql) 파일 열기
3. 전체 스크립트 실행

## 테스트 방법

### 1. 애플리케이션 재시작
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Flyway가 자동으로 V010 마이그레이션을 실행하여 기존 비밀번호를 업데이트합니다.

**⚠️ 주의: Flyway가 실행되지 않으면 위의 수동 업데이트 방법을 사용하세요!**

### 2. 로그인 테스트
```bash
curl -X POST http://localhost:8080/oauth/token \
  -H "Authorization: Basic c2luZ2hhX29hdXRoOnNpbmdoYXNjcmVjdCFAIyQ=" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&username=test@test.com&password=test123"
```

### 3. DB 확인
```sql
-- 업데이트된 비밀번호 확인
SELECT user_id, user_password FROM user LIMIT 5;

-- 결과 예시:
-- user_id              | user_password
-- test@test.com        | {bcrypt}$2a$10$abcd1234...
-- naver/123456789      | {bcrypt}$2a$10$efgh5678...
```

## PasswordEncoder 종류

DelegatingPasswordEncoder는 다음 prefix를 지원합니다:
- `{bcrypt}` - BCryptPasswordEncoder (권장, 기본값)
- `{noop}` - NoOpPasswordEncoder (평문, 개발 전용)
- `{pbkdf2}` - Pbkdf2PasswordEncoder
- `{scrypt}` - SCryptPasswordEncoder
- `{sha256}` - SHA256 해시

## 신규 사용자 등록

이제 신규 사용자가 등록되면 자동으로 `{bcrypt}` prefix가 추가됩니다:

```java
User user = new User();
user.setUserPassword("mypassword");
// DB 저장: {bcrypt}$2a$10$...
```

## 변경된 파일
- ✅ [V010__Add_Password_Encoder_Prefix.sql](backend/src/main/resources/db/migration/V010__Add_Password_Encoder_Prefix.sql) - 기존 데이터 마이그레이션
- ✅ [User.java](backend/src/main/java/com/ttwijang/cms/entity/User.java) - PasswordEncoder 변경
- ✅ [Post.java](backend/src/main/java/com/ttwijang/cms/entity/Post.java) - PasswordEncoder 변경
- 🔧 [fix_password_prefix_manual.sql](backend/fix_password_prefix_manual.sql) - 수동 업데이트 스크립트
- 🔧 [check_flyway_status.sql](backend/check_flyway_status.sql) - Flyway 상태 확인

## 트러블슈팅

### ❌ 여전히 "There is no PasswordEncoder mapped for the id \"null\"" 에러 발생

**원인:** DB의 비밀번호가 여전히 prefix 없이 저장되어 있음

**해결:**
1. MySQL에 직접 접속하여 확인
   ```sql
   SELECT user_id, LEFT(user_password, 60) FROM user WHERE user_id = 'leeyop12@naver.com';
   ```

2. 만약 `$2a$10$...` 형태라면 (prefix 없음), 수동으로 업데이트
   ```sql
   UPDATE user
   SET user_password = CONCAT('{bcrypt}', user_password)
   WHERE user_password LIKE '$2%' AND user_password NOT LIKE '{%';
   ```

3. 결과가 `{bcrypt}$2a$10$...` 형태인지 확인

4. 애플리케이션 재시작 후 로그인 테스트

### ❌ Flyway 마이그레이션이 실행되지 않음

**확인:**
```sql
SELECT * FROM flyway_schema_history WHERE version = '010';
```

**해결:**
- 결과가 없으면: Flyway가 V010을 인식하지 못한 것
- 수동으로 [fix_password_prefix_manual.sql](backend/fix_password_prefix_manual.sql) 실행

### ❌ "Bad credentials" 에러

**원인:** 비밀번호가 올바르지 않거나 사용자가 없음

**확인:**
```sql
SELECT user_id, enabled, not_locked FROM user WHERE user_id = 'your@email.com';
```

## 참고
- [Spring Security - Password Encoding](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html)
- [DelegatingPasswordEncoder](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/password/DelegatingPasswordEncoder.html)

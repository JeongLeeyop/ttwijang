-- =============================================
-- 수동 비밀번호 Prefix 추가 스크립트
-- 실행 방법: MySQL 클라이언트에서 직접 실행
-- =============================================

-- 1. 현재 상태 확인
SELECT 
    user_id,
    LEFT(user_password, 50) as password_preview,
    CASE 
        WHEN user_password LIKE '{%' THEN 'HAS PREFIX'
        WHEN user_password LIKE '$2%' THEN 'NO PREFIX (BCrypt)'
        ELSE 'UNKNOWN'
    END as password_status
FROM user
LIMIT 10;

-- 2. Prefix 없는 BCrypt 비밀번호 개수 확인
SELECT COUNT(*) as passwords_without_prefix
FROM user
WHERE user_password IS NOT NULL
  AND user_password NOT LIKE '{%'
  AND (user_password LIKE '$2a$%' 
       OR user_password LIKE '$2b$%' 
       OR user_password LIKE '$2y$%');

-- 3. 수동으로 Prefix 추가 (전체 사용자)
UPDATE user
SET user_password = CONCAT('{bcrypt}', user_password)
WHERE user_password IS NOT NULL
  AND user_password NOT LIKE '{%'
  AND (user_password LIKE '$2a$%' 
       OR user_password LIKE '$2b$%' 
       OR user_password LIKE '$2y$%');

-- 4. 특정 사용자만 업데이트 (테스트용)
-- UPDATE user
-- SET user_password = CONCAT('{bcrypt}', user_password)
-- WHERE user_id = 'leeyop12@naver.com'
--   AND user_password NOT LIKE '{%'
--   AND user_password LIKE '$2%';

-- 5. 업데이트 결과 확인
SELECT 
    user_id,
    LEFT(user_password, 60) as password_preview,
    CASE 
        WHEN user_password LIKE '{bcrypt}%' THEN 'OK - HAS BCRYPT PREFIX'
        WHEN user_password LIKE '{noop}%' THEN 'OK - HAS NOOP PREFIX'
        WHEN user_password LIKE '$2%' THEN 'ERROR - NO PREFIX'
        ELSE 'UNKNOWN'
    END as password_status
FROM user
LIMIT 10;

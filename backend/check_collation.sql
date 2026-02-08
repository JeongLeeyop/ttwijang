-- =============================================
-- Collation 확인 및 수정 스크립트
-- =============================================

-- 1. 데이터베이스 기본 Collation 확인
SELECT 
    SCHEMA_NAME,
    DEFAULT_CHARACTER_SET_NAME,
    DEFAULT_COLLATION_NAME
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'ttwijang';

-- 2. 모든 테이블의 Collation 확인
SELECT 
    TABLE_NAME,
    TABLE_COLLATION
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'ttwijang'
ORDER BY TABLE_NAME;

-- 3. role_code 컬럼의 Collation 확인
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    COLLATION_NAME,
    COLUMN_TYPE
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'ttwijang'
  AND COLUMN_NAME = 'role_code'
ORDER BY TABLE_NAME;

-- 4. user_role과 role 테이블 상세 정보
SHOW CREATE TABLE user_role;
SHOW CREATE TABLE role;

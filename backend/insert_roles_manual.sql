-- =============================================
-- 수동 Role 데이터 삽입 스크립트
-- =============================================

-- 1. 현재 role 테이블 확인
SELECT * FROM role;

-- 2. role 테이블이 비어있는지 확인
SELECT COUNT(*) as role_count FROM role;

-- 3. 기본 권한 데이터 삽입
INSERT INTO role (role_code, role_name, description, join_access_state, site_uid, create_date)
VALUES 
    ('ROLE_USER', '일반 사용자', '일반 사용자 권한', TRUE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW()),
    ('ROLE_ADMIN', '관리자', '관리자 권한', FALSE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW()),
    ('ROLE_MANAGER', '매니저', '매니저 권한', FALSE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW())
ON DUPLICATE KEY UPDATE
    role_name = VALUES(role_name),
    description = VALUES(description);

-- 4. 삽입 결과 확인
SELECT role_code, role_name, description FROM role;

-- 5. user_role 테이블 확인 (어떤 role_code가 사용되는지)
SELECT DISTINCT role_code, COUNT(*) as usage_count
FROM user_role
GROUP BY role_code;

-- 6. 사용자별 권한 확인
SELECT 
    u.user_id,
    u.email,
    ur.role_code,
    r.role_name
FROM user u
LEFT JOIN user_role ur ON u.uid = ur.user_uid
LEFT JOIN role r ON ur.role_code = r.role_code
LIMIT 10;

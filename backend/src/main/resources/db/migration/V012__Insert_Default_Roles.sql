-- =============================================
-- 마이그레이션: V012__Insert_Default_Roles.sql
-- 작성일: 2026-02-04
-- 설명: 기본 권한 데이터 삽입
-- =============================================

-- role 테이블에 기본 권한 데이터 삽입
INSERT INTO role (role_code, role_name, description, join_access_state, site_uid, create_date)
VALUES 
    ('ROLE_USER', '일반 사용자', '일반 사용자 권한', TRUE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW()),
    ('ROLE_ADMIN', '관리자', '관리자 권한', FALSE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW()),
    ('ROLE_MANAGER', '매니저', '매니저 권한', FALSE, '00070154-eb1d-4972-97b0-03365762fcc1', NOW())
ON DUPLICATE KEY UPDATE
    role_name = VALUES(role_name),
    description = VALUES(description);

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DELETE FROM role WHERE role_code IN ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER');

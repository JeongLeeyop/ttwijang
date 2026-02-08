-- =============================================
-- 마이그레이션: V011__Fix_Collation_Conflicts.sql
-- 작성일: 2026-02-04
-- 설명: Collation 충돌 해결 - 모든 테이블을 utf8mb4_unicode_ci로 통일
-- =============================================

-- user_role 테이블 Collation 수정
ALTER TABLE user_role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- role 테이블 Collation 수정 (존재하는 경우)
ALTER TABLE role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- user 테이블 Collation 수정
ALTER TABLE user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 기타 주요 테이블들도 통일
ALTER TABLE team CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE post CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE board CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE category CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- OAuth 테이블들도 통일
ALTER TABLE oauth_client_details CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE oauth_access_token CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE oauth_refresh_token CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE oauth_code CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE oauth_approvals CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE ClientDetails CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- =============================================
-- 롤백 스크립트 (필요시)
-- =============================================
-- ALTER TABLE user_role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
-- ALTER TABLE role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

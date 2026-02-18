-- =============================================
-- 마이그레이션: V021__Fix_Point_History_Team_Uid.sql
-- 작성일: 2026-02-19
-- 작성자: Developer
-- 설명: point_history 테이블의 team_uid 컬럼에 기본값 NULL 설정
-- =============================================

-- team_uid 컬럼이 존재하면 nullable + 기본값 NULL로 변경
ALTER TABLE point_history MODIFY COLUMN team_uid VARCHAR(255) NULL DEFAULT NULL;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- ALTER TABLE point_history MODIFY COLUMN team_uid VARCHAR(255);

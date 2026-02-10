-- =============================================
-- 마이그레이션: V017__Remove_Grade_Column_From_Team.sql
-- 작성일: 2026-02-10
-- 작성자: Developer
-- 설명: team 테이블에서 grade 컬럼 제거
--       A/B/C는 리그 이름의 일부이며, 팀에 별도 등급 필드가 필요 없음
-- =============================================

-- grade 컬럼 삭제
ALTER TABLE team DROP COLUMN IF EXISTS grade;

-- =============================================
-- 롤백 스크립트 (필요시 사용)
-- =============================================
-- ALTER TABLE team ADD COLUMN grade VARCHAR(10) COMMENT '팀 등급 (A, B, C)' AFTER manner_score;

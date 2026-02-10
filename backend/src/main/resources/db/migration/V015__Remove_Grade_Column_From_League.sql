-- =============================================
-- 마이그레이션: V015__Remove_Grade_Column_From_League.sql
-- 작성일: 2026-02-08
-- 작성자: Developer
-- 설명: league 테이블에서 grade 컬럼 제거
--       - BR-05: 리그에는 grade 등급이 존재하지 않음
--       - A리그, B리그, C리그는 이름(name)의 일부일 뿐
--       - 관련 인덱스도 함께 제거
-- =============================================

-- grade 컬럼 관련 인덱스 제거
DROP INDEX IF EXISTS idx_league_region_grade ON league;
DROP INDEX IF EXISTS idx_league_grade_region ON league;

-- grade 컬럼 제거
ALTER TABLE league DROP COLUMN IF EXISTS grade;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- ALTER TABLE league ADD COLUMN grade VARCHAR(10) COMMENT '리그 등급 (A, B, C)';
-- CREATE INDEX idx_league_region_grade ON league(region_sido, region_sigungu, grade);
-- CREATE INDEX idx_league_grade_region ON league(grade, region_sido);

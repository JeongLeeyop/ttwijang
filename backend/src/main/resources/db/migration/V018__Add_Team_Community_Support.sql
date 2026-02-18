-- V018__Add_Team_Community_Support.sql
-- 작성일: 2026-02-15
-- 설명: 팀 커뮤니티 기능 지원을 위한 post 테이블 team_uid 컬럼 추가

-- post 테이블에 team_uid 컬럼 추가
ALTER TABLE post ADD COLUMN team_uid VARCHAR(255) COMMENT '팀 고유값 (팀 커뮤니티용)' AFTER board_uid;

-- team_uid 인덱스 추가
CREATE INDEX idx_post_team_uid ON post (team_uid);

-- 복합 인덱스: team_uid + delete_status + create_date (팀 커뮤니티 목록 조회 최적화)
CREATE INDEX idx_post_team_community ON post (team_uid, delete_status, create_date);

-- 롤백: ALTER TABLE post DROP COLUMN team_uid;
-- 롤백: DROP INDEX idx_post_team_uid ON post;
-- 롤백: DROP INDEX idx_post_team_community ON post;

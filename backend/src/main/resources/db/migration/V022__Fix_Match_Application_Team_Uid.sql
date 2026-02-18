-- =============================================
-- 마이그레이션: V022__Fix_Match_Application_Team_Uid.sql
-- 작성일: 2026-02-03
-- 작성자: Developer
-- 설명: match_application 테이블의 기존 team_uid 컬럼을 nullable로 변경
--       (applicant_team_uid 컬럼으로 대체되었으므로 기존 team_uid는 더 이상 사용하지 않음)
-- =============================================

-- 외래키 제약조건 삭제 (team_uid -> team.uid)
ALTER TABLE match_application DROP FOREIGN KEY fk_application_team;

-- team_uid 컬럼을 nullable + 기본값 NULL로 변경
ALTER TABLE match_application MODIFY COLUMN team_uid VARCHAR(36) NULL DEFAULT NULL COMMENT '(deprecated) 신청 팀 UID - applicant_team_uid로 대체됨';

-- =============================================
-- 롤백 스크립트 (필요시 사용)
-- =============================================
-- ALTER TABLE match_application MODIFY COLUMN team_uid VARCHAR(36) NOT NULL COMMENT '신청 팀 UID';
-- ALTER TABLE match_application ADD CONSTRAINT fk_application_team FOREIGN KEY (team_uid) REFERENCES team(uid) ON DELETE CASCADE;

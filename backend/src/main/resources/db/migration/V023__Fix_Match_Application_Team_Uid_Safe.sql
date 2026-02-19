-- =============================================
-- 마이그레이션: V023__Fix_Match_Application_Team_Uid_Safe.sql
-- 작성일: 2026-02-19
-- 설명: match_application.team_uid 컬럼을 안전하게 NULL 허용으로 변경
--       (V022에서 FK drop 실패 시 대비)
-- =============================================

-- FK가 존재하는 경우에만 삭제 (조건부 실행)
SET @fk_exists = (
    SELECT COUNT(*)
    FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE()
    AND TABLE_NAME = 'match_application'
    AND CONSTRAINT_NAME = 'fk_application_team'
    AND CONSTRAINT_TYPE = 'FOREIGN KEY'
);

SET @sql = IF(@fk_exists > 0,
    'ALTER TABLE match_application DROP FOREIGN KEY fk_application_team',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- team_uid 컬럼이 NOT NULL인 경우에만 NULL 허용으로 변경
ALTER TABLE match_application
    MODIFY COLUMN team_uid VARCHAR(36) NULL DEFAULT NULL
    COMMENT '(deprecated) 신청 팀 UID - applicant_team_uid로 대체됨';

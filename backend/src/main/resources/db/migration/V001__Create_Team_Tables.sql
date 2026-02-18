-- =============================================
-- 마이그레이션: V001__Create_Team_Tables.sql
-- 작성일: 2025-01-01
-- 설명: 팀 관련 테이블 생성 (Team, TeamMember)
-- =============================================

-- 팀 테이블
CREATE TABLE IF NOT EXISTS team (
    uid VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL COMMENT '팀 이름',
    team_code VARCHAR(20) NOT NULL COMMENT '팀 고유 코드 (URL용)',
    description TEXT COMMENT '팀 소개',
    logo_url TEXT COMMENT '팀 로고 URL',
    established_year INT COMMENT '창단 연도',
    activity_days VARCHAR(100) COMMENT '활동 요일 (CSV)',
    activity_times VARCHAR(100) COMMENT '활동 시간대 (CSV)',
    region_sido VARCHAR(50) COMMENT '활동 지역 - 시/도',
    region_sigungu VARCHAR(50) COMMENT '활동 지역 - 시/군/구',
    home_stadium VARCHAR(100) COMMENT '홈 구장',
    age_groups INT DEFAULT 0 COMMENT '연령대 비트마스크',
    gender_type INT DEFAULT 2 COMMENT '0: 남성, 1: 여성, 2: 혼성',
    skill_level INT DEFAULT 1 COMMENT '실력 레벨 1-5',
    manner_score DOUBLE DEFAULT 4.0 COMMENT '매너 점수',
    member_count INT DEFAULT 1 COMMENT '팀원 수',
    total_sponsorship INT DEFAULT 0 COMMENT '총 후원금',
    owner_uid VARCHAR(36) NOT NULL COMMENT '팀장 UID',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, INACTIVE, DISBANDED',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    UNIQUE KEY uk_team_code (team_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 팀원 테이블
CREATE TABLE IF NOT EXISTS team_member (
    uid VARCHAR(36) NOT NULL,
    team_uid VARCHAR(36) NOT NULL COMMENT '팀 UID',
    user_uid VARCHAR(36) NOT NULL COMMENT '회원 UID',
    role VARCHAR(20) DEFAULT 'MEMBER' COMMENT 'OWNER, MANAGER, MEMBER',
    position VARCHAR(50) COMMENT '포지션',
    jersey_number INT COMMENT '등번호',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, ACTIVE, REJECTED, LEFT',
    application_message TEXT COMMENT '가입 신청 메시지',
    joined_date DATETIME COMMENT '가입 승인 일자',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    UNIQUE KEY uk_team_member (team_uid, user_uid),
    INDEX idx_team_member_team (team_uid),
    INDEX idx_team_member_user (user_uid),
    INDEX idx_team_member_status (status),
    CONSTRAINT fk_team_member_team FOREIGN KEY (team_uid) REFERENCES team(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 인덱스 생성
CREATE INDEX idx_team_region ON team(region_sido, region_sigungu);
CREATE INDEX idx_team_status ON team(status);
CREATE INDEX idx_team_owner ON team(owner_uid);

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP TABLE IF EXISTS team_member;
-- DROP TABLE IF EXISTS team;

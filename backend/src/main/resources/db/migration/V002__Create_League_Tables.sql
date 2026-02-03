-- =============================================
-- 마이그레이션: V002__Create_League_Tables.sql
-- 작성일: 2025-01-01
-- 설명: 리그 관련 테이블 생성 (League, LeagueTeam, LeagueMatch)
-- =============================================

-- 리그 테이블
CREATE TABLE IF NOT EXISTS league (
    uid VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL COMMENT '리그 이름',
    grade VARCHAR(1) NOT NULL COMMENT 'A, B, C 등급',
    season VARCHAR(50) COMMENT '시즌 정보',
    region_sido VARCHAR(50) COMMENT '지역 - 시/도',
    region_sigungu VARCHAR(50) COMMENT '지역 - 시/군/구',
    status VARCHAR(20) DEFAULT 'RECRUITING' COMMENT 'RECRUITING, IN_PROGRESS, FINISHED',
    max_teams INT DEFAULT 12 COMMENT '최대 참가 팀 수',
    current_teams INT DEFAULT 0 COMMENT '현재 참가 팀 수',
    start_date DATE COMMENT '리그 시작일',
    end_date DATE COMMENT '리그 종료일',
    description TEXT COMMENT '리그 소개',
    rules TEXT COMMENT '리그 규정',
    entry_fee INT DEFAULT 0 COMMENT '참가비',
    prize_info TEXT COMMENT '상금/경품 정보',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_league_grade (grade),
    INDEX idx_league_status (status),
    INDEX idx_league_region (region_sido, region_sigungu)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 리그 참가 팀 테이블
CREATE TABLE IF NOT EXISTS league_team (
    uid VARCHAR(36) NOT NULL,
    league_uid VARCHAR(36) NOT NULL COMMENT '리그 UID',
    team_uid VARCHAR(36) NOT NULL COMMENT '팀 UID',
    team_rank INT COMMENT '순위',
    points INT DEFAULT 0 COMMENT '승점',
    matches_played INT DEFAULT 0 COMMENT '경기 수',
    wins INT DEFAULT 0 COMMENT '승리',
    draws INT DEFAULT 0 COMMENT '무승부',
    losses INT DEFAULT 0 COMMENT '패배',
    goals_for INT DEFAULT 0 COMMENT '득점',
    goals_against INT DEFAULT 0 COMMENT '실점',
    goal_difference INT DEFAULT 0 COMMENT '골득실',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'PENDING, ACTIVE, WITHDRAWN',
    joined_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    UNIQUE KEY uk_league_team (league_uid, team_uid),
    INDEX idx_league_team_league (league_uid),
    INDEX idx_league_team_points (points DESC),
    CONSTRAINT fk_league_team_league FOREIGN KEY (league_uid) REFERENCES league(uid) ON DELETE CASCADE,
    CONSTRAINT fk_league_team_team FOREIGN KEY (team_uid) REFERENCES team(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 리그 경기 테이블
CREATE TABLE IF NOT EXISTS league_match (
    uid VARCHAR(36) NOT NULL,
    league_uid VARCHAR(36) NOT NULL COMMENT '리그 UID',
    round INT NOT NULL COMMENT '라운드',
    home_team_uid VARCHAR(36) NOT NULL COMMENT '홈팀 UID',
    away_team_uid VARCHAR(36) NOT NULL COMMENT '원정팀 UID',
    home_score INT COMMENT '홈팀 점수',
    away_score INT COMMENT '원정팀 점수',
    match_date DATE COMMENT '경기 일자',
    match_time TIME COMMENT '경기 시간',
    stadium_name VARCHAR(100) COMMENT '경기장',
    stadium_address VARCHAR(255) COMMENT '경기장 주소',
    status VARCHAR(20) DEFAULT 'SCHEDULED' COMMENT 'SCHEDULED, IN_PROGRESS, FINISHED, CANCELLED, POSTPONED',
    notes TEXT COMMENT '비고',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_league_match_league (league_uid),
    INDEX idx_league_match_date (match_date),
    INDEX idx_league_match_round (league_uid, round),
    CONSTRAINT fk_league_match_league FOREIGN KEY (league_uid) REFERENCES league(uid) ON DELETE CASCADE,
    CONSTRAINT fk_league_match_home FOREIGN KEY (home_team_uid) REFERENCES team(uid) ON DELETE CASCADE,
    CONSTRAINT fk_league_match_away FOREIGN KEY (away_team_uid) REFERENCES team(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP TABLE IF EXISTS league_match;
-- DROP TABLE IF EXISTS league_team;
-- DROP TABLE IF EXISTS league;

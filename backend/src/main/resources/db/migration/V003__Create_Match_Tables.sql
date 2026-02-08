-- =============================================
-- 마이그레이션: V003__Create_Match_Tables.sql
-- 작성일: 2025-01-01
-- 설명: 매치 및 게스트 모집 테이블 생성
-- =============================================

-- 풋살 매치 테이블
CREATE TABLE IF NOT EXISTS futsal_match (
    uid VARCHAR(36) NOT NULL,
    host_team_uid VARCHAR(36) NOT NULL COMMENT '주최 팀 UID',
    opponent_team_uid VARCHAR(36) COMMENT '상대 팀 UID',
    match_type VARCHAR(20) DEFAULT 'FRIENDLY' COMMENT 'FRIENDLY, FREE',
    match_format VARCHAR(20) DEFAULT 'FIVE_VS_FIVE' COMMENT 'FOUR_VS_FOUR, FIVE_VS_FIVE, SIX_VS_SIX, SEVEN_VS_SEVEN',
    match_date DATE NOT NULL COMMENT '경기 일자',
    match_time TIME NOT NULL COMMENT '경기 시간',
    stadium_name VARCHAR(100) COMMENT '구장명',
    stadium_address VARCHAR(255) COMMENT '구장 주소',
    region_sido VARCHAR(50) COMMENT '지역 - 시/도',
    region_sigungu VARCHAR(50) COMMENT '지역 - 시/군/구',
    fee INT DEFAULT 0 COMMENT '참가비',
    description TEXT COMMENT '추가 설명',
    gender_type INT DEFAULT 2 COMMENT '0: 남성, 1: 여성, 2: 무관',
    age_groups INT COMMENT '연령대 비트마스크',
    status VARCHAR(20) DEFAULT 'RECRUITING' COMMENT 'RECRUITING, MATCHED, COMPLETED, CANCELLED',
    host_score INT COMMENT '주최팀 점수',
    opponent_score INT COMMENT '상대팀 점수',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_match_date (match_date),
    INDEX idx_match_status (status),
    INDEX idx_match_region (region_sido, region_sigungu),
    INDEX idx_match_host (host_team_uid),
    CONSTRAINT fk_match_host FOREIGN KEY (host_team_uid) REFERENCES team(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 매치 신청 테이블
CREATE TABLE IF NOT EXISTS match_application (
    uid VARCHAR(36) NOT NULL,
    match_uid VARCHAR(36) NOT NULL COMMENT '매치 UID',
    team_uid VARCHAR(36) NOT NULL COMMENT '신청 팀 UID',
    user_uid VARCHAR(36) COMMENT '신청자 UID',
    message TEXT COMMENT '신청 메시지',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, ACCEPTED, REJECTED, CANCELLED',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_application_match (match_uid),
    INDEX idx_application_team (team_uid),
    CONSTRAINT fk_application_match FOREIGN KEY (match_uid) REFERENCES futsal_match(uid) ON DELETE CASCADE,
    CONSTRAINT fk_application_team FOREIGN KEY (team_uid) REFERENCES team(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 게스트(용병) 모집 테이블
CREATE TABLE IF NOT EXISTS guest_recruitment (
    uid VARCHAR(36) NOT NULL,
    team_uid VARCHAR(36) NOT NULL COMMENT '모집 팀 UID',
    match_uid VARCHAR(36) COMMENT '관련 매치 UID',
    match_date DATE NOT NULL COMMENT '경기 일자',
    match_time TIME NOT NULL COMMENT '경기 시간',
    stadium_name VARCHAR(100) COMMENT '구장명',
    stadium_address VARCHAR(255) COMMENT '구장 주소',
    region_sido VARCHAR(50) COMMENT '지역 - 시/도',
    region_sigungu VARCHAR(50) COMMENT '지역 - 시/군/구',
    gender_type INT DEFAULT 2 COMMENT '모집 성별',
    age_groups INT COMMENT '모집 연령대 비트마스크',
    position_type VARCHAR(20) DEFAULT 'ANY' COMMENT 'FIELD, GK, ANY',
    max_guests INT NOT NULL COMMENT '모집 인원',
    current_guests INT DEFAULT 0 COMMENT '현재 신청 인원',
    fee INT DEFAULT 0 COMMENT '참가비 (0: 무료)',
    guaranteed_minutes INT COMMENT '보장 시간 (분)',
    additional_info TEXT COMMENT '추가 안내',
    status VARCHAR(20) DEFAULT 'RECRUITING' COMMENT 'RECRUITING, COMPLETED, CANCELLED, EXPIRED',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_guest_date (match_date),
    INDEX idx_guest_status (status),
    INDEX idx_guest_team (team_uid),
    CONSTRAINT fk_guest_team FOREIGN KEY (team_uid) REFERENCES team(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 게스트 신청 테이블
CREATE TABLE IF NOT EXISTS guest_application (
    uid VARCHAR(36) NOT NULL,
    recruitment_uid VARCHAR(36) NOT NULL COMMENT '모집 UID',
    user_uid VARCHAR(36) NOT NULL COMMENT '신청자 UID',
    position VARCHAR(50) COMMENT '포지션',
    age INT COMMENT '나이',
    message TEXT COMMENT '신청 메시지',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, APPROVED, REJECTED, CANCELLED',
    payment_completed TINYINT(1) DEFAULT 0 COMMENT '결제 완료 여부',
    payment_amount INT COMMENT '결제 금액',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    UNIQUE KEY uk_guest_application (recruitment_uid, user_uid),
    INDEX idx_guest_app_recruitment (recruitment_uid),
    INDEX idx_guest_app_user (user_uid),
    CONSTRAINT fk_guest_app_recruitment FOREIGN KEY (recruitment_uid) REFERENCES guest_recruitment(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 팀원 모집 테이블
CREATE TABLE IF NOT EXISTS member_recruitment (
    uid VARCHAR(36) NOT NULL,
    team_uid VARCHAR(36) NOT NULL COMMENT '팀 UID',
    title VARCHAR(200) COMMENT '모집 제목',
    description TEXT COMMENT '모집 설명',
    positions VARCHAR(100) COMMENT '모집 포지션 (CSV)',
    requirements TEXT COMMENT '자격 요건',
    recruit_count INT DEFAULT 1 COMMENT '모집 인원',
    status VARCHAR(20) DEFAULT 'RECRUITING' COMMENT 'RECRUITING, COMPLETED, CANCELLED',
    expire_date DATE COMMENT '모집 마감일',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_member_recruit_team (team_uid),
    INDEX idx_member_recruit_status (status),
    CONSTRAINT fk_member_recruit_team FOREIGN KEY (team_uid) REFERENCES team(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP TABLE IF EXISTS member_recruitment;
-- DROP TABLE IF EXISTS guest_application;
-- DROP TABLE IF EXISTS guest_recruitment;
-- DROP TABLE IF EXISTS match_application;
-- DROP TABLE IF EXISTS futsal_match;

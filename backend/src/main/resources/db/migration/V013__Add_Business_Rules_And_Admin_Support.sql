-- =============================================
-- 마이그레이션: V013__Add_Business_Rules_And_Admin_Support.sql
-- 작성일: 2026-02-08
-- 작성자: Developer
-- 설명: 비즈니스 규칙 지원 및 관리자 기능 보강
--       BR-01 (1계정 1팀 가입) 인덱스 추가
--       BR-02 (1계정 1팀 생성) 인덱스 추가
--       BR-04/BR-05 (지역별/등급별 리그) 인덱스 추가
--       BR-11/BR-12 (관리자 리그/팀 관리) 지원
-- =============================================

-- =============================================
-- 1. team 테이블 보강
-- =============================================

-- 팀 후원 관련 컬럼 추가 (은행명, 계좌번호)
ALTER TABLE team
    ADD COLUMN IF NOT EXISTS bank_name VARCHAR(50) COMMENT '후원 계좌 은행명' AFTER home_stadium_address,
    ADD COLUMN IF NOT EXISTS bank_account VARCHAR(50) COMMENT '후원 계좌 번호' AFTER bank_name,
    ADD COLUMN IF NOT EXISTS refund_bank_name VARCHAR(50) COMMENT '환불 계좌 은행명' AFTER bank_account,
    ADD COLUMN IF NOT EXISTS refund_bank_account VARCHAR(50) COMMENT '환불 계좌 번호' AFTER refund_bank_name,
    ADD COLUMN IF NOT EXISTS active_days INT DEFAULT 0 COMMENT '활동 요일 비트마스크 (일=1,월=2,화=4,수=8,목=16,금=32,토=64)' AFTER refund_bank_account,
    ADD COLUMN IF NOT EXISTS active_time_slots INT DEFAULT 0 COMMENT '활동 시간대 비트마스크 (아침=1,낮=2,저녁=4,심야=8)' AFTER active_days,
    ADD COLUMN IF NOT EXISTS grade VARCHAR(10) COMMENT '팀 등급 (A, B, C)' AFTER manner_score,
    ADD COLUMN IF NOT EXISTS monthly_fee INT DEFAULT 0 COMMENT '월 회비' AFTER grade,
    ADD COLUMN IF NOT EXISTS sponsor_owner_uid VARCHAR(36) COMMENT '구단주 UID' AFTER owner_uid,
    ADD COLUMN IF NOT EXISTS recruiting_members BOOLEAN DEFAULT FALSE COMMENT '회원 모집 중 여부' AFTER status,
    ADD COLUMN IF NOT EXISTS feature_tags TEXT COMMENT '팀 특징 태그 (JSON)' AFTER recruiting_members,
    ADD COLUMN IF NOT EXISTS logo_file_uid VARCHAR(255) COMMENT '팀 로고 파일 UID' AFTER description,
    ADD COLUMN IF NOT EXISTS deleted_date DATETIME COMMENT '삭제 일시' AFTER updated_date;

-- BR-02: 1계정 1팀 생성 확인용 인덱스
CREATE INDEX IF NOT EXISTS idx_team_owner_status ON team(owner_uid, status);

-- 지역 기반 검색 인덱스
CREATE INDEX IF NOT EXISTS idx_team_region_status ON team(region_sido, region_sigungu, status);

-- 모집 중 팀 검색 인덱스
CREATE INDEX IF NOT EXISTS idx_team_recruiting ON team(recruiting_members, status);

-- =============================================
-- 2. team_member 테이블 보강
-- =============================================

-- 가입 신청서 관련 컬럼 추가
ALTER TABLE team_member
    ADD COLUMN IF NOT EXISTS application_region VARCHAR(100) COMMENT '가입 신청 - 지역' AFTER status,
    ADD COLUMN IF NOT EXISTS application_age INT COMMENT '가입 신청 - 나이' AFTER application_region,
    ADD COLUMN IF NOT EXISTS application_experience TEXT COMMENT '가입 신청 - 실력/경력' AFTER application_age,
    ADD COLUMN IF NOT EXISTS back_number INT COMMENT '등번호' AFTER position;

-- BR-01: 1계정 1팀 가입 확인용 인덱스
CREATE INDEX IF NOT EXISTS idx_team_member_user_status ON team_member(user_uid, status);

-- =============================================
-- 3. league 테이블 보강
-- =============================================

-- BR-04/BR-05: 지역+등급별 리그 검색 인덱스
CREATE INDEX IF NOT EXISTS idx_league_region_grade ON league(region_sido, region_sigungu, grade);
CREATE INDEX IF NOT EXISTS idx_league_grade_region ON league(grade, region_sido);
CREATE INDEX IF NOT EXISTS idx_league_region_status ON league(region_sido, region_sigungu, status);
CREATE INDEX IF NOT EXISTS idx_league_season ON league(season);

-- =============================================
-- 4. 회원 모집 테이블 보강
-- =============================================

CREATE TABLE IF NOT EXISTS member_recruitment (
    uid VARCHAR(36) NOT NULL,
    team_uid VARCHAR(36) NOT NULL COMMENT '모집 팀 UID',
    title VARCHAR(200) NOT NULL COMMENT '모집 제목',
    content TEXT COMMENT '모집 내용',
    feature_tags TEXT COMMENT '팀 특징 태그 (JSON)',
    active_days INT DEFAULT 0 COMMENT '활동 요일 비트마스크',
    active_time_slots INT DEFAULT 0 COMMENT '활동 시간대 비트마스크',
    region_sido VARCHAR(50) COMMENT '활동 지역 - 도/시',
    region_sigungu VARCHAR(50) COMMENT '활동 지역 - 시/군/구',
    monthly_fee INT DEFAULT 0 COMMENT '월 회비',
    gender_type INT DEFAULT 2 COMMENT '모집 성별 (0:남, 1:여, 2:무관)',
    age_groups INT DEFAULT 0 COMMENT '모집 연령대 비트마스크',
    target_count INT DEFAULT 0 COMMENT '모집 인원',
    current_applicants INT DEFAULT 0 COMMENT '현재 신청 인원',
    team_photo_uid VARCHAR(255) COMMENT '팀 단체 사진 UID',
    status VARCHAR(20) DEFAULT 'RECRUITING' COMMENT 'RECRUITING, COMPLETED, CLOSED',
    closing_date DATE COMMENT '모집 마감일',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_member_recruit_team (team_uid),
    INDEX idx_member_recruit_status (status),
    INDEX idx_member_recruit_region (region_sido, region_sigungu),
    CONSTRAINT fk_member_recruit_team FOREIGN KEY (team_uid) REFERENCES team(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 5. 지역 코드 테이블 (서비스 운영 지역 관리)
-- =============================================

CREATE TABLE IF NOT EXISTS region_code (
    uid VARCHAR(36) NOT NULL,
    code VARCHAR(10) NOT NULL COMMENT '지역 코드',
    name VARCHAR(50) NOT NULL COMMENT '지역명',
    parent_code VARCHAR(10) COMMENT '부모 코드 (시/도의 경우 NULL)',
    level INT DEFAULT 1 COMMENT '레벨 (1: 시/도, 2: 시/군/구)',
    sort_order INT DEFAULT 0 COMMENT '정렬 순서',
    enabled BOOLEAN DEFAULT TRUE COMMENT '사용 여부',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    UNIQUE KEY uk_region_code (code),
    INDEX idx_region_parent (parent_code),
    INDEX idx_region_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 6. 매너 평가 테이블
-- =============================================

CREATE TABLE IF NOT EXISTS manner_rating (
    uid VARCHAR(36) NOT NULL,
    rater_uid VARCHAR(36) NOT NULL COMMENT '평가자 UID',
    target_uid VARCHAR(36) NOT NULL COMMENT '피평가자 UID (팀 또는 개인)',
    target_type VARCHAR(20) NOT NULL COMMENT 'TEAM, USER',
    match_uid VARCHAR(36) COMMENT '관련 매치 UID',
    score DECIMAL(3,2) NOT NULL COMMENT '매너 점수 (0.0 ~ 5.0)',
    comment TEXT COMMENT '평가 코멘트',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_manner_rater (rater_uid),
    INDEX idx_manner_target (target_uid, target_type),
    INDEX idx_manner_match (match_uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 7. futsal_match 테이블 인덱스 보강
-- =============================================

-- BR-06: 소속 팀 매치 검색용 인덱스
CREATE INDEX IF NOT EXISTS idx_match_host ON futsal_match(host_team_uid);
CREATE INDEX IF NOT EXISTS idx_match_guest ON futsal_match(guest_team_uid);
CREATE INDEX IF NOT EXISTS idx_match_date_status ON futsal_match(match_date, status);
CREATE INDEX IF NOT EXISTS idx_match_region ON futsal_match(region_sido, region_sigungu);

-- =============================================
-- 8. guest_recruitment 테이블 인덱스 보강
-- =============================================

CREATE INDEX IF NOT EXISTS idx_guest_recruit_date ON guest_recruitment(match_date);
CREATE INDEX IF NOT EXISTS idx_guest_recruit_region ON guest_recruitment(region_sido, region_sigungu);
CREATE INDEX IF NOT EXISTS idx_guest_recruit_status ON guest_recruitment(status);

-- =============================================
-- 9. 기본 데이터: 경남 지역 코드 삽입
-- =============================================

INSERT INTO region_code (uid, code, name, parent_code, level, sort_order, enabled) VALUES
    (UUID(), '48', '경상남도', NULL, 1, 48, TRUE),
    (UUID(), '4817', '진주시', '48', 2, 1, TRUE),
    (UUID(), '4811', '창원시', '48', 2, 2, TRUE),
    (UUID(), '4831', '김해시', '48', 2, 3, TRUE),
    (UUID(), '4833', '양산시', '48', 2, 4, TRUE),
    (UUID(), '4835', '거제시', '48', 2, 5, TRUE),
    (UUID(), '4837', '통영시', '48', 2, 6, TRUE),
    (UUID(), '4813', '사천시', '48', 2, 7, TRUE),
    (UUID(), '4815', '밀양시', '48', 2, 8, TRUE)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP TABLE IF EXISTS manner_rating;
-- DROP TABLE IF EXISTS region_code;
-- DROP TABLE IF EXISTS member_recruitment;
-- DROP INDEX idx_guest_recruit_status ON guest_recruitment;
-- DROP INDEX idx_guest_recruit_region ON guest_recruitment;
-- DROP INDEX idx_guest_recruit_date ON guest_recruitment;
-- DROP INDEX idx_match_region ON futsal_match;
-- DROP INDEX idx_match_date_status ON futsal_match;
-- DROP INDEX idx_match_guest ON futsal_match;
-- DROP INDEX idx_match_host ON futsal_match;
-- DROP INDEX idx_league_season ON league;
-- DROP INDEX idx_league_region_status ON league;
-- DROP INDEX idx_league_grade_region ON league;
-- DROP INDEX idx_league_region_grade ON league;
-- DROP INDEX idx_team_member_user_status ON team_member;
-- DROP INDEX idx_team_recruiting ON team;
-- DROP INDEX idx_team_region_status ON team;
-- DROP INDEX idx_team_owner_status ON team;

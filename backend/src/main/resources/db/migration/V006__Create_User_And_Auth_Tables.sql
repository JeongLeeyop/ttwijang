-- =============================================
-- 마이그레이션: V006__Create_User_And_Auth_Tables.sql
-- 작성일: 2026-02-04
-- 설명: 사용자 및 인증 관련 테이블 생성
-- =============================================

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS user (
    uid VARCHAR(36) NOT NULL,
    site_uid VARCHAR(36) DEFAULT '00070154-eb1d-4972-97b0-03365762fcc1',
    user_id VARCHAR(100) NOT NULL COMMENT '사용자 아이디 (이메일)',
    user_password VARCHAR(255) COMMENT '비밀번호 (암호화)',
    actual_name VARCHAR(100) COMMENT '실명',
    concat_number VARCHAR(20) COMMENT '연락처',
    provider VARCHAR(20) COMMENT 'SOCIAL 제공처 (GOOGLE, NAVER, KAKAO 등)',
    provider_id VARCHAR(255) COMMENT 'SOCIAL 제공처 사용자 고유값',
    enabled BOOLEAN DEFAULT TRUE COMMENT '계정 사용여부',
    not_locked BOOLEAN DEFAULT TRUE COMMENT '계정 잠금여부',
    birth DATE COMMENT '생년월일',
    email VARCHAR(255) NOT NULL COMMENT '이메일',
    post_code VARCHAR(20) COMMENT '우편번호',
    address VARCHAR(500) COMMENT '주소',
    address_detail VARCHAR(500) COMMENT '상세주소',
    lat VARCHAR(50) COMMENT '위도',
    lon VARCHAR(50) COMMENT '경도',
    gender INT DEFAULT 0 COMMENT '성별 (0: 남성, 1: 여성)',
    withdraw_status BOOLEAN DEFAULT FALSE COMMENT '탈퇴여부',
    join_status BOOLEAN DEFAULT FALSE COMMENT '가입완료여부',
    marketing_status BOOLEAN DEFAULT FALSE COMMENT '마케팅 동의여부',
    point INT DEFAULT 0 COMMENT '포인트',
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    PRIMARY KEY (uid),
    UNIQUE KEY uk_user_email (email),
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_user_provider (provider, provider_id),
    INDEX idx_user_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 사용자 권한 테이블
CREATE TABLE IF NOT EXISTS user_role (
    uid VARCHAR(36) NOT NULL,
    user_uid VARCHAR(36) NOT NULL COMMENT '사용자 UID',
    role_code VARCHAR(50) NOT NULL COMMENT '권한 코드 (ROLE_USER, ROLE_ADMIN 등)',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    UNIQUE KEY uk_user_role (user_uid, role_code),
    INDEX idx_user_role_user (user_uid),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_uid) REFERENCES user(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- FCM 토큰 테이블 (푸시 알림용)
CREATE TABLE IF NOT EXISTS user_fcm_token (
    uid VARCHAR(36) NOT NULL,
    user_uid VARCHAR(36) NOT NULL COMMENT '사용자 UID',
    fcm_token TEXT NOT NULL COMMENT 'FCM 토큰',
    device_type VARCHAR(20) COMMENT 'ANDROID, IOS, WEB',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_fcm_token_user (user_uid),
    CONSTRAINT fk_fcm_token_user FOREIGN KEY (user_uid) REFERENCES user(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP TABLE IF EXISTS user_fcm_token;
-- DROP TABLE IF EXISTS user_role;
-- DROP TABLE IF EXISTS user;

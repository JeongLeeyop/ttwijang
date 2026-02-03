-- =============================================
-- 마이그레이션: V004__Create_Cash_Tables.sql
-- 작성일: 2025-01-01
-- 설명: 캐시/지갑, 후원, 매너 평가 테이블 생성
-- =============================================

-- 캐시 지갑 테이블
CREATE TABLE IF NOT EXISTS cash_wallet (
    uid VARCHAR(36) NOT NULL,
    user_uid VARCHAR(36) NOT NULL COMMENT '유저 UID',
    balance INT DEFAULT 0 COMMENT '현재 잔액',
    total_charged INT DEFAULT 0 COMMENT '총 충전 금액',
    total_used INT DEFAULT 0 COMMENT '총 사용 금액',
    last_charged_date DATETIME COMMENT '마지막 충전일',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    UNIQUE KEY uk_wallet_user (user_uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 캐시 거래 내역 테이블
CREATE TABLE IF NOT EXISTS cash_transaction (
    uid VARCHAR(36) NOT NULL,
    wallet_uid VARCHAR(36) NOT NULL COMMENT '지갑 UID',
    transaction_type VARCHAR(20) NOT NULL COMMENT 'CHARGE, USE, REFUND, EARN, CANCEL',
    amount INT NOT NULL COMMENT '거래 금액',
    balance_after INT COMMENT '거래 후 잔액',
    description VARCHAR(255) COMMENT '거래 설명',
    reference_id VARCHAR(100) COMMENT '외부 참조 ID',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_transaction_wallet (wallet_uid),
    INDEX idx_transaction_date (created_date),
    INDEX idx_transaction_type (transaction_type),
    CONSTRAINT fk_transaction_wallet FOREIGN KEY (wallet_uid) REFERENCES cash_wallet(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 팀 후원 테이블
CREATE TABLE IF NOT EXISTS team_sponsorship (
    uid VARCHAR(36) NOT NULL,
    team_uid VARCHAR(36) NOT NULL COMMENT '팀 UID',
    sponsor_uid VARCHAR(36) NOT NULL COMMENT '후원자 UID',
    sponsorship_type VARCHAR(20) NOT NULL COMMENT 'OWNER, ONE_TIME, REGULAR',
    amount INT COMMENT '후원 금액',
    total_amount INT DEFAULT 0 COMMENT '총 후원 금액',
    message VARCHAR(255) COMMENT '후원 메시지',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, CANCELLED, EXPIRED',
    last_payment_date DATETIME COMMENT '마지막 결제일',
    next_payment_date DATETIME COMMENT '다음 결제 예정일',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_sponsorship_team (team_uid),
    INDEX idx_sponsorship_sponsor (sponsor_uid),
    INDEX idx_sponsorship_type (sponsorship_type),
    CONSTRAINT fk_sponsorship_team FOREIGN KEY (team_uid) REFERENCES team(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 매너 평가 테이블
CREATE TABLE IF NOT EXISTS manner_rating (
    uid VARCHAR(36) NOT NULL,
    rater_uid VARCHAR(36) NOT NULL COMMENT '평가자 UID',
    rated_user_uid VARCHAR(36) NOT NULL COMMENT '피평가자 UID',
    match_uid VARCHAR(36) COMMENT '관련 매치 UID',
    guest_recruitment_uid VARCHAR(36) COMMENT '관련 게스트 모집 UID',
    rating_type VARCHAR(20) NOT NULL COMMENT 'MATCH, GUEST',
    score DECIMAL(3,2) NOT NULL COMMENT '평가 점수 (1.0-5.0)',
    comment TEXT COMMENT '평가 코멘트',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    UNIQUE KEY uk_rating (rater_uid, rated_user_uid, match_uid, guest_recruitment_uid),
    INDEX idx_rating_rated (rated_user_uid),
    INDEX idx_rating_type (rating_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 알림 테이블
CREATE TABLE IF NOT EXISTS notification (
    uid VARCHAR(36) NOT NULL,
    user_uid VARCHAR(36) NOT NULL COMMENT '수신자 UID',
    notification_type VARCHAR(20) NOT NULL COMMENT 'MATCH, GUEST, TEAM, LEAGUE, CASH, SYSTEM',
    title VARCHAR(100) COMMENT '알림 제목',
    message TEXT COMMENT '알림 내용',
    reference_uid VARCHAR(36) COMMENT '관련 엔티티 UID',
    reference_type VARCHAR(50) COMMENT '관련 엔티티 타입',
    is_read TINYINT(1) DEFAULT 0 COMMENT '읽음 여부',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_notification_user (user_uid),
    INDEX idx_notification_read (user_uid, is_read),
    INDEX idx_notification_date (created_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 지역 코드 테이블
CREATE TABLE IF NOT EXISTS region_code (
    code VARCHAR(10) NOT NULL,
    name VARCHAR(50) NOT NULL COMMENT '지역명',
    type VARCHAR(10) NOT NULL COMMENT 'SIDO, SIGUNGU',
    parent_code VARCHAR(10) COMMENT '상위 지역 코드',
    PRIMARY KEY (code),
    INDEX idx_region_type (type),
    INDEX idx_region_parent (parent_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 기본 데이터 삽입 (지역 코드)
-- =============================================
INSERT INTO region_code (code, name, type, parent_code) VALUES
('11', '서울특별시', 'SIDO', NULL),
('26', '부산광역시', 'SIDO', NULL),
('27', '대구광역시', 'SIDO', NULL),
('28', '인천광역시', 'SIDO', NULL),
('29', '광주광역시', 'SIDO', NULL),
('30', '대전광역시', 'SIDO', NULL),
('31', '울산광역시', 'SIDO', NULL),
('36', '세종특별자치시', 'SIDO', NULL),
('41', '경기도', 'SIDO', NULL),
('42', '강원도', 'SIDO', NULL),
('43', '충청북도', 'SIDO', NULL),
('44', '충청남도', 'SIDO', NULL),
('45', '전라북도', 'SIDO', NULL),
('46', '전라남도', 'SIDO', NULL),
('47', '경상북도', 'SIDO', NULL),
('48', '경상남도', 'SIDO', NULL),
('50', '제주특별자치도', 'SIDO', NULL)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 경상남도 시/군/구
INSERT INTO region_code (code, name, type, parent_code) VALUES
('48170', '진주시', 'SIGUNGU', '48'),
('48220', '창원시', 'SIGUNGU', '48'),
('48250', '김해시', 'SIGUNGU', '48'),
('48270', '양산시', 'SIGUNGU', '48'),
('48240', '통영시', 'SIGUNGU', '48'),
('48310', '거제시', 'SIGUNGU', '48')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 서울특별시 구
INSERT INTO region_code (code, name, type, parent_code) VALUES
('11680', '강남구', 'SIGUNGU', '11'),
('11740', '강동구', 'SIGUNGU', '11'),
('11305', '강북구', 'SIGUNGU', '11'),
('11500', '강서구', 'SIGUNGU', '11'),
('11620', '관악구', 'SIGUNGU', '11'),
('11215', '광진구', 'SIGUNGU', '11'),
('11530', '구로구', 'SIGUNGU', '11'),
('11545', '금천구', 'SIGUNGU', '11'),
('11350', '노원구', 'SIGUNGU', '11'),
('11320', '도봉구', 'SIGUNGU', '11'),
('11230', '동대문구', 'SIGUNGU', '11'),
('11590', '동작구', 'SIGUNGU', '11'),
('11440', '마포구', 'SIGUNGU', '11'),
('11410', '서대문구', 'SIGUNGU', '11'),
('11650', '서초구', 'SIGUNGU', '11'),
('11200', '성동구', 'SIGUNGU', '11'),
('11290', '성북구', 'SIGUNGU', '11'),
('11710', '송파구', 'SIGUNGU', '11'),
('11470', '양천구', 'SIGUNGU', '11'),
('11560', '영등포구', 'SIGUNGU', '11'),
('11170', '용산구', 'SIGUNGU', '11'),
('11380', '은평구', 'SIGUNGU', '11'),
('11110', '종로구', 'SIGUNGU', '11'),
('11140', '중구', 'SIGUNGU', '11'),
('11260', '중랑구', 'SIGUNGU', '11')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DELETE FROM region_code;
-- DROP TABLE IF EXISTS region_code;
-- DROP TABLE IF EXISTS notification;
-- DROP TABLE IF EXISTS manner_rating;
-- DROP TABLE IF EXISTS team_sponsorship;
-- DROP TABLE IF EXISTS cash_transaction;
-- DROP TABLE IF EXISTS cash_wallet;

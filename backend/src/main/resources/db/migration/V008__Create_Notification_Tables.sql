-- =============================================
-- 마이그레이션: V008__Create_Notification_Tables.sql
-- 작성일: 2026-02-04
-- 설명: 알림 관련 테이블 생성
-- =============================================

-- 알림 테이블
CREATE TABLE IF NOT EXISTS notification (
    uid VARCHAR(36) NOT NULL,
    user_uid VARCHAR(36) NOT NULL COMMENT '사용자 UID',
    type VARCHAR(50) NOT NULL COMMENT '알림 타입 (MATCH, LEAGUE, TEAM, POST 등)',
    title VARCHAR(255) NOT NULL COMMENT '알림 제목',
    content TEXT COMMENT '알림 내용',
    related_type VARCHAR(50) COMMENT '관련 타입',
    related_id VARCHAR(36) COMMENT '관련 ID',
    read_yn BOOLEAN DEFAULT FALSE COMMENT '읽음 여부',
    read_date DATETIME COMMENT '읽은 일시',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_notification_user (user_uid),
    INDEX idx_notification_read (read_yn, created_date),
    CONSTRAINT fk_notification_user FOREIGN KEY (user_uid) REFERENCES user(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 포인트 이력 테이블
CREATE TABLE IF NOT EXISTS point_history (
    uid VARCHAR(36) NOT NULL,
    user_uid VARCHAR(36) NOT NULL COMMENT '사용자 UID',
    amount INT NOT NULL COMMENT '포인트 증감량 (양수: 적립, 음수: 차감)',
    balance INT NOT NULL COMMENT '잔액',
    type VARCHAR(50) NOT NULL COMMENT '타입 (EARN, USE, REFUND 등)',
    reason VARCHAR(255) COMMENT '사유',
    related_type VARCHAR(50) COMMENT '관련 타입',
    related_id VARCHAR(36) COMMENT '관련 ID',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_point_history_user (user_uid),
    INDEX idx_point_history_created (created_date),
    CONSTRAINT fk_point_history_user FOREIGN KEY (user_uid) REFERENCES user(uid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP TABLE IF EXISTS point_history;
-- DROP TABLE IF EXISTS notification;

-- ====================================================
-- V024: 구단주 관련 테이블 생성
-- - sponsor_setting: 구단주 신청 금액 설정 (단일 행)
-- - team_sponsor_banner: 팀별 후원 배너 이미지
-- ====================================================

-- 구단주 신청 금액 설정 테이블
CREATE TABLE IF NOT EXISTS `sponsor_setting` (
    `uid`          VARCHAR(50)  NOT NULL COMMENT '설정 UID (항상 "default")',
    `owner_amount` INT          NOT NULL DEFAULT 0 COMMENT '구단주 신청 금액 (원)',
    `updated_date` DATETIME     COMMENT '최종 수정일시',
    PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='구단주 신청 금액 설정 (단일 행 테이블)';

-- 기본값 삽입
INSERT INTO `sponsor_setting` (`uid`, `owner_amount`, `updated_date`)
VALUES ('default', 0, NOW())
ON DUPLICATE KEY UPDATE `uid` = `uid`;

-- 팀별 후원 배너 이미지 테이블
CREATE TABLE IF NOT EXISTS `team_sponsor_banner` (
    `uid`          VARCHAR(50)   NOT NULL COMMENT '배너 UID',
    `team_uid`     VARCHAR(50)   NOT NULL COMMENT '대상 팀 UID',
    `team_name`    VARCHAR(100)  COMMENT '팀 이름 (조회 편의용)',
    `image_url`    VARCHAR(500)  NOT NULL COMMENT '배너 이미지 URL',
    `description`  TEXT          COMMENT '배너 설명',
    `created_date` DATETIME      COMMENT '등록일시',
    `updated_date` DATETIME      COMMENT '수정일시',
    PRIMARY KEY (`uid`),
    INDEX `idx_team_sponsor_banner_team_uid` (`team_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='팀별 후원 배너 이미지 (나의 팀 → 후원내역 표시용)';

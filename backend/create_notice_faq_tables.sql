-- ============================================================
-- 공지사항 / FAQ 테이블 생성 DDL
-- ============================================================

CREATE TABLE IF NOT EXISTS notice (
    uid          VARCHAR(36)  NOT NULL PRIMARY KEY,
    title        VARCHAR(200) NOT NULL,
    content      TEXT,
    created_date DATETIME(6),
    updated_date DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS faq (
    uid           VARCHAR(36)  NOT NULL PRIMARY KEY,
    question      VARCHAR(300) NOT NULL,
    answer        TEXT,
    category      VARCHAR(50),
    display_order INT          NOT NULL DEFAULT 0,
    created_date  DATETIME(6),
    updated_date  DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

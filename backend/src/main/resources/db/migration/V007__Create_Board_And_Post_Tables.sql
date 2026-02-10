-- =============================================
-- 마이그레이션: V007__Create_Board_And_Post_Tables.sql
-- 작성일: 2026-02-04
-- 설명: 게시판 및 게시글 관련 테이블 생성
-- =============================================

-- 게시판 테이블
CREATE TABLE IF NOT EXISTS board (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '게시판 이름',
    description TEXT COMMENT '게시판 설명',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 게시판 초기 데이터
INSERT INTO board (id, name, description) VALUES 
    (1, '영양상담', '영양 상담 게시판'),
    (2, '건강피드', '건강 관련 피드'),
    (3, '식단후기', '식단 후기 게시판'),
    (4, '공지사항', '공지사항 게시판'),
    (5, '서비스문의', '서비스 문의 게시판')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 카테고리 테이블
CREATE TABLE IF NOT EXISTS category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '카테고리 이름',
    sort_order INT DEFAULT 0 COMMENT '정렬 순서',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 게시글 테이블
CREATE TABLE IF NOT EXISTS post (
    id BIGINT NOT NULL AUTO_INCREMENT,
    account_id VARCHAR(36) NOT NULL COMMENT '작성자 UID',
    board_id BIGINT NOT NULL COMMENT '게시판 ID',
    category_id BIGINT COMMENT '카테고리 ID',
    title VARCHAR(255) NOT NULL COMMENT '제목',
    content LONGTEXT COMMENT '내용',
    count INT DEFAULT 0 COMMENT '조회수',
    score DECIMAL(3,2) COMMENT '평점',
    secret_yn CHAR(1) DEFAULT 'N' COMMENT '비밀글 여부',
    use_yn CHAR(1) DEFAULT 'Y' COMMENT '사용 여부',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_post_account_id (account_id),
    INDEX idx_post_board_id (board_id),
    INDEX idx_post_category_id (category_id),
    INDEX idx_post_created_date (created_date),
    INDEX idx_post_use_yn (use_yn),
    CONSTRAINT fk_post_board FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 게시글 좋아요 테이블
CREATE TABLE IF NOT EXISTS post_like (
    id BIGINT NOT NULL AUTO_INCREMENT,
    post_id BIGINT NOT NULL COMMENT '게시글 ID',
    account_id VARCHAR(36) NOT NULL COMMENT '사용자 UID',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_post_like (post_id, account_id),
    INDEX idx_post_like_post (post_id),
    INDEX idx_post_like_account (account_id),
    CONSTRAINT fk_post_like_post FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 게시글 태그 테이블
CREATE TABLE IF NOT EXISTS post_tag (
    id BIGINT NOT NULL AUTO_INCREMENT,
    post_id BIGINT NOT NULL COMMENT '게시글 ID',
    tag_name VARCHAR(50) NOT NULL COMMENT '태그명',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_post_tag_post (post_id),
    INDEX idx_post_tag_name (tag_name),
    CONSTRAINT fk_post_tag_post FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 첨부파일 테이블
CREATE TABLE IF NOT EXISTS attached_file (
    uid VARCHAR(36) NOT NULL,
    file_name VARCHAR(255) NOT NULL COMMENT '파일명',
    file_path TEXT NOT NULL COMMENT '파일 경로',
    file_size BIGINT COMMENT '파일 크기 (bytes)',
    file_type VARCHAR(100) COMMENT '파일 타입 (MIME)',
    related_type VARCHAR(50) COMMENT '관련 타입 (POST, USER 등)',
    related_id VARCHAR(36) COMMENT '관련 ID',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uid),
    INDEX idx_attached_file_related (related_type, related_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP TABLE IF EXISTS attached_file;
-- DROP TABLE IF EXISTS post_tag;
-- DROP TABLE IF EXISTS post_like;
-- DROP TABLE IF EXISTS post;
-- DROP TABLE IF EXISTS category;
-- DROP TABLE IF EXISTS board;

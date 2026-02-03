# Database Migration Script Guide (MySQL)

## 마이그레이션 스크립트 네이밍 규칙

```
V{버전번호}__{설명}.sql

예시:
V001__Create_Account_Table.sql
V002__Create_Post_Table.sql
V003__Add_Profile_Image_Column.sql
V004__Create_Index_For_Performance.sql
```

## 기본 마이그레이션 템플릿

### 테이블 생성 (CREATE TABLE)
```sql
-- =============================================
-- 마이그레이션: V001__Create_Account_Table.sql
-- 작성일: 2026-02-03
-- 작성자: Developer
-- 설명: 사용자 계정 테이블 생성
-- =============================================

-- 테이블 생성
CREATE TABLE IF NOT EXISTS account (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    tel VARCHAR(20),
    birth INT,
    gender TINYINT DEFAULT 0 COMMENT '0: 남성, 1: 여성',
    height DOUBLE,
    weight DOUBLE,
    dest_weight DOUBLE,
    dest_date DATE,
    activity_level INT DEFAULT 1,
    diet_purpose INT DEFAULT 0,
    diet_precaution INT DEFAULT 0,
    diet_check INT DEFAULT 0,
    authority INT DEFAULT 0 COMMENT '0: USER, 1: ADMIN',
    point INT DEFAULT 0,
    status VARCHAR(20),
    thumbnail_file_uid VARCHAR(255),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_date DATETIME,
    PRIMARY KEY (id),
    UNIQUE KEY uk_account_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 인덱스 생성 (성능 최적화)
CREATE INDEX idx_account_email ON account(email);
CREATE INDEX idx_account_created_date ON account(created_date);
CREATE INDEX idx_account_authority ON account(authority);

-- =============================================
-- 롤백 스크립트 (필요시 사용)
-- =============================================
-- DROP INDEX idx_account_authority ON account;
-- DROP INDEX idx_account_created_date ON account;
-- DROP INDEX idx_account_email ON account;
-- DROP TABLE IF EXISTS account;
```

### 게시판 관련 테이블 생성
```sql
-- =============================================
-- 마이그레이션: V002__Create_Board_Tables.sql
-- 작성일: 2026-02-03
-- 설명: 게시판, 카테고리, 게시글 테이블 생성
-- =============================================

-- 게시판 테이블
CREATE TABLE IF NOT EXISTS board (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 카테고리 테이블
CREATE TABLE IF NOT EXISTS category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    sort_order INT DEFAULT 0,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 게시글 테이블
CREATE TABLE IF NOT EXISTS post (
    id BIGINT NOT NULL AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    board_id BIGINT NOT NULL,
    category_id BIGINT,
    title VARCHAR(255) NOT NULL,
    content LONGTEXT,
    count INT DEFAULT 0 COMMENT '조회수',
    score DECIMAL(3,2) COMMENT '평점',
    secret_yn CHAR(1) DEFAULT 'N',
    use_yn CHAR(1) DEFAULT 'Y',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_post_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_board FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 인덱스 생성
CREATE INDEX idx_post_account_id ON post(account_id);
CREATE INDEX idx_post_board_id ON post(board_id);
CREATE INDEX idx_post_created_date ON post(created_date);
CREATE INDEX idx_post_use_yn ON post(use_yn);

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP TABLE IF EXISTS post;
-- DROP TABLE IF EXISTS category;
-- DROP TABLE IF EXISTS board;
```

### 컬럼 추가 (ALTER TABLE)
```sql
-- =============================================
-- 마이그레이션: V003__Add_Profile_Columns.sql
-- 작성일: 2026-02-03
-- 설명: account 테이블에 프로필 관련 컬럼 추가
-- =============================================

-- 컬럼 존재 여부 확인 후 추가
SET @dbname = DATABASE();
SET @tablename = 'account';
SET @columnname = 'profile_image_url';
SET @preparedStatement = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = @dbname
       AND TABLE_NAME = @tablename
       AND COLUMN_NAME = @columnname) > 0,
    'SELECT 1',
    'ALTER TABLE account ADD COLUMN profile_image_url TEXT AFTER thumbnail_file_uid'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 간단한 방식 (컬럼이 없을 때만 실행)
ALTER TABLE account 
    ADD COLUMN IF NOT EXISTS nickname VARCHAR(50) AFTER name,
    ADD COLUMN IF NOT EXISTS bio TEXT AFTER nickname,
    ADD COLUMN IF NOT EXISTS last_login_date DATETIME AFTER updated_date;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- ALTER TABLE account DROP COLUMN IF EXISTS last_login_date;
-- ALTER TABLE account DROP COLUMN IF EXISTS bio;
-- ALTER TABLE account DROP COLUMN IF EXISTS nickname;
-- ALTER TABLE account DROP COLUMN IF EXISTS profile_image_url;
```

### 컬럼 수정 (MODIFY COLUMN)
```sql
-- =============================================
-- 마이그레이션: V004__Modify_Column_Types.sql
-- 작성일: 2026-02-03
-- 설명: 컬럼 타입 및 속성 변경
-- =============================================

-- 컬럼 타입 변경
ALTER TABLE account MODIFY COLUMN tel VARCHAR(50);

-- 컬럼명 변경 (MySQL 8.0+)
ALTER TABLE account RENAME COLUMN tel TO phone_number;

-- 기본값 변경
ALTER TABLE account ALTER COLUMN point SET DEFAULT 100;

-- NOT NULL 제약조건 추가
ALTER TABLE account MODIFY COLUMN name VARCHAR(100) NOT NULL;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- ALTER TABLE account MODIFY COLUMN name VARCHAR(100);
-- ALTER TABLE account ALTER COLUMN point SET DEFAULT 0;
-- ALTER TABLE account RENAME COLUMN phone_number TO tel;
-- ALTER TABLE account MODIFY COLUMN tel VARCHAR(20);
```

### 인덱스 관리
```sql
-- =============================================
-- 마이그레이션: V005__Create_Indexes.sql
-- 작성일: 2026-02-03
-- 설명: 성능 최적화를 위한 인덱스 생성
-- =============================================

-- 단일 컬럼 인덱스
CREATE INDEX IF NOT EXISTS idx_post_title ON post(title);

-- 복합 인덱스 (자주 함께 조회되는 컬럼)
CREATE INDEX IF NOT EXISTS idx_post_board_created 
    ON post(board_id, created_date DESC);

CREATE INDEX IF NOT EXISTS idx_post_account_board 
    ON post(account_id, board_id, use_yn);

-- FULLTEXT 인덱스 (검색 성능 최적화)
CREATE FULLTEXT INDEX IF NOT EXISTS ft_post_content 
    ON post(title, content) WITH PARSER ngram;

-- 유니크 인덱스
CREATE UNIQUE INDEX IF NOT EXISTS uk_account_nickname 
    ON account(nickname);

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP INDEX uk_account_nickname ON account;
-- DROP INDEX ft_post_content ON post;
-- DROP INDEX idx_post_account_board ON post;
-- DROP INDEX idx_post_board_created ON post;
-- DROP INDEX idx_post_title ON post;
```

### 외래키 관리
```sql
-- =============================================
-- 마이그레이션: V006__Add_Foreign_Keys.sql
-- 작성일: 2026-02-03
-- 설명: 외래키 제약조건 추가
-- =============================================

-- 외래키 추가
ALTER TABLE post_like 
    ADD CONSTRAINT fk_post_like_post 
    FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE;

ALTER TABLE post_like 
    ADD CONSTRAINT fk_post_like_account 
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE;

-- 외래키 삭제 (필요시)
-- ALTER TABLE post_like DROP FOREIGN KEY fk_post_like_post;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- ALTER TABLE post_like DROP FOREIGN KEY fk_post_like_account;
-- ALTER TABLE post_like DROP FOREIGN KEY fk_post_like_post;
```

### 데이터 마이그레이션
```sql
-- =============================================
-- 마이그레이션: V007__Migrate_Data.sql
-- 작성일: 2026-02-03
-- 설명: 기존 데이터 마이그레이션
-- =============================================

-- 트랜잭션 시작
START TRANSACTION;

-- 기존 데이터 백업 (임시 테이블)
CREATE TABLE account_backup AS SELECT * FROM account;

-- 데이터 변환
UPDATE account 
SET gender = CASE 
    WHEN gender = 0 THEN 'MALE'
    WHEN gender = 1 THEN 'FEMALE'
    ELSE 'UNKNOWN'
END
WHERE gender IN (0, 1);

-- 기본 데이터 삽입
INSERT INTO board (id, name) VALUES 
    (1, '영양상담'),
    (2, '건강피드'),
    (3, '식단후기'),
    (4, '공지사항'),
    (5, '서비스문의')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 커밋
COMMIT;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- START TRANSACTION;
-- DROP TABLE IF EXISTS account;
-- RENAME TABLE account_backup TO account;
-- COMMIT;
```

### Soft Delete 구현
```sql
-- =============================================
-- 마이그레이션: V008__Implement_Soft_Delete.sql
-- 작성일: 2026-02-03
-- 설명: Soft Delete 패턴 구현
-- =============================================

-- deleted_at 컬럼 추가
ALTER TABLE account ADD COLUMN deleted_at DATETIME DEFAULT NULL;
ALTER TABLE post ADD COLUMN deleted_at DATETIME DEFAULT NULL;

-- Soft Delete용 인덱스
CREATE INDEX idx_account_deleted ON account(deleted_at);
CREATE INDEX idx_post_deleted ON post(deleted_at);

-- View 생성 (삭제되지 않은 데이터만 조회)
CREATE OR REPLACE VIEW v_active_accounts AS
SELECT * FROM account WHERE deleted_at IS NULL;

CREATE OR REPLACE VIEW v_active_posts AS
SELECT * FROM post WHERE deleted_at IS NULL AND use_yn = 'Y';

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP VIEW IF EXISTS v_active_posts;
-- DROP VIEW IF EXISTS v_active_accounts;
-- DROP INDEX idx_post_deleted ON post;
-- DROP INDEX idx_account_deleted ON account;
-- ALTER TABLE post DROP COLUMN deleted_at;
-- ALTER TABLE account DROP COLUMN deleted_at;
```

## 마이그레이션 실행 가이드 (Flyway)

### pom.xml 설정
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

### application.yml 설정
```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    baseline-version: 0
    validate-on-migrate: true
```

### 디렉토리 구조
```
src/main/resources/
└── db/
    └── migration/
        ├── V001__Create_Account_Table.sql
        ├── V002__Create_Board_Tables.sql
        ├── V003__Add_Profile_Columns.sql
        └── ...
```

## 마이그레이션 체크리스트

- [ ] 마이그레이션 파일 네이밍 규칙 준수 (`V{버전}__{설명}.sql`)
- [ ] 롤백 스크립트 작성
- [ ] 개발 환경에서 테스트 완료
- [ ] 인덱스 추가 시 성능 영향 분석
- [ ] 대용량 테이블 변경 시 다운타임 고려
- [ ] 외래키 제약조건 확인
- [ ] 문자셋 및 Collation 설정 확인 (`utf8mb4_unicode_ci`)

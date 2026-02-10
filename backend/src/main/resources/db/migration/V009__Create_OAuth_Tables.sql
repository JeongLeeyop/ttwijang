-- =============================================
-- 마이그레이션: V009__Create_OAuth_Tables.sql
-- 작성일: 2026-02-04
-- 작성자: Developer
-- 설명: Spring Security OAuth2 관련 테이블 생성
-- =============================================

-- OAuth2 클라이언트 정보 테이블
CREATE TABLE IF NOT EXISTS oauth_client_details (
    client_id VARCHAR(256) NOT NULL COMMENT 'OAuth 클라이언트 ID',
    resource_ids VARCHAR(256) COMMENT '리소스 ID',
    client_secret VARCHAR(256) COMMENT '클라이언트 시크릿 (암호화)',
    scope VARCHAR(256) COMMENT '접근 범위 (read, write 등)',
    authorized_grant_types VARCHAR(256) COMMENT '인가 방식 (password, refresh_token 등)',
    web_server_redirect_uri VARCHAR(256) COMMENT '리다이렉트 URI',
    authorities VARCHAR(256) COMMENT '권한',
    access_token_validity INT COMMENT '액세스 토큰 유효시간 (초)',
    refresh_token_validity INT COMMENT '리프레시 토큰 유효시간 (초)',
    additional_information VARCHAR(4096) COMMENT '추가 정보 (JSON)',
    autoapprove VARCHAR(256) COMMENT '자동 승인 여부',
    PRIMARY KEY (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- OAuth2 액세스 토큰 테이블
CREATE TABLE IF NOT EXISTS oauth_access_token (
    token_id VARCHAR(256) COMMENT '토큰 ID',
    token BLOB COMMENT '액세스 토큰',
    authentication_id VARCHAR(256) NOT NULL COMMENT '인증 ID',
    user_name VARCHAR(256) COMMENT '사용자명',
    client_id VARCHAR(256) COMMENT '클라이언트 ID',
    authentication BLOB COMMENT '인증 정보',
    refresh_token VARCHAR(256) COMMENT '리프레시 토큰 ID',
    PRIMARY KEY (authentication_id),
    INDEX idx_token_id (token_id),
    INDEX idx_client_id (client_id),
    INDEX idx_user_name (user_name),
    INDEX idx_refresh_token (refresh_token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- OAuth2 리프레시 토큰 테이블
CREATE TABLE IF NOT EXISTS oauth_refresh_token (
    token_id VARCHAR(256) COMMENT '토큰 ID',
    token BLOB COMMENT '리프레시 토큰',
    authentication BLOB COMMENT '인증 정보',
    INDEX idx_token_id (token_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- OAuth2 인가 코드 테이블 (authorization_code grant type 사용시)
CREATE TABLE IF NOT EXISTS oauth_code (
    code VARCHAR(256) COMMENT '인가 코드',
    authentication BLOB COMMENT '인증 정보',
    INDEX idx_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- OAuth2 승인 정보 테이블
CREATE TABLE IF NOT EXISTS oauth_approvals (
    userId VARCHAR(256) COMMENT '사용자 ID',
    clientId VARCHAR(256) COMMENT '클라이언트 ID',
    scope VARCHAR(256) COMMENT '접근 범위',
    status VARCHAR(10) COMMENT '승인 상태',
    expiresAt TIMESTAMP COMMENT '만료 시간',
    lastModifiedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '마지막 수정 시간'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 클라이언트 토큰 테이블
CREATE TABLE IF NOT EXISTS ClientDetails (
    appId VARCHAR(256) NOT NULL COMMENT '앱 ID',
    resourceIds VARCHAR(256) COMMENT '리소스 ID',
    appSecret VARCHAR(256) COMMENT '앱 시크릿',
    scope VARCHAR(256) COMMENT '접근 범위',
    grantTypes VARCHAR(256) COMMENT '인가 타입',
    redirectUrl VARCHAR(256) COMMENT '리다이렉트 URL',
    authorities VARCHAR(256) COMMENT '권한',
    access_token_validity INT COMMENT '액세스 토큰 유효시간',
    refresh_token_validity INT COMMENT '리프레시 토큰 유효시간',
    additionalInformation VARCHAR(4096) COMMENT '추가 정보',
    autoApproveScopes VARCHAR(256) COMMENT '자동 승인 범위',
    PRIMARY KEY (appId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 기본 OAuth 클라이언트 데이터 삽입
-- =============================================

-- 기본 OAuth 클라이언트 등록 (ttwijang 앱용)
-- client_secret: 'singhascrect!@#$' (프론트엔드에서 사용 중인 값)
-- 참고: BCrypt 암호화는 애플리케이션 시작 시 자동으로 처리됩니다.
INSERT INTO oauth_client_details (
    client_id,
    resource_ids,
    client_secret,
    scope,
    authorized_grant_types,
    web_server_redirect_uri,
    authorities,
    access_token_validity,
    refresh_token_validity,
    additional_information,
    autoapprove
) VALUES (
    'singha_oauth',
    'SinghaOAuthResourceIds',
    '{noop}singhascrect!@#$', -- {noop} 접두사: 평문 저장 (테스트용)
    'read,write',
    'password,refresh_token',
    NULL,
    'ROLE_CLIENT',
    86400,      -- 24시간 (초 단위)
    2592000,    -- 30일 (초 단위)
    NULL,
    'true'
) ON DUPLICATE KEY UPDATE
    resource_ids = VALUES(resource_ids),
    client_secret = VALUES(client_secret),
    scope = VALUES(scope),
    authorized_grant_types = VALUES(authorized_grant_types),
    access_token_validity = VALUES(access_token_validity),
    refresh_token_validity = VALUES(refresh_token_validity);

-- =============================================
-- 인덱스 최적화
-- =============================================

-- 성능 최적화를 위한 추가 인덱스
CREATE INDEX idx_oauth_access_created ON oauth_access_token(authentication_id, client_id);
CREATE INDEX idx_oauth_approvals_user ON oauth_approvals(userId, clientId);

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DROP TABLE IF EXISTS ClientDetails;
-- DROP TABLE IF EXISTS oauth_approvals;
-- DROP TABLE IF EXISTS oauth_code;
-- DROP TABLE IF EXISTS oauth_refresh_token;
-- DROP TABLE IF EXISTS oauth_access_token;
-- DROP TABLE IF EXISTS oauth_client_details;

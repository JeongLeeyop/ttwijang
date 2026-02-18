-- 배너 테이블 생성
CREATE TABLE IF NOT EXISTS banner (
  uid VARCHAR(36) PRIMARY KEY,
  title VARCHAR(255) NOT NULL COMMENT '배너 제목',
  image_url VARCHAR(500) NOT NULL COMMENT '배너 이미지 URL',
  link_url VARCHAR(500) COMMENT '클릭시 이동 URL',
  display_order INT DEFAULT 0 COMMENT '표시 순서 (낮을수록 먼저 표시)',
  status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '상태: ACTIVE, INACTIVE',
  start_date DATE COMMENT '노출 시작일',
  end_date DATE COMMENT '노출 종료일',
  region_sido VARCHAR(50) COMMENT '지역 시도',
  region_sigungu VARCHAR(50) COMMENT '지역 시군구',
  target_page VARCHAR(20) DEFAULT 'ALL' COMMENT '타겟 페이지: HOME, LEAGUE, MATCH, TEAM, ALL',
  created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_display_order (display_order),
  INDEX idx_status_dates (status, start_date, end_date),
  INDEX idx_target_page (target_page),
  INDEX idx_region (region_sigungu)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='배너 관리 테이블';

-- 샘플 배너 데이터 INSERT (진주시 리그 페이지용)
INSERT INTO banner (uid, title, image_url, link_url, display_order, status, start_date, end_date, region_sido, region_sigungu, target_page) VALUES
('banner-001', '2026 봄 시즌 리그 참가 모집', 'https://via.placeholder.com/800x300/FF6B6B/FFFFFF?text=2026+%EB%B4%84+%EC%8B%9C%EC%A6%8C+%EB%A6%AC%EA%B7%B8+%EC%B0%B8%EA%B0%80+%EB%AA%A8%EC%A7%91', '/league', 1, 'ACTIVE', '2026-02-01', '2026-03-31', '경상남도', '진주시', 'LEAGUE'),

('banner-002', '진주 풋살장 오픈 기념 이벤트', 'https://via.placeholder.com/800x300/4ECDC4/FFFFFF?text=%EC%A7%84%EC%A3%BC+%ED%92%8B%EC%82%B4%EC%9E%A5+%EC%98%A4%ED%94%88+%EA%B8%B0%EB%85%90', 'https://example.com/event', 2, 'ACTIVE', '2026-02-01', '2026-02-28', '경상남도', '진주시', 'LEAGUE'),

('banner-003', '뛰장 리그 우승팀 시상식', 'https://via.placeholder.com/800x300/95E1D3/333333?text=%EC%9A%B0%EC%8A%B9%ED%8C%80+%EC%8B%9C%EC%83%81%EC%8B%9D', '/league-status', 3, 'ACTIVE', '2026-01-15', '2026-02-28', '경상남도', '진주시', 'LEAGUE'),

('banner-004', '신규 회원 가입 이벤트', 'https://via.placeholder.com/800x300/F38181/FFFFFF?text=%EC%8B%A0%EA%B7%9C+%ED%9A%8C%EC%9B%90+%EA%B0%80%EC%9E%85+%EC%9D%B4%EB%B2%A4%ED%8A%B8', '/join', 4, 'ACTIVE', '2026-02-01', '2026-03-31', '경상남도', '진주시', 'ALL'),

('banner-005', '팀 매칭 서비스 오픈!', 'https://via.placeholder.com/800x300/AA96DA/FFFFFF?text=%ED%8C%80+%EB%A7%A4%EC%B9%AD+%EC%84%9C%EB%B9%84%EC%8A%A4', '/match', 5, 'ACTIVE', '2026-02-01', '2026-06-30', '경상남도', '진주시', 'LEAGUE');

-- 샘플 비활성 배너 (테스트용)
INSERT INTO banner (uid, title, image_url, link_url, display_order, status, start_date, end_date, region_sido, region_sigungu, target_page) VALUES
('banner-inactive-001', '종료된 이벤트', 'https://via.placeholder.com/800x300/CCCCCC/666666?text=%EC%A2%85%EB%A3%8C%EB%90%9C+%EC%9D%B4%EB%B2%A4%ED%8A%B8', '/old-event', 10, 'INACTIVE', '2026-01-01', '2026-01-31', '경상남도', '진주시', 'LEAGUE');

-- 확인 쿼리
SELECT uid, title, status, start_date, end_date, region_sigungu, target_page, display_order 
FROM banner 
ORDER BY display_order;

-- 활성 배너만 조회 (현재 날짜 기준)
SELECT uid, title, status, start_date, end_date, region_sigungu, target_page, display_order 
FROM banner 
WHERE status = 'ACTIVE' 
  AND (start_date IS NULL OR start_date <= CURDATE()) 
  AND (end_date IS NULL OR end_date >= CURDATE())
  AND target_page IN ('LEAGUE', 'ALL')
  AND region_sigungu = '진주시'
ORDER BY display_order;

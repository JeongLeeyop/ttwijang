-- =============================================
-- 마이그레이션: V014__Insert_League_Sample_Data.sql
-- 작성일: 2026-02-08
-- 작성자: Developer
-- 설명: 리그 샘플 데이터 삽입
--       - 지역별 리그 생성
--       - 2025-2026 시즌 리그 데이터
--       - 진행 중(IN_PROGRESS) 및 모집 중(RECRUITING) 상태
--       - A리그/B리그/C리그는 이름(name)의 일부 (별도 grade 필드 없음)
-- =============================================

-- =============================================
-- 1. 서울 지역 리그
-- =============================================

-- 서울 A리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 서울 A리그', '2025-2026', '서울', '강남구', 'IN_PROGRESS', 12, '2025-09-01', '2026-03-31', '서울 강남구 최상위 풋살 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 서울 B리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 서울 B리그', '2025-2026', '서울', '송파구', 'IN_PROGRESS', 16, '2025-09-01', '2026-03-31', '서울 송파구 중급 풋살 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 서울 C리그 (모집 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 서울 C리그', '2025-2026', '서울', '마포구', 'RECRUITING', 12, '2026-03-15', '2026-08-31', '서울 마포구 초급 풋살 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 2. 경기 지역 리그
-- =============================================

-- 경기 A리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 경기 A리그', '2025-2026', '경기', '성남시', 'IN_PROGRESS', 12, '2025-09-01', '2026-03-31', '경기 성남 프리미어 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 경기 B리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 경기 B리그', '2025-2026', '경기', '수원시', 'IN_PROGRESS', 16, '2025-09-01', '2026-03-31', '경기 수원 챌린저스 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 경기 C리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 경기 C리그', '2025-2026', '경기', '용인시', 'IN_PROGRESS', 12, '2025-09-15', '2026-04-15', '경기 용인 비기너스 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 3. 인천 지역 리그
-- =============================================

-- 인천 A리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 인천 A리그', '2025-2026', '인천', '남동구', 'IN_PROGRESS', 10, '2025-09-01', '2026-03-31', '인천 남동구 엘리트 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 인천 B리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 인천 B리그', '2025-2026', '인천', '연수구', 'IN_PROGRESS', 14, '2025-09-01', '2026-03-31', '인천 연수구 챔피언십', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 인천 C리그 (모집 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2026 상반기 인천 C리그', '2026', '인천', '부평구', 'RECRUITING', 12, '2026-04-01', '2026-08-31', '인천 부평구 신인 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 4. 부산 지역 리그
-- =============================================

-- 부산 A리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 부산 A리그', '2025-2026', '부산', '해운대구', 'IN_PROGRESS', 12, '2025-09-01', '2026-03-31', '부산 해운대 프리미어 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 부산 B리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 부산 B리그', '2025-2026', '부산', '동래구', 'IN_PROGRESS', 16, '2025-09-01', '2026-03-31', '부산 동래구 디비전1', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 부산 C리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 부산 C리그', '2025-2026', '부산', '사하구', 'IN_PROGRESS', 10, '2025-10-01', '2026-04-30', '부산 사하구 입문 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 5. 대구 지역 리그
-- =============================================

-- 대구 A리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 대구 A리그', '2025-2026', '대구', '수성구', 'IN_PROGRESS', 10, '2025-09-01', '2026-03-31', '대구 수성구 최강자전', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 대구 B리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 대구 B리그', '2025-2026', '대구', '달서구', 'IN_PROGRESS', 14, '2025-09-01', '2026-03-31', '대구 달서구 챌린지컵', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 6. 경남 지역 리그 - 진주 중심
-- =============================================

-- 경남 진주 A리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 경남 A리그', '2025-2026', '경남', '진주시', 'IN_PROGRESS', 10, '2025-09-01', '2026-03-31', '경남 진주시 프리미어 디비전', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 경남 창원 B리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 경남 B리그', '2025-2026', '경남', '창원시', 'IN_PROGRESS', 16, '2025-09-01', '2026-03-31', '경남 창원시 퍼스트 디비전', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 경남 김해 C리그 (모집 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2026 상반기 경남 C리그', '2026', '경남', '김해시', 'RECRUITING', 12, '2026-03-15', '2026-08-31', '경남 김해시 뉴비 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 7. 대전 지역 리그
-- =============================================

-- 대전 A리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 대전 A리그', '2025-2026', '대전', '유성구', 'IN_PROGRESS', 8, '2025-09-01', '2026-03-31', '대전 유성구 톱티어 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 대전 B리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 대전 B리그', '2025-2026', '대전', '서구', 'IN_PROGRESS', 12, '2025-09-01', '2026-03-31', '대전 서구 세컨드 디비전', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 8. 광주 지역 리그
-- =============================================

-- 광주 A리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 광주 A리그', '2025-2026', '광주', '북구', 'IN_PROGRESS', 10, '2025-09-01', '2026-03-31', '광주 북구 챔피언스 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 광주 B리그 (모집 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2026 상반기 광주 B리그', '2026', '광주', '광산구', 'RECRUITING', 14, '2026-03-01', '2026-08-31', '광주 광산구 오픈 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 9. 울산 지역 리그
-- =============================================

-- 울산 A리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 울산 A리그', '2025-2026', '울산', '남구', 'IN_PROGRESS', 8, '2025-09-01', '2026-03-31', '울산 남구 엘리트컵', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 울산 B리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 울산 B리그', '2025-2026', '울산', '중구', 'IN_PROGRESS', 12, '2025-09-01', '2026-03-31', '울산 중구 스탠다드 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 10. 강원 지역 리그
-- =============================================

-- 강원 B리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 강원 B리그', '2025-2026', '강원', '춘천시', 'IN_PROGRESS', 10, '2025-09-15', '2026-04-15', '강원 춘천시 지역 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 강원 C리그 (모집 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2026 상반기 강원 C리그', '2026', '강원', '원주시', 'RECRUITING', 8, '2026-04-01', '2026-08-31', '강원 원주시 풋살 페스티벌', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 11. 제주 지역 리그
-- =============================================

-- 제주 B리그 (진행 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2025-2026 제주 B리그', '2025-2026', '제주', '제주시', 'IN_PROGRESS', 8, '2025-10-01', '2026-04-30', '제주시 아일랜드 리그', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 제주 C리그 (모집 중)
INSERT INTO league (uid, name, season, region_sido, region_sigungu, status, max_teams, start_date, end_date, description, created_date, updated_date)
VALUES
    (UUID(), '2026 제주 오픈 C리그', '2026', '제주', '서귀포시', 'RECRUITING', 10, '2026-05-01', '2026-09-30', '서귀포시 오픈 풋살컵', NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 통계 확인 쿼리 (주석 처리)
-- =============================================

-- 지역별 리그 수 확인
-- SELECT region_sido, COUNT(*) as league_count
-- FROM league
-- GROUP BY region_sido
-- ORDER BY region_sido;

-- 상태별 리그 수 확인
-- SELECT status, COUNT(*) as league_count
-- FROM league
-- GROUP BY status;

-- 전체 리그 목록 확인
-- SELECT name, region_sido, region_sigungu, status, max_teams
-- FROM league
-- ORDER BY region_sido, name;

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DELETE FROM league WHERE season IN ('2025-2026', '2026');

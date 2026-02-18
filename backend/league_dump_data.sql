-- =============================================
-- 리그 페이지 덤프 데이터 생성 스크립트
-- 작성일: 2026-02-06
-- 설명: 리그, 팀, 리그 참가 팀, 경기 데이터 삽입
-- =============================================

-- =============================================
-- 1. 팀 데이터 삽입
-- =============================================
INSERT INTO team (
    uid, name, team_code, logo_file_uid, description,
    active_days, active_time_slots, gender_type, age_groups,
    region_sido, region_sigungu, total_sponsorship, home_stadium,
    home_stadium_address, manner_score, grade, monthly_fee,
    owner_uid, member_count, status, recruiting_members,
    created_date, updated_date
) VALUES
-- A리그 팀
('team-uuid-001', '최강숏FC', 'strongshot', 'logo-001', '최강의 팀을 꿈꾸는 최강숏FC입니다.',
    127, 14, 0, 30, '인천광역시', '연수구', 1500000, '송도풋살장',
    '인천광역시 연수구 송도동 123-45', 4.8, 'A', 50000,
    'admin-uid', 15, 'ACTIVE', false,
    NOW(), NOW()),

('team-uuid-002', '라이온FC', 'lionfc', 'logo-002', '사자처럼 강한 팀 라이온FC',
    96, 12, 0, 62, '서울특별시', '강남구', 2000000, '강남풋살장',
    '서울특별시 강남구 역삼동 123-45', 4.7, 'A', 60000,
    'admin-uid', 18, 'ACTIVE', true,
    NOW(), NOW()),

('team-uuid-003', '서울유나이티드', 'seoulunited', 'logo-003', '서울을 대표하는 유나이티드',
    32, 10, 0, 30, '서울특별시', '마포구', 1800000, '마포풋살장',
    '서울특별시 마포구 상암동 123-45', 4.9, 'A', 55000,
    'admin-uid', 20, 'ACTIVE', false,
    NOW(), NOW()),

-- B리그 팀
('team-uuid-004', '위더스FC', 'withersfc', 'logo-004', '함께하는 우리들, 위더스FC',
    48, 12, 0, 30, '인천광역시', '남동구', 800000, '구월풋살장',
    '인천광역시 남동구 구월동 123-45', 4.5, 'B', 40000,
    'admin-uid', 14, 'ACTIVE', true,
    NOW(), NOW()),

('team-uuid-005', '진주고FC', 'jinjugofc', 'logo-005', '진주의 자랑 진주고FC',
    64, 8, 0, 14, '경상남도', '진주시', 500000, '진주풋살장',
    '경상남도 진주시 평거동 123-45', 4.3, 'B', 35000,
    'admin-uid', 12, 'ACTIVE', true,
    NOW(), NOW()),

('team-uuid-006', '인천블루스', 'incheonblues', 'logo-006', '인천의 푸른 전사들',
    96, 12, 0, 30, '인천광역시', '부평구', 1200000, '부평풋살장',
    '인천광역시 부평구 부평동 123-45', 4.6, 'B', 45000,
    'admin-uid', 16, 'ACTIVE', false,
    NOW(), NOW()),

-- C리그 팀
('team-uuid-007', '아란치FC', 'arancifc', 'logo-007', '오렌지빛 열정의 아란치FC',
    32, 8, 0, 14, '서울특별시', '송파구', 600000, '송파풋살장',
    '서울특별시 송파구 잠실동 123-45', 4.2, 'C', 30000,
    'admin-uid', 10, 'ACTIVE', true,
    NOW(), NOW()),

('team-uuid-008', '경기타이탄', 'gyeonggititan', 'logo-008', '경기를 지배하는 타이탄',
    48, 10, 0, 30, '경기도', '수원시', 900000, '수원풋살장',
    '경기도 수원시 팔달구 매산로 123-45', 4.4, 'C', 35000,
    'admin-uid', 11, 'ACTIVE', true,
    NOW(), NOW()),

('team-uuid-009', '강남FC', 'gangnamfc', 'logo-009', '강남 스타일 강남FC',
    64, 12, 0, 62, '서울특별시', '강남구', 1500000, '선릉풋살장',
    '서울특별시 강남구 선릉로 123-45', 4.5, 'C', 50000,
    'admin-uid', 13, 'ACTIVE', false,
    NOW(), NOW()),

('team-uuid-010', '대성풋살클럽', 'daeseongfc', 'logo-010', '대성을 이루는 풋살클럽',
    32, 8, 0, 30, '인천광역시', '연수구', 700000, '대성풋살장',
    '인천광역시 연수구 청학동 123-45', 4.8, 'C', 40000,
    'admin-uid', 14, 'ACTIVE', true,
    NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 2. 리그 데이터 삽입
-- =============================================
INSERT INTO league (
    uid, name, grade, description, season,
    start_date, end_date, region_sido, region_sigungu,
    max_teams, current_teams, status, rules,
    created_date, updated_date
) VALUES
-- A리그 (진행중)
('league-uuid-a-001', '2026 인천 A리그', 'A',
    '인천 지역 최고 등급 풋살 리그입니다. 최상위 팀들의 치열한 경쟁이 펼쳐집니다.',
    '2026-01', '2026-01-15', '2026-06-30',
    '인천광역시', null, 8, 3, 'IN_PROGRESS',
    '1. 경기 시간: 전후반 각 20분\n2. 선수 등록: 최소 10명, 최대 15명\n3. 경고 누적 3회 시 1경기 출전 정지',
    NOW(), NOW()),

-- B리그 (진행중)
('league-uuid-b-001', '2026 인천 B리그', 'B',
    '인천 지역 중급 등급 풋살 리그입니다. 실력 향상과 팀워크를 다지는 리그입니다.',
    '2026-01', '2026-01-15', '2026-06-30',
    '인천광역시', null, 10, 3, 'IN_PROGRESS',
    '1. 경기 시간: 전후반 각 20분\n2. 선수 등록: 최소 8명, 최대 12명\n3. 경고 누적 3회 시 1경기 출전 정지',
    NOW(), NOW()),

-- C리그 (모집중)
('league-uuid-c-001', '2026 인천 C리그', 'C',
    '인천 지역 입문자 등급 풋살 리그입니다. 풋살을 처음 시작하는 팀들을 위한 리그입니다.',
    '2026-03', '2026-03-01', '2026-08-31',
    '인천광역시', null, 12, 4, 'RECRUITING',
    '1. 경기 시간: 전후반 각 20분\n2. 선수 등록: 최소 8명, 최대 12명\n3. 초보자 친화적 규칙 적용',
    NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- =============================================
-- 3. 리그 참가 팀 데이터 삽입
-- =============================================
INSERT INTO league_team (
    uid, league_uid, team_uid, ranking,
    played, wins, draws, losses,
    goals_for, goals_against, goal_difference, points,
    manner_score, created_date, updated_date
) VALUES
-- A리그 참가 팀
('lt-uuid-001', 'league-uuid-a-001', 'team-uuid-001', 1,
    10, 8, 1, 1, 45, 15, 30, 25, 4.8, NOW(), NOW()),
('lt-uuid-002', 'league-uuid-a-001', 'team-uuid-002', 2,
    10, 6, 3, 1, 35, 20, 15, 21, 4.7, NOW(), NOW()),
('lt-uuid-003', 'league-uuid-a-001', 'team-uuid-003', 3,
    10, 6, 2, 2, 32, 18, 14, 20, 4.9, NOW(), NOW()),

-- B리그 참가 팀
('lt-uuid-004', 'league-uuid-b-001', 'team-uuid-004', 1,
    8, 6, 1, 1, 28, 12, 16, 19, 4.5, NOW(), NOW()),
('lt-uuid-005', 'league-uuid-b-001', 'team-uuid-005', 2,
    8, 5, 2, 1, 24, 14, 10, 17, 4.3, NOW(), NOW()),
('lt-uuid-006', 'league-uuid-b-001', 'team-uuid-006', 3,
    8, 4, 3, 1, 22, 15, 7, 15, 4.6, NOW(), NOW()),

-- C리그 참가 팀 (아직 경기 시작 전)
('lt-uuid-007', 'league-uuid-c-001', 'team-uuid-007', 1,
    0, 0, 0, 0, 0, 0, 0, 0, 4.2, NOW(), NOW()),
('lt-uuid-008', 'league-uuid-c-001', 'team-uuid-008', 2,
    0, 0, 0, 0, 0, 0, 0, 0, 4.4, NOW(), NOW()),
('lt-uuid-009', 'league-uuid-c-001', 'team-uuid-009', 3,
    0, 0, 0, 0, 0, 0, 0, 0, 4.5, NOW(), NOW()),
('lt-uuid-010', 'league-uuid-c-001', 'team-uuid-010', 4,
    0, 0, 0, 0, 0, 0, 0, 0, 4.8, NOW(), NOW())
ON DUPLICATE KEY UPDATE ranking = VALUES(ranking);

-- =============================================
-- 4. 리그 경기 일정 데이터 삽입
-- =============================================
INSERT INTO league_match (
    uid, league_uid, round,
    home_team_uid, away_team_uid,
    match_date, match_time,
    stadium_name, stadium_address,
    home_score, away_score, status,
    created_date, updated_date
) VALUES
-- A리그 완료된 경기
('match-uuid-001', 'league-uuid-a-001', 1,
    'team-uuid-001', 'team-uuid-002',
    '2026-01-20', '19:00:00',
    '송도풋살장', '인천광역시 연수구 송도동 123-45',
    5, 2, 'COMPLETED', NOW(), NOW()),

('match-uuid-002', 'league-uuid-a-001', 1,
    'team-uuid-003', 'team-uuid-001',
    '2026-01-27', '18:00:00',
    '마포풋살장', '서울특별시 마포구 상암동 123-45',
    2, 3, 'COMPLETED', NOW(), NOW()),

-- A리그 예정 경기
('match-uuid-003', 'league-uuid-a-001', 2,
    'team-uuid-002', 'team-uuid-003',
    '2026-02-10', '19:00:00',
    '강남풋살장', '서울특별시 강남구 역삼동 123-45',
    null, null, 'SCHEDULED', NOW(), NOW()),

('match-uuid-004', 'league-uuid-a-001', 2,
    'team-uuid-001', 'team-uuid-003',
    '2026-02-17', '18:30:00',
    '송도풋살장', '인천광역시 연수구 송도동 123-45',
    null, null, 'SCHEDULED', NOW(), NOW()),

-- B리그 완료된 경기
('match-uuid-005', 'league-uuid-b-001', 1,
    'team-uuid-004', 'team-uuid-005',
    '2026-01-22', '20:00:00',
    '구월풋살장', '인천광역시 남동구 구월동 123-45',
    3, 2, 'COMPLETED', NOW(), NOW()),

('match-uuid-006', 'league-uuid-b-001', 1,
    'team-uuid-006', 'team-uuid-004',
    '2026-01-29', '19:30:00',
    '부평풋살장', '인천광역시 부평구 부평동 123-45',
    1, 1, 'COMPLETED', NOW(), NOW()),

-- B리그 예정 경기
('match-uuid-007', 'league-uuid-b-001', 2,
    'team-uuid-005', 'team-uuid-006',
    '2026-02-12', '20:00:00',
    '진주풋살장', '경상남도 진주시 평거동 123-45',
    null, null, 'SCHEDULED', NOW(), NOW()),

-- C리그 예정 경기 (아직 시작 전)
('match-uuid-008', 'league-uuid-c-001', 1,
    'team-uuid-007', 'team-uuid-008',
    '2026-03-05', '19:00:00',
    '송파풋살장', '서울특별시 송파구 잠실동 123-45',
    null, null, 'SCHEDULED', NOW(), NOW()),

('match-uuid-009', 'league-uuid-c-001', 1,
    'team-uuid-009', 'team-uuid-010',
    '2026-03-05', '20:30:00',
    '선릉풋살장', '서울특별시 강남구 선릉로 123-45',
    null, null, 'SCHEDULED', NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status);

-- =============================================
-- 5. 팀 멤버 데이터 삽입 (샘플)
-- =============================================
-- 실제 사용자 UID가 있다면 아래 admin-uid를 실제 UID로 변경하세요
INSERT INTO team_member (
    uid, team_uid, account_uid, role, status,
    jersey_number, position, join_date,
    created_date, updated_date
) VALUES
('tm-uuid-001', 'team-uuid-001', 'admin-uid', 'OWNER', 'ACTIVE',
    10, 'FW', '2025-12-01', NOW(), NOW()),
('tm-uuid-002', 'team-uuid-002', 'admin-uid', 'OWNER', 'ACTIVE',
    7, 'MF', '2025-12-01', NOW(), NOW()),
('tm-uuid-003', 'team-uuid-004', 'admin-uid', 'OWNER', 'ACTIVE',
    9, 'FW', '2025-12-01', NOW(), NOW())
ON DUPLICATE KEY UPDATE role = VALUES(role);

-- =============================================
-- 롤백 스크립트 (필요시 사용)
-- =============================================
-- DELETE FROM league_match WHERE uid LIKE 'match-uuid-%';
-- DELETE FROM league_team WHERE uid LIKE 'lt-uuid-%';
-- DELETE FROM league WHERE uid LIKE 'league-uuid-%';
-- DELETE FROM team_member WHERE uid LIKE 'tm-uuid-%';
-- DELETE FROM team WHERE uid LIKE 'team-uuid-%';

-- =============================================
-- 스크립트 실행 완료
-- =============================================
-- 데이터 삽입이 완료되었습니다.
-- 프론트엔드에서 리그 페이지를 확인하세요.

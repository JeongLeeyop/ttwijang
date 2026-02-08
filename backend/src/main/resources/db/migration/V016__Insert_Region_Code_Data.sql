-- =============================================
-- 마이그레이션: V016__Insert_Region_Code_Data.sql
-- 작성일: 2026-02-08
-- 설명: 지역 코드 샘플 데이터 삽입 (시/도, 시/군/구)
-- =============================================

-- 시/도 데이터 삽입 (level = 1)
INSERT INTO region_code (uid, code, name, parent_code, level, sort_order, enabled, created_date) VALUES
(UUID(), '11', '서울', NULL, 1, 1, TRUE, NOW()),
(UUID(), '41', '경기', NULL, 1, 2, TRUE, NOW()),
(UUID(), '28', '인천', NULL, 1, 3, TRUE, NOW()),
(UUID(), '26', '부산', NULL, 1, 4, TRUE, NOW()),
(UUID(), '27', '대구', NULL, 1, 5, TRUE, NOW()),
(UUID(), '30', '대전', NULL, 1, 6, TRUE, NOW()),
(UUID(), '29', '광주', NULL, 1, 7, TRUE, NOW()),
(UUID(), '31', '울산', NULL, 1, 8, TRUE, NOW()),
(UUID(), '48', '경남', NULL, 1, 9, TRUE, NOW()),
(UUID(), '47', '경북', NULL, 1, 10, TRUE, NOW()),
(UUID(), '44', '충남', NULL, 1, 11, TRUE, NOW()),
(UUID(), '43', '충북', NULL, 1, 12, TRUE, NOW()),
(UUID(), '46', '전남', NULL, 1, 13, TRUE, NOW()),
(UUID(), '45', '전북', NULL, 1, 14, TRUE, NOW()),
(UUID(), '42', '강원', NULL, 1, 15, TRUE, NOW()),
(UUID(), '50', '제주', NULL, 1, 16, TRUE, NOW());

-- 서울 시/군/구 (level = 2)
INSERT INTO region_code (uid, code, name, parent_code, level, sort_order, enabled, created_date) VALUES
(UUID(), '11010', '강남구', '11', 2, 1, TRUE, NOW()),
(UUID(), '11020', '강동구', '11', 2, 2, TRUE, NOW()),
(UUID(), '11030', '강북구', '11', 2, 3, TRUE, NOW()),
(UUID(), '11040', '강서구', '11', 2, 4, TRUE, NOW()),
(UUID(), '11050', '관악구', '11', 2, 5, TRUE, NOW()),
(UUID(), '11060', '광진구', '11', 2, 6, TRUE, NOW()),
(UUID(), '11070', '구로구', '11', 2, 7, TRUE, NOW()),
(UUID(), '11080', '금천구', '11', 2, 8, TRUE, NOW()),
(UUID(), '11090', '노원구', '11', 2, 9, TRUE, NOW()),
(UUID(), '11100', '도봉구', '11', 2, 10, TRUE, NOW()),
(UUID(), '11110', '동대문구', '11', 2, 11, TRUE, NOW()),
(UUID(), '11120', '동작구', '11', 2, 12, TRUE, NOW()),
(UUID(), '11130', '마포구', '11', 2, 13, TRUE, NOW()),
(UUID(), '11140', '서대문구', '11', 2, 14, TRUE, NOW()),
(UUID(), '11150', '서초구', '11', 2, 15, TRUE, NOW()),
(UUID(), '11160', '성동구', '11', 2, 16, TRUE, NOW()),
(UUID(), '11170', '성북구', '11', 2, 17, TRUE, NOW()),
(UUID(), '11180', '송파구', '11', 2, 18, TRUE, NOW()),
(UUID(), '11190', '양천구', '11', 2, 19, TRUE, NOW()),
(UUID(), '11200', '영등포구', '11', 2, 20, TRUE, NOW()),
(UUID(), '11210', '용산구', '11', 2, 21, TRUE, NOW()),
(UUID(), '11220', '은평구', '11', 2, 22, TRUE, NOW()),
(UUID(), '11230', '종로구', '11', 2, 23, TRUE, NOW()),
(UUID(), '11240', '중구', '11', 2, 24, TRUE, NOW()),
(UUID(), '11250', '중랑구', '11', 2, 25, TRUE, NOW());

-- 경기 시/군/구 (일부만 샘플로 추가)
INSERT INTO region_code (uid, code, name, parent_code, level, sort_order, enabled, created_date) VALUES
(UUID(), '41010', '수원시', '41', 2, 1, TRUE, NOW()),
(UUID(), '41020', '성남시', '41', 2, 2, TRUE, NOW()),
(UUID(), '41030', '고양시', '41', 2, 3, TRUE, NOW()),
(UUID(), '41040', '용인시', '41', 2, 4, TRUE, NOW()),
(UUID(), '41050', '부천시', '41', 2, 5, TRUE, NOW()),
(UUID(), '41060', '안산시', '41', 2, 6, TRUE, NOW()),
(UUID(), '41070', '안양시', '41', 2, 7, TRUE, NOW()),
(UUID(), '41080', '남양주시', '41', 2, 8, TRUE, NOW()),
(UUID(), '41090', '화성시', '41', 2, 9, TRUE, NOW()),
(UUID(), '41100', '평택시', '41', 2, 10, TRUE, NOW()),
(UUID(), '41110', '의정부시', '41', 2, 11, TRUE, NOW()),
(UUID(), '41120', '시흥시', '41', 2, 12, TRUE, NOW()),
(UUID(), '41130', '파주시', '41', 2, 13, TRUE, NOW()),
(UUID(), '41140', '김포시', '41', 2, 14, TRUE, NOW()),
(UUID(), '41150', '광명시', '41', 2, 15, TRUE, NOW());

-- 인천 시/군/구
INSERT INTO region_code (uid, code, name, parent_code, level, sort_order, enabled, created_date) VALUES
(UUID(), '28010', '중구', '28', 2, 1, TRUE, NOW()),
(UUID(), '28020', '동구', '28', 2, 2, TRUE, NOW()),
(UUID(), '28030', '남구', '28', 2, 3, TRUE, NOW()),
(UUID(), '28040', '연수구', '28', 2, 4, TRUE, NOW()),
(UUID(), '28050', '남동구', '28', 2, 5, TRUE, NOW()),
(UUID(), '28060', '부평구', '28', 2, 6, TRUE, NOW()),
(UUID(), '28070', '계양구', '28', 2, 7, TRUE, NOW()),
(UUID(), '28080', '서구', '28', 2, 8, TRUE, NOW()),
(UUID(), '28090', '강화군', '28', 2, 9, TRUE, NOW()),
(UUID(), '28100', '옹진군', '28', 2, 10, TRUE, NOW());

-- 부산 시/군/구
INSERT INTO region_code (uid, code, name, parent_code, level, sort_order, enabled, created_date) VALUES
(UUID(), '26010', '중구', '26', 2, 1, TRUE, NOW()),
(UUID(), '26020', '서구', '26', 2, 2, TRUE, NOW()),
(UUID(), '26030', '동구', '26', 2, 3, TRUE, NOW()),
(UUID(), '26040', '영도구', '26', 2, 4, TRUE, NOW()),
(UUID(), '26050', '부산진구', '26', 2, 5, TRUE, NOW()),
(UUID(), '26060', '동래구', '26', 2, 6, TRUE, NOW()),
(UUID(), '26070', '남구', '26', 2, 7, TRUE, NOW()),
(UUID(), '26080', '북구', '26', 2, 8, TRUE, NOW()),
(UUID(), '26090', '해운대구', '26', 2, 9, TRUE, NOW()),
(UUID(), '26100', '사하구', '26', 2, 10, TRUE, NOW()),
(UUID(), '26110', '금정구', '26', 2, 11, TRUE, NOW()),
(UUID(), '26120', '강서구', '26', 2, 12, TRUE, NOW()),
(UUID(), '26130', '연제구', '26', 2, 13, TRUE, NOW()),
(UUID(), '26140', '수영구', '26', 2, 14, TRUE, NOW()),
(UUID(), '26150', '사상구', '26', 2, 15, TRUE, NOW()),
(UUID(), '26160', '기장군', '26', 2, 16, TRUE, NOW());

-- 기타 광역시는 시/군/구 없이 시/도만 유지 (필요시 추가)

-- =============================================
-- 롤백 스크립트
-- =============================================
-- DELETE FROM region_code WHERE level = 2;
-- DELETE FROM region_code WHERE level = 1;

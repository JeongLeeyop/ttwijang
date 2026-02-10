-- 샘플 팀 2개를 진주시로 변경
UPDATE team 
SET region_sido = '경상남도', region_sigungu = '진주시'
WHERE uid IN (
    'team-4817-0001-0000-000000000001',
    'team-4817-0002-0000-000000000002'
);

-- 리그도 진주시로 변경
UPDATE league
SET region_sido = '경상남도', region_sigungu = '진주시'
WHERE uid IN (
    'league-4817-0001-0000-000000000001',
    'league-4817-0002-0000-000000000002'
);

-- 확인
SELECT uid, name, region_sido, region_sigungu FROM team WHERE uid LIKE 'team-4817%';
SELECT uid, name, region_sido, region_sigungu FROM league WHERE uid LIKE 'league-4817%';

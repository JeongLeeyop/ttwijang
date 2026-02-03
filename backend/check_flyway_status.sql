-- =============================================
-- Flyway 마이그레이션 상태 확인
-- =============================================

-- 1. Flyway 마이그레이션 히스토리 확인
SELECT 
    installed_rank,
    version,
    description,
    type,
    script,
    installed_on,
    execution_time,
    success
FROM flyway_schema_history
ORDER BY installed_rank DESC
LIMIT 20;

-- 2. V010 마이그레이션 실행 여부 확인
SELECT 
    version,
    description,
    installed_on,
    success
FROM flyway_schema_history
WHERE version = '010'
   OR description LIKE '%Password%';

-- 3. 마지막 마이그레이션 확인
SELECT 
    MAX(installed_rank) as last_rank,
    MAX(version) as last_version
FROM flyway_schema_history;

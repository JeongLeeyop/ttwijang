-- =============================================
-- 마이그레이션: V010__Add_Password_Encoder_Prefix.sql
-- 작성일: 2026-02-04
-- 작성자: Developer
-- 설명: 기존 비밀번호에 PasswordEncoder prefix 추가
-- =============================================

-- user 테이블의 비밀번호에 {bcrypt} prefix 추가
-- BCrypt 암호화된 비밀번호는 '$2a$', '$2b$', '$2y$'로 시작
UPDATE user
SET user_password = CONCAT('{bcrypt}', user_password)
WHERE user_password IS NOT NULL
  AND user_password NOT LIKE '{%'
  AND (user_password LIKE '$2a$%' 
       OR user_password LIKE '$2b$%' 
       OR user_password LIKE '$2y$%');

-- 평문 비밀번호가 있다면 {noop} prefix 추가 (보안상 권장하지 않음)
-- 주의: 실제 운영 환경에서는 평문 비밀번호를 사용하지 말 것
UPDATE user
SET user_password = CONCAT('{noop}', user_password)
WHERE user_password IS NOT NULL
  AND user_password NOT LIKE '{%'
  AND user_password NOT LIKE '$2a$%'
  AND user_password NOT LIKE '$2b$%'
  AND user_password NOT LIKE '$2y$%';

-- =============================================
-- 롤백 스크립트
-- =============================================
-- UPDATE user SET user_password = SUBSTRING(user_password, 9) WHERE user_password LIKE '{bcrypt}%';
-- UPDATE user SET user_password = SUBSTRING(user_password, 7) WHERE user_password LIKE '{noop}%';

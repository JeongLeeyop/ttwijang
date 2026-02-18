-- V019__Fix_Post_Account_Id_Default.sql
-- 작성일: 2026-02-19
-- 설명: post 테이블의 account_id 컬럼을 nullable로 변경
--       (JPA 엔티티에서 user_uid를 사용하며 account_id는 사용하지 않음)

ALTER TABLE post MODIFY COLUMN account_id VARCHAR(36) NULL COMMENT '작성자 UID (레거시, user_uid 사용)';

-- 롤백: ALTER TABLE post MODIFY COLUMN account_id VARCHAR(36) NOT NULL COMMENT '작성자 UID';

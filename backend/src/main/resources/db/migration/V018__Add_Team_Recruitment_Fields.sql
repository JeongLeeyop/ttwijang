-- V018: 팀 회원 모집 관련 필드 추가
ALTER TABLE team ADD COLUMN recruitment_description TEXT COMMENT '회원 모집 추가사항';
ALTER TABLE team ADD COLUMN team_photo_file_uid VARCHAR(255) COMMENT '팀 단체 사진 파일 UID';

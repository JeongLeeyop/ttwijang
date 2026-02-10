-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ttwijang
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attached_file`
--

DROP TABLE IF EXISTS `attached_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attached_file` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '파일명',
  `file_path` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '파일 경로',
  `file_size` bigint DEFAULT NULL COMMENT '파일 크기 (bytes)',
  `file_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '파일 타입 (MIME)',
  `related_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '관련 타입 (POST, USER 등)',
  `related_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '관련 ID',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_date` datetime DEFAULT NULL,
  `original_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `use_yn` char(1) COLLATE utf8mb4_unicode_ci DEFAULT 'Y',
  PRIMARY KEY (`uid`),
  KEY `idx_attached_file_related` (`related_type`,`related_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attached_file`
--

/*!40000 ALTER TABLE `attached_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `attached_file` ENABLE KEYS */;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '게시판 이름',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '게시판 설명',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auth_comment` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auth_read` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auth_reply` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auth_write` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comment_state` bit(1) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `file_count_limit` int NOT NULL,
  `file_size_limit` int NOT NULL,
  `file_use_state` bit(1) NOT NULL,
  `list_size` int NOT NULL,
  `notice_state` bit(1) NOT NULL,
  `private_state` bit(1) NOT NULL,
  `reply_state` bit(1) NOT NULL,
  `secret_state` bit(1) NOT NULL,
  `skin` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` (`id`, `name`, `description`, `created_date`, `uid`, `auth_comment`, `auth_read`, `auth_reply`, `auth_write`, `comment_state`, `create_date`, `file_count_limit`, `file_size_limit`, `file_use_state`, `list_size`, `notice_state`, `private_state`, `reply_state`, `secret_state`, `skin`) VALUES (1,'영양상담','영양 상담 게시판','2026-02-04 04:10:25','',NULL,NULL,NULL,NULL,_binary '\0',NULL,0,0,_binary '\0',0,_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),(2,'건강피드','건강 관련 피드','2026-02-04 04:10:25','',NULL,NULL,NULL,NULL,_binary '\0',NULL,0,0,_binary '\0',0,_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),(3,'식단후기','식단 후기 게시판','2026-02-04 04:10:25','',NULL,NULL,NULL,NULL,_binary '\0',NULL,0,0,_binary '\0',0,_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),(4,'공지사항','공지사항 게시판','2026-02-04 04:10:25','',NULL,NULL,NULL,NULL,_binary '\0',NULL,0,0,_binary '\0',0,_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL),(5,'서비스문의','서비스 문의 게시판','2026-02-04 04:10:25','',NULL,NULL,NULL,NULL,_binary '\0',NULL,0,0,_binary '\0',0,_binary '\0',_binary '\0',_binary '\0',_binary '\0',NULL);
/*!40000 ALTER TABLE `board` ENABLE KEYS */;

--
-- Table structure for table `board_category`
--

DROP TABLE IF EXISTS `board_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_category` (
  `uid` varchar(255) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `depth` int NOT NULL,
  `descript` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_uid` varchar(255) DEFAULT NULL,
  `view_order` int NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKkqi6tecqcjf57sman464espt5` (`parent_uid`),
  CONSTRAINT `FKkqi6tecqcjf57sman464espt5` FOREIGN KEY (`parent_uid`) REFERENCES `board_category` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_category`
--

/*!40000 ALTER TABLE `board_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_category` ENABLE KEYS */;

--
-- Table structure for table `board_field`
--

DROP TABLE IF EXISTS `board_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_field` (
  `uid` varchar(255) NOT NULL,
  `board_uid` varchar(255) DEFAULT NULL,
  `field_name` varchar(255) DEFAULT NULL,
  `field_type_code` varchar(255) DEFAULT NULL,
  `file_size_limit` int DEFAULT NULL,
  `input_limit` int NOT NULL,
  `required_state` bit(1) NOT NULL,
  `search_state` bit(1) NOT NULL,
  `view_order` int DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `UK_9q2ure13dmrf4xqcrgn4g5byf` (`field_type_code`),
  CONSTRAINT `FK88vcyr2t3vi1emug0q93tnu08` FOREIGN KEY (`field_type_code`) REFERENCES `field_type` (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_field`
--

/*!40000 ALTER TABLE `board_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_field` ENABLE KEYS */;

--
-- Table structure for table `board_role`
--

DROP TABLE IF EXISTS `board_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_role` (
  `uid` varchar(255) NOT NULL,
  `action` varchar(255) DEFAULT NULL,
  `board_uid` varchar(255) DEFAULT NULL,
  `role_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_role`
--

/*!40000 ALTER TABLE `board_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_role` ENABLE KEYS */;

--
-- Table structure for table `board_skin`
--

DROP TABLE IF EXISTS `board_skin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_skin` (
  `code` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_skin`
--

/*!40000 ALTER TABLE `board_skin` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_skin` ENABLE KEYS */;

--
-- Table structure for table `board_use_category`
--

DROP TABLE IF EXISTS `board_use_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_use_category` (
  `uid` varchar(255) NOT NULL,
  `board_uid` varchar(255) DEFAULT NULL,
  `category_uid` varchar(255) DEFAULT NULL,
  `view_order` int DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKekyasu7quvip33fdinwbise0r` (`category_uid`),
  CONSTRAINT `FKekyasu7quvip33fdinwbise0r` FOREIGN KEY (`category_uid`) REFERENCES `board_category` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_use_category`
--

/*!40000 ALTER TABLE `board_use_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_use_category` ENABLE KEYS */;

--
-- Table structure for table `cash_transaction`
--

DROP TABLE IF EXISTS `cash_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash_transaction` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `wallet_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '지갑 UID',
  `transaction_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'CHARGE, USE, REFUND, EARN, CANCEL',
  `amount` int NOT NULL COMMENT '거래 금액',
  `balance_after` int DEFAULT NULL COMMENT '거래 후 잔액',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '거래 설명',
  `reference_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '외부 참조 ID',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `payment_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reference_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reference_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `idx_transaction_wallet` (`wallet_uid`),
  KEY `idx_transaction_date` (`created_date`),
  KEY `idx_transaction_type` (`transaction_type`),
  KEY `FKi4mavq6lyys4r96swyoaxfpxg` (`user_uid`),
  CONSTRAINT `fk_transaction_wallet` FOREIGN KEY (`wallet_uid`) REFERENCES `cash_wallet` (`uid`) ON DELETE CASCADE,
  CONSTRAINT `FKi4mavq6lyys4r96swyoaxfpxg` FOREIGN KEY (`user_uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cash_transaction`
--

/*!40000 ALTER TABLE `cash_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `cash_transaction` ENABLE KEYS */;

--
-- Table structure for table `cash_wallet`
--

DROP TABLE IF EXISTS `cash_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash_wallet` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '유저 UID',
  `balance` int DEFAULT '0' COMMENT '현재 잔액',
  `total_charged` int DEFAULT '0' COMMENT '총 충전 금액',
  `total_used` int DEFAULT '0' COMMENT '총 사용 금액',
  `last_charged_date` datetime DEFAULT NULL COMMENT '마지막 충전일',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `total_refunded` int DEFAULT '0',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_wallet_user` (`user_uid`),
  CONSTRAINT `FKis9va3a6n9yw2stacc0mjqdk3` FOREIGN KEY (`user_uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cash_wallet`
--

/*!40000 ALTER TABLE `cash_wallet` DISABLE KEYS */;
/*!40000 ALTER TABLE `cash_wallet` ENABLE KEYS */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '카테고리 이름',
  `sort_order` int DEFAULT '0' COMMENT '정렬 순서',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

--
-- Table structure for table `clientdetails`
--

DROP TABLE IF EXISTS `clientdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientdetails` (
  `appId` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '앱 ID',
  `resourceIds` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '리소스 ID',
  `appSecret` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '앱 시크릿',
  `scope` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '접근 범위',
  `grantTypes` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '인가 타입',
  `redirectUrl` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '리다이렉트 URL',
  `authorities` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '권한',
  `access_token_validity` int DEFAULT NULL COMMENT '액세스 토큰 유효시간',
  `refresh_token_validity` int DEFAULT NULL COMMENT '리프레시 토큰 유효시간',
  `additionalInformation` varchar(4096) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '추가 정보',
  `autoApproveScopes` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '자동 승인 범위',
  PRIMARY KEY (`appId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientdetails`
--

/*!40000 ALTER TABLE `clientdetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `clientdetails` ENABLE KEYS */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `uid` varchar(255) NOT NULL,
  `contents` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `depth` int NOT NULL,
  `hide` bit(1) NOT NULL,
  `parent_uid` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `post_uid` varchar(255) DEFAULT NULL,
  `user_uid` varchar(255) DEFAULT NULL,
  `view_order` int NOT NULL,
  `writer` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKesf7ethov6up68yrgoa4712o5` (`parent_uid`),
  CONSTRAINT `FKesf7ethov6up68yrgoa4712o5` FOREIGN KEY (`parent_uid`) REFERENCES `comment` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;

--
-- Table structure for table `field_type`
--

DROP TABLE IF EXISTS `field_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `field_type` (
  `type_code` varchar(255) NOT NULL,
  `type_name` varchar(255) DEFAULT NULL,
  `view_order` int NOT NULL,
  PRIMARY KEY (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `field_type`
--

/*!40000 ALTER TABLE `field_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `field_type` ENABLE KEYS */;

--
-- Table structure for table `futsal_match`
--

DROP TABLE IF EXISTS `futsal_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `futsal_match` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `host_team_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '주최 팀 UID',
  `opponent_team_uid` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '상대 팀 UID',
  `match_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'FRIENDLY' COMMENT 'FRIENDLY, FREE',
  `match_format` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'FIVE_VS_FIVE' COMMENT 'FOUR_VS_FOUR, FIVE_VS_FIVE, SIX_VS_SIX, SEVEN_VS_SEVEN',
  `match_date` date NOT NULL COMMENT '경기 일자',
  `match_time` time NOT NULL COMMENT '경기 시간',
  `stadium_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '구장명',
  `stadium_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '구장 주소',
  `region_sido` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지역 - 시/도',
  `region_sigungu` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지역 - 시/군/구',
  `fee` int DEFAULT '0' COMMENT '참가비',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '추가 설명',
  `gender_type` int DEFAULT '2' COMMENT '0: 남성, 1: 여성, 2: 무관',
  `age_groups` int DEFAULT NULL COMMENT '연령대 비트마스크',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'RECRUITING' COMMENT 'RECRUITING, MATCHED, COMPLETED, CANCELLED',
  `host_score` int DEFAULT NULL COMMENT '주최팀 점수',
  `opponent_score` int DEFAULT NULL COMMENT '상대팀 점수',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `additional_info` text COLLATE utf8mb4_unicode_ci,
  `away_score` int DEFAULT NULL,
  `duration_hours` int DEFAULT NULL,
  `guest_team_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `home_score` int DEFAULT NULL,
  `recruitment_deadline` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `idx_match_date` (`match_date`),
  KEY `idx_match_status` (`status`),
  KEY `idx_match_region` (`region_sido`,`region_sigungu`),
  KEY `idx_match_host` (`host_team_uid`),
  KEY `FKxy82bnj665kf6eix3sc7gdqd` (`guest_team_uid`),
  CONSTRAINT `fk_match_host` FOREIGN KEY (`host_team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE,
  CONSTRAINT `FKxy82bnj665kf6eix3sc7gdqd` FOREIGN KEY (`guest_team_uid`) REFERENCES `team` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `futsal_match`
--

/*!40000 ALTER TABLE `futsal_match` DISABLE KEYS */;
/*!40000 ALTER TABLE `futsal_match` ENABLE KEYS */;

--
-- Table structure for table `guest_application`
--

DROP TABLE IF EXISTS `guest_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guest_application` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `recruitment_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '모집 UID',
  `user_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '신청자 UID',
  `position` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '포지션',
  `age` int DEFAULT NULL COMMENT '나이',
  `message` text COLLATE utf8mb4_unicode_ci COMMENT '신청 메시지',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING' COMMENT 'PENDING, APPROVED, REJECTED, CANCELLED',
  `payment_completed` tinyint(1) DEFAULT '0' COMMENT '결제 완료 여부',
  `payment_amount` int DEFAULT NULL COMMENT '결제 금액',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `payment_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `processed_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_guest_application` (`recruitment_uid`,`user_uid`),
  UNIQUE KEY `UKl02kdbn48y5b7pai8nxusl33h` (`recruitment_uid`,`user_uid`),
  KEY `idx_guest_app_recruitment` (`recruitment_uid`),
  KEY `idx_guest_app_user` (`user_uid`),
  CONSTRAINT `FK5h1i46p7dti80f623pghucg53` FOREIGN KEY (`user_uid`) REFERENCES `user` (`uid`),
  CONSTRAINT `fk_guest_app_recruitment` FOREIGN KEY (`recruitment_uid`) REFERENCES `guest_recruitment` (`uid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guest_application`
--

/*!40000 ALTER TABLE `guest_application` DISABLE KEYS */;
/*!40000 ALTER TABLE `guest_application` ENABLE KEYS */;

--
-- Table structure for table `guest_recruitment`
--

DROP TABLE IF EXISTS `guest_recruitment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guest_recruitment` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `team_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '모집 팀 UID',
  `match_uid` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '관련 매치 UID',
  `match_date` date NOT NULL COMMENT '경기 일자',
  `match_time` time NOT NULL COMMENT '경기 시간',
  `stadium_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '구장명',
  `stadium_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '구장 주소',
  `region_sido` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지역 - 시/도',
  `region_sigungu` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지역 - 시/군/구',
  `gender_type` int DEFAULT '2' COMMENT '모집 성별',
  `age_groups` int DEFAULT NULL COMMENT '모집 연령대 비트마스크',
  `position_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ANY' COMMENT 'FIELD, GK, ANY',
  `max_guests` int NOT NULL COMMENT '모집 인원',
  `current_guests` int DEFAULT '0' COMMENT '현재 신청 인원',
  `fee` int DEFAULT '0' COMMENT '참가비 (0: 무료)',
  `guaranteed_minutes` int DEFAULT NULL COMMENT '보장 시간 (분)',
  `additional_info` text COLLATE utf8mb4_unicode_ci COMMENT '추가 안내',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'RECRUITING' COMMENT 'RECRUITING, COMPLETED, CANCELLED, EXPIRED',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`),
  KEY `idx_guest_date` (`match_date`),
  KEY `idx_guest_status` (`status`),
  KEY `idx_guest_team` (`team_uid`),
  KEY `FK5v9vsofrjs8qbn3fn2ngjp37i` (`match_uid`),
  CONSTRAINT `FK5v9vsofrjs8qbn3fn2ngjp37i` FOREIGN KEY (`match_uid`) REFERENCES `futsal_match` (`uid`),
  CONSTRAINT `fk_guest_team` FOREIGN KEY (`team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guest_recruitment`
--

/*!40000 ALTER TABLE `guest_recruitment` DISABLE KEYS */;
/*!40000 ALTER TABLE `guest_recruitment` ENABLE KEYS */;

--
-- Table structure for table `league`
--

DROP TABLE IF EXISTS `league`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `league` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '리그 이름',
  `grade` varchar(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'A, B, C 등급',
  `season` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '시즌 정보',
  `region_sido` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지역 - 시/도',
  `region_sigungu` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지역 - 시/군/구',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'RECRUITING' COMMENT 'RECRUITING, IN_PROGRESS, FINISHED',
  `max_teams` int DEFAULT '12' COMMENT '최대 참가 팀 수',
  `current_teams` int DEFAULT '0' COMMENT '현재 참가 팀 수',
  `start_date` date DEFAULT NULL COMMENT '리그 시작일',
  `end_date` date DEFAULT NULL COMMENT '리그 종료일',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '리그 소개',
  `rules` text COLLATE utf8mb4_unicode_ci COMMENT '리그 규정',
  `entry_fee` int DEFAULT '0' COMMENT '참가비',
  `prize_info` text COLLATE utf8mb4_unicode_ci COMMENT '상금/경품 정보',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `banner_file_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `idx_league_grade` (`grade`),
  KEY `idx_league_status` (`status`),
  KEY `idx_league_region` (`region_sido`,`region_sigungu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `league`
--

/*!40000 ALTER TABLE `league` DISABLE KEYS */;
/*!40000 ALTER TABLE `league` ENABLE KEYS */;

--
-- Table structure for table `league_match`
--

DROP TABLE IF EXISTS `league_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `league_match` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `league_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '리그 UID',
  `round` int NOT NULL COMMENT '라운드',
  `home_team_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '홈팀 UID',
  `away_team_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '원정팀 UID',
  `home_score` int DEFAULT NULL COMMENT '홈팀 점수',
  `away_score` int DEFAULT NULL COMMENT '원정팀 점수',
  `match_date` date DEFAULT NULL COMMENT '경기 일자',
  `match_time` time DEFAULT NULL COMMENT '경기 시간',
  `stadium_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '경기장',
  `stadium_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '경기장 주소',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'SCHEDULED' COMMENT 'SCHEDULED, IN_PROGRESS, FINISHED, CANCELLED, POSTPONED',
  `notes` text COLLATE utf8mb4_unicode_ci COMMENT '비고',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `duration_minutes` int DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `idx_league_match_league` (`league_uid`),
  KEY `idx_league_match_date` (`match_date`),
  KEY `idx_league_match_round` (`league_uid`,`round`),
  KEY `fk_league_match_home` (`home_team_uid`),
  KEY `fk_league_match_away` (`away_team_uid`),
  CONSTRAINT `fk_league_match_away` FOREIGN KEY (`away_team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE,
  CONSTRAINT `fk_league_match_home` FOREIGN KEY (`home_team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE,
  CONSTRAINT `fk_league_match_league` FOREIGN KEY (`league_uid`) REFERENCES `league` (`uid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `league_match`
--

/*!40000 ALTER TABLE `league_match` DISABLE KEYS */;
/*!40000 ALTER TABLE `league_match` ENABLE KEYS */;

--
-- Table structure for table `league_team`
--

DROP TABLE IF EXISTS `league_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `league_team` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `league_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '리그 UID',
  `team_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '팀 UID',
  `team_rank` int DEFAULT NULL COMMENT '순위',
  `points` int DEFAULT '0' COMMENT '승점',
  `matches_played` int DEFAULT '0' COMMENT '경기 수',
  `wins` int DEFAULT '0' COMMENT '승리',
  `draws` int DEFAULT '0' COMMENT '무승부',
  `losses` int DEFAULT '0' COMMENT '패배',
  `goals_for` int DEFAULT '0' COMMENT '득점',
  `goals_against` int DEFAULT '0' COMMENT '실점',
  `goal_difference` int DEFAULT '0' COMMENT '골득실',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE' COMMENT 'PENDING, ACTIVE, WITHDRAWN',
  `joined_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `manner_score` decimal(3,2) DEFAULT '0.00',
  `played` int DEFAULT '0',
  `ranking` int DEFAULT '0',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_league_team` (`league_uid`,`team_uid`),
  UNIQUE KEY `UKqa470cpjrmr045mvyv12fijmm` (`league_uid`,`team_uid`),
  KEY `idx_league_team_league` (`league_uid`),
  KEY `idx_league_team_points` (`points` DESC),
  KEY `fk_league_team_team` (`team_uid`),
  CONSTRAINT `fk_league_team_league` FOREIGN KEY (`league_uid`) REFERENCES `league` (`uid`) ON DELETE CASCADE,
  CONSTRAINT `fk_league_team_team` FOREIGN KEY (`team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `league_team`
--

/*!40000 ALTER TABLE `league_team` DISABLE KEYS */;
/*!40000 ALTER TABLE `league_team` ENABLE KEYS */;

--
-- Table structure for table `manner_rating`
--

DROP TABLE IF EXISTS `manner_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manner_rating` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rater_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '평가자 UID',
  `rated_user_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '피평가자 UID',
  `match_uid` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '관련 매치 UID',
  `guest_recruitment_uid` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '관련 게스트 모집 UID',
  `rating_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'MATCH, GUEST',
  `score` decimal(3,2) NOT NULL COMMENT '평가 점수 (1.0-5.0)',
  `comment` text COLLATE utf8mb4_unicode_ci COMMENT '평가 코멘트',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `rated_team_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rater_user_uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tags` text COLLATE utf8mb4_unicode_ci,
  `target_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_rating` (`rater_uid`,`rated_user_uid`,`match_uid`,`guest_recruitment_uid`),
  KEY `idx_rating_rated` (`rated_user_uid`),
  KEY `idx_rating_type` (`rating_type`),
  KEY `FK9i8wg3cftj9hisxegd86diein` (`rated_team_uid`),
  KEY `FKs7em6b29245dy8alhbmna6x1k` (`rater_user_uid`),
  CONSTRAINT `FK9i8wg3cftj9hisxegd86diein` FOREIGN KEY (`rated_team_uid`) REFERENCES `team` (`uid`),
  CONSTRAINT `FKbj68y043f7obypxm6rm9anxr4` FOREIGN KEY (`rated_user_uid`) REFERENCES `user` (`uid`),
  CONSTRAINT `FKs7em6b29245dy8alhbmna6x1k` FOREIGN KEY (`rater_user_uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manner_rating`
--

/*!40000 ALTER TABLE `manner_rating` DISABLE KEYS */;
/*!40000 ALTER TABLE `manner_rating` ENABLE KEYS */;

--
-- Table structure for table `match_application`
--

DROP TABLE IF EXISTS `match_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `match_application` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '매치 UID',
  `team_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '신청 팀 UID',
  `user_uid` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '신청자 UID',
  `message` text COLLATE utf8mb4_unicode_ci COMMENT '신청 메시지',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING' COMMENT 'PENDING, ACCEPTED, REJECTED, CANCELLED',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `applicant_team_uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `applicant_user_uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `processed_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `idx_application_match` (`match_uid`),
  KEY `idx_application_team` (`team_uid`),
  KEY `FK9yx28da6uh8c9exg60yllsioy` (`applicant_team_uid`),
  KEY `FKc21n5vq74mwo90wupv0qho6ru` (`applicant_user_uid`),
  CONSTRAINT `FK9yx28da6uh8c9exg60yllsioy` FOREIGN KEY (`applicant_team_uid`) REFERENCES `team` (`uid`),
  CONSTRAINT `fk_application_match` FOREIGN KEY (`match_uid`) REFERENCES `futsal_match` (`uid`) ON DELETE CASCADE,
  CONSTRAINT `fk_application_team` FOREIGN KEY (`team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE,
  CONSTRAINT `FKc21n5vq74mwo90wupv0qho6ru` FOREIGN KEY (`applicant_user_uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_application`
--

/*!40000 ALTER TABLE `match_application` DISABLE KEYS */;
/*!40000 ALTER TABLE `match_application` ENABLE KEYS */;

--
-- Table structure for table `member_recruitment`
--

DROP TABLE IF EXISTS `member_recruitment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_recruitment` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `team_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '팀 UID',
  `title` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '모집 제목',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '모집 설명',
  `positions` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '모집 포지션 (CSV)',
  `requirements` text COLLATE utf8mb4_unicode_ci COMMENT '자격 요건',
  `recruit_count` int DEFAULT '1' COMMENT '모집 인원',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'RECRUITING' COMMENT 'RECRUITING, COMPLETED, CANCELLED',
  `expire_date` date DEFAULT NULL COMMENT '모집 마감일',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active_days` int DEFAULT NULL,
  `active_time_slots` int DEFAULT NULL,
  `age_groups` int DEFAULT NULL,
  `closing_date` date DEFAULT NULL,
  `content` text COLLATE utf8mb4_unicode_ci,
  `current_applicants` int DEFAULT '0',
  `feature_tags` text COLLATE utf8mb4_unicode_ci,
  `gender_type` int DEFAULT NULL,
  `monthly_fee` int DEFAULT NULL,
  `region_sido` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `region_sigungu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `target_count` int DEFAULT NULL,
  `team_photo_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `idx_member_recruit_team` (`team_uid`),
  KEY `idx_member_recruit_status` (`status`),
  CONSTRAINT `fk_member_recruit_team` FOREIGN KEY (`team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_recruitment`
--

/*!40000 ALTER TABLE `member_recruitment` DISABLE KEYS */;
/*!40000 ALTER TABLE `member_recruitment` ENABLE KEYS */;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '수신자 UID',
  `notification_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'MATCH, GUEST, TEAM, LEAGUE, CASH, SYSTEM',
  `title` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '알림 제목',
  `message` text COLLATE utf8mb4_unicode_ci COMMENT '알림 내용',
  `reference_uid` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '관련 엔티티 UID',
  `reference_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '관련 엔티티 타입',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '읽음 여부',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `action_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `content` text COLLATE utf8mb4_unicode_ci,
  `read_date` datetime DEFAULT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `idx_notification_user` (`user_uid`),
  KEY `idx_notification_read` (`user_uid`,`is_read`),
  KEY `idx_notification_date` (`created_date`),
  CONSTRAINT `FKiw9hruer08q0pcfrjdm7qapie` FOREIGN KEY (`user_uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;

--
-- Table structure for table `oauth_access_token`
--

DROP TABLE IF EXISTS `oauth_access_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '토큰 ID',
  `token` blob COMMENT '액세스 토큰',
  `authentication_id` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '인증 ID',
  `user_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '사용자명',
  `client_id` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '클라이언트 ID',
  `authentication` blob COMMENT '인증 정보',
  `refresh_token` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '리프레시 토큰 ID',
  PRIMARY KEY (`authentication_id`),
  KEY `idx_token_id` (`token_id`),
  KEY `idx_client_id` (`client_id`),
  KEY `idx_user_name` (`user_name`),
  KEY `idx_refresh_token` (`refresh_token`),
  KEY `idx_oauth_access_created` (`authentication_id`,`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_access_token`
--

/*!40000 ALTER TABLE `oauth_access_token` DISABLE KEYS */;
INSERT INTO `oauth_access_token` (`token_id`, `token`, `authentication_id`, `user_name`, `client_id`, `authentication`, `refresh_token`) VALUES ('cc341f612131c3117e4b37bf78dd7190',_binary '�\�\0sr\0Corg.springframework.security.oauth2.common.DefaultOAuth2AccessToken��6$�\�\0L\0additionalInformationt\0Ljava/util/Map;L\0\nexpirationt\0Ljava/util/Date;L\0refreshTokent\0?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\0scopet\0Ljava/util/Set;L\0	tokenTypet\0Ljava/lang/String;L\0valueq\0~\0xpsr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\nactualNamet\0	정이욥t\0statussr\0java.lang.Boolean\� r�՜�\�\0Z\0valuexpt\0jtit\0$d7b12051-c02e-47e3-925f-6ebfb10977d8x\0sr\0java.util.Datehj�KYt\0\0xpw\0\0�*�y\�xsr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationq\0~\0xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valueq\0~\0xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6ImQ3YjEyMDUxLWMwMmUtNDdlMy05MjVmLTZlYmZiMTA5NzdkOCIsImV4cCI6MTc3Mjc0NTc3OCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjAzMzliMTA1LTQxMWMtNDUyYS05MDkxLTEyODVmNmU1ZmVjNyIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.nwud_yrZTqaC649O0mh-9ONHHRiYSwjn8z_OOhDt3-Asq\0~\0w\0\0��\�\�\�xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writext\0bearert�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTc3MDI0MDE3OCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6ImQ3YjEyMDUxLWMwMmUtNDdlMy05MjVmLTZlYmZiMTA5NzdkOCIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.YpmJMwkyi2P6F1D_HMk_2SYTVirNByNobOneXPs2A2M','5964b5bb9557c4091680d712f7c42612','leeyop12@naver.com','singha_oauth',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.comxsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.comsr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�/\�xt\0leeyop12@naver.comsr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$b44556b8-bb41-4b1f-a426-dc4103109899q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�/\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$f5356076-28c2-46af-bfa9-c117ff2b80f4q\0~\0Ct\0$b44556b8-bb41-4b1f-a426-dc4103109899xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0D{bcrypt}$2a$10$D3nvYhvvWcFsUmdXv7z/hejr5c3CtdWpdkgrmgl6NhNQYI3j3eqw6','15c0afce52b811a8a3e00908a84b41a9'),('63abfa4c901804978fcf9aaa2d6c5744',_binary '�\�\0sr\0Corg.springframework.security.oauth2.common.DefaultOAuth2AccessToken��6$�\�\0L\0additionalInformationt\0Ljava/util/Map;L\0\nexpirationt\0Ljava/util/Date;L\0refreshTokent\0?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\0scopet\0Ljava/util/Set;L\0	tokenTypet\0Ljava/lang/String;L\0valueq\0~\0xpsr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\nactualNamet\0\n정이욥2t\0statussr\0java.lang.Boolean\� r�՜�\�\0Z\0valuexpt\0jtit\0$d907306c-9433-43af-a979-eceab952a96bx\0sr\0java.util.Datehj�KYt\0\0xpw\0\0�*o\�xsr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationq\0~\0xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valueq\0~\0xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlMiIsImF1ZCI6WyJTaW5naGFPQXV0aFJlc291cmNlSWRzIl0sInVzZXJfbmFtZSI6ImxlZXlvcDEyQG5hdmVyLmNvbTIiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiZDkwNzMwNmMtOTQzMy00M2FmLWE5NzktZWNlYWI5NTJhOTZiIiwiZXhwIjoxNzcyNzQ0MDQ1LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiOTcxM2U3NmUtN2Y0Mi00NmZjLTgzODAtNDA2ZDFkYmVmZDU0IiwiY2xpZW50X2lkIjoic2luZ2hhX29hdXRoIiwic3RhdHVzIjp0cnVlfQ.ku9pCJRlNFl3-SQpd1vCJ8Txe-ekWTYO5L-e2T1KMr0sq\0~\0w\0\0��\�t\�xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writext\0bearert�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlMiIsImF1ZCI6WyJTaW5naGFPQXV0aFJlc291cmNlSWRzIl0sInVzZXJfbmFtZSI6ImxlZXlvcDEyQG5hdmVyLmNvbTIiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNzcwMjM4NDQ1LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiZDkwNzMwNmMtOTQzMy00M2FmLWE5NzktZWNlYWI5NTJhOTZiIiwiY2xpZW50X2lkIjoic2luZ2hhX29hdXRoIiwic3RhdHVzIjp0cnVlfQ.nucvRUgzhHOx8c8qIcQO5GGlwrW3t7jM8RY5JI8WYMg','e38b0f270bad8ce42d15e5d407cda9b8','leeyop12@naver.com2','singha_oauth',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.com2xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.com2sr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0\n정이욥2ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493119sq\0~\0Ew\n\0\0\�6�xt\0leeyop12@naver.com2sr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$d13597c6-6d04-4155-8aa8-a6401608ca1aq\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�6�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$26348098-93b0-4095-8b13-15d13a169931q\0~\0Ct\0$d13597c6-6d04-4155-8aa8-a6401608ca1axsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0D{bcrypt}$2a$10$HIn8njKssvt0sXwPwEgicuiR.qc6XhFSL4MKvRXhw247GP98Qabse','e9fd0a637f4b17e03346f105331b3fa6');
/*!40000 ALTER TABLE `oauth_access_token` ENABLE KEYS */;

--
-- Table structure for table `oauth_approvals`
--

DROP TABLE IF EXISTS `oauth_approvals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oauth_approvals` (
  `userId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '사용자 ID',
  `clientId` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '클라이언트 ID',
  `scope` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '접근 범위',
  `status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '승인 상태',
  `expiresAt` timestamp NULL DEFAULT NULL COMMENT '만료 시간',
  `lastModifiedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '마지막 수정 시간',
  KEY `idx_oauth_approvals_user` (`userId`,`clientId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_approvals`
--

/*!40000 ALTER TABLE `oauth_approvals` DISABLE KEYS */;
/*!40000 ALTER TABLE `oauth_approvals` ENABLE KEYS */;

--
-- Table structure for table `oauth_client_details`
--

DROP TABLE IF EXISTS `oauth_client_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'OAuth 클라이언트 ID',
  `resource_ids` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '리소스 ID',
  `client_secret` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '클라이언트 시크릿 (암호화)',
  `scope` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '접근 범위 (read, write 등)',
  `authorized_grant_types` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '인가 방식 (password, refresh_token 등)',
  `web_server_redirect_uri` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '리다이렉트 URI',
  `authorities` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '권한',
  `access_token_validity` int DEFAULT NULL COMMENT '액세스 토큰 유효시간 (초)',
  `refresh_token_validity` int DEFAULT NULL COMMENT '리프레시 토큰 유효시간 (초)',
  `additional_information` varchar(4096) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '추가 정보 (JSON)',
  `autoapprove` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '자동 승인 여부',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_client_details`
--

/*!40000 ALTER TABLE `oauth_client_details` DISABLE KEYS */;
INSERT INTO `oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('singha_oauth','SinghaOAuthResourceIds','{noop}singhascrect!@#$','read,write','password,refresh_token',NULL,'ROLE_CLIENT',86400,2592000,NULL,'true');
/*!40000 ALTER TABLE `oauth_client_details` ENABLE KEYS */;

--
-- Table structure for table `oauth_code`
--

DROP TABLE IF EXISTS `oauth_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oauth_code` (
  `code` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '인가 코드',
  `authentication` blob COMMENT '인증 정보',
  KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_code`
--

/*!40000 ALTER TABLE `oauth_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `oauth_code` ENABLE KEYS */;

--
-- Table structure for table `oauth_refresh_token`
--

DROP TABLE IF EXISTS `oauth_refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '토큰 ID',
  `token` blob COMMENT '리프레시 토큰',
  `authentication` blob COMMENT '인증 정보',
  KEY `idx_token_id` (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth_refresh_token`
--

/*!40000 ALTER TABLE `oauth_refresh_token` DISABLE KEYS */;
INSERT INTO `oauth_refresh_token` (`token_id`, `token`, `authentication`) VALUES ('c1c71396027a4aa65fceddb871cf2ca8',_binary '�\�\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valuet\0Ljava/lang/String;xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6IjI0ZDRiNzkxLTJjZjktNGZlNi04YzgwLTY4NjY3MWMyMWI5NCIsImV4cCI6MTc3Mjc0MjkzNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjRjMGI5MDUyLWFjYWYtNGE1MC1iNmRlLWI1MzliMjgwODBiMyIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.cfybdEYi5iZlHqr1RUZYBWZFAHlAtrU-aboDJI2BPU8sr\0java.util.Datehj�KYt\0\0xpw\0\0����$x',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.comxsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.comsr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�/\�xt\0leeyop12@naver.comsr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$b44556b8-bb41-4b1f-a426-dc4103109899q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�/\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$f5356076-28c2-46af-bfa9-c117ff2b80f4q\0~\0Ct\0$b44556b8-bb41-4b1f-a426-dc4103109899xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0D{bcrypt}$2a$10$1ksJScV3lV0hsgyHMGmTL.Mt5KYghg6F8J0SZhqFFOK5Rk93RjEKq'),('e9fd0a637f4b17e03346f105331b3fa6',_binary '�\�\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valuet\0Ljava/lang/String;xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlMiIsImF1ZCI6WyJTaW5naGFPQXV0aFJlc291cmNlSWRzIl0sInVzZXJfbmFtZSI6ImxlZXlvcDEyQG5hdmVyLmNvbTIiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiZDkwNzMwNmMtOTQzMy00M2FmLWE5NzktZWNlYWI5NTJhOTZiIiwiZXhwIjoxNzcyNzQ0MDQ1LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiOTcxM2U3NmUtN2Y0Mi00NmZjLTgzODAtNDA2ZDFkYmVmZDU0IiwiY2xpZW50X2lkIjoic2luZ2hhX29hdXRoIiwic3RhdHVzIjp0cnVlfQ.ku9pCJRlNFl3-SQpd1vCJ8Txe-ekWTYO5L-e2T1KMr0sr\0java.util.Datehj�KYt\0\0xpw\0\0��\�t\�x',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.com2xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.com2sr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0\n정이욥2ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�6�xt\0leeyop12@naver.com2sr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$d13597c6-6d04-4155-8aa8-a6401608ca1aq\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�6�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$26348098-93b0-4095-8b13-15d13a169931q\0~\0Ct\0$d13597c6-6d04-4155-8aa8-a6401608ca1axsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0D{bcrypt}$2a$10$HIn8njKssvt0sXwPwEgicuiR.qc6XhFSL4MKvRXhw247GP98Qabse'),('7e0a2c271320ab420f61035f26e9ca49',_binary '�\�\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valuet\0Ljava/lang/String;xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6ImM4NWVlOTI3LTI3ZWEtNDJkZS1iYjdkLTZiYzAyYTRkZGRhYSIsImV4cCI6MTc3Mjc0NTQ0NywiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjBkOWI5OWM5LWVmM2EtNDFmZi1iNDZkLTJkNTE3OTkyM2VmMiIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.GDAfLyBl0aJtPgJ7ToSerub112ZekYcP7r_C-To03BIsr\0java.util.Datehj�KYt\0\0xpw\0\0��\�\�\�x',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.comxsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.comsr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�/\�xt\0leeyop12@naver.comsr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$b44556b8-bb41-4b1f-a426-dc4103109899q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�/\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$f5356076-28c2-46af-bfa9-c117ff2b80f4q\0~\0Ct\0$b44556b8-bb41-4b1f-a426-dc4103109899xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0D{bcrypt}$2a$10$b0yFlHk4.gl6pZSiO0JQwO6h6KzNqPbbYCjkJdy1/j6WH60bOrsgW'),('15c0afce52b811a8a3e00908a84b41a9',_binary '�\�\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valuet\0Ljava/lang/String;xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6ImQ3YjEyMDUxLWMwMmUtNDdlMy05MjVmLTZlYmZiMTA5NzdkOCIsImV4cCI6MTc3Mjc0NTc3OCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjAzMzliMTA1LTQxMWMtNDUyYS05MDkxLTEyODVmNmU1ZmVjNyIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.nwud_yrZTqaC649O0mh-9ONHHRiYSwjn8z_OOhDt3-Asr\0java.util.Datehj�KYt\0\0xpw\0\0��\�\�\�x',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.comxsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.comsr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�/\�xt\0leeyop12@naver.comsr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$b44556b8-bb41-4b1f-a426-dc4103109899q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�/\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$f5356076-28c2-46af-bfa9-c117ff2b80f4q\0~\0Ct\0$b44556b8-bb41-4b1f-a426-dc4103109899xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0D{bcrypt}$2a$10$D3nvYhvvWcFsUmdXv7z/hejr5c3CtdWpdkgrmgl6NhNQYI3j3eqw6');
/*!40000 ALTER TABLE `oauth_refresh_token` ENABLE KEYS */;

--
-- Table structure for table `payment_request`
--

DROP TABLE IF EXISTS `payment_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_request` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `order_id` varchar(255) DEFAULT NULL,
  `order_name` varchar(255) DEFAULT NULL,
  `status` int NOT NULL,
  `user_uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_request`
--

/*!40000 ALTER TABLE `payment_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_request` ENABLE KEYS */;

--
-- Table structure for table `payment_result`
--

DROP TABLE IF EXISTS `payment_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_result` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `approved_at` varchar(255) DEFAULT NULL,
  `balance_amount` int NOT NULL,
  `culture_expense` bit(1) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `last_transaction_key` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `order_name` varchar(255) DEFAULT NULL,
  `payment_key` varchar(255) DEFAULT NULL,
  `requested_at` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `supplied_amount` int NOT NULL,
  `tax_free_amount` int NOT NULL,
  `total_amount` int NOT NULL,
  `use_escrow` bit(1) DEFAULT NULL,
  `user_uid` varchar(255) DEFAULT NULL,
  `vat` int NOT NULL,
  `version` date DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_result`
--

/*!40000 ALTER TABLE `payment_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_result` ENABLE KEYS */;

--
-- Table structure for table `payment_result_card`
--

DROP TABLE IF EXISTS `payment_result_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_result_card` (
  `idx` bigint NOT NULL AUTO_INCREMENT,
  `approved_at` datetime DEFAULT NULL,
  `balance_amount` int NOT NULL,
  `culture_expense` bit(1) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `last_transaction_key` varchar(255) DEFAULT NULL,
  `method` int NOT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `order_name` varchar(255) DEFAULT NULL,
  `payment_key` varchar(255) DEFAULT NULL,
  `requested_at` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `supplied_amount` int NOT NULL,
  `tax_free_amount` int NOT NULL,
  `total_amount` int NOT NULL,
  `use_escrow` bit(1) DEFAULT NULL,
  `user_uid` varchar(255) DEFAULT NULL,
  `vat` int NOT NULL,
  `version` date DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_result_card`
--

/*!40000 ALTER TABLE `payment_result_card` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_result_card` ENABLE KEYS */;

--
-- Table structure for table `point_history`
--

DROP TABLE IF EXISTS `point_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `point_history` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `point` int DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `remain_point` int DEFAULT NULL,
  `user_uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_history`
--

/*!40000 ALTER TABLE `point_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `point_history` ENABLE KEYS */;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '작성자 UID',
  `board_id` bigint NOT NULL COMMENT '게시판 ID',
  `category_id` bigint DEFAULT NULL COMMENT '카테고리 ID',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '제목',
  `content` longtext COLLATE utf8mb4_unicode_ci COMMENT '내용',
  `count` int DEFAULT '0' COMMENT '조회수',
  `score` decimal(3,2) DEFAULT NULL COMMENT '평점',
  `secret_yn` char(1) COLLATE utf8mb4_unicode_ci DEFAULT 'N' COMMENT '비밀글 여부',
  `use_yn` char(1) COLLATE utf8mb4_unicode_ci DEFAULT 'Y' COMMENT '사용 여부',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `board_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `delete_status` bit(1) NOT NULL,
  `depth` int NOT NULL,
  `hidden_status` bit(1) NOT NULL,
  `notice_status` bit(1) NOT NULL,
  `parent_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `view_count` int NOT NULL,
  `view_order` int NOT NULL,
  `writer` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_post_account_id` (`account_id`),
  KEY `idx_post_board_id` (`board_id`),
  KEY `idx_post_category_id` (`category_id`),
  KEY `idx_post_created_date` (`created_date`),
  KEY `idx_post_use_yn` (`use_yn`),
  CONSTRAINT `fk_post_board` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_post_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

/*!40000 ALTER TABLE `post` DISABLE KEYS */;
/*!40000 ALTER TABLE `post` ENABLE KEYS */;

--
-- Table structure for table `post_category`
--

DROP TABLE IF EXISTS `post_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_category` (
  `uid` varchar(255) NOT NULL,
  `category_uid` varchar(255) DEFAULT NULL,
  `post_uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKt16dyoyl5e595yr8irt56mdu1` (`category_uid`),
  CONSTRAINT `FKt16dyoyl5e595yr8irt56mdu1` FOREIGN KEY (`category_uid`) REFERENCES `board_category` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_category`
--

/*!40000 ALTER TABLE `post_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_category` ENABLE KEYS */;

--
-- Table structure for table `post_data`
--

DROP TABLE IF EXISTS `post_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_data` (
  `uid` varchar(255) NOT NULL,
  `field_uid` varchar(255) DEFAULT NULL,
  `input_value` varchar(255) DEFAULT NULL,
  `post_uid` varchar(255) DEFAULT NULL,
  `view_order` int DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKiy14aiik7py8ev819fi1q93bl` (`field_uid`),
  CONSTRAINT `FKiy14aiik7py8ev819fi1q93bl` FOREIGN KEY (`field_uid`) REFERENCES `board_field` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_data`
--

/*!40000 ALTER TABLE `post_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_data` ENABLE KEYS */;

--
-- Table structure for table `post_file`
--

DROP TABLE IF EXISTS `post_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_file` (
  `uid` varchar(255) NOT NULL,
  `field_uid` varchar(255) DEFAULT NULL,
  `file_uid` varchar(255) DEFAULT NULL,
  `post_uid` varchar(255) DEFAULT NULL,
  `view_order` int DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_file`
--

/*!40000 ALTER TABLE `post_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_file` ENABLE KEYS */;

--
-- Table structure for table `post_like`
--

DROP TABLE IF EXISTS `post_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL COMMENT '게시글 ID',
  `account_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '사용자 UID',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `idx` int NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `post_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_like` (`post_id`,`account_id`),
  KEY `idx_post_like_post` (`post_id`),
  KEY `idx_post_like_account` (`account_id`),
  CONSTRAINT `fk_post_like_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_like`
--

/*!40000 ALTER TABLE `post_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_like` ENABLE KEYS */;

--
-- Table structure for table `post_tag`
--

DROP TABLE IF EXISTS `post_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL COMMENT '게시글 ID',
  `tag_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '태그명',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `idx` int NOT NULL,
  `tag` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `post_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_post_tag_post` (`post_id`),
  KEY `idx_post_tag_name` (`tag_name`),
  CONSTRAINT `fk_post_tag_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_tag`
--

/*!40000 ALTER TABLE `post_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_tag` ENABLE KEYS */;

--
-- Table structure for table `region_code`
--

DROP TABLE IF EXISTS `region_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `region_code` (
  `code` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '지역명',
  `type` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'SIDO, SIGUNGU',
  `parent_code` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '상위 지역 코드',
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `level` int DEFAULT NULL,
  `sort_order` int DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `idx_region_type` (`type`),
  KEY `idx_region_parent` (`parent_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region_code`
--

/*!40000 ALTER TABLE `region_code` DISABLE KEYS */;
INSERT INTO `region_code` (`code`, `name`, `type`, `parent_code`, `uid`, `created_date`, `enabled`, `level`, `sort_order`) VALUES ('11','서울특별시','SIDO',NULL,'',NULL,1,NULL,NULL),('11110','종로구','SIGUNGU','11','',NULL,1,NULL,NULL),('11140','중구','SIGUNGU','11','',NULL,1,NULL,NULL),('11170','용산구','SIGUNGU','11','',NULL,1,NULL,NULL),('11200','성동구','SIGUNGU','11','',NULL,1,NULL,NULL),('11215','광진구','SIGUNGU','11','',NULL,1,NULL,NULL),('11230','동대문구','SIGUNGU','11','',NULL,1,NULL,NULL),('11260','중랑구','SIGUNGU','11','',NULL,1,NULL,NULL),('11290','성북구','SIGUNGU','11','',NULL,1,NULL,NULL),('11305','강북구','SIGUNGU','11','',NULL,1,NULL,NULL),('11320','도봉구','SIGUNGU','11','',NULL,1,NULL,NULL),('11350','노원구','SIGUNGU','11','',NULL,1,NULL,NULL),('11380','은평구','SIGUNGU','11','',NULL,1,NULL,NULL),('11410','서대문구','SIGUNGU','11','',NULL,1,NULL,NULL),('11440','마포구','SIGUNGU','11','',NULL,1,NULL,NULL),('11470','양천구','SIGUNGU','11','',NULL,1,NULL,NULL),('11500','강서구','SIGUNGU','11','',NULL,1,NULL,NULL),('11530','구로구','SIGUNGU','11','',NULL,1,NULL,NULL),('11545','금천구','SIGUNGU','11','',NULL,1,NULL,NULL),('11560','영등포구','SIGUNGU','11','',NULL,1,NULL,NULL),('11590','동작구','SIGUNGU','11','',NULL,1,NULL,NULL),('11620','관악구','SIGUNGU','11','',NULL,1,NULL,NULL),('11650','서초구','SIGUNGU','11','',NULL,1,NULL,NULL),('11680','강남구','SIGUNGU','11','',NULL,1,NULL,NULL),('11710','송파구','SIGUNGU','11','',NULL,1,NULL,NULL),('11740','강동구','SIGUNGU','11','',NULL,1,NULL,NULL),('26','부산광역시','SIDO',NULL,'',NULL,1,NULL,NULL),('27','대구광역시','SIDO',NULL,'',NULL,1,NULL,NULL),('28','인천광역시','SIDO',NULL,'',NULL,1,NULL,NULL),('29','광주광역시','SIDO',NULL,'',NULL,1,NULL,NULL),('30','대전광역시','SIDO',NULL,'',NULL,1,NULL,NULL),('31','울산광역시','SIDO',NULL,'',NULL,1,NULL,NULL),('36','세종특별자치시','SIDO',NULL,'',NULL,1,NULL,NULL),('41','경기도','SIDO',NULL,'',NULL,1,NULL,NULL),('42','강원도','SIDO',NULL,'',NULL,1,NULL,NULL),('43','충청북도','SIDO',NULL,'',NULL,1,NULL,NULL),('44','충청남도','SIDO',NULL,'',NULL,1,NULL,NULL),('45','전라북도','SIDO',NULL,'',NULL,1,NULL,NULL),('46','전라남도','SIDO',NULL,'',NULL,1,NULL,NULL),('47','경상북도','SIDO',NULL,'',NULL,1,NULL,NULL),('48','경상남도','SIDO',NULL,'',NULL,1,NULL,NULL),('48170','진주시','SIGUNGU','48','',NULL,1,NULL,NULL),('48220','창원시','SIGUNGU','48','',NULL,1,NULL,NULL),('48240','통영시','SIGUNGU','48','',NULL,1,NULL,NULL),('48250','김해시','SIGUNGU','48','',NULL,1,NULL,NULL),('48270','양산시','SIGUNGU','48','',NULL,1,NULL,NULL),('48310','거제시','SIGUNGU','48','',NULL,1,NULL,NULL),('50','제주특별자치도','SIDO',NULL,'',NULL,1,NULL,NULL);
/*!40000 ALTER TABLE `region_code` ENABLE KEYS */;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `delete_status` bit(1) NOT NULL,
  `order_group_idx` int DEFAULT NULL,
  `score` int NOT NULL,
  `user_uid` varchar(255) DEFAULT NULL,
  `view_count` int NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

/*!40000 ALTER TABLE `review` DISABLE KEYS */;
/*!40000 ALTER TABLE `review` ENABLE KEYS */;

--
-- Table structure for table `review_like`
--

DROP TABLE IF EXISTS `review_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_like` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `review_idx` int DEFAULT NULL,
  `user_uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_like`
--

/*!40000 ALTER TABLE `review_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `review_like` ENABLE KEYS */;

--
-- Table structure for table `review_photo`
--

DROP TABLE IF EXISTS `review_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_photo` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `file_uid` varchar(255) DEFAULT NULL,
  `view_order` int DEFAULT NULL,
  `review_idx` int DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `FKkmuwuphrbpoikgd1i5dr7csoe` (`review_idx`),
  CONSTRAINT `FKkmuwuphrbpoikgd1i5dr7csoe` FOREIGN KEY (`review_idx`) REFERENCES `review` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_photo`
--

/*!40000 ALTER TABLE `review_photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `review_photo` ENABLE KEYS */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `role_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `join_access_state` bit(1) NOT NULL,
  `role_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `site_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`role_code`, `create_date`, `description`, `join_access_state`, `role_name`, `site_uid`) VALUES ('ROLE_USER','2026-02-04 05:35:19','일반 사용자 권한',_binary '','일반 사용자','00070154-eb1d-4972-97b0-03365762fcc1');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `count` int DEFAULT '1',
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1wdpsed5kna2y38hnbgrnhi5b` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '팀 이름',
  `team_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '팀 고유 코드 (URL용)',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '팀 소개',
  `logo_url` text COLLATE utf8mb4_unicode_ci COMMENT '팀 로고 URL',
  `established_year` int DEFAULT NULL COMMENT '창단 연도',
  `activity_days` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '활동 요일 (CSV)',
  `activity_times` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '활동 시간대 (CSV)',
  `region_sido` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '활동 지역 - 시/도',
  `region_sigungu` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '활동 지역 - 시/군/구',
  `home_stadium` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '홈 구장',
  `age_groups` int DEFAULT '0' COMMENT '연령대 비트마스크',
  `gender_type` int DEFAULT '2' COMMENT '0: 남성, 1: 여성, 2: 혼성',
  `skill_level` int DEFAULT '1' COMMENT '실력 레벨 1-5',
  `manner_score` double DEFAULT '4' COMMENT '매너 점수',
  `member_count` int DEFAULT '1' COMMENT '팀원 수',
  `total_sponsorship` int DEFAULT '0' COMMENT '총 후원금',
  `owner_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '팀장 UID',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE' COMMENT 'ACTIVE, INACTIVE, DISBANDED',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active_days` int DEFAULT NULL,
  `active_time_slots` int DEFAULT NULL,
  `bank_account` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bank_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted_date` datetime DEFAULT NULL,
  `feature_tags` text COLLATE utf8mb4_unicode_ci,
  `grade` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `home_stadium_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `logo_file_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `monthly_fee` int DEFAULT NULL,
  `recruiting_members` bit(1) DEFAULT NULL,
  `refund_bank_account` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `refund_bank_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sponsor_owner_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_team_code` (`team_code`),
  KEY `idx_team_region` (`region_sido`,`region_sigungu`),
  KEY `idx_team_status` (`status`),
  KEY `idx_team_owner` (`owner_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

/*!40000 ALTER TABLE `team` DISABLE KEYS */;
/*!40000 ALTER TABLE `team` ENABLE KEYS */;

--
-- Table structure for table `team_member`
--

DROP TABLE IF EXISTS `team_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_member` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `team_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '팀 UID',
  `user_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '회원 UID',
  `role` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'MEMBER' COMMENT 'OWNER, MANAGER, MEMBER',
  `position` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '포지션',
  `jersey_number` int DEFAULT NULL COMMENT '등번호',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING' COMMENT 'PENDING, ACTIVE, REJECTED, LEFT',
  `application_message` text COLLATE utf8mb4_unicode_ci COMMENT '가입 신청 메시지',
  `joined_date` datetime DEFAULT NULL COMMENT '가입 승인 일자',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `application_age` int DEFAULT NULL,
  `application_experience` text COLLATE utf8mb4_unicode_ci,
  `application_region` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `back_number` int DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_team_member` (`team_uid`,`user_uid`),
  UNIQUE KEY `UKhgwov2gdt1oj3tmgenbr240ja` (`team_uid`,`user_uid`),
  KEY `idx_team_member_team` (`team_uid`),
  KEY `idx_team_member_user` (`user_uid`),
  KEY `idx_team_member_status` (`status`),
  CONSTRAINT `FK5ijmx04pcsns6ae58sfa5cu1f` FOREIGN KEY (`user_uid`) REFERENCES `user` (`uid`),
  CONSTRAINT `fk_team_member_team` FOREIGN KEY (`team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_member`
--

/*!40000 ALTER TABLE `team_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `team_member` ENABLE KEYS */;

--
-- Table structure for table `team_sponsorship`
--

DROP TABLE IF EXISTS `team_sponsorship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_sponsorship` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `team_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '팀 UID',
  `sponsor_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '후원자 UID',
  `sponsorship_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'OWNER, ONE_TIME, REGULAR',
  `amount` int DEFAULT NULL COMMENT '후원 금액',
  `total_amount` int DEFAULT '0' COMMENT '총 후원 금액',
  `message` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '후원 메시지',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE' COMMENT 'ACTIVE, CANCELLED, EXPIRED',
  `last_payment_date` datetime DEFAULT NULL COMMENT '마지막 결제일',
  `next_payment_date` datetime DEFAULT NULL COMMENT '다음 결제 예정일',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `end_date` datetime DEFAULT NULL,
  `payment_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recurring_months` int DEFAULT NULL,
  `sponsor_user_uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `idx_sponsorship_team` (`team_uid`),
  KEY `idx_sponsorship_sponsor` (`sponsor_uid`),
  KEY `idx_sponsorship_type` (`sponsorship_type`),
  KEY `FK7ccyuuyfd9pfqvohmeforflo3` (`sponsor_user_uid`),
  CONSTRAINT `FK7ccyuuyfd9pfqvohmeforflo3` FOREIGN KEY (`sponsor_user_uid`) REFERENCES `user` (`uid`),
  CONSTRAINT `fk_sponsorship_team` FOREIGN KEY (`team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_sponsorship`
--

/*!40000 ALTER TABLE `team_sponsorship` DISABLE KEYS */;
/*!40000 ALTER TABLE `team_sponsorship` ENABLE KEYS */;

--
-- Table structure for table `toss_payment_shop`
--

DROP TABLE IF EXISTS `toss_payment_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `toss_payment_shop` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `secret_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `toss_payment_shop`
--

/*!40000 ALTER TABLE `toss_payment_shop` DISABLE KEYS */;
/*!40000 ALTER TABLE `toss_payment_shop` ENABLE KEYS */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `site_uid` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT '00070154-eb1d-4972-97b0-03365762fcc1',
  `user_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '사용자 아이디 (이메일)',
  `user_password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '비밀번호 (암호화)',
  `actual_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '실명',
  `concat_number` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '연락처',
  `provider` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'SOCIAL 제공처 (GOOGLE, NAVER, KAKAO 등)',
  `provider_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'SOCIAL 제공처 사용자 고유값',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '계정 사용여부',
  `not_locked` tinyint(1) DEFAULT '1' COMMENT '계정 잠금여부',
  `birth` date DEFAULT NULL COMMENT '생년월일',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '이메일',
  `post_code` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '우편번호',
  `address` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '주소',
  `address_detail` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '상세주소',
  `lat` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '위도',
  `lon` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '경도',
  `gender` int DEFAULT '0' COMMENT '성별 (0: 남성, 1: 여성)',
  `withdraw_status` tinyint(1) DEFAULT '0' COMMENT '탈퇴여부',
  `join_status` tinyint(1) DEFAULT '0' COMMENT '가입완료여부',
  `marketing_status` tinyint(1) DEFAULT '0' COMMENT '마케팅 동의여부',
  `point` int DEFAULT '0' COMMENT '포인트',
  `register_info_status` tinyint(1) DEFAULT '0' COMMENT '등록 정보 상태',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_user_email` (`email`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_user_provider` (`provider`,`provider_id`),
  KEY `idx_user_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`uid`, `site_uid`, `user_id`, `user_password`, `actual_name`, `concat_number`, `provider`, `provider_id`, `enabled`, `not_locked`, `birth`, `email`, `post_code`, `address`, `address_detail`, `lat`, `lon`, `gender`, `withdraw_status`, `join_status`, `marketing_status`, `point`, `register_info_status`, `create_date`) VALUES ('b44556b8-bb41-4b1f-a426-dc4103109899','00070154-eb1d-4972-97b0-03365762fcc1','leeyop12@naver.com','{bcrypt}$2a$10$D3nvYhvvWcFsUmdXv7z/hejr5c3CtdWpdkgrmgl6NhNQYI3j3eqw6','정이욥','01044493118',NULL,NULL,1,1,'1994-12-26','leeyop12@naver.com',NULL,NULL,NULL,NULL,NULL,0,0,1,1,0,0,'2026-02-04 04:47:30'),('d13597c6-6d04-4155-8aa8-a6401608ca1a','00070154-eb1d-4972-97b0-03365762fcc1','leeyop12@naver.com2','{bcrypt}$2a$10$HIn8njKssvt0sXwPwEgicuiR.qc6XhFSL4MKvRXhw247GP98Qabse','정이욥2','01044493119',NULL,NULL,1,1,'1994-12-26','leeyop12@naver.com2',NULL,NULL,NULL,NULL,NULL,0,0,1,1,0,0,'2026-02-04 05:54:02');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '사용자 UID',
  `role_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '권한 코드 (ROLE_USER, ROLE_ADMIN 등)',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_user_role` (`user_uid`,`role_code`),
  KEY `idx_user_role_user` (`user_uid`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`uid`, `user_uid`, `role_code`, `created_date`, `create_date`) VALUES ('26348098-93b0-4095-8b13-15d13a169931','d13597c6-6d04-4155-8aa8-a6401608ca1a','ROLE_USER','2026-02-04 05:54:01','2026-02-04 05:54:02'),('f5356076-28c2-46af-bfa9-c117ff2b80f4','b44556b8-bb41-4b1f-a426-dc4103109899','ROLE_USER','2026-02-04 04:47:28','2026-02-04 04:47:30');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;

--
-- Table structure for table `withdraw_history`
--

DROP TABLE IF EXISTS `withdraw_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `withdraw_history` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `user_uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `withdraw_history`
--

/*!40000 ALTER TABLE `withdraw_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `withdraw_history` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-04  7:00:50

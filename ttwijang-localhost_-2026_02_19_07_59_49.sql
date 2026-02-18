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
INSERT INTO `attached_file` (`uid`, `file_name`, `file_path`, `file_size`, `file_type`, `related_type`, `related_id`, `created_date`, `create_date`, `original_name`, `use_yn`) VALUES ('165b3f3c-6700-4dd3-aad2-e18824737804','165b3f3c-6700-4dd3-aad2-e18824737804_tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','post/2026/02/19/165b3f3c-6700-4dd3-aad2-e18824737804',57735,'image/jpeg',NULL,NULL,'2026-02-19 05:33:07','2026-02-19 05:33:08','tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','Y'),('2c6b42be-1fbe-4e68-acaf-ff1c28184e39','2c6b42be-1fbe-4e68-acaf-ff1c28184e39_20241012_030726.png','team/2026/02/19/2c6b42be-1fbe-4e68-acaf-ff1c28184e39',33289,'image/png',NULL,NULL,'2026-02-19 03:56:13','2026-02-19 03:56:13','20241012_030726.png','Y'),('326aae43-2606-45e3-822f-00bbf41924d5','326aae43-2606-45e3-822f-00bbf41924d5_tahsin-labib-m3Mufrx8gpQ-unsplash.jpg','post/2026/02/19/326aae43-2606-45e3-822f-00bbf41924d5',1429179,'image/jpeg',NULL,NULL,'2026-02-19 05:36:55','2026-02-19 05:36:56','tahsin-labib-m3Mufrx8gpQ-unsplash.jpg','Y'),('38c464bd-bcd9-4b78-a173-f412fd85a259','38c464bd-bcd9-4b78-a173-f412fd85a259_tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','team/2026/02/19/38c464bd-bcd9-4b78-a173-f412fd85a259',57735,'image/jpeg',NULL,NULL,'2026-02-19 03:15:04','2026-02-19 03:15:06','tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','Y'),('442cf130-d1bf-446a-aed3-4cd33ec52207','442cf130-d1bf-446a-aed3-4cd33ec52207_tahsin-labib-m3Mufrx8gpQ-unsplash.jpg','post/2026/02/19/442cf130-d1bf-446a-aed3-4cd33ec52207',1429179,'image/jpeg',NULL,NULL,'2026-02-19 06:04:03','2026-02-19 06:04:03','tahsin-labib-m3Mufrx8gpQ-unsplash.jpg','Y'),('763e2d8f-3957-42e2-8794-21901205aeef','763e2d8f-3957-42e2-8794-21901205aeef_tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','post/2026/02/19/763e2d8f-3957-42e2-8794-21901205aeef',57735,'image/jpeg',NULL,NULL,'2026-02-19 03:57:24','2026-02-19 03:57:24','tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','Y'),('7d3e7eec-8520-477c-bdaf-fa82c3fc361a','7d3e7eec-8520-477c-bdaf-fa82c3fc361a_20241017_074016.png','team/2026/02/19/7d3e7eec-8520-477c-bdaf-fa82c3fc361a',7155,'image/png',NULL,NULL,'2026-02-19 03:35:13','2026-02-19 03:35:14','20241017_074016.png','Y'),('88fbb30e-dced-4995-ad6c-fe7e59aa522a','88fbb30e-dced-4995-ad6c-fe7e59aa522a_tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','team/2026/02/19/88fbb30e-dced-4995-ad6c-fe7e59aa522a',57735,'image/jpeg',NULL,NULL,'2026-02-19 03:08:29','2026-02-19 03:08:31','tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','Y'),('8a8fbc4b-fe88-4342-bb97-16363022303e','8a8fbc4b-fe88-4342-bb97-16363022303e_20241017_074016.png','team/2026/02/19/8a8fbc4b-fe88-4342-bb97-16363022303e',7155,'image/png',NULL,NULL,'2026-02-19 02:57:18','2026-02-19 02:57:19','20241017_074016.png','Y'),('a3f9e316-5c5f-4c49-ad5c-d745a1106012','a3f9e316-5c5f-4c49-ad5c-d745a1106012_tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','post/2026/02/19/a3f9e316-5c5f-4c49-ad5c-d745a1106012',57735,'image/jpeg',NULL,NULL,'2026-02-19 05:50:42','2026-02-19 05:50:43','tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','Y'),('c24b87c7-0088-4df6-9df4-dfcc762c261c','c24b87c7-0088-4df6-9df4-dfcc762c261c_20241017_074016.png','team/2026/02/19/c24b87c7-0088-4df6-9df4-dfcc762c261c',7155,'image/png',NULL,NULL,'2026-02-19 03:56:08','2026-02-19 03:56:09','20241017_074016.png','Y'),('d21f91ac-e02e-4883-85d6-c3f01bfa7fe4','d21f91ac-e02e-4883-85d6-c3f01bfa7fe4_tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','post/2026/02/19/d21f91ac-e02e-4883-85d6-c3f01bfa7fe4',57735,'image/jpeg',NULL,NULL,'2026-02-19 03:50:19','2026-02-19 03:50:20','tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','Y'),('e0c23f97-9317-441a-809e-ff4a612de770','e0c23f97-9317-441a-809e-ff4a612de770_20241017_074016.png','team/2026/02/19/e0c23f97-9317-441a-809e-ff4a612de770',7155,'image/png',NULL,NULL,'2026-02-19 05:31:07','2026-02-19 05:31:08','20241017_074016.png','Y'),('f149fbad-d89d-4ecd-a0b1-e1137cc0aa3f','f149fbad-d89d-4ecd-a0b1-e1137cc0aa3f_tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','post/2026/02/19/f149fbad-d89d-4ecd-a0b1-e1137cc0aa3f',57735,'image/jpeg',NULL,NULL,'2026-02-19 06:03:43','2026-02-19 06:03:44','tahsin-labib-m3Mufrx8gpQ-unsplash (1).jpg','Y');
/*!40000 ALTER TABLE `attached_file` ENABLE KEYS */;

--
-- Table structure for table `banner`
--

DROP TABLE IF EXISTS `banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banner` (
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '배너 제목',
  `image_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '배너 이미지 URL',
  `link_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '클릭시 이동 URL',
  `display_order` int DEFAULT '0' COMMENT '표시 순서 (낮을수록 먼저 표시)',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE' COMMENT '상태: ACTIVE, INACTIVE',
  `start_date` date DEFAULT NULL COMMENT '노출 시작일',
  `end_date` date DEFAULT NULL COMMENT '노출 종료일',
  `region_sido` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지역 시도',
  `region_sigungu` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지역 시군구',
  `target_page` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'ALL' COMMENT '타겟 페이지: HOME, LEAGUE, MATCH, TEAM, ALL',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`),
  KEY `idx_display_order` (`display_order`),
  KEY `idx_status_dates` (`status`,`start_date`,`end_date`),
  KEY `idx_target_page` (`target_page`),
  KEY `idx_region` (`region_sigungu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='배너 관리 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banner`
--

/*!40000 ALTER TABLE `banner` DISABLE KEYS */;
INSERT INTO `banner` (`uid`, `title`, `image_url`, `link_url`, `display_order`, `status`, `start_date`, `end_date`, `region_sido`, `region_sigungu`, `target_page`, `created_date`, `updated_date`) VALUES ('banner-001','2026 봄 시즌 리그 참가 모집','https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','/league',1,'ACTIVE','2026-02-01','2026-03-31','경상남도','진주시','LEAGUE','2026-02-08 19:53:32','2026-02-08 20:03:46'),('banner-002','진주 풋살장 오픈 기념 이벤트','https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','https://example.com/event',2,'ACTIVE','2026-02-01','2026-02-28','경상남도','진주시','LEAGUE','2026-02-08 19:53:32','2026-02-08 20:03:46'),('banner-003','뛰장 리그 우승팀 시상식','https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','/league-status',3,'ACTIVE','2026-01-15','2026-02-28','경상남도','진주시','LEAGUE','2026-02-08 19:53:32','2026-02-08 20:03:46'),('banner-004','신규 회원 가입 이벤트','https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','/join',4,'ACTIVE','2026-02-01','2026-03-31','경상남도','진주시','ALL','2026-02-08 19:53:32','2026-02-08 20:03:46'),('banner-005','팀 매칭 서비스 오픈!','https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','/match',5,'ACTIVE','2026-02-01','2026-06-30','경상남도','진주시','LEAGUE','2026-02-08 19:53:32','2026-02-08 20:03:46'),('banner-inactive-001','종료된 이벤트','https://images.unsplash.com/photo-1760420919593-c1ae7509faaf?q=80&w=800&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D','/old-event',10,'INACTIVE','2026-01-01','2026-01-31','경상남도','진주시','LEAGUE','2026-02-08 19:53:38','2026-02-08 20:03:46');
/*!40000 ALTER TABLE `banner` ENABLE KEYS */;

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
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `depth` int NOT NULL,
  `descript` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parent_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `view_order` int NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKkqi6tecqcjf57sman464espt5` (`parent_uid`),
  CONSTRAINT `FKkqi6tecqcjf57sman464espt5` FOREIGN KEY (`parent_uid`) REFERENCES `board_category` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `board_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `field_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `field_type_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_size_limit` int DEFAULT NULL,
  `input_limit` int NOT NULL,
  `required_state` bit(1) NOT NULL,
  `search_state` bit(1) NOT NULL,
  `view_order` int DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `UK_9q2ure13dmrf4xqcrgn4g5byf` (`field_type_code`),
  CONSTRAINT `FK88vcyr2t3vi1emug0q93tnu08` FOREIGN KEY (`field_type_code`) REFERENCES `field_type` (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `action` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `board_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `board_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `view_order` int DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKekyasu7quvip33fdinwbise0r` (`category_uid`),
  CONSTRAINT `FKekyasu7quvip33fdinwbise0r` FOREIGN KEY (`category_uid`) REFERENCES `board_category` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
INSERT INTO `cash_wallet` (`uid`, `user_uid`, `balance`, `total_charged`, `total_used`, `last_charged_date`, `created_date`, `updated_date`, `total_refunded`) VALUES ('3a55c2e7-43c2-46fe-88f8-94f378274eff','1c2301ea-9743-4d57-86b0-3b7b4eab1e72',100000,0,0,NULL,'2026-02-19 01:11:32','2026-02-19 06:59:58',NULL),('58c2da0b-06b1-4a7e-913a-8e3f0ad5621f','1c2301ea-9743-4d57-86b0-3b7b4eab1e73',100000,0,0,NULL,'2026-02-19 05:10:00','2026-02-19 06:59:58',NULL),('b5f4a4c8-2c4b-4f13-95af-4eeb8b31d7a7','1c2301ea-9743-4d57-86b0-3b7b4eab1e68',100000,0,0,NULL,'2026-02-19 01:08:23','2026-02-19 06:59:58',NULL),('d599808f-9b40-4b04-8b1d-3ba03361f414','1c2301ea-9743-4d57-86b0-3b7b4eab1e67',100000,0,0,NULL,'2026-02-08 16:55:42','2026-02-19 06:59:58',NULL),('fc11e6eb-a8f0-4862-be16-c79fdae13716','1c2301ea-9743-4d57-86b0-3b7b4eab1e66',100000,0,0,NULL,'2026-02-08 04:32:58','2026-02-19 06:59:58',NULL);
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
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contents` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `depth` int NOT NULL,
  `hide` bit(1) NOT NULL,
  `parent_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `post_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `view_order` int NOT NULL,
  `writer` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKesf7ethov6up68yrgoa4712o5` (`parent_uid`),
  CONSTRAINT `FKesf7ethov6up68yrgoa4712o5` FOREIGN KEY (`parent_uid`) REFERENCES `comment` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` (`uid`, `contents`, `create_date`, `depth`, `hide`, `parent_uid`, `password`, `post_uid`, `user_uid`, `view_order`, `writer`) VALUES ('065d669f-3e96-4c84-bfe1-536e8d53e110','test','2026-02-19 05:37:11',1,_binary '\0',NULL,NULL,'5c25dc51-03ac-46fd-82dd-4e10e8870333','1c2301ea-9743-4d57-86b0-3b7b4eab1e72',1,'정이욥');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;

--
-- Table structure for table `field_type`
--

DROP TABLE IF EXISTS `field_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `field_type` (
  `type_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `view_order` int NOT NULL,
  PRIMARY KEY (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  KEY `idx_match_guest` (`guest_team_uid`),
  KEY `idx_match_date_status` (`match_date`,`status`),
  CONSTRAINT `fk_match_host` FOREIGN KEY (`host_team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE,
  CONSTRAINT `FKxy82bnj665kf6eix3sc7gdqd` FOREIGN KEY (`guest_team_uid`) REFERENCES `team` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `futsal_match`
--

/*!40000 ALTER TABLE `futsal_match` DISABLE KEYS */;
INSERT INTO `futsal_match` (`uid`, `host_team_uid`, `opponent_team_uid`, `match_type`, `match_format`, `match_date`, `match_time`, `stadium_name`, `stadium_address`, `region_sido`, `region_sigungu`, `fee`, `description`, `gender_type`, `age_groups`, `status`, `host_score`, `opponent_score`, `created_date`, `updated_date`, `additional_info`, `away_score`, `duration_hours`, `guest_team_uid`, `home_score`, `recruitment_deadline`) VALUES ('121a11eb-42d0-483d-8a4b-0f5ba7ad3141','team-4817-0001-0000-000000000001',NULL,'FRIENDLY','FIVE_VS_FIVE','2026-02-28','20:00:00','진주풋살장',NULL,NULL,NULL,0,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-19 01:39:06','2026-02-19 01:39:06',NULL,NULL,NULL,NULL,NULL,NULL),('458d91da-0c26-476c-911c-df30dc32e433','cfdb914c-dd93-4750-b7a7-be4f1d3ada85',NULL,'FRIENDLY','FIVE_VS_FIVE','2026-02-28','20:00:00','진주풋살장',NULL,NULL,NULL,0,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-19 06:23:10','2026-02-19 06:23:10',NULL,NULL,NULL,NULL,NULL,NULL),('4db0a616-aa43-433d-87b0-14675569162f','cfdb914c-dd93-4750-b7a7-be4f1d3ada85',NULL,'FRIENDLY','FIVE_VS_FIVE','2026-02-20','20:00:00','풋살장',NULL,NULL,NULL,10000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-19 06:39:47','2026-02-19 06:39:47',NULL,NULL,2,NULL,NULL,NULL),('5c70aea9-dab6-44c0-8b2c-da74612e8d3f','team-4817-0002-0000-000000000002',NULL,'FRIENDLY','FIVE_VS_FIVE','2026-02-25','20:00:00','23',NULL,NULL,NULL,0,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 20:40:25','2026-02-08 20:40:25',NULL,NULL,NULL,NULL,NULL,NULL),('a9dcda9b-ac09-405c-a0f8-0677df5f843f','cfdb914c-dd93-4750-b7a7-be4f1d3ada85',NULL,'FREE','FIVE_VS_FIVE','2026-02-28','20:00:00','test',NULL,NULL,NULL,0,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-19 05:05:55','2026-02-19 05:05:55',NULL,NULL,NULL,NULL,NULL,NULL),('fm-4817-0001-0000-000000000001','team-4817-0001-0000-000000000001',NULL,'FRIENDLY','5vs5','2026-02-10','19:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','경남','진주시',30000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','레벨 중급 이상 팀 환영합니다! 매너 게임 지향.',NULL,2,NULL,NULL,'2026-02-09 23:59:59'),('fm-4817-0002-0000-000000000002','team-4817-0001-0000-000000000001',NULL,'FRIENDLY','6vs6','2026-02-14','20:00:00','송파 풋살아레나','서울 송파구 잠실동 88-10','경남','진주시',40000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','발렌타인데이 특별 매치! 간식 제공합니다.',NULL,2,NULL,NULL,'2026-02-13 18:00:00'),('fm-4817-0003-0000-000000000003','team-4817-0001-0000-000000000001',NULL,'FREE','5vs5','2026-02-22','15:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','경남','진주시',0,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','자체 연습 경기입니다. 부담 없이 즐겨요!',NULL,2,NULL,NULL,'2026-02-21 23:59:59'),('fm-4817-0004-0000-000000000004','team-4817-0002-0000-000000000002',NULL,'FRIENDLY','5vs5','2026-02-11','20:00:00','수원 월드컵 풋살장','경기도 수원시 팔달구 월드컵로 310','경남','진주시',25000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','수원 지역 팀 우선! 다른 지역도 환영합니다.',NULL,2,NULL,NULL,'2026-02-10 23:59:59'),('fm-4817-0005-0000-000000000005','team-4817-0002-0000-000000000002',NULL,'FRIENDLY','4vs4','2026-02-15','14:00:00','수원역 미니풋살장','경기도 수원시 팔달구 매산로 55','경남','진주시',15000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','4vs4 미니게임! 가볍게 한 경기 하실 팀 모집합니다.',NULL,1,NULL,NULL,'2026-02-14 12:00:00'),('fm-4817-0006-0000-000000000006','team-4817-0002-0000-000000000002',NULL,'FRIENDLY','5vs5','2026-02-28','19:00:00','수원 월드컵 풋살장','경기도 수원시 팔달구 월드컵로 310','경남','진주시',30000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','2월 마지막 주말 매치! 실력 무관, 매너 필수.',NULL,2,NULL,NULL,'2026-02-27 23:59:59'),('fm-4817-0007-0000-000000000007','team-4817-0003-0000-000000000003',NULL,'FRIENDLY','5vs5','2026-02-12','21:00:00','인천 남동 풋살경기장','인천 남동구 구월동 55-12','경남','진주시',20000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','야간 매치! 조명 시설 완비. 중급 이상 팀 환영.',NULL,2,NULL,NULL,'2026-02-11 23:59:59'),('fm-4817-0008-0000-000000000008','team-4817-0003-0000-000000000003',NULL,'FRIENDLY','6vs6','2026-02-21','10:00:00','인천 축구전용구장 풋살코트','인천 남동구 논현동 700','경남','진주시',35000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','토요일 오전 매치! 주차 가능합니다.',NULL,2,NULL,NULL,'2026-02-20 23:59:59'),('fm-4817-0009-0000-000000000009','team-4817-0004-0000-000000000004',NULL,'FRIENDLY','5vs5','2026-02-13','19:30:00','탄천 풋살구장','경기도 성남시 분당구 탄천로 215','경남','진주시',25000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','혼성팀 환영! 남녀 모두 참여 가능한 매치입니다.',NULL,2,NULL,NULL,'2026-02-12 23:59:59'),('fm-4817-0010-0000-000000000010','team-4817-0004-0000-000000000004',NULL,'FRIENDLY','5vs5','2026-02-20','20:00:00','판교 풋살파크','경기도 성남시 분당구 판교역로 166','경남','진주시',30000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','IT 직장인 팀 환영합니다! 판교역 도보 5분.',NULL,2,NULL,NULL,'2026-02-19 18:00:00'),('fm-4817-0011-0000-000000000011','team-4817-0004-0000-000000000004',NULL,'FREE','5vs5','2026-03-01','16:00:00','탄천 풋살구장','경기도 성남시 분당구 탄천로 215','경남','진주시',0,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','3월 첫 경기! 자체 연습 겸 친선전.',NULL,2,NULL,NULL,'2026-02-28 23:59:59'),('fm-4817-0012-0000-000000000012','team-4817-0005-0000-000000000005',NULL,'FRIENDLY','5vs5','2026-02-11','19:00:00','부천 종합운동장 풋살코트','경기도 부천시 소사구 경인로 66','경남','진주시',20000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','화요일 정기 매치! 부천/인천 지역 팀 환영.',NULL,2,NULL,NULL,'2026-02-10 23:59:59'),('fm-4817-0013-0000-000000000013','team-4817-0005-0000-000000000005',NULL,'FRIENDLY','5vs5','2026-02-18','19:00:00','부천 종합운동장 풋살코트','경기도 부천시 소사구 경인로 66','경남','진주시',20000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','수요일 저녁 매치. 초급~중급 팀 반갑습니다!',NULL,2,NULL,NULL,'2026-02-17 23:59:59'),('fm-4817-0014-0000-000000000014','team-4817-0006-0000-000000000006',NULL,'FRIENDLY','5vs5','2026-02-09','20:00:00','강남 스포츠센터 풋살장','서울 강남구 테헤란로 152','경남','진주시',35000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','월요일 야간 매치! 직장인 팀 모집합니다.',NULL,2,NULL,NULL,'2026-02-09 17:00:00'),('fm-4817-0015-0000-000000000015','team-4817-0006-0000-000000000006',NULL,'FRIENDLY','6vs6','2026-02-15','10:00:00','올림픽공원 풋살장','서울 송파구 올림픽로 424','경남','진주시',50000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','주말 오전 6vs6! 올림픽공원에서 시원하게!',NULL,3,NULL,NULL,'2026-02-14 23:59:59'),('fm-4817-0016-0000-000000000016','team-4817-0006-0000-000000000006',NULL,'FRIENDLY','5vs5','2026-02-22','19:00:00','강남 스포츠센터 풋살장','서울 강남구 테헤란로 152','경남','진주시',30000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','금요일 저녁 정기 매치. 상급 팀 환영!',NULL,2,NULL,NULL,'2026-02-21 18:00:00'),('fm-4817-0017-0000-000000000017','team-4817-0006-0000-000000000006',NULL,'FREE','7vs7','2026-03-01','14:00:00','잠실 종합운동장 풋살장','서울 송파구 올림픽로 25','경남','진주시',0,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','7vs7 대규모 자체 경기! 인원 모집 중.',NULL,3,NULL,NULL,'2026-02-28 23:59:59'),('fm-4817-0018-0000-000000000018','team-4817-0001-0000-000000000001',NULL,'FRIENDLY','5vs5','2026-03-07','19:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','경남','진주시',30000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','3월 첫째주 토요일 매치! 봄맞이 친선전.',NULL,2,NULL,NULL,'2026-03-06 23:59:59'),('fm-4817-0019-0000-000000000019','team-4817-0003-0000-000000000003',NULL,'FRIENDLY','5vs5','2026-03-08','15:00:00','인천 남동 풋살경기장','인천 남동구 구월동 55-12','경남','진주시',25000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','일요일 오후 매치! 인천 지역 중급 팀 모집.',NULL,2,NULL,NULL,'2026-03-07 23:59:59'),('fm-4817-0020-0000-000000000020','team-4817-0004-0000-000000000004',NULL,'FRIENDLY','5vs5','2026-03-14','18:00:00','판교 풋살파크','경기도 성남시 분당구 판교역로 166','경남','진주시',30000,NULL,2,NULL,'RECRUITING',NULL,NULL,'2026-02-08 17:21:34','2026-02-08 17:24:47','판교 IT기업 대항전! 혼성 가능.',NULL,2,NULL,NULL,'2026-03-13 23:59:59');
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
INSERT INTO `guest_application` (`uid`, `recruitment_uid`, `user_uid`, `position`, `age`, `message`, `status`, `payment_completed`, `payment_amount`, `created_date`, `updated_date`, `payment_uid`, `processed_date`) VALUES ('49590c04-77de-49bb-b30d-a6e49504bda4','gr-4817-0009-0000-000000000009','1c2301ea-9743-4d57-86b0-3b7b4eab1e67',NULL,NULL,NULL,'PENDING',0,NULL,'2026-02-08 20:50:28','2026-02-08 20:50:27',NULL,NULL);
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
  KEY `idx_guest_recruit_date` (`match_date`),
  KEY `idx_guest_recruit_region` (`region_sido`,`region_sigungu`),
  KEY `idx_guest_recruit_status` (`status`),
  CONSTRAINT `FK5v9vsofrjs8qbn3fn2ngjp37i` FOREIGN KEY (`match_uid`) REFERENCES `futsal_match` (`uid`),
  CONSTRAINT `fk_guest_team` FOREIGN KEY (`team_uid`) REFERENCES `team` (`uid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guest_recruitment`
--

/*!40000 ALTER TABLE `guest_recruitment` DISABLE KEYS */;
INSERT INTO `guest_recruitment` (`uid`, `team_uid`, `match_uid`, `match_date`, `match_time`, `stadium_name`, `stadium_address`, `region_sido`, `region_sigungu`, `gender_type`, `age_groups`, `position_type`, `max_guests`, `current_guests`, `fee`, `guaranteed_minutes`, `additional_info`, `status`, `created_date`, `updated_date`) VALUES ('20dc8152-6c07-436e-bfc0-249e7084b559','team-4817-0002-0000-000000000002','fm-4817-0005-0000-000000000005','2026-02-15','14:00:00','수원역 미니풋살장',NULL,NULL,NULL,0,0,'FIELD',3,0,0,60,'test','RECRUITING','2026-02-08 20:42:36','2026-02-08 20:42:36'),('3eaa86ef-9fd0-4d68-aa4a-b4b17e33be2d','cfdb914c-dd93-4750-b7a7-be4f1d3ada85','4db0a616-aa43-433d-87b0-14675569162f','2026-02-20','20:00:00','풋살장',NULL,NULL,NULL,0,0,'FIELD',3,0,0,60,'test','RECRUITING','2026-02-19 07:44:04','2026-02-19 07:44:04'),('4ae4dd1b-aeef-4b6f-a71e-c263fda0ef0a','cfdb914c-dd93-4750-b7a7-be4f1d3ada85','4db0a616-aa43-433d-87b0-14675569162f','2026-02-20','20:00:00','풋살장',NULL,NULL,NULL,0,0,'FIELD',3,0,5000,60,'ㅅㄷㄴㅅ','RECRUITING','2026-02-19 07:51:36','2026-02-19 07:51:36'),('gr-4817-0001-0000-000000000001','team-4817-0001-0000-000000000001','fm-4817-0001-0000-000000000001','2026-02-10','19:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','서울특별시','강남구',0,6,'FIELD',3,0,10000,60,'필드 3명 급구! 기본기 가능하신 분 환영합니다.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0002-0000-000000000002','team-4817-0001-0000-000000000001','fm-4817-0002-0000-000000000002','2026-02-14','20:00:00','송파 풋살아레나','서울 송파구 잠실동 88-10','서울특별시','송파구',0,6,'GK',1,0,15000,80,'골키퍼 1명 모집! 경험자 우대. 간식 제공.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0003-0000-000000000003','team-4817-0001-0000-000000000001',NULL,'2026-02-22','15:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','서울특별시','강남구',0,14,'ANY',5,1,0,100,'포지션 무관 5명 모집. 자체 연습경기라 부담 없어요!','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0004-0000-000000000004','team-4817-0002-0000-000000000002','fm-4817-0004-0000-000000000004','2026-02-11','20:00:00','수원 월드컵 풋살장','경기도 수원시 팔달구 월드컵로 310','경기도','수원시',0,6,'FIELD',2,0,8000,60,'필드 2명 모집합니다. 수원 거주자 환영!','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0005-0000-000000000005','team-4817-0002-0000-000000000002','fm-4817-0005-0000-000000000005','2026-02-15','14:00:00','수원역 미니풋살장','경기도 수원시 팔달구 매산로 55','경기도','수원시',0,2,'ANY',2,0,5000,60,'4vs4 미니게임 인원 부족! 포지션 상관없이 2명 모집.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0006-0000-000000000006','team-4817-0003-0000-000000000003','fm-4817-0007-0000-000000000007','2026-02-12','21:00:00','인천 남동 풋살경기장','인천 남동구 구월동 55-12','인천광역시','남동구',0,6,'FIELD',2,0,8000,60,'필드 2명 급구합니다! 야간 매치.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0007-0000-000000000007','team-4817-0003-0000-000000000003','fm-4817-0008-0000-000000000008','2026-02-21','10:00:00','인천 축구전용구장 풋살코트','인천 남동구 논현동 700','인천광역시','남동구',0,14,'GK',1,0,10000,80,'토요일 오전 골키퍼 모집! 장갑 제공.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0008-0000-000000000008','team-4817-0003-0000-000000000003',NULL,'2026-02-25','20:00:00','인천 남동 풋살경기장','인천 남동구 구월동 55-12','인천광역시','남동구',0,6,'ANY',4,0,5000,60,'수요일 저녁 자체 연습 인원 모집. 초보도 환영!','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0009-0000-000000000009','team-4817-0004-0000-000000000004','fm-4817-0009-0000-000000000009','2026-02-13','19:30:00','탄천 풋살구장','경기도 성남시 분당구 탄천로 215','경기도','성남시',2,14,'FIELD',3,0,8000,60,'혼성 매치! 필드 3명 모집합니다. 여성분도 환영!','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0010-0000-000000000010','team-4817-0004-0000-000000000004','fm-4817-0010-0000-000000000010','2026-02-20','20:00:00','판교 풋살파크','경기도 성남시 분당구 판교역로 166','경기도','성남시',0,6,'ANY',4,2,10000,80,'판교 직장인 모집! 퇴근 후 가볍게 한 판.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0011-0000-000000000011','team-4817-0004-0000-000000000004',NULL,'2026-03-01','16:00:00','탄천 풋살구장','경기도 성남시 분당구 탄천로 215','경기도','성남시',2,14,'GK',1,0,0,100,'골키퍼 1명 급구! 무료 참가. 보장시간 100분.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0012-0000-000000000012','team-4817-0005-0000-000000000005','fm-4817-0012-0000-000000000012','2026-02-11','19:00:00','부천 종합운동장 풋살코트','경기도 부천시 소사구 경인로 66','경기도','부천시',0,6,'ANY',3,0,7000,60,'화요일 정기 매치 인원 보충! 포지션 무관 3명.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0013-0000-000000000013','team-4817-0005-0000-000000000005','fm-4817-0013-0000-000000000013','2026-02-18','19:00:00','부천 종합운동장 풋살코트','경기도 부천시 소사구 경인로 66','경기도','부천시',0,6,'FIELD',2,0,7000,60,'필드 2명 모집. 부천/인천 거주자 우대.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0014-0000-000000000014','team-4817-0006-0000-000000000006','fm-4817-0014-0000-000000000014','2026-02-09','20:00:00','강남 스포츠센터 풋살장','서울 강남구 테헤란로 152','서울특별시','강남구',0,14,'FIELD',2,0,12000,60,'월요일 야간 매치 필드 2명 급모! 강남역 근처.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0015-0000-000000000015','team-4817-0006-0000-000000000006','fm-4817-0015-0000-000000000015','2026-02-15','10:00:00','올림픽공원 풋살장','서울 송파구 올림픽로 424','서울특별시','송파구',2,14,'ANY',6,3,15000,100,'6vs6 올림픽공원 주말 매치! 혼성 가능. 보장시간 100분!','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0016-0000-000000000016','team-4817-0006-0000-000000000006','fm-4817-0016-0000-000000000016','2026-02-22','19:00:00','강남 스포츠센터 풋살장','서울 강남구 테헤란로 152','서울특별시','강남구',0,6,'GK',1,0,10000,80,'금요 저녁 골키퍼 모집! 경험자 우대합니다.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0017-0000-000000000017','team-4817-0006-0000-000000000006',NULL,'2026-03-07','14:00:00','강남 스포츠센터 풋살장','서울 강남구 테헤란로 152','서울특별시','강남구',0,6,'ANY',5,0,0,60,'3월 첫째주 무료 자체 연습. 모든 포지션 환영!','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0018-0000-000000000018','team-4817-0002-0000-000000000002',NULL,'2026-03-08','13:00:00','수원 월드컵 풋살장','경기도 수원시 팔달구 월드컵로 310','경기도','수원시',0,14,'FIELD',4,0,8000,80,'일요일 오후 매치! 필드 4명 대모집.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0019-0000-000000000019','team-4817-0005-0000-000000000005',NULL,'2026-03-14','19:00:00','부천 종합운동장 풋살코트','경기도 부천시 소사구 경인로 66','경기도','부천시',0,6,'GK',1,0,5000,60,'3월 정기 매치 골키퍼 1명 모집합니다.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34'),('gr-4817-0020-0000-000000000020','team-4817-0001-0000-000000000001','fm-4817-0018-0000-000000000018','2026-03-07','19:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','서울특별시','강남구',0,14,'ANY',3,0,10000,80,'봄맞이 친선전 인원 보충! 3명 모집합니다.','RECRUITING','2026-02-08 17:21:34','2026-02-08 17:21:34');
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
  KEY `idx_league_status` (`status`),
  KEY `idx_league_region` (`region_sido`,`region_sigungu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `league`
--

/*!40000 ALTER TABLE `league` DISABLE KEYS */;
INSERT INTO `league` (`uid`, `name`, `season`, `region_sido`, `region_sigungu`, `status`, `max_teams`, `current_teams`, `start_date`, `end_date`, `description`, `rules`, `entry_fee`, `prize_info`, `created_date`, `updated_date`, `banner_file_uid`) VALUES ('ffe7e84e-045e-11f1-969c-309c23e78196','A리그','2025-2026','경남','진주시','IN_PROGRESS',10,0,'2025-09-01','2026-03-31','경남 진주시 프리미어 디비전',NULL,0,NULL,'2026-02-08 04:55:49','2026-02-08 04:56:12',NULL),('league-4817-0001-0000-000000000001','2026 진주 풋살 리그','2026-01','경남','진주시','IN_PROGRESS',8,4,'2026-02-01','2026-04-30','서울 강남 지역 풋살팀들의 열정적인 리그전! 매주 주말 경기가 진행됩니다.','1. 5대5 풋살 규칙 적용\n2. 전후반 각 20분\n3. 교체 자유\n4. 승리 3점, 무승부 1점, 패배 0점',0,NULL,'2026-02-08 17:01:31','2026-02-08 19:55:18',NULL),('league-4817-0002-0000-000000000002','2026 사천 풋살 리그','2026-01','경남','진주시','IN_PROGRESS',8,4,'2026-02-15','2026-05-31','경기도 전역 풋살팀이 참가하는 챔피언십 대회! 최강팀을 가립니다.','1. 5대5 풋살 규칙 적용\n2. 전후반 각 25분\n3. 교체 자유\n4. 승리 3점, 무승부 1점, 패배 0점\n5. 골득실로 순위 결정',0,NULL,'2026-02-08 17:01:31','2026-02-08 19:55:18',NULL);
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
INSERT INTO `league_match` (`uid`, `league_uid`, `round`, `home_team_uid`, `away_team_uid`, `home_score`, `away_score`, `match_date`, `match_time`, `stadium_name`, `stadium_address`, `status`, `notes`, `created_date`, `updated_date`, `duration_minutes`) VALUES ('lm-4817-0001-0000-000000000001','league-4817-0001-0000-000000000001',3,'team-4817-0001-0000-000000000001','team-4817-0004-0000-000000000004',NULL,NULL,'2026-02-14','14:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',40),('lm-4817-0002-0000-000000000002','league-4817-0001-0000-000000000001',3,'team-4817-0006-0000-000000000006','team-4817-0003-0000-000000000003',NULL,NULL,'2026-02-14','15:00:00','강남 스포츠센터 풋살장','서울 강남구 테헤란로 152','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',40),('lm-4817-0003-0000-000000000003','league-4817-0001-0000-000000000001',4,'team-4817-0003-0000-000000000003','team-4817-0001-0000-000000000001',NULL,NULL,'2026-02-21','10:00:00','인천 남동 풋살경기장','인천 남동구 구월동 55-12','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',40),('lm-4817-0004-0000-000000000004','league-4817-0001-0000-000000000001',4,'team-4817-0004-0000-000000000004','team-4817-0006-0000-000000000006',NULL,NULL,'2026-02-21','14:00:00','탄천 풋살구장','경기도 성남시 분당구 탄천로 215','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',40),('lm-4817-0005-0000-000000000005','league-4817-0001-0000-000000000001',5,'team-4817-0001-0000-000000000001','team-4817-0006-0000-000000000006',NULL,NULL,'2026-03-07','15:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',40),('lm-4817-0006-0000-000000000006','league-4817-0001-0000-000000000001',5,'team-4817-0003-0000-000000000003','team-4817-0004-0000-000000000004',NULL,NULL,'2026-03-07','16:00:00','인천 남동 풋살경기장','인천 남동구 구월동 55-12','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',40),('lm-4817-0007-0000-000000000007','league-4817-0001-0000-000000000001',6,'team-4817-0006-0000-000000000006','team-4817-0001-0000-000000000001',NULL,NULL,'2026-03-14','14:00:00','강남 스포츠센터 풋살장','서울 강남구 테헤란로 152','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',40),('lm-4817-0008-0000-000000000008','league-4817-0001-0000-000000000001',6,'team-4817-0004-0000-000000000004','team-4817-0003-0000-000000000003',NULL,NULL,'2026-03-14','15:30:00','탄천 풋살구장','경기도 성남시 분당구 탄천로 215','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',40),('lm-4817-0009-0000-000000000009','league-4817-0002-0000-000000000002',2,'team-4817-0001-0000-000000000001','team-4817-0005-0000-000000000005',NULL,NULL,'2026-02-15','13:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',50),('lm-4817-0010-0000-000000000010','league-4817-0002-0000-000000000002',2,'team-4817-0004-0000-000000000004','team-4817-0002-0000-000000000002',NULL,NULL,'2026-02-15','15:00:00','탄천 풋살구장','경기도 성남시 분당구 탄천로 215','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',50),('lm-4817-0011-0000-000000000011','league-4817-0002-0000-000000000002',3,'team-4817-0005-0000-000000000005','team-4817-0004-0000-000000000004',NULL,NULL,'2026-02-22','11:00:00','부천 종합운동장 풋살코트','경기도 부천시 소사구 경인로 66','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',50),('lm-4817-0012-0000-000000000012','league-4817-0002-0000-000000000002',3,'team-4817-0002-0000-000000000002','team-4817-0001-0000-000000000001',NULL,NULL,'2026-02-22','14:00:00','수원 월드컵 풋살장','경기도 수원시 팔달구 월드컵로 310','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',50),('lm-4817-0013-0000-000000000013','league-4817-0002-0000-000000000002',4,'team-4817-0001-0000-000000000001','team-4817-0004-0000-000000000004',NULL,NULL,'2026-03-08','13:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',50),('lm-4817-0014-0000-000000000014','league-4817-0002-0000-000000000002',4,'team-4817-0005-0000-000000000005','team-4817-0002-0000-000000000002',NULL,NULL,'2026-03-08','15:00:00','부천 종합운동장 풋살코트','경기도 부천시 소사구 경인로 66','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',50),('lm-4817-0015-0000-000000000015','league-4817-0002-0000-000000000002',5,'team-4817-0002-0000-000000000002','team-4817-0005-0000-000000000005',NULL,NULL,'2026-03-22','10:00:00','수원 월드컵 풋살장','경기도 수원시 팔달구 월드컵로 310','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',50),('lm-4817-0016-0000-000000000016','league-4817-0002-0000-000000000002',5,'team-4817-0004-0000-000000000004','team-4817-0001-0000-000000000001',NULL,NULL,'2026-03-22','14:00:00','탄천 풋살구장','경기도 성남시 분당구 탄천로 215','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',50),('lm-4817-0017-0000-000000000017','league-4817-0002-0000-000000000002',6,'team-4817-0001-0000-000000000001','team-4817-0002-0000-000000000002',NULL,NULL,'2026-04-05','13:00:00','강남 풋살파크','서울 강남구 역삼동 123-45','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',50),('lm-4817-0018-0000-000000000018','league-4817-0002-0000-000000000002',6,'team-4817-0004-0000-000000000004','team-4817-0005-0000-000000000005',NULL,NULL,'2026-04-05','15:00:00','탄천 풋살구장','경기도 성남시 분당구 탄천로 215','SCHEDULED',NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',50);
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
INSERT INTO `league_team` (`uid`, `league_uid`, `team_uid`, `team_rank`, `points`, `matches_played`, `wins`, `draws`, `losses`, `goals_for`, `goals_against`, `goal_difference`, `status`, `joined_date`, `created_date`, `updated_date`, `manner_score`, `played`, `ranking`) VALUES ('lt-4817-0001-0000-000000000001','league-4817-0001-0000-000000000001','team-4817-0001-0000-000000000001',NULL,6,0,2,0,0,7,3,4,'ACTIVE','2026-02-08 17:01:31','2026-02-08 17:01:31','2026-02-08 17:01:31',4.50,2,1),('lt-4817-0002-0000-000000000002','league-4817-0001-0000-000000000001','team-4817-0006-0000-000000000006',NULL,3,0,1,0,1,5,4,1,'ACTIVE','2026-02-08 17:01:31','2026-02-08 17:01:31','2026-02-08 17:01:31',4.30,2,2),('lt-4817-0003-0000-000000000003','league-4817-0001-0000-000000000001','team-4817-0003-0000-000000000003',NULL,3,0,1,0,1,4,5,-1,'ACTIVE','2026-02-08 17:01:31','2026-02-08 17:01:31','2026-02-08 17:01:31',4.00,2,3),('lt-4817-0004-0000-000000000004','league-4817-0001-0000-000000000001','team-4817-0004-0000-000000000004',NULL,0,0,0,0,2,2,6,-4,'ACTIVE','2026-02-08 17:01:31','2026-02-08 17:01:31','2026-02-08 17:01:31',4.20,2,4),('lt-4817-0005-0000-000000000005','league-4817-0002-0000-000000000002','team-4817-0002-0000-000000000002',NULL,3,0,1,0,0,4,2,2,'ACTIVE','2026-02-08 17:01:31','2026-02-08 17:01:31','2026-02-08 17:01:31',4.00,1,1),('lt-4817-0006-0000-000000000006','league-4817-0002-0000-000000000002','team-4817-0005-0000-000000000005',NULL,1,0,0,1,0,2,2,0,'ACTIVE','2026-02-08 17:01:31','2026-02-08 17:01:31','2026-02-08 17:01:31',3.80,1,2),('lt-4817-0007-0000-000000000007','league-4817-0002-0000-000000000002','team-4817-0004-0000-000000000004',NULL,1,0,0,1,0,2,2,0,'ACTIVE','2026-02-08 17:01:31','2026-02-08 17:01:31','2026-02-08 17:01:31',4.30,1,3),('lt-4817-0008-0000-000000000008','league-4817-0002-0000-000000000002','team-4817-0001-0000-000000000001',NULL,0,0,0,0,1,2,4,-2,'ACTIVE','2026-02-08 17:01:31','2026-02-08 17:01:31','2026-02-08 17:01:31',4.40,1,4);
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
  `target_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '피평가자 UID (팀 또는 개인)',
  `target_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'TEAM, USER',
  `match_uid` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '관련 매치 UID',
  `score` decimal(3,2) NOT NULL COMMENT '매너 점수 (0.0 ~ 5.0)',
  `comment` text COLLATE utf8mb4_unicode_ci COMMENT '평가 코멘트',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`),
  KEY `idx_manner_rater` (`rater_uid`),
  KEY `idx_manner_target` (`target_uid`,`target_type`),
  KEY `idx_manner_match` (`match_uid`)
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
INSERT INTO `oauth_access_token` (`token_id`, `token`, `authentication_id`, `user_name`, `client_id`, `authentication`, `refresh_token`) VALUES ('c3310c6bdf4369562ef19dd8d04e0ad4',_binary '�\�\0sr\0Corg.springframework.security.oauth2.common.DefaultOAuth2AccessToken��6$�\�\0L\0additionalInformationt\0Ljava/util/Map;L\0\nexpirationt\0Ljava/util/Date;L\0refreshTokent\0?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\0scopet\0Ljava/util/Set;L\0	tokenTypet\0Ljava/lang/String;L\0valueq\0~\0xpsr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\nactualNamet\0	정이욥t\0statussr\0java.lang.Boolean\� r�՜�\�\0Z\0valuexpt\0jtit\0$e7964a92-0a0c-4656-bb74-6200ab1bc405x\0sr\0java.util.Datehj�KYt\0\0xpw\0\0�v�\�\�xsr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationq\0~\0xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valueq\0~\0xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tMyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiJlNzk2NGE5Mi0wYTBjLTQ2NTYtYmI3NC02MjAwYWIxYmM0MDUiLCJleHAiOjE3NzQwMjI4OTksImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIwMDg0MGFmMi1kYmZjLTQ1ZDAtOTY1Zi05Y2IyMDI4ODY5OGEiLCJjbGllbnRfaWQiOiJzaW5naGFfb2F1dGgiLCJzdGF0dXMiOnRydWV9.t-1dPNxSmsy2gdEduMIXwKk7uORCb48AbFU49sRh6A8sq\0~\0w\0\0�5\�xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writext\0bearert�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tMyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE3NzE1MTcyOTksImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJlNzk2NGE5Mi0wYTBjLTQ2NTYtYmI3NC02MjAwYWIxYmM0MDUiLCJjbGllbnRfaWQiOiJzaW5naGFfb2F1dGgiLCJzdGF0dXMiOnRydWV9.HMvVQzPvHkPGwRH83K99m52gUsNca1_pIr7necA9A3s','446122c1dfe803eecb7f47102adba7f7','leeyop12@naver.com3','singha_oauth',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.com3xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.com3sr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�9\�xt\0leeyop12@naver.com3sr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e68q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�9\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$495abb26-21e3-4b4e-85a6-eb7d17e47950q\0~\0Ct\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e68xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0<$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','d29e34b90965713efdde8d9792f309f5'),('29bf0552ac1cdec28e3f2adbc274b925',_binary '�\�\0sr\0Corg.springframework.security.oauth2.common.DefaultOAuth2AccessToken��6$�\�\0L\0additionalInformationt\0Ljava/util/Map;L\0\nexpirationt\0Ljava/util/Date;L\0refreshTokent\0?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\0scopet\0Ljava/util/Set;L\0	tokenTypet\0Ljava/lang/String;L\0valueq\0~\0xpsr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\nactualNamet\0	정이욥t\0statussr\0java.lang.Boolean\� r�՜�\�\0Z\0valuexpt\0jtit\0$2db45c2c-e5f4-4319-8ffb-b3a2cfc61c19x\0sr\0java.util.Datehj�KYt\0\0xpw\0\0�v���xsr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationq\0~\0xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valueq\0~\0xpt�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tNyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiIyZGI0NWMyYy1lNWY0LTQzMTktOGZmYi1iM2EyY2ZjNjFjMTkiLCJleHAiOjE3NzQwMjI5NjIsImp0aSI6IjczMzNmNWNiLTBjYzAtNDhlMC05Y2UxLWVmNDYyMWI5YjQ4MCIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.AQxgYUYvDNlmxjVvjLslfH6C73w18n9xvNrFb6T6cB0sq\0~\0w\0\0�-�xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writext\0bearertxeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tNyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE3NzE1MTczNjIsImp0aSI6IjJkYjQ1YzJjLWU1ZjQtNDMxOS04ZmZiLWIzYTJjZmM2MWMxOSIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.Tft0kPJO8bPEjuLn9d8HuVInWpbiZ4pyyDgkg0xkMSY','4df8a6234aacccf8b5c8820f6c89a380','leeyop12@naver.com7','singha_oauth',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.com7xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.com7sr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�9\�xt\0leeyop12@naver.com7sr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppsq\0~\0J\0��pppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Pxp\0����\0\0\0t\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e72q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�9\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0N\0����\0\0\0\0q\0~\0q\0~\0Zt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$495abb26-21e3-4b4e-85a6-eb7d17e47960q\0~\0Ct\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e72xsq\0~\0\0\0\0w\0\0\0q\0~\0Wxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Rq\0~\0=t\0<$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','92287ee93930f9b41586457d14f54b64'),('d25e0b765ac77fb512b58cc07f0775c7',_binary '�\�\0sr\0Corg.springframework.security.oauth2.common.DefaultOAuth2AccessToken��6$�\�\0L\0additionalInformationt\0Ljava/util/Map;L\0\nexpirationt\0Ljava/util/Date;L\0refreshTokent\0?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\0scopet\0Ljava/util/Set;L\0	tokenTypet\0Ljava/lang/String;L\0valueq\0~\0xpsr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\nactualNamet\0	정이욥t\0statussr\0java.lang.Boolean\� r�՜�\�\0Z\0valuexpt\0jtit\0$1f019228-82ec-4cd8-b0de-e0589c90c990x\0sr\0java.util.Datehj�KYt\0\0xpw\0\0�v�f�xsr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationq\0~\0xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valueq\0~\0xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6IjFmMDE5MjI4LTgyZWMtNGNkOC1iMGRlLWUwNTg5YzkwYzk5MCIsImV4cCI6MTc3NDAyMTgyNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6ImY1NjIzODAxLTQ0NGEtNDJhZS04ZmUwLWFlOWU5NzU5MmI3NCIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.jZsJ6ggdrNQ7iAjLLRzrs_YA4glCm_vBfdunyoTXkaksq\0~\0w\0\0�\�҃xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writext\0bearert�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTc3MTUxNjIyNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjFmMDE5MjI4LTgyZWMtNGNkOC1iMGRlLWUwNTg5YzkwYzk5MCIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.rIdOxLSit39Yio6Qrd8pnFyzqfhVF1Zp0vCZAqIfC5Y','5964b5bb9557c4091680d712f7c42612','leeyop12@naver.com','singha_oauth',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.comxsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.comsr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�9\�xt\0leeyop12@naver.comsr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e66q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�9\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$495abb26-21e3-4b4e-85a6-eb7d17e47958q\0~\0Ct\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e66xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0<$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','8c80360d71d86902a5030f64a2a3d81a'),('8199dbf635bfbee6f76475409de1c93a',_binary '�\�\0sr\0Corg.springframework.security.oauth2.common.DefaultOAuth2AccessToken��6$�\�\0L\0additionalInformationt\0Ljava/util/Map;L\0\nexpirationt\0Ljava/util/Date;L\0refreshTokent\0?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\0scopet\0Ljava/util/Set;L\0	tokenTypet\0Ljava/lang/String;L\0valueq\0~\0xpsr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\nactualNamet\0	정이욥t\0statussr\0java.lang.Boolean\� r�՜�\�\0Z\0valuexpt\0jtit\0$4a32281f-0f62-4901-ae8c-3e1e9a75a164x\0sr\0java.util.Datehj�KYt\0\0xpw\0\0�w��\rxsr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationq\0~\0xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valueq\0~\0xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tOCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiI0YTMyMjgxZi0wZjYyLTQ5MDEtYWU4Yy0zZTFlOWE3NWExNjQiLCJleHAiOjE3NzQwMzczNzksImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI4NWZhMDZhMy03YTI1LTRiMTMtYjc3My0zZWY1NjRkYjVlZjIiLCJjbGllbnRfaWQiOiJzaW5naGFfb2F1dGgiLCJzdGF0dXMiOnRydWV9.NPu3wgOK_czbDd7Wf0Ot8vaM9GlaLewfcFau1X5pojQsq\0~\0w\0\0�\�(	xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writext\0bearert�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tOCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE3NzE1MzE3NzksImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI0YTMyMjgxZi0wZjYyLTQ5MDEtYWU4Yy0zZTFlOWE3NWExNjQiLCJjbGllbnRfaWQiOiJzaW5naGFfb2F1dGgiLCJzdGF0dXMiOnRydWV9.Uz4RBg7-PwYcDr1AUVS7kNkvgIKfWSyR0M3NL16qJVI','64d22145c7a8c1989ac6be20fc3ae91a','leeyop12@naver.com8','singha_oauth',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.com8xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.com8sr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�9\�xt\0leeyop12@naver.com8sr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppsq\0~\0J\0��pppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Pxp\0����\0\0\0t\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e73q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�9\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0N\0����\0\0\0\0q\0~\0q\0~\0Zt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$495abb26-21e3-4b4e-85a6-eb7d17e47961q\0~\0Ct\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e73xsq\0~\0\0\0\0w\0\0\0q\0~\0Wxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Rq\0~\0=t\0<$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','8adaa6acab17c358d9198650d4ad8d5c'),('de29244fc9eda6d6ac31fa30018b4627',_binary '�\�\0sr\0Corg.springframework.security.oauth2.common.DefaultOAuth2AccessToken��6$�\�\0L\0additionalInformationt\0Ljava/util/Map;L\0\nexpirationt\0Ljava/util/Date;L\0refreshTokent\0?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\0scopet\0Ljava/util/Set;L\0	tokenTypet\0Ljava/lang/String;L\0valueq\0~\0xpsr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\nactualNamet\0	정이욥t\0statussr\0java.lang.Boolean\� r�՜�\�\0Z\0valuexpt\0jtit\0$76dab879-22b2-4dd4-ae7b-d462edfa332ax\0sr\0java.util.Datehj�KYt\0\0xpw\0\0�v~��xsr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationq\0~\0xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valueq\0~\0xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tMiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiI3NmRhYjg3OS0yMmIyLTRkZDQtYWU3Yi1kNDYyZWRmYTMzMmEiLCJleHAiOjE3NzQwMjAxMzQsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJiZDAwNmViMC1lMGY0LTRhNDctOTRkZS04MDRhNmM3Mjk0M2EiLCJjbGllbnRfaWQiOiJzaW5naGFfb2F1dGgiLCJzdGF0dXMiOnRydWV9.DSLKS6tMQnyApZ7qprv9uMdiGzZiyyKlj1VJicpDth8sq\0~\0w\0\0�\��xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writext\0bearert�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tMiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE3NzE1MTQ1MzQsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI3NmRhYjg3OS0yMmIyLTRkZDQtYWU3Yi1kNDYyZWRmYTMzMmEiLCJjbGllbnRfaWQiOiJzaW5naGFfb2F1dGgiLCJzdGF0dXMiOnRydWV9.JB50dKEAUjPpixsMcd4Y3IArEum9O86dTi2USlpXNxw','e38b0f270bad8ce42d15e5d407cda9b8','leeyop12@naver.com2','singha_oauth',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.com2xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.com2sr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�9\�xt\0leeyop12@naver.com2sr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e67q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�9\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$495abb26-21e3-4b4e-85a6-eb7d17e47959q\0~\0Ct\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e67xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0<$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','4b4c1e801dbf8aa5af6a7e435b8a69bb');
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
INSERT INTO `oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('singha_oauth','SinghaOAuthResourceIds','$2a$10$GCftg3mj5dIdbluVjvHAneDFXe4zxmDv98VdqvJ8zP4V3xCIuVW5i','read,write','password,refresh_token',NULL,'ROLE_CLIENT',86400,2592000,NULL,'true');
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
INSERT INTO `oauth_refresh_token` (`token_id`, `token`, `authentication`) VALUES ('4b4c1e801dbf8aa5af6a7e435b8a69bb',_binary '�\�\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valuet\0Ljava/lang/String;xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tMiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiI3NmRhYjg3OS0yMmIyLTRkZDQtYWU3Yi1kNDYyZWRmYTMzMmEiLCJleHAiOjE3NzQwMjAxMzQsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJiZDAwNmViMC1lMGY0LTRhNDctOTRkZS04MDRhNmM3Mjk0M2EiLCJjbGllbnRfaWQiOiJzaW5naGFfb2F1dGgiLCJzdGF0dXMiOnRydWV9.DSLKS6tMQnyApZ7qprv9uMdiGzZiyyKlj1VJicpDth8sr\0java.util.Datehj�KYt\0\0xpw\0\0�\��x',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.com2xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.com2sr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�9\�xt\0leeyop12@naver.com2sr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e67q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�9\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$495abb26-21e3-4b4e-85a6-eb7d17e47959q\0~\0Ct\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e67xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0<$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C'),('8c80360d71d86902a5030f64a2a3d81a',_binary '�\�\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valuet\0Ljava/lang/String;xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6IjFmMDE5MjI4LTgyZWMtNGNkOC1iMGRlLWUwNTg5YzkwYzk5MCIsImV4cCI6MTc3NDAyMTgyNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6ImY1NjIzODAxLTQ0NGEtNDJhZS04ZmUwLWFlOWU5NzU5MmI3NCIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.jZsJ6ggdrNQ7iAjLLRzrs_YA4glCm_vBfdunyoTXkaksr\0java.util.Datehj�KYt\0\0xpw\0\0�\�҃x',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.comxsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.comsr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�9\�xt\0leeyop12@naver.comsr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e66q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�9\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$495abb26-21e3-4b4e-85a6-eb7d17e47958q\0~\0Ct\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e66xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0<$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C'),('d29e34b90965713efdde8d9792f309f5',_binary '�\�\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valuet\0Ljava/lang/String;xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tMyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiJlNzk2NGE5Mi0wYTBjLTQ2NTYtYmI3NC02MjAwYWIxYmM0MDUiLCJleHAiOjE3NzQwMjI4OTksImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiIwMDg0MGFmMi1kYmZjLTQ1ZDAtOTY1Zi05Y2IyMDI4ODY5OGEiLCJjbGllbnRfaWQiOiJzaW5naGFfb2F1dGgiLCJzdGF0dXMiOnRydWV9.t-1dPNxSmsy2gdEduMIXwKk7uORCb48AbFU49sRh6A8sr\0java.util.Datehj�KYt\0\0xpw\0\0�5\�x',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.com3xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.com3sr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�9\�xt\0leeyop12@naver.com3sr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e68q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�9\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$495abb26-21e3-4b4e-85a6-eb7d17e47950q\0~\0Ct\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e68xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0<$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C'),('92287ee93930f9b41586457d14f54b64',_binary '�\�\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valuet\0Ljava/lang/String;xpt�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tNyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiIyZGI0NWMyYy1lNWY0LTQzMTktOGZmYi1iM2EyY2ZjNjFjMTkiLCJleHAiOjE3NzQwMjI5NjIsImp0aSI6IjczMzNmNWNiLTBjYzAtNDhlMC05Y2UxLWVmNDYyMWI5YjQ4MCIsImNsaWVudF9pZCI6InNpbmdoYV9vYXV0aCIsInN0YXR1cyI6dHJ1ZX0.AQxgYUYvDNlmxjVvjLslfH6C73w18n9xvNrFb6T6cB0sr\0java.util.Datehj�KYt\0\0xpw\0\0�-�x',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0\0w\0\0\0\0xq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUrit\0Ljava/lang/String;L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.com7xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0 w\0\0\0?@\0\0\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0roleq\0~\0xpt\0ROLE_CLIENTxsq\0~\0?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0 w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0 w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0\0w\0\0\0\0xq\0~\0/sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0\Zq\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0\0xpt\0leeyop12@naver.com7sr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0?L\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Cw\n\0\0\�9\�xt\0leeyop12@naver.com7sr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Jpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Mxp\0����\0\0\0t\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e72q\0~\0At\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0\0w\0\0\0\0xsq\0~\0\0\0\0\0w\0\0\0\0xpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Oq\0~\0;t\0<$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C'),('8adaa6acab17c358d9198650d4ad8d5c',_binary '�\�\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\�Gc�\�ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\�\ncT\�^\0L\0valuet\0Ljava/lang/String;xpt\�eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY3R1YWxOYW1lIjoi7KCV7J207JqlIiwiYXVkIjpbIlNpbmdoYU9BdXRoUmVzb3VyY2VJZHMiXSwidXNlcl9uYW1lIjoibGVleW9wMTJAbmF2ZXIuY29tOCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiI0YTMyMjgxZi0wZjYyLTQ5MDEtYWU4Yy0zZTFlOWE3NWExNjQiLCJleHAiOjE3NzQwMzczNzksImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI4NWZhMDZhMy03YTI1LTRiMTMtYjc3My0zZWY1NjRkYjVlZjIiLCJjbGllbnRfaWQiOiJzaW5naGFfb2F1dGgiLCJzdGF0dXMiOnRydWV9.NPu3wgOK_czbDd7Wf0Ot8vaM9GlaLewfcFau1X5pojQsr\0java.util.Datehj�KYt\0\0xpw\0\0�\�(	x',_binary '�\�\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1�\�\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0�\�^\�\0L\0cq\0~\0xpsr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0&\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0singha_oauthsr\0%java.util.Collections$UnmodifiableMap\��t\�B\0L\0mq\0~\0xpsr\0java.util.HashMap\��\�`\�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0\ngrant_typet\0passwordt\0usernamet\0leeyop12@naver.com8xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet\�l\�Z�\�*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0readt\0writexsq\0~\0#w\0\0\0?@\0\0\0\0\0sq\0~\0\rt\0ROLE_CLIENTxsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xppsq\0~\0#w\0\0\0?@\0\0\0\0\0t\0SinghaOAuthResourceIdsxsq\0~\0#w\0\0\0?@\0\0\0\0\0\0xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0&\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\01sr\0java.util.LinkedHashMap4�N\\l��\0Z\0accessOrderxq\0~\0\Z?@\0\0\0\0\0w\0\0\0\0\0\0q\0~\0q\0~\0q\0~\0q\0~\0x\0psr\0!com.ttwijang.cms.oauth.SinghaUser\�\�\�b>3\�Z\0L\0usert\0Lcom/ttwijang/cms/entity/User;xr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0&\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0 sr\0java.util.TreeSetݘP��\�[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0&\0\0xpw\0\0\0q\0~\0xpt\0leeyop12@naver.com8sr\0com.ttwijang.cms.entity.User\0\0\0\0\0\0\0\0Z\0enabledZ\0\njoinStatusZ\0marketingStatusZ\0	notLockedZ\0withdrawStatusL\0\nactualNameq\0~\0L\0addressq\0~\0L\0\raddressDetailq\0~\0L\0birtht\0Ljava/time/LocalDate;L\0concatNumberq\0~\0L\0\ncreateDatet\0Ljava/time/LocalDateTime;L\0emailq\0~\0L\0gendert\0Ljava/lang/Integer;L\0latq\0~\0L\0lonq\0~\0L\0pointq\0~\0AL\0postCodeq\0~\0L\0providert\0*Lcom/ttwijang/cms/oauth/soical/SocialType;L\0\nproviderIdq\0~\0L\0rolesq\0~\0L\0siteUidq\0~\0L\0uidq\0~\0L\0userIdq\0~\0L\0userPasswordq\0~\0xp\0t\0	정이욥ppsr\0\rjava.time.Ser�]��\"H�\0\0xpw\0\0\�\Zxt\001044493118sq\0~\0Ew\n\0\0\�9\�xt\0leeyop12@naver.com8sr\0java.lang.Integer⠤\���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0ppq\0~\0Lpppsr\0/org.hibernate.collection.internal.PersistentBag�Wů\�O�D\0L\0bagq\0~\0L\0providedCollectionq\0~\0xr\0>org.hibernate.collection.internal.AbstractPersistentCollectionW�]��sT\0Z\0allowLoadOutsideTransactionI\0\ncachedSizeZ\0dirtyZ\0elementRemovedZ\0initializedZ\0\risTempSessionL\0keyt\0Ljava/io/Serializable;L\0ownerq\0~\0L\0roleq\0~\0L\0sessionFactoryUuidq\0~\0L\0storedSnapshotq\0~\0Oxp\0����\0\0\0t\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e73q\0~\0Ct\0\"com.ttwijang.cms.entity.User.rolespsq\0~\0\0\0\0w\0\0\0sr\0 com.ttwijang.cms.entity.UserRole\0\0\0\0\0\0\0\0L\0\ncreateDateq\0~\0@L\0rolet\0Lcom/ttwijang/cms/entity/Role;L\0roleCodeq\0~\0L\0uidq\0~\0L\0userq\0~\05L\0userUidq\0~\0xpsq\0~\0Ew\n\0\0\�9\�xsr\0com.ttwijang.cms.entity.Role\0\0\0\0\0\0\0\0Z\0joinAccessStateL\0\ncreateDateq\0~\0@L\0descriptionq\0~\0L\0roleCodeq\0~\0L\0roleNameq\0~\0L\0siteUidq\0~\0L\0	userRolesq\0~\0xpsq\0~\0Ew\n\0\0\�#\�xt\0일반 사용자 권한q\0~\0t\0일반 사용자t\0$00070154-eb1d-4972-97b0-03365762fcc1sq\0~\0M\0����\0\0\0\0q\0~\0q\0~\0Yt\0&com.ttwijang.cms.entity.Role.userRolesppppt\0	ROLE_USERt\0$495abb26-21e3-4b4e-85a6-eb7d17e47961q\0~\0Ct\0$1c2301ea-9743-4d57-86b0-3b7b4eab1e73xsq\0~\0\0\0\0w\0\0\0q\0~\0Vxpt\0$00070154-eb1d-4972-97b0-03365762fcc1q\0~\0Qq\0~\0=t\0<$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C');
/*!40000 ALTER TABLE `oauth_refresh_token` ENABLE KEYS */;

--
-- Table structure for table `payment_request`
--

DROP TABLE IF EXISTS `payment_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_request` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `order_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `order_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` int NOT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `approved_at` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `balance_amount` int NOT NULL,
  `culture_expense` bit(1) DEFAULT NULL,
  `currency` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_transaction_key` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `method` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `order_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `order_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `payment_key` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `requested_at` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `supplied_amount` int NOT NULL,
  `tax_free_amount` int NOT NULL,
  `total_amount` int NOT NULL,
  `use_escrow` bit(1) DEFAULT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `vat` int NOT NULL,
  `version` date DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `currency` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_transaction_key` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `method` int NOT NULL,
  `order_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `order_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `payment_key` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `requested_at` datetime DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `supplied_amount` int NOT NULL,
  `tax_free_amount` int NOT NULL,
  `total_amount` int NOT NULL,
  `use_escrow` bit(1) DEFAULT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `vat` int NOT NULL,
  `version` date DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remain_point` int DEFAULT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `team_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `account_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '작성자 UID (레거시, user_uid 사용)',
  `board_id` bigint DEFAULT NULL,
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
  `team_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '팀 고유값 (팀 커뮤니티용)',
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
  KEY `idx_post_team_uid` (`team_uid`),
  KEY `idx_post_team_community` (`team_uid`,`delete_status`,`create_date`),
  CONSTRAINT `fk_post_board` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_post_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` (`id`, `account_id`, `board_id`, `category_id`, `title`, `content`, `count`, `score`, `secret_yn`, `use_yn`, `created_date`, `updated_date`, `uid`, `board_uid`, `team_uid`, `create_date`, `delete_status`, `depth`, `hidden_status`, `notice_status`, `parent_uid`, `password`, `user_uid`, `view_count`, `view_order`, `writer`) VALUES (1,NULL,NULL,NULL,'ㅅㄷㄴㅅ','ㅅㄷㄴㅅ',0,NULL,'N','Y','2026-02-19 05:36:45','2026-02-19 05:36:45','5c25dc51-03ac-46fd-82dd-4e10e8870333',NULL,'cfdb914c-dd93-4750-b7a7-be4f1d3ada85','2026-02-19 05:36:46',_binary '\0',1,_binary '\0',_binary '',NULL,NULL,'1c2301ea-9743-4d57-86b0-3b7b4eab1e72',0,0,'정이욥'),(2,NULL,NULL,NULL,'ㅅㄷㄴㅅ','ㅅㄷㄴㅅ',0,NULL,'N','Y','2026-02-19 05:36:56','2026-02-19 05:36:56','a15f2123-dd07-4a78-80aa-cd457cee1a22',NULL,'cfdb914c-dd93-4750-b7a7-be4f1d3ada85','2026-02-19 05:36:57',_binary '\0',1,_binary '\0',_binary '',NULL,NULL,'1c2301ea-9743-4d57-86b0-3b7b4eab1e72',0,1,'정이욥'),(3,NULL,NULL,NULL,'test2','test2',0,NULL,'N','Y','2026-02-19 05:50:43','2026-02-19 05:55:19','3408d534-3fcd-46ac-a66b-aae569b56a25',NULL,'cfdb914c-dd93-4750-b7a7-be4f1d3ada85','2026-02-19 05:50:43',_binary '',1,_binary '\0',_binary '\0',NULL,NULL,'1c2301ea-9743-4d57-86b0-3b7b4eab1e73',0,2,NULL),(4,NULL,NULL,NULL,'ㅅㄷㄴㅅ2','ㅅㄷㄴㅅ2',0,NULL,'N','Y','2026-02-19 05:55:22','2026-02-19 06:03:55','4309de62-7c4b-4baa-a5a2-5b6cd2d83252',NULL,'cfdb914c-dd93-4750-b7a7-be4f1d3ada85','2026-02-19 05:55:23',_binary '\0',1,_binary '\0',_binary '\0',NULL,NULL,'1c2301ea-9743-4d57-86b0-3b7b4eab1e73',0,3,NULL);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;

--
-- Table structure for table `post_category`
--

DROP TABLE IF EXISTS `post_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_category` (
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `post_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKt16dyoyl5e595yr8irt56mdu1` (`category_uid`),
  CONSTRAINT `FKt16dyoyl5e595yr8irt56mdu1` FOREIGN KEY (`category_uid`) REFERENCES `board_category` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `field_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `input_value` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `post_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `view_order` int DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKiy14aiik7py8ev819fi1q93bl` (`field_uid`),
  CONSTRAINT `FKiy14aiik7py8ev819fi1q93bl` FOREIGN KEY (`field_uid`) REFERENCES `board_field` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `uid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `field_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `post_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `view_order` int DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_file`
--

/*!40000 ALTER TABLE `post_file` DISABLE KEYS */;
INSERT INTO `post_file` (`uid`, `field_uid`, `file_uid`, `post_uid`, `view_order`) VALUES ('7ddeb353-0249-4032-bbb4-83b1492bfc7f',NULL,'326aae43-2606-45e3-822f-00bbf41924d5','a15f2123-dd07-4a78-80aa-cd457cee1a22',0),('e7e6444f-8b9a-4e06-bbf2-874c86d542a6',NULL,'442cf130-d1bf-446a-aed3-4cd33ec52207','4309de62-7c4b-4baa-a5a2-5b6cd2d83252',0);
/*!40000 ALTER TABLE `post_file` ENABLE KEYS */;

--
-- Table structure for table `post_like`
--

DROP TABLE IF EXISTS `post_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_like` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `post_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `uk_post_like` (`post_uid`,`user_uid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_like`
--

/*!40000 ALTER TABLE `post_like` DISABLE KEYS */;
INSERT INTO `post_like` (`idx`, `post_uid`, `user_uid`, `create_date`) VALUES (5,'4309de62-7c4b-4baa-a5a2-5b6cd2d83252','1c2301ea-9743-4d57-86b0-3b7b4eab1e73','2026-02-19 06:12:36');
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
  `uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '지역 코드',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '지역명',
  `parent_code` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '부모 코드 (시/도의 경우 NULL)',
  `level` int DEFAULT '1' COMMENT '레벨 (1: 시/도, 2: 시/군/구)',
  `sort_order` int DEFAULT '0' COMMENT '정렬 순서',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '사용 여부',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_region_code` (`code`),
  KEY `idx_region_parent` (`parent_code`),
  KEY `idx_region_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region_code`
--

/*!40000 ALTER TABLE `region_code` DISABLE KEYS */;
INSERT INTO `region_code` (`uid`, `code`, `name`, `parent_code`, `level`, `sort_order`, `enabled`, `created_date`) VALUES ('e7714bf6-045c-11f1-969c-309c23e78196','48','경상남도',NULL,1,48,0,'2026-02-08 04:40:49'),('e771543f-045c-11f1-969c-309c23e78196','4817','진주시','48',2,1,1,'2026-02-08 04:40:49'),('e7716273-045c-11f1-969c-309c23e78196','4811','창원시','48',2,2,0,'2026-02-08 04:40:49'),('e7716548-045c-11f1-969c-309c23e78196','4831','김해시','48',2,3,0,'2026-02-08 04:40:49'),('e771669f-045c-11f1-969c-309c23e78196','4833','양산시','48',2,4,0,'2026-02-08 04:40:49'),('e77167b4-045c-11f1-969c-309c23e78196','4835','거제시','48',2,5,0,'2026-02-08 04:40:49'),('e77168a9-045c-11f1-969c-309c23e78196','4837','통영시','48',2,6,0,'2026-02-08 04:40:49'),('e77169a7-045c-11f1-969c-309c23e78196','4813','사천시','48',2,7,0,'2026-02-08 04:40:49'),('e7716a9f-045c-11f1-969c-309c23e78196','4815','밀양시','48',2,8,0,'2026-02-08 04:40:49');
/*!40000 ALTER TABLE `region_code` ENABLE KEYS */;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `delete_status` bit(1) NOT NULL,
  `order_group_idx` int DEFAULT NULL,
  `score` int NOT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `view_count` int NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `file_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `view_order` int DEFAULT NULL,
  `review_idx` int DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `FKkmuwuphrbpoikgd1i5dr7csoe` (`review_idx`),
  CONSTRAINT `FKkmuwuphrbpoikgd1i5dr7csoe` FOREIGN KEY (`review_idx`) REFERENCES `review` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1wdpsed5kna2y38hnbgrnhi5b` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
  `active_days` int DEFAULT '0' COMMENT '활동 요일 비트마스크 (일=1,월=2,화=4,수=8,목=16,금=32,토=64',
  `active_time_slots` int DEFAULT '0' COMMENT '활동 시간대 비트마스크 (아침 =1, 낮=2,저녁=4,삼야=8)',
  `bank_account` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bank_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted_date` datetime DEFAULT NULL,
  `feature_tags` text COLLATE utf8mb4_unicode_ci,
  `grade` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `home_stadium_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `logo_file_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `monthly_fee` int DEFAULT '0',
  `recruiting_members` tinyint(1) DEFAULT NULL,
  `refund_bank_account` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `refund_bank_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sponsor_owner_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recruitment_description` text COLLATE utf8mb4_unicode_ci COMMENT '회원 모집 추가사항',
  `team_photo_file_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '팀 단체 사진 파일 UID',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_team_code` (`team_code`),
  KEY `idx_team_region` (`region_sido`,`region_sigungu`),
  KEY `idx_team_status` (`status`),
  KEY `idx_team_owner` (`owner_uid`),
  KEY `idx_team_owner_status` (`owner_uid`,`status`),
  KEY `idx_team_region_status` (`region_sido`,`region_sigungu`,`status`),
  KEY `idx_team_recruiting` (`recruiting_members`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` (`uid`, `name`, `team_code`, `description`, `logo_url`, `established_year`, `activity_days`, `activity_times`, `region_sido`, `region_sigungu`, `home_stadium`, `age_groups`, `gender_type`, `skill_level`, `manner_score`, `member_count`, `total_sponsorship`, `owner_uid`, `status`, `created_date`, `updated_date`, `active_days`, `active_time_slots`, `bank_account`, `bank_name`, `deleted_date`, `feature_tags`, `grade`, `home_stadium_address`, `logo_file_uid`, `monthly_fee`, `recruiting_members`, `refund_bank_account`, `refund_bank_name`, `sponsor_owner_uid`, `recruitment_description`, `team_photo_file_uid`) VALUES ('cfdb914c-dd93-4750-b7a7-be4f1d3ada85','test','test',NULL,NULL,NULL,NULL,NULL,'경남','진주시','test',7,2,1,0,2,NULL,'1c2301ea-9743-4d57-86b0-3b7b4eab1e72','ACTIVE','2026-02-19 03:35:34','2026-02-19 06:10:10',66,6,'test2','test2',NULL,'[\"함께 실력을 키워요\",\"뛰장 리그에 나가요\",\"5 vs 5 풋살 정식룰로 해요\"]',NULL,NULL,'e0c23f97-9317-441a-809e-ff4a612de770',50000,1,NULL,NULL,NULL,'test','2c6b42be-1fbe-4e68-acaf-ff1c28184e39'),('team-4817-0001-0000-000000000001','FC 번개','4817-001','빠른 패싱과 공격적인 축구를 지향하는 풋살팀입니다. 매주 주말 강남에서 활동하고 있습니다.',NULL,NULL,NULL,NULL,'경상남도','진주시','강남 풋살파크',6,0,1,4.2,12,0,'1c2301ea-9743-4d57-86b0-3b7b4eab1e66','ACTIVE','2026-02-08 17:01:31','2026-02-08 18:38:40',65,6,NULL,NULL,NULL,NULL,NULL,'서울 강남구 역삼동 123-45',NULL,30000,1,NULL,NULL,NULL,NULL,NULL),('team-4817-0002-0000-000000000002','수원 드래곤즈','4817-002','수원 지역 최강 풋살팀! 열정적인 멤버들과 함께합니다.',NULL,NULL,NULL,NULL,'경상남도','진주시','수원 월드컵 풋살장',14,0,1,3.8,10,0,'1c2301ea-9743-4d57-86b0-3b7b4eab1e67','ACTIVE','2026-02-08 17:01:31','2026-02-19 03:15:11',65,6,NULL,NULL,NULL,'[\"함께 실력을 키워요\",\"전문 코치님이 있어요\"]',NULL,'경기도 수원시 팔달구 월드컵로 310',NULL,25000,1,NULL,NULL,NULL,'testtest','38c464bd-bcd9-4b78-a173-f412fd85a259'),('team-4817-0003-0000-000000000003','인천 스톰 FC','4817-003','인천을 대표하는 풋살팀. 강한 수비와 빠른 역습이 특기입니다.',NULL,NULL,NULL,NULL,'경상남도','진주시','인천 남동 풋살경기장',6,0,1,4,9,0,'1c2301ea-9743-4d57-86b0-3b7b4eab1e68','ACTIVE','2026-02-08 17:01:31','2026-02-19 02:33:38',97,6,NULL,NULL,NULL,NULL,NULL,'인천 남동구 구월동 55-12',NULL,20000,0,NULL,NULL,NULL,NULL,NULL),('team-4817-0004-0000-000000000004','성남 유나이티드','4817-004','성남에서 활동하는 친목+실력 겸비 풋살팀. 초보자도 환영합니다!',NULL,NULL,NULL,NULL,'경상남도','진주시','탄천 풋살구장',14,2,1,4.5,15,0,'1c2301ea-9743-4d57-86b0-3b7b4eab1e69','ACTIVE','2026-02-08 17:01:31','2026-02-19 02:33:38',65,6,NULL,NULL,NULL,NULL,NULL,'경기도 성남시 분당구 탄천로 215',NULL,35000,0,NULL,NULL,NULL,NULL,NULL),('team-4817-0005-0000-000000000005','부천 이글스','4817-005','부천 소사 지역 풋살팀. 화요일/목요일 저녁에 정기 모임합니다.',NULL,NULL,NULL,NULL,'경상남도','진주시','부천 종합운동장 풋살코트',6,0,1,3.9,8,0,'1c2301ea-9743-4d57-86b0-3b7b4eab1e70','ACTIVE','2026-02-08 17:01:31','2026-02-19 02:33:38',20,4,NULL,NULL,NULL,NULL,NULL,'경기도 부천시 소사구 경인로 66',NULL,15000,0,NULL,NULL,NULL,NULL,NULL),('team-4817-0006-0000-000000000006','강남 풋살 클럽','4817-006','강남역 근처에서 활동하는 직장인 풋살 모임. 금요일 저녁, 주말 활동.',NULL,NULL,NULL,NULL,'경상남도','진주시','강남 스포츠센터 풋살장',14,2,1,4.1,11,0,'1c2301ea-9743-4d57-86b0-3b7b4eab1e71','ACTIVE','2026-02-08 17:01:31','2026-02-19 02:33:38',97,4,NULL,NULL,NULL,NULL,NULL,'서울 강남구 테헤란로 152',NULL,40000,0,NULL,NULL,NULL,NULL,NULL);
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
INSERT INTO `team_member` (`uid`, `team_uid`, `user_uid`, `role`, `position`, `jersey_number`, `status`, `application_message`, `joined_date`, `created_date`, `updated_date`, `application_age`, `application_experience`, `application_region`, `back_number`) VALUES ('5459812e-41f3-48f3-8993-b016e2589b47','cfdb914c-dd93-4750-b7a7-be4f1d3ada85','1c2301ea-9743-4d57-86b0-3b7b4eab1e73','MEMBER',NULL,NULL,'APPROVED',NULL,NULL,'2026-02-19 05:09:50','2026-02-19 06:10:10',NULL,NULL,NULL,NULL),('fc422c4d-96cb-4db6-a38a-047ef33cda2f','cfdb914c-dd93-4750-b7a7-be4f1d3ada85','1c2301ea-9743-4d57-86b0-3b7b4eab1e72','OWNER',NULL,NULL,'APPROVED',NULL,NULL,'2026-02-19 03:35:34','2026-02-19 03:35:34',NULL,NULL,NULL,NULL),('tm-4817-0001-0000-000000000001','team-4817-0001-0000-000000000001','1c2301ea-9743-4d57-86b0-3b7b4eab1e66','OWNER','MF',NULL,'APPROVED',NULL,NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',NULL,NULL,NULL,10),('tm-4817-0002-0000-000000000002','team-4817-0002-0000-000000000002','1c2301ea-9743-4d57-86b0-3b7b4eab1e67','OWNER','FW',NULL,'APPROVED',NULL,NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',NULL,NULL,NULL,9),('tm-4817-0003-0000-000000000003','team-4817-0003-0000-000000000003','1c2301ea-9743-4d57-86b0-3b7b4eab1e68','OWNER','DF',NULL,'APPROVED',NULL,NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',NULL,NULL,NULL,4),('tm-4817-0004-0000-000000000004','team-4817-0004-0000-000000000004','1c2301ea-9743-4d57-86b0-3b7b4eab1e69','OWNER','GK',NULL,'APPROVED',NULL,NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',NULL,NULL,NULL,1),('tm-4817-0005-0000-000000000005','team-4817-0005-0000-000000000005','1c2301ea-9743-4d57-86b0-3b7b4eab1e70','OWNER','FW',NULL,'APPROVED',NULL,NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',NULL,NULL,NULL,7),('tm-4817-0006-0000-000000000006','team-4817-0006-0000-000000000006','1c2301ea-9743-4d57-86b0-3b7b4eab1e71','OWNER','MF',NULL,'APPROVED',NULL,NULL,'2026-02-08 17:01:31','2026-02-08 17:01:31',NULL,NULL,NULL,8);
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
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `secret_key` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
INSERT INTO `user` (`uid`, `site_uid`, `user_id`, `user_password`, `actual_name`, `concat_number`, `provider`, `provider_id`, `enabled`, `not_locked`, `birth`, `email`, `post_code`, `address`, `address_detail`, `lat`, `lon`, `gender`, `withdraw_status`, `join_status`, `marketing_status`, `point`, `register_info_status`, `create_date`) VALUES ('1c2301ea-9743-4d57-86b0-3b7b4eab1e66','00070154-eb1d-4972-97b0-03365762fcc1','leeyop12@naver.com','$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','정이욥','01044493118',NULL,NULL,1,1,'1994-12-26','leeyop12@naver.com',NULL,NULL,NULL,NULL,NULL,0,0,1,1,0,0,'2026-02-08 02:57:32'),('1c2301ea-9743-4d57-86b0-3b7b4eab1e67','00070154-eb1d-4972-97b0-03365762fcc1','leeyop12@naver.com2','$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','정이욥','01044493118',NULL,NULL,1,1,'1994-12-26','leeyop12@naver.com2',NULL,NULL,NULL,NULL,NULL,0,0,1,1,0,0,'2026-02-08 02:57:32'),('1c2301ea-9743-4d57-86b0-3b7b4eab1e68','00070154-eb1d-4972-97b0-03365762fcc1','leeyop12@naver.com3','$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','정이욥','01044493118',NULL,NULL,1,1,'1994-12-26','leeyop12@naver.com3',NULL,NULL,NULL,NULL,NULL,0,0,1,1,0,0,'2026-02-08 02:57:32'),('1c2301ea-9743-4d57-86b0-3b7b4eab1e69','00070154-eb1d-4972-97b0-03365762fcc1','leeyop12@naver.com4','$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','정이욥','01044493118',NULL,NULL,1,1,'1994-12-26','leeyop12@naver.com4',NULL,NULL,NULL,NULL,NULL,0,0,1,1,0,0,'2026-02-08 02:57:32'),('1c2301ea-9743-4d57-86b0-3b7b4eab1e70','00070154-eb1d-4972-97b0-03365762fcc1','leeyop12@naver.com5','$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','정이욥','01044493118',NULL,NULL,1,1,'1994-12-26','leeyop12@naver.com5',NULL,NULL,NULL,NULL,NULL,0,0,1,1,0,0,'2026-02-08 02:57:32'),('1c2301ea-9743-4d57-86b0-3b7b4eab1e71','00070154-eb1d-4972-97b0-03365762fcc1','leeyop12@naver.com6','$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','정이욥','01044493118',NULL,NULL,1,1,'1994-12-26','leeyop12@naver.com6',NULL,NULL,NULL,NULL,NULL,0,0,1,1,0,0,'2026-02-08 02:57:32'),('1c2301ea-9743-4d57-86b0-3b7b4eab1e72','00070154-eb1d-4972-97b0-03365762fcc1','leeyop12@naver.com7','$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','정이욥','01044493118',NULL,NULL,1,1,'1994-12-26','leeyop12@naver.com7',NULL,NULL,NULL,NULL,NULL,0,0,1,1,100000,0,'2026-02-08 02:57:32'),('1c2301ea-9743-4d57-86b0-3b7b4eab1e73','00070154-eb1d-4972-97b0-03365762fcc1','leeyop12@naver.com8','$2a$10$xcU/h5Lhy/tsbagNZ6B4Fee9kFZhUMximbAk2E9EjnfXbYPv/dC7C','정이욥','01044493118',NULL,NULL,1,1,'2000-12-26','leeyop12@naver.com8',NULL,NULL,NULL,NULL,NULL,0,0,1,1,100000,0,'2026-02-08 02:57:32');
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
INSERT INTO `user_role` (`uid`, `user_uid`, `role_code`, `created_date`, `create_date`) VALUES ('495abb26-21e3-4b4e-85a6-eb7d17e47950','1c2301ea-9743-4d57-86b0-3b7b4eab1e68','ROLE_USER','2026-02-08 02:57:32','2026-02-08 02:57:32'),('495abb26-21e3-4b4e-85a6-eb7d17e47951','1c2301ea-9743-4d57-86b0-3b7b4eab1e69','ROLE_USER','2026-02-08 02:57:32','2026-02-08 02:57:32'),('495abb26-21e3-4b4e-85a6-eb7d17e47952','1c2301ea-9743-4d57-86b0-3b7b4eab1e70','ROLE_USER','2026-02-08 02:57:32','2026-02-08 02:57:32'),('495abb26-21e3-4b4e-85a6-eb7d17e47953','1c2301ea-9743-4d57-86b0-3b7b4eab1e71','ROLE_USER','2026-02-08 02:57:32','2026-02-08 02:57:32'),('495abb26-21e3-4b4e-85a6-eb7d17e47958','1c2301ea-9743-4d57-86b0-3b7b4eab1e66','ROLE_USER','2026-02-08 02:57:32','2026-02-08 02:57:32'),('495abb26-21e3-4b4e-85a6-eb7d17e47959','1c2301ea-9743-4d57-86b0-3b7b4eab1e67','ROLE_USER','2026-02-08 02:57:32','2026-02-08 02:57:32'),('495abb26-21e3-4b4e-85a6-eb7d17e47960','1c2301ea-9743-4d57-86b0-3b7b4eab1e72','ROLE_USER','2026-02-08 02:57:32','2026-02-08 02:57:32'),('495abb26-21e3-4b4e-85a6-eb7d17e47961','1c2301ea-9743-4d57-86b0-3b7b4eab1e73','ROLE_USER','2026-02-08 02:57:32','2026-02-08 02:57:32');
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
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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

-- Dump completed on 2026-02-19  7:59:50

CREATE DATABASE  IF NOT EXISTS `exchange` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `exchange`;
-- MySQL dump 10.13  Distrib 8.0.38, for macos14 (arm64)
--
-- Host: 127.0.0.1    Database: exchange
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `currency` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `balance` decimal(65,2) NOT NULL DEFAULT '0.00',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,1,'USD',0.00,'2024-08-28 17:28:32','2024-08-28 17:29:04'),(2,3,'USD',12345678.90,'2024-08-28 17:28:32','2024-08-28 17:29:04'),(3,7,'USD',0.00,'2024-08-28 17:28:32','2024-08-28 17:29:04'),(4,9,'USD',12345678.90,'2024-08-28 17:28:32','2024-08-29 14:38:18');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `currency` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `address` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `locked` bit(1) NOT NULL DEFAULT b'0',
  `locked_by` bigint NOT NULL DEFAULT '0',
  `locked_until` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_address` (`address`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'BTC','tb1q2fgzsjtxcr6rpm0mm70l527k4cr9mk03ceh642',_binary '',9,'2024-09-03 14:23:49');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fulfillment`
--

DROP TABLE IF EXISTS `fulfillment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fulfillment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `buy_order_id` bigint NOT NULL,
  `sell_order_id` bigint NOT NULL,
  `buyer_id` bigint NOT NULL,
  `seller_id` bigint NOT NULL,
  `currency` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `price` decimal(65,2) NOT NULL,
  `amount` decimal(65,8) NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_bin NOT NULL DEFAULT 'INIT',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fulfillment`
--

LOCK TABLES `fulfillment` WRITE;
/*!40000 ALTER TABLE `fulfillment` DISABLE KEYS */;
/*!40000 ALTER TABLE `fulfillment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `currency` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `price` decimal(65,2) NOT NULL,
  `amount` decimal(65,8) NOT NULL,
  `amount_left` decimal(65,8) NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_bin NOT NULL DEFAULT 'INIT',
  `created_by` bigint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_cby` (`created_by`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,0,'BUY','BTC',34567.89,0.00000001,0.00000001,'init',3,'2024-08-27 20:20:21','2024-08-27 20:20:21'),(2,0,'SELL','BTC',34567.89,0.00000001,0.00000001,'init',9,'2024-08-27 20:25:10','2024-08-27 20:25:10');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statement`
--

DROP TABLE IF EXISTS `statement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statement` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `amount` decimal(65,2) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statement`
--

LOCK TABLES `statement` WRITE;
/*!40000 ALTER TABLE `statement` DISABLE KEYS */;
INSERT INTO `statement` VALUES (1,2,'DEPOSIT',12345678.90,'2024-08-28 17:31:53','2024-08-28 17:31:53'),(2,2,'WITHDRAW',-12345678.90,'2024-08-28 17:31:53','2024-08-28 19:35:43'),(4,2,'DEPOSIT',12345678.90,'2024-08-28 17:31:53','2024-08-28 17:31:53'),(7,2,'WITHDRAW',-12345678.90,'2024-08-28 17:31:53','2024-08-28 17:31:53'),(8,2,'DEPOSIT',12345678.90,'2024-08-28 17:31:53','2024-08-28 17:31:53'),(10,2,'BUY',0.00,'2024-08-28 17:31:53','2024-08-28 17:31:53'),(11,4,'DEPOSIT',12345678.90,'2024-08-29 14:38:18','2024-08-29 14:38:18');
/*!40000 ALTER TABLE `statement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(256) COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'jayliaoo@outlook.com','Jay Liao','+hL2VR3/28UXVElSk4SMZDjlnoo7MRx0a/dW9ZBuwPMN/+2tL7cd+c+PNhyrBitw','2024-08-28 17:32:50','2024-08-28 17:32:50'),(3,'jayliaooo@outlook.com','Jay Liao','+hL2VR3/28UXVElSk4SMZDjlnoo7MRx0a/dW9ZBuwPMN/+2tL7cd+c+PNhyrBitw','2024-08-28 17:32:50','2024-08-28 17:32:50'),(7,'jayliaoooo@outlook.com','Jay Liao','+hL2VR3/28UXVElSk4SMZDjlnoo7MRx0a/dW9ZBuwPMN/+2tL7cd+c+PNhyrBitw','2024-08-28 17:32:50','2024-08-28 17:32:50'),(9,'jayliao2@outlook.com','Jay Liao2','+hL2VR3/28UXVElSk4SMZDjlnoo7MRx0a/dW9ZBuwPMN/+2tL7cd+c+PNhyrBitw','2024-08-28 17:32:50','2024-08-28 17:32:50');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-04 19:57:07

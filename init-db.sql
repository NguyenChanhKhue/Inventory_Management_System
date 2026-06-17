-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventory_DB2
-- ------------------------------------------------------
-- Server version	8.4.9

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
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `details` varchar(1000) DEFAULT NULL,
  `entity_id` bigint NOT NULL,
  `entity_name` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_logs`
--

LOCK TABLES `audit_logs` WRITE;
/*!40000 ALTER TABLE `audit_logs` DISABLE KEYS */;
INSERT INTO `audit_logs` VALUES (1,'CREATE','2026-06-16 10:19:45.214299','Tạo phiếu xuất bán (SL: 100, SP: Áo thun nam)',352,'Transaction','dungrom4269@gmail.com'),(2,'DELETE','2026-06-16 10:21:02.531039','Xóa danh mục (ID: 4)',4,'Category','md@gmail.com'),(3,'CREATE','2026-06-16 10:21:20.575810','Tạo phiếu điều chỉnh kho (Chênh lệch: 4, SP: Áo thun nam)',353,'Transaction','md@gmail.com'),(4,'CREATE','2026-06-16 10:22:04.466239','Tạo phiếu nhập hàng (SL: 100, SP: Áo thun nam)',354,'Transaction','md@gmail.com'),(5,'UPDATE','2026-06-16 10:34:21.194455','Cập nhật thông tin tài khoản: md@gmail.com',102,'User','md@gmail.com'),(6,'UPDATE','2026-06-16 10:34:28.189696','Cập nhật thông tin tài khoản: mandungk506@gmail.com',152,'User','md@gmail.com'),(7,'UPDATE','2026-06-16 10:40:54.244046','Cập nhật thông tin tài khoản: dungrom4269@gmail.com',153,'User','md@gmail.com'),(8,'DELETE','2026-06-16 10:43:59.130228','Vô hiệu hóa (Khóa) tài khoản: mandungk506@gmail.com',152,'User','md@gmail.com'),(9,'UPDATE','2026-06-16 10:44:04.054664','Cập nhật thông tin tài khoản: dungrom4269@gmail.com',153,'User','md@gmail.com'),(10,'UPDATE','2026-06-16 10:44:42.521140','Cập nhật thông tin tài khoản: dungrom4269@gmail.com',153,'User','dungrom4269@gmail.com'),(11,'UPDATE','2026-06-16 10:48:46.117778','Cập nhật thông tin tài khoản: dungrom4269@gmail.com',153,'User','md@gmail.com'),(12,'UPDATE','2026-06-16 10:49:12.432614','Cập nhật thông tin tài khoản: dungrom4269@gmail.com',153,'User','md@gmail.com'),(13,'CREATE','2026-06-16 14:26:08.381848','Tạo mới danh mục: 3.ĐỒ GIA DỤNG',102,'Category','md@gmail.com'),(14,'CREATE','2026-06-16 14:27:10.244191','Tạo mới sản phẩm: tai nghe bluetooth',102,'Product','md@gmail.com'),(15,'CREATE','2026-06-16 14:27:53.073670','Tạo mới nhà cung cấp: Dũng đẹp trai',52,'Supplier','md@gmail.com'),(16,'UPDATE','2026-06-16 14:27:56.834901','Cập nhật nhà cung cấp: Dũng đẹp trai',52,'Supplier','md@gmail.com'),(17,'CREATE','2026-06-16 14:29:01.557258','Tạo phiếu nhập hàng (SL: 100, SP: tai nghe bluetooth)',402,'Transaction','md@gmail.com'),(18,'UPDATE','2026-06-16 17:29:06.243448','Cập nhật thông tin tài khoản: md@gmail.com',102,'User','md@gmail.com'),(19,'UPDATE','2026-06-16 17:56:22.163211','Cập nhật thông tin tài khoản: md@gmail.com',102,'User','md@gmail.com'),(20,'CREATE','2026-06-16 17:56:57.560183','Tạo mới nhà cung cấp: mdung',102,'Supplier','md@gmail.com'),(21,'DELETE','2026-06-16 17:57:05.084955','Xóa cứng nhà cung cấp (ID: 102)',102,'Supplier','md@gmail.com'),(22,'UPDATE','2026-06-16 17:57:20.201311','Cập nhật nhà cung cấp: Dũng đẹp trai',52,'Supplier','md@gmail.com'),(23,'CREATE','2026-06-16 18:36:45.110123','Tạo phiếu xuất bán (SL: 50, SP: tai nghe bluetooth)',452,'Transaction','md@gmail.com'),(24,'CREATE','2026-06-16 18:37:40.710666','Tạo phiếu nhập hàng (SL: 100, SP: tai nghe bluetooth)',453,'Transaction','md@gmail.com'),(25,'CREATE','2026-06-16 18:38:23.902745','Tạo phiếu xuất bán (SL: 50, SP: tai nghe bluetooth)',454,'Transaction','md@gmail.com'),(26,'CREATE','2026-06-16 18:40:07.914549','Tạo phiếu trả hàng cho NCC (SL: 100, SP: tai nghe bluetooth)',455,'Transaction','md@gmail.com'),(27,'CREATE','2026-06-16 18:42:30.033345','Tạo mới sản phẩm: Quần TÂy',152,'Product','md@gmail.com'),(28,'CREATE','2026-06-16 18:48:00.221724','Tạo phiếu điều chỉnh kho (Chênh lệch: 10, SP: Quần TÂy)',456,'Transaction','md@gmail.com'),(29,'CREATE','2026-06-16 19:17:51.297718','Tạo phiếu nhập hàng (SL: 100, SP: Quần TÂy)',502,'Transaction','md@gmail.com'),(30,'CREATE','2026-06-16 19:18:29.081227','Tạo phiếu trả hàng cho NCC (SL: 50, SP: Quần TÂy)',503,'Transaction','md@gmail.com'),(31,'CREATE','2026-06-16 19:19:07.142461','Tạo phiếu xuất bán (SL: 205, SP: tai nghe)',504,'Transaction','md@gmail.com'),(32,'DELETE','2026-06-16 19:22:16.474694','Xóa sản phẩm: tai nghe bluetooth',102,'Product','md@gmail.com'),(33,'UPDATE','2026-06-16 19:49:32.163018','Cập nhật trạng thái phiếu giao dịch thành: COMPLETED',503,'Transaction','md@gmail.com'),(34,'UPDATE','2026-06-16 19:51:39.210640','Cập nhật trạng thái phiếu giao dịch thành: PENDING',456,'Transaction','md@gmail.com'),(35,'UPDATE','2026-06-16 19:57:54.979177','Cập nhật trạng thái phiếu giao dịch thành: CANCELLED',456,'Transaction','md@gmail.com'),(36,'UPDATE','2026-06-16 20:08:07.559380','Cập nhật trạng thái phiếu giao dịch thành: COMPLETED',455,'Transaction','md@gmail.com'),(37,'UPDATE','2026-06-16 20:13:31.333622','Cập nhật trạng thái phiếu giao dịch thành: COMPLETED',503,'Transaction','md@gmail.com'),(38,'UPDATE','2026-06-16 20:13:53.788160','Cập nhật trạng thái phiếu giao dịch thành: CANCELLED',503,'Transaction','md@gmail.com'),(39,'CREATE','2026-06-16 20:19:12.248755','Tạo phiếu trả hàng cho NCC (SL: 50, SP: Quần TÂy)',552,'Transaction','md@gmail.com'),(40,'UPDATE','2026-06-16 20:19:28.971259','Cập nhật trạng thái phiếu giao dịch thành: COMPLETED',552,'Transaction','md@gmail.com'),(41,'UPDATE','2026-06-16 20:20:03.203779','Cập nhật trạng thái phiếu giao dịch thành: CANCELLED',552,'Transaction','md@gmail.com'),(42,'CREATE','2026-06-16 20:29:40.311338','Tạo phiếu xuất bán (SL: 50, SP: Quần TÂy)',602,'Transaction','md@gmail.com'),(43,'UPDATE','2026-06-16 20:32:48.532336','Cập nhật trạng thái phiếu giao dịch thành: CANCELLED',602,'Transaction','md@gmail.com'),(44,'CREATE','2026-06-16 20:36:32.201450','Tạo phiếu xuất bán (SL: 50, SP: Quần TÂy)',652,'Transaction','md@gmail.com'),(45,'CREATE','2026-06-16 20:37:30.932597','Tạo phiếu nhập hàng (SL: 100, SP: Quần TÂy)',653,'Transaction','md@gmail.com'),(46,'UPDATE','2026-06-16 20:42:31.706050','Cập nhật trạng thái phiếu giao dịch thành: CANCELLED',653,'Transaction','md@gmail.com'),(47,'CREATE','2026-06-16 20:43:37.995328','Tạo phiếu xuất bán (SL: 50, SP: Quần TÂy)',702,'Transaction','md@gmail.com'),(48,'CREATE','2026-06-16 20:44:16.656725','Tạo phiếu nhập hàng (SL: 1000, SP: Quần TÂy)',703,'Transaction','md@gmail.com'),(49,'CREATE','2026-06-16 20:53:12.200071','Tạo phiếu trả hàng cho NCC (SL: 500, SP: Quần TÂy)',752,'Transaction','md@gmail.com'),(50,'UPDATE','2026-06-16 20:53:30.995526','Cập nhật trạng thái phiếu giao dịch thành: COMPLETED',752,'Transaction','md@gmail.com'),(51,'CREATE','2026-06-16 20:55:48.620867','Tạo phiếu xuất bán (SL: 400, SP: Quần TÂy)',753,'Transaction','dungrom4269@gmail.com'),(52,'CREATE','2026-06-17 08:26:23.651573','Tạo phiếu nhập hàng (SL: 100, SP: Quần TÂy)',802,'Transaction','md@gmail.com'),(53,'UPDATE','2026-06-17 08:27:17.650424','Cập nhật trạng thái phiếu giao dịch thành: CANCELLED',802,'Transaction','md@gmail.com'),(54,'UPDATE','2026-06-17 08:29:23.484709','Cập nhật nhà cung cấp: Dũng đẹp trai',52,'Supplier','md@gmail.com'),(55,'CREATE','2026-06-17 08:32:56.400845','Tạo phiếu xuất bán (SL: 100, SP: Quần TÂy)',803,'Transaction','md@gmail.com'),(56,'CREATE','2026-06-17 08:33:46.537594','Tạo phiếu xuất bán (SL: 49, SP: tai nghe)',804,'Transaction','md@gmail.com'),(57,'CREATE','2026-06-17 08:34:12.563263','Tạo phiếu điều chỉnh kho (Chênh lệch: 0, SP: tai nghe)',805,'Transaction','md@gmail.com'),(58,'CREATE','2026-06-17 08:37:43.594951','Tạo phiếu điều chỉnh kho (Chênh lệch: 9, SP: tai nghe)',806,'Transaction','md@gmail.com'),(59,'CREATE','2026-06-17 08:38:22.803880','Tạo phiếu nhập hàng (SL: 11, SP: tai nghe)',807,'Transaction','md@gmail.com'),(60,'CREATE','2026-06-17 08:39:09.421626','Tạo phiếu điều chỉnh kho (Chênh lệch: 11, SP: Quần TÂy)',808,'Transaction','md@gmail.com'),(61,'UPDATE','2026-06-17 08:40:38.633778','Cập nhật thông tin tài khoản: mandungk506@gmail.com',152,'User','md@gmail.com'),(62,'DELETE','2026-06-17 08:49:00.690414','Xóa sản phẩm: tai nghe',52,'Product','md@gmail.com'),(63,'CREATE','2026-06-17 08:51:22.454372','Tạo mới sản phẩm: TV Samsung',202,'Product','md@gmail.com'),(64,'UPDATE','2026-06-17 08:51:34.093813','Cập nhật sản phẩm: TV Samsung',202,'Product','md@gmail.com'),(65,'CREATE','2026-06-17 08:52:27.470535','Tạo phiếu nhập hàng (SL: 1000, SP: TV Samsung)',809,'Transaction','md@gmail.com'),(66,'CREATE','2026-06-17 08:54:23.411108','Tạo phiếu điều chỉnh kho (Chênh lệch: 0, SP: TV Samsung)',810,'Transaction','md@gmail.com'),(67,'CREATE','2026-06-17 08:57:10.529173','Tạo mới sản phẩm: Máy tính Dell',203,'Product','md@gmail.com'),(68,'UPDATE','2026-06-17 08:57:40.979210','Cập nhật sản phẩm: Máy tính Dell',203,'Product','md@gmail.com'),(69,'CREATE','2026-06-17 08:58:11.984014','Tạo phiếu nhập hàng (SL: 10000, SP: Máy tính Dell)',811,'Transaction','md@gmail.com'),(70,'CREATE','2026-06-17 09:01:09.292036','Tạo mới danh mục: Áo nam',152,'Category','md@gmail.com'),(71,'CREATE','2026-06-17 09:01:20.598267','Tạo mới danh mục: Áo polo nam',153,'Category','md@gmail.com'),(72,'CREATE','2026-06-17 09:01:24.994073','Tạo mới danh mục: Áo nữ',154,'Category','md@gmail.com'),(73,'UPDATE','2026-06-17 09:01:30.823215','Cập nhật nhà cung cấp: minh thuận',3,'Supplier','md@gmail.com'),(74,'UPDATE','2026-06-17 09:01:32.318926','Cập nhật nhà cung cấp: Dũng đẹp trai',52,'Supplier','md@gmail.com'),(75,'CREATE','2026-06-17 09:02:12.839594','Tạo mới sản phẩm: Áo nữ ',204,'Product','md@gmail.com'),(76,'CREATE','2026-06-17 09:02:50.116265','Tạo mới sản phẩm: Áo polo nam',205,'Product','md@gmail.com'),(77,'CREATE','2026-06-17 09:06:19.605432','Tạo mới danh mục: Đồ ăn',155,'Category','md@gmail.com'),(78,'CREATE','2026-06-17 09:07:01.554100','Tạo mới sản phẩm: Hamburger',206,'Product','md@gmail.com'),(79,'CREATE','2026-06-17 09:07:42.263189','Tạo mới sản phẩm: Khoai tây chiên',207,'Product','md@gmail.com'),(80,'CREATE','2026-06-17 09:08:28.133064','Tạo mới sản phẩm: Điện thoại Iphone',208,'Product','md@gmail.com'),(81,'CREATE','2026-06-17 09:09:20.660915','Tạo mới sản phẩm: Điện thoại xịn',209,'Product','md@gmail.com'),(82,'CREATE','2026-06-17 09:10:04.912691','Tạo mới sản phẩm: Đồ ngủ nữ',210,'Product','md@gmail.com');
/*!40000 ALTER TABLE `audit_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `idx_name` (`name`),
  KEY `FKsaok720gsu4u2wrgbk10b5n8d` (`parent_id`),
  CONSTRAINT `FKsaok720gsu4u2wrgbk10b5n8d` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'1.Áo Thun ',NULL),(2,'2.Quần Tây',NULL),(52,'5.đồ điện tử',NULL),(102,'3.ĐỒ GIA DỤNG',NULL),(152,'Áo nam',NULL),(153,'Áo polo nam',152),(154,'Áo nữ',NULL),(155,'Đồ ăn',NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories_seq`
--

DROP TABLE IF EXISTS `categories_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories_seq`
--

LOCK TABLES `categories_seq` WRITE;
/*!40000 ALTER TABLE `categories_seq` DISABLE KEYS */;
INSERT INTO `categories_seq` VALUES (251);
/*!40000 ALTER TABLE `categories_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_tokens`
--

DROP TABLE IF EXISTS `password_reset_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `expires_at` datetime(6) NOT NULL,
  `otp_verified` bit(1) NOT NULL,
  `token_hash` varchar(64) NOT NULL,
  `used` bit(1) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKajre85ybxavf1tt4omkrs5p6g` (`token_hash`),
  KEY `FKk3ndxg5xp6v7wd4gjyusp15gq` (`user_id`),
  CONSTRAINT `FKk3ndxg5xp6v7wd4gjyusp15gq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_tokens`
--

LOCK TABLES `password_reset_tokens` WRITE;
/*!40000 ALTER TABLE `password_reset_tokens` DISABLE KEYS */;
INSERT INTO `password_reset_tokens` VALUES (3,'2026-06-16 04:59:29.319423','2026-06-16 05:09:53.531081',_binary '','e1bf2783620cb619f2a0d454d4d84d28e5086f2f06ffa26bdb65e8878456bd39',_binary '',153);
/*!40000 ALTER TABLE `password_reset_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sku` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `stock_quantity` int NOT NULL DEFAULT '0',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `exp_date_time` timestamp NULL DEFAULT NULL,
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `category_id` bigint DEFAULT NULL,
  `import_price` decimal(38,2) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `min_stock` int NOT NULL,
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku` (`sku`),
  KEY `idx_sku` (`sku`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_name` (`name`),
  KEY `idx_products_stock` (`stock_quantity`),
  CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'quần','q01',20000.00,10,'ok',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781583277/ojm4dag36ifkdsm691po.png','2026-06-16 04:14:33',2,NULL,0,NULL,0,NULL),(2,'áo','a01',35000.00,19,'siêu đẹp',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781586117/o3tna1dkbdgqzmohqtel.png','2026-06-16 05:01:53',1,NULL,0,NULL,0,NULL),(52,'tai nghe','st001',70000.00,21,'xịn chất lượng',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781598086/mlzrnlcsnuptzg1hdobl.jpg','2026-06-16 08:21:24',52,20000.00,0,'kệ F',10,'cái'),(53,'Áo thun nam','A001',199999.00,110,'chất lượng vải ok',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781598182/s7psoitoducpwtiqr7xk.jpg','2026-06-16 08:23:00',1,59000.00,1,'kệ B ',10,'cái'),(102,'tai nghe bluetooth','tn001',200000.00,0,'chống nước, cách âm',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781620030/joedn1ttnww2eyrt6hx9.jpg','2026-06-16 14:27:07',52,50000.00,0,'Kệ D',10,'cái'),(152,'Quần TÂy','t111',NULL,11,'mẫu mới bên nhật',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781635349/mriyvk4fmemvn6kornes.jpg','2026-06-16 18:42:27',2,40000.00,1,'kê E',10,'cái'),(202,'TV Samsung','TV001',NULL,1000,'Nhập 1000 cái TV',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781686280/y8meho2tnok7s1q776ch.jpg','2026-06-17 08:51:19',52,1000000000.00,1,'Kệ A',5,'Cái'),(203,'Máy tính Dell','DELL1',NULL,10000,'Nhập hàng máy tính',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781686629/liu2svrere4nymmavlee.jpg','2026-06-17 08:57:10',52,20000000.00,1,'F',10,'Cái'),(204,'Áo nữ ','AONU1',NULL,0,'Nhập áo nữ',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781686930/xlsgzz6qamw1l6mc2uay.jpg','2026-06-17 09:02:11',154,100000.00,1,'Kệ D',10,'Thùng'),(205,'Áo polo nam','POLO2',NULL,0,'Nhập áo nam',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781686968/d53vulmb4yaivnceqvve.jpg','2026-06-17 09:02:48',153,200000.00,1,'Kệ G',20,'Hộp'),(206,'Hamburger','KUHYA',NULL,0,'Nhập',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781687219/enqwjmqjtztnjkhhpnxe.jpg','2026-06-17 09:07:00',155,50000.00,1,'Kệ A',10,'Hộp'),(207,'Khoai tây chiên','KHOAI',NULL,0,'Nhập',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781687260/xdmoukb9r7nefdbvirm0.jpg','2026-06-17 09:07:41',155,30000.00,1,'Kệ F',10,'Hộp'),(208,'Điện thoại Iphone','IPHONE',NULL,0,'Nhập điện thoại',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781687307/pc9esqnk47x2wq6omtrx.jpg','2026-06-17 09:08:26',52,1000000.00,1,'Kệ D',10,'Hộp'),(209,'Điện thoại xịn','DIEN1',NULL,0,'Nhập điện thoại',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781687358/xnqlhlkusch2b9gdpu2b.jpg','2026-06-17 09:09:19',52,1000000.00,1,'Kệ D',10,'Thùng'),(210,'Đồ ngủ nữ','AONU2',NULL,0,'Nhập áo',NULL,'https://res.cloudinary.com/dwcxxkbg6/image/upload/v1781687403/mjctpmmzpgfjyp22rvrk.jpg','2026-06-17 09:10:04',154,10000.00,1,'Kệ F',10,'Thùng');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products_seq`
--

DROP TABLE IF EXISTS `products_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products_seq`
--

LOCK TABLES `products_seq` WRITE;
/*!40000 ALTER TABLE `products_seq` DISABLE KEYS */;
INSERT INTO `products_seq` VALUES (301);
/*!40000 ALTER TABLE `products_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `contact_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (2,'Chánh Khuê','0122222','phú yên',_binary ''),(3,'minh thuận','099999','cần thơ',_binary ''),(52,'Dũng đẹp trai','0222222','Gia Lai',_binary '');
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers_seq`
--

DROP TABLE IF EXISTS `suppliers_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers_seq`
--

LOCK TABLES `suppliers_seq` WRITE;
/*!40000 ALTER TABLE `suppliers_seq` DISABLE KEYS */;
INSERT INTO `suppliers_seq` VALUES (201);
/*!40000 ALTER TABLE `suppliers_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `total_product` int NOT NULL,
  `total_price` decimal(38,2) DEFAULT NULL,
  `transaction_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `transaction_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `user_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `supplier_id` bigint DEFAULT NULL,
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `customer_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_transaction_type` (`transaction_type`),
  KEY `idx_create_at` (`create_at`),
  KEY `idx_transactions_status` (`transaction_status`),
  CONSTRAINT `fk_transactions_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_transactions_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_transactions_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=812 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,10,200000.00,'IMPORT','COMPLETED','aaa','aaaaa','2026-06-16 04:31:35','2026-06-16 18:42:59',102,1,3,NULL,NULL),(2,20,700000.00,'IMPORT','COMPLETED','aaaa','ddddd','2026-06-16 05:26:33','2026-06-16 18:42:59',102,2,3,NULL,NULL),(3,10,350000.00,'IMPORT','COMPLETED','aaa','ddd','2026-06-16 05:27:30','2026-06-16 18:42:59',102,2,2,NULL,NULL),(4,20,700000.00,'EXPORT','COMPLETED','cho mdung','dddd','2026-06-16 05:29:03','2026-06-16 18:42:59',102,2,NULL,NULL,NULL),(5,20,0.00,'RETURN_TO_SUPPLIER','PROCESSING','áo ','đéo thích','2026-06-16 05:29:47',NULL,102,2,3,NULL,NULL),(52,30,600000.00,'EXPORT','COMPLETED','16/06/226','cho cửa hàng lan sang','2026-06-16 06:39:19','2026-06-16 18:42:59',102,1,NULL,NULL,NULL),(102,100,19900000.00,'IMPORT','COMPLETED','aaaa','bbbbb','2026-06-16 08:25:28','2026-06-16 18:42:59',102,53,3,NULL,NULL),(103,299,20630701.00,'IMPORT','COMPLETED','aaaa','cccc','2026-06-16 08:26:02','2026-06-16 18:42:59',102,52,2,NULL,NULL),(104,50,3449950.00,'EXPORT','COMPLETED','aaa','ccc','2026-06-16 08:27:01','2026-06-16 18:42:59',102,52,NULL,NULL,NULL),(105,100,6899900.00,'EXPORT','COMPLETED','aaaa','lan sang','2026-06-16 08:27:52','2026-06-16 18:42:59',153,52,NULL,NULL,NULL),(106,111,7658889.00,'IMPORT','COMPLETED','aaaa','vvvvv','2026-06-16 08:28:33','2026-06-16 18:42:59',153,52,3,NULL,NULL),(107,-5,0.00,'ADJUSTMENT','COMPLETED','Kiểm kê kho','hư hỏng','2026-06-16 08:29:21',NULL,153,52,NULL,NULL,NULL),(152,50,9999950.00,'EXPORT','COMPLETED','bán áo thun ','bán 50 cái áo ','2026-06-16 09:13:12','2026-06-16 18:42:59',102,53,NULL,'Lan Sang','0223344'),(153,50,9999950.00,'EXPORT','COMPLETED','aaa','ddd','2026-06-16 09:15:52','2026-06-16 18:42:59',102,53,NULL,'bốn kiến','011'),(202,20,3999980.00,'EXPORT','CANCELLED','aa','bb','2026-06-16 09:21:26','2026-06-16 18:42:59',102,53,NULL,'dũng','01'),(252,1111,222198889.00,'IMPORT','COMPLETED','fd','sdf','2026-06-16 09:34:55','2026-06-16 18:42:59',102,53,2,NULL,NULL),(302,1205,240998795.00,'EXPORT','COMPLETED','mua áo ','1205 cái','2026-06-16 09:55:51','2026-06-16 18:42:59',102,53,NULL,'m dũng','0222'),(303,250,17500000.00,'EXPORT','CANCELLED','aaa','bbbb','2026-06-16 09:56:45','2026-06-16 18:42:59',102,52,NULL,'khuê','11122'),(304,100,19999900.00,'IMPORT','COMPLETED','mua áo','100 cái','2026-06-16 09:57:41','2026-06-16 18:42:59',102,53,2,NULL,NULL),(352,100,19999900.00,'EXPORT','COMPLETED','aa','bb','2026-06-16 10:19:45','2026-06-16 18:42:59',153,53,NULL,'dũng','0111'),(353,4,0.00,'ADJUSTMENT','COMPLETED','Kiểm kê kho','','2026-06-16 10:21:21',NULL,102,53,NULL,NULL,NULL),(354,100,19999900.00,'IMPORT','COMPLETED','nhập áo','áo ','2026-06-16 10:22:04','2026-06-16 18:42:59',102,53,2,NULL,NULL),(402,100,20000000.00,'IMPORT','COMPLETED','nhập tai nghe','100 cái','2026-06-16 14:29:02','2026-06-16 18:42:59',102,102,52,NULL,NULL),(452,50,2500000.00,'EXPORT','COMPLETED','xuất tai nghe cho cửa hàng app store','hàng dễ vỡ','2026-06-16 18:36:45',NULL,102,102,NULL,NULL,NULL),(453,100,5000000.00,'IMPORT','COMPLETED','nhập tai nghe mã zzz','dễ vỡ','2026-06-16 18:37:41',NULL,102,102,52,NULL,NULL),(454,50,2500000.00,'EXPORT','COMPLETED','aa','aa','2026-06-16 18:38:24',NULL,102,102,NULL,NULL,NULL),(455,100,0.00,'RETURN_TO_SUPPLIER','COMPLETED','hàng lỗi','hàng dễ vỡ','2026-06-16 18:40:08','2026-06-16 20:08:08',102,102,52,NULL,NULL),(456,10,0.00,'ADJUSTMENT','CANCELLED','Kiểm kê kho','','2026-06-16 18:48:00','2026-06-16 19:57:55',102,152,NULL,NULL,NULL),(502,100,4000000.00,'IMPORT','COMPLETED',' quần nhật','aaa','2026-06-16 19:17:51',NULL,102,152,52,NULL,NULL),(503,50,0.00,'RETURN_TO_SUPPLIER','CANCELLED','aaa','bbbb','2026-06-16 19:18:29','2026-06-16 20:13:54',102,152,52,NULL,NULL),(504,205,4100000.00,'EXPORT','COMPLETED','tai nghe ','dễ vỡ','2026-06-16 19:19:07',NULL,102,52,NULL,NULL,NULL),(552,50,2000000.00,'RETURN_TO_SUPPLIER','CANCELLED','aa','aaa','2026-06-16 20:19:12','2026-06-16 20:20:03',102,152,52,NULL,NULL),(602,50,2000000.00,'EXPORT','CANCELLED','aaa','aaaa','2026-06-16 20:29:40','2026-06-16 20:32:49',102,152,NULL,NULL,NULL),(652,50,2000000.00,'EXPORT','COMPLETED','aaa','aaaa','2026-06-16 20:36:32',NULL,102,152,NULL,NULL,NULL),(653,100,4000000.00,'IMPORT','CANCELLED','aaa','aaa','2026-06-16 20:37:31','2026-06-16 20:42:32',102,152,52,NULL,NULL),(702,50,2000000.00,'EXPORT','COMPLETED','aaa','aaa','2026-06-16 20:43:38',NULL,102,152,NULL,NULL,NULL),(703,1000,40000000.00,'IMPORT','COMPLETED','aaa','aaa','2026-06-16 20:44:17',NULL,102,152,52,NULL,NULL),(752,500,20000000.00,'RETURN_TO_SUPPLIER','COMPLETED','aaaa','aaa','2026-06-16 20:53:12','2026-06-16 20:53:31',102,152,52,NULL,NULL),(753,400,16000000.00,'EXPORT','COMPLETED','aaaa','aaa','2026-06-16 20:55:49',NULL,153,152,NULL,NULL,NULL),(802,100,4000000.00,'IMPORT','CANCELLED','Nhập quần','nhập 100 cái','2026-06-17 08:26:24','2026-06-17 08:27:18',102,152,2,NULL,NULL),(803,100,4000000.00,'EXPORT','COMPLETED','Xuất kho','Xuất 100 cái quần','2026-06-17 08:32:56',NULL,102,152,NULL,NULL,NULL),(804,49,980000.00,'EXPORT','COMPLETED','Xuất 49','Xuất 49 cái','2026-06-17 08:33:47',NULL,102,52,NULL,NULL,NULL),(805,0,0.00,'ADJUSTMENT','COMPLETED','Kiểm kê kho','','2026-06-17 08:34:13',NULL,102,52,NULL,NULL,NULL),(806,9,0.00,'ADJUSTMENT','COMPLETED','Kiểm kê kho','','2026-06-17 08:37:44',NULL,102,52,NULL,NULL,NULL),(807,11,220000.00,'IMPORT','COMPLETED','Nhập 11 cái','Nhập 11 cái','2026-06-17 08:38:23',NULL,102,52,2,NULL,NULL),(808,11,0.00,'ADJUSTMENT','COMPLETED','Kiểm kê kho','','2026-06-17 08:39:09',NULL,102,152,NULL,NULL,NULL),(809,1000,1000000000000.00,'IMPORT','COMPLETED','Nhập 100 cái TV','Nhập 100 cái','2026-06-17 08:52:27',NULL,102,202,2,NULL,NULL),(810,0,0.00,'ADJUSTMENT','COMPLETED','Kiểm kê kho','','2026-06-17 08:54:23',NULL,102,202,NULL,NULL,NULL),(811,10000,200000000000.00,'IMPORT','COMPLETED','Nhập 1000 cái','Nhập máy tính ','2026-06-17 08:58:12',NULL,102,203,2,NULL,NULL);
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions_seq`
--

DROP TABLE IF EXISTS `transactions_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions_seq`
--

LOCK TABLES `transactions_seq` WRITE;
/*!40000 ALTER TABLE `transactions_seq` DISABLE KEYS */;
INSERT INTO `transactions_seq` VALUES (901);
/*!40000 ALTER TABLE `transactions_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (102,'M Dũng','md@gmail.com','$2a$10$hbSGjaajFJXeEZRtz896Ee1r4akLpANG6cQH//IhP3YHdkrGytNga','012345','ADMIN','2026-06-15 16:42:54',_binary ''),(152,'mdung','mandungk506@gmail.com','$2a$10$fbAd0eYWGuAd/FBufOjGm.lBja6snJRh348JBnbIijm54C7tJyPwy','0877388942','MANAGER','2026-06-16 04:42:57',_binary ''),(153,'dũng','dungrom4269@gmail.com','$2a$10$nlXHHZe/XdrZ8jy0vPQXCOxwt4FRSYtQq2/OAwuaEBD8VOonWouMy','0877388942','MANAGER','2026-06-16 04:45:02',_binary '');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_seq`
--

DROP TABLE IF EXISTS `users_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_seq`
--

LOCK TABLES `users_seq` WRITE;
/*!40000 ALTER TABLE `users_seq` DISABLE KEYS */;
INSERT INTO `users_seq` VALUES (251);
/*!40000 ALTER TABLE `users_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-17 16:16:17

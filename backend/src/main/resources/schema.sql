-- 先删除有外键引用的表
DROP TABLE IF EXISTS `b_feedback`;
DROP TABLE IF EXISTS `b_order`;
DROP TABLE IF EXISTS `b_discount_rule`;
DROP TABLE IF EXISTS `b_scooter`;
DROP TABLE IF EXISTS `b_user`;

-- 创建用户表
CREATE TABLE `b_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL, 
  `password` varchar(255) NOT NULL,
  `avatar` text NOT NULL,
  `birthday` date NOT NULL,
  `user_type` tinyint(1)  NOT NULL,
  `mobile` varchar(13) NOT NULL,
  `email` varchar(50) NOT NULL,
  `payment_method` text DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `role` tinyint(1) NOT NULL DEFAULT 1,
  `is_frequent_user` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 创建滑板车表
CREATE TABLE `b_scooter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `location` varchar(255) NOT NULL,
  `price_hour` decimal(5, 2) NOT NULL,
  `price_four_hour` decimal(5, 2) NOT NULL,
  `price_day` decimal(5, 2) NOT NULL,
  `price_week` decimal(5, 2) NOT NULL, 
  `status` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 创建订单表
CREATE TABLE `b_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_time` datetime NOT NULL,
  `status` tinyint(1)  NOT NULL DEFAULT 1,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `hire_period` varchar(10)  NOT NULL,
  `price` decimal(7, 2) NOT NULL,
  `user_id` bigint NOT NULL,
  `scooter_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `b_order_scooter_id_fk`(`scooter_id`) USING BTREE,
  INDEX `b_order_user_id_fk`(`user_id`) USING BTREE,
  CONSTRAINT `b_order_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `b_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `b_order_scooter_id_fk` FOREIGN KEY (`scooter_id`) REFERENCES `b_scooter` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 创建折扣规则表
CREATE TABLE `b_discount_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_type` varchar(10) NOT NULL,
  `discount_rate` decimal(3, 2) NOT NULL DEFAULT 1.00,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 创建反馈表
CREATE TABLE `b_feedback`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `submit_time` datetime NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 0,
  `priority` tinyint(1)  NOT NULL,
  `user_id` bigint NOT NULL,
  `scooter_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `b_feedback_user_id_fk`(`user_id`) USING BTREE,
  INDEX `b_feedback_scooter_id_fk`(`scooter_id`) USING BTREE,
  CONSTRAINT `b_feedback_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `b_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `b_feedback_scooter_id_fk` FOREIGN KEY (`scooter_id`) REFERENCES `b_scooter` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic; 
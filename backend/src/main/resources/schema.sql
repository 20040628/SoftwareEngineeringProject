-- 先删除有外键引用的表
DROP TABLE IF EXISTS `b_feedback`;
DROP TABLE IF EXISTS `b_order`;
DROP TABLE IF EXISTS `b_discount_rule`;
DROP TABLE IF EXISTS `b_scooter`;
DROP TABLE IF EXISTS `b_user`;
DROP TABLE IF EXISTS `b_store`;
DROP TABLE IF EXISTS `b_weekly_revenue`;

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
  `bank_card` varchar(50) NOT NULL,
  `bank_balance` DECIMAL(7,2) CHECK (bank_balance BETWEEN 1000.00 AND 10000.00),
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `role` tinyint(1) NOT NULL DEFAULT 1,
  `is_frequent_user` tinyint(1) NOT NULL DEFAULT 0,
  `is_student` INT DEFAULT 0,
  `is_senior` INT DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 创建店铺表
CREATE TABLE `b_store` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    `longitude` decimal(9, 6) NOT NULL CHECK (longitude BETWEEN -180.00 AND 180.00),
    `latitude` decimal(9, 6) NOT NULL CHECK (latitude BETWEEN -90.00 AND 90.00),
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 创建滑板车表
CREATE TABLE `b_scooter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price_hour` decimal(5, 2) NOT NULL,
  `price_four_hour` decimal(5, 2) NOT NULL,
  `price_day` decimal(5, 2) NOT NULL,
  `price_week` decimal(5, 2) NOT NULL, 
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `battery` decimal(5, 2) NOT NULL  CHECK (battery BETWEEN 0.00 AND 100.00),
  `speed` decimal(5, 2) NOT NULL,
  `store_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  FOREIGN KEY (`store_id`) REFERENCES `b_store` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 创建订单表
CREATE TABLE `b_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_time` datetime NOT NULL,
  `status` tinyint(1)  NOT NULL DEFAULT 1,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `hire_period` varchar(10)  NOT NULL,
  `price_before_discount` decimal(7, 2) NOT NULL,
  `price` decimal(7, 2) NOT NULL,
  `deposit_paid` boolean NOT NULL DEFAULT false,
  `deposit_amount` decimal(7, 2) NOT NULL DEFAULT 0.00,
  `deposit_refunded` boolean NOT NULL DEFAULT false,
  `return_time` datetime NULL,
  `payment_method` varchar(20) NULL,
  `user_id` bigint NOT NULL,
  `scooter_id` bigint NOT NULL,
  `staff_id` bigint NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `b_order_scooter_id_fk`(`scooter_id`) USING BTREE,
  INDEX `b_order_user_id_fk`(`user_id`) USING BTREE,
  INDEX `b_order_staff_id_fk`(`staff_id`) USING BTREE,
  CONSTRAINT `b_order_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `b_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `b_order_scooter_id_fk` FOREIGN KEY (`scooter_id`) REFERENCES `b_scooter` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `b_order_staff_id_fk` FOREIGN KEY (`staff_id`) REFERENCES `b_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 创建折扣规则表
CREATE TABLE `b_discount_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_type` varchar(10) NOT NULL,
  `discount_rate` decimal(3, 2) NOT NULL DEFAULT 1.00,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 创建反馈表
CREATE TABLE `b_feedback` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `content` text NOT NULL,
    `create_time` datetime NOT NULL,
    `status` varchar(50) DEFAULT 'pending',
    `priority` int DEFAULT 0,
    `admin_response` varchar(500),
    `response_time` datetime,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `b_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- 创建周收入统计表
CREATE TABLE `b_weekly_revenue` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `week_start_date` datetime NOT NULL,
    `week_end_date` datetime NOT NULL,
    `hourly_revenue` decimal(10, 2) NOT NULL DEFAULT 0.00,
    `four_hours_revenue` decimal(10, 2) NOT NULL DEFAULT 0.00,
    `daily_revenue` decimal(10, 2) NOT NULL DEFAULT 0.00,
    `weekly_revenue` decimal(10, 2) NOT NULL DEFAULT 0.00,
    `total_revenue` decimal(12, 2) NOT NULL DEFAULT 0.00,
    `orders_count` int NOT NULL DEFAULT 0,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_week_start_date` (`week_start_date`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- 创建每日收入统计表
CREATE TABLE IF NOT EXISTS `b_daily_revenue` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `revenue_date` date NOT NULL,
    `day_of_week` int NOT NULL,
    `day_of_week_name` varchar(10) NOT NULL,
    `hourly_revenue` decimal(10, 2) NOT NULL DEFAULT 0.00,
    `four_hours_revenue` decimal(10, 2) NOT NULL DEFAULT 0.00,
    `daily_revenue` decimal(10, 2) NOT NULL DEFAULT 0.00,
    `weekly_revenue` decimal(10, 2) NOT NULL DEFAULT 0.00,
    `total_revenue` decimal(12, 2) NOT NULL DEFAULT 0.00,
    `orders_count` int NOT NULL DEFAULT 0,
    `total_discount` decimal(10, 2) NOT NULL DEFAULT 0.00,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_revenue_date` (`revenue_date`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci; 
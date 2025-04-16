use vuesb;
-- 添加测试用户数据
INSERT INTO b_user (username, password, avatar, birthday, user_type, mobile, email, payment_method, status, role, is_frequent_user) 
VALUES 
('admin', '$2a$10$pfvdfutJj.b/NloLAgOos.sOAgkJXH3uKAIasXfgAMNEpfzGG3Uni', 'default_avatar.jpg', '1990-01-01', 0, '1234567890', 'admin@example.com', 'credit_card', 1, 0, 0),
('user', '$2a$10$pfvdfutJj.b/NloLAgOos.sOAgkJXH3uKAIasXfgAMNEpfzGG3Uni', 'default_avatar.jpg', '1995-05-05', 0, '9876543210', 'user@example.com', 'paypal', 1, 1, 0);

-- 添加测试滑板车数据
INSERT INTO b_scooter (price_hour, price_four_hour, price_day, price_week, status, longitude, latitude, battery, speed)
VALUES
    (5.00, 10.00, 20.00, 100.00, 1, 103.984500, 30.765000, 100.00, 100.00),
    (5.00, 10.00, 20.00, 100.00, 1, 103.987000, 30.764000, 50.00, 50.00),
    (5.00, 10.00, 20.00, 100.00, 1, 103.986500, 30.766000, 0.00, 50.00);


-- 添加折扣规则
INSERT INTO b_discount_rule (user_type, discount_rate) 
VALUES 
('student', 0.85),
('elderly', 0.80),
('frequent', 0.90);

-- 添加订单
INSERT INTO b_order (id, order_time, status, start_time, end_time, hire_period, price_before_discount, price, user_id, scooter_id, staff_id)
VALUES
    (1, '2025-04-15 10:51:16', 1, '2025-04-23 22:01:00', '2025-04-23 23:01:00', 'HOUR', 5.00, 5.00, 2, 1, 1),
    (2, '2025-04-15 11:00:02', 1, '2025-04-28 22:01:00', '2025-04-29 22:01:00', 'DAY', 20.00,20.00,2,2,NULL);

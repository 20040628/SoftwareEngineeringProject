use vuesb;
-- 添加测试用户数据
INSERT INTO b_user (username, password, avatar, birthday, user_type, mobile, email, payment_method, status, role, is_frequent_user) 
VALUES 
('admin', '$2a$10$5pscTMMTiM1NoWwzL//69u7NO07t7UsAPGdeMqZvd3TQ8oEARrspG', 'default_avatar.jpg', '1990-01-01', 0, '1234567890', 'admin@example.com', 'credit_card', 1, 0, 0),
('user', '$2a$10$5pscTMMTiM1NoWwzL//69u7NO07t7UsAPGdeMqZvd3TQ8oEARrspG', 'default_avatar.jpg', '1995-05-05', 0, '9876543210', 'user@example.com', 'paypal', 1, 1, 0);

-- 添加测试滑板车数据
INSERT INTO b_scooter (location, price_hour, price_four_hour, price_day, price_week, status, longitude, latitude)
VALUES
    ('Campus Center', 5.00, 10.00, 20.00, 100.00, 1, 103.984500, 30.765000),
    ('Library', 5.00, 10.00, 20.00, 100.00, 1, 103.987000, 30.764000),
    ('Student Union', 5.00, 10.00, 20.00, 100.00, 1, 103.986500, 30.766000);


-- 添加折扣规则
INSERT INTO b_discount_rule (user_type, discount_rate) 
VALUES 
('student', 0.80),
('elderly', 0.70),
('frequent', 0.85);


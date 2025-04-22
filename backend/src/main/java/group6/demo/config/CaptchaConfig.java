package group6.demo.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptchaConfig {
    // 注释：验证码配置现在移至 CaptchaServiceImpl，每次请求动态创建新实例
} 
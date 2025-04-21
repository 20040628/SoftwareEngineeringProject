package group6.demo.service.impl;

import group6.demo.service.CaptchaService;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.FontFormatException;
import java.io.IOException;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private Captcha captcha;

    // 使用内存存储验证码信息（生产环境可考虑使用Redis）
    private final Map<String, CaptchaInfo> captchaStore = new ConcurrentHashMap<>();

    @Override
    public String generateCaptchaKey() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String generateCaptchaBase64(String captchaKey) {
        try {
            // 生成验证码
            String captchaText = captcha.text();
            String captchaBase64 = captcha.toBase64();
            
            // 存储验证码，5分钟过期
            captchaStore.put(captchaKey, new CaptchaInfo(captchaText, System.currentTimeMillis() + 300000));
            
            return captchaBase64;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate captcha", e);
        }
    }

    @Override
    public boolean validateCaptcha(String captchaKey, String userCaptchaInput) {
        if (captchaKey == null || userCaptchaInput == null) {
            return false;
        }
        
        CaptchaInfo captchaInfo = captchaStore.get(captchaKey);
        if (captchaInfo == null) {
            return false;
        }
        
        // 验证码过期检查
        if (System.currentTimeMillis() > captchaInfo.getExpireTime()) {
            captchaStore.remove(captchaKey);
            return false;
        }
        
        // 验证码验证（不区分大小写）
        boolean isValid = captchaInfo.getCaptchaText().equalsIgnoreCase(userCaptchaInput);
        
        // 无论成功与否都移除验证码，确保一次性使用
        captchaStore.remove(captchaKey);
        
        return isValid;
    }
    
    /**
     * 验证码信息内部类
     */
    private static class CaptchaInfo {
        private final String captchaText;
        private final long expireTime;
        
        public CaptchaInfo(String captchaText, long expireTime) {
            this.captchaText = captchaText;
            this.expireTime = expireTime;
        }
        
        public String getCaptchaText() {
            return captchaText;
        }
        
        public long getExpireTime() {
            return expireTime;
        }
    }
} 
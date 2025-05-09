package group6.demo.service.impl;

import group6.demo.entity.User;
import group6.demo.repository.UserRepository;
import group6.demo.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    @Autowired
    private JavaMailSender emailSender;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    // 存储重置码，使用邮箱作为键
    private final Map<String, ResetCodeInfo> resetCodeStore = new ConcurrentHashMap<>();
    
    @Override
    public boolean sendResetCode(String email) {
        // 验证邮箱是否存在
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        
        // 生成6位数字验证码
        String resetCode = generateSixDigitCode();
        
        // 存储验证码，10分钟过期
        resetCodeStore.put(email, new ResetCodeInfo(resetCode, System.currentTimeMillis() + 600000));
        
        // 发送邮件
        sendResetEmail(user, resetCode);
        
        return true;
    }
    
    @Override
    public boolean resetPassword(String email, String resetCode, String newPassword) {
        // 验证码校验
        ResetCodeInfo resetInfo = resetCodeStore.get(email);
        if (resetInfo == null) {
            return false;
        }
        
        // 验证码过期检查
        if (System.currentTimeMillis() > resetInfo.getExpireTime()) {
            resetCodeStore.remove(email);
            return false;
        }
        
        // 验证码匹配检查
        if (!resetInfo.getResetCode().equals(resetCode)) {
            return false;
        }
        
        // 查找用户
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // 移除验证码
        resetCodeStore.remove(email);
        
        return true;
    }
    
    private String generateSixDigitCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成100000-999999之间的随机数
        return String.valueOf(code);
    }
    
    private void sendResetEmail(User user, String resetCode) {
        // 构建并发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset Verification Code");
        message.setText("Dear " + user.getUsername() + ",\n\n" +
                "You have requested to reset your password. Here is your verification code:\n\n" +
                resetCode + "\n\n" +
                "This code will expire in 30 minutes.\n\n" +
                "If you did not request a password reset, please ignore this email or contact customer service.\n\n" +
                "Regards,\n" +
                "E-Scooter Rental Platform Team");

        emailSender.send(message);
    }
    
    // 重置码信息内部类
    private static class ResetCodeInfo {
        private final String resetCode;
        private final long expireTime;
        
        public ResetCodeInfo(String resetCode, long expireTime) {
            this.resetCode = resetCode;
            this.expireTime = expireTime;
        }
        
        public String getResetCode() {
            return resetCode;
        }
        
        public long getExpireTime() {
            return expireTime;
        }
    }
} 
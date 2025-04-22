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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("E-Scooter Rental System <bc_somebody@qq.com>");
        message.setTo(user.getEmail());
        message.setSubject("密码重置验证码");
        
        String emailContent = String.format(
            "尊敬的 %s,\n\n" +
            "您正在请求重置密码。请使用以下验证码完成密码重置：\n\n" +
            "验证码: %s\n\n" +
            "该验证码将在10分钟内有效。如果您没有请求重置密码，请忽略此邮件。\n\n" +
            "谢谢！\n" +
            "E-Scooter Rental Team",
            user.getUsername(),
            resetCode
        );
        
        message.setText(emailContent);
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
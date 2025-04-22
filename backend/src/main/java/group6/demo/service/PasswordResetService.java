package group6.demo.service;

public interface PasswordResetService {
    /**
     * 发送密码重置验证码
     * @param email 用户邮箱
     * @return 操作结果
     */
    boolean sendResetCode(String email);
    
    /**
     * 验证重置码并重置密码
     * @param email 用户邮箱
     * @param resetCode 重置验证码
     * @param newPassword 新密码
     * @return 操作结果
     */
    boolean resetPassword(String email, String resetCode, String newPassword);
} 
package group6.demo.service;

public interface CaptchaService {
    /**
     * 生成验证码唯一标识
     * @return 验证码key
     */
    String generateCaptchaKey();
    
    /**
     * 生成验证码并获取Base64编码
     * @param captchaKey 验证码唯一标识
     * @return Base64编码的验证码图片
     */
    String generateCaptchaBase64(String captchaKey);
    
    /**
     * 验证用户输入的验证码
     * @param captchaKey 验证码唯一标识
     * @param userCaptchaInput 用户输入的验证码
     * @return 验证结果
     */
    boolean validateCaptcha(String captchaKey, String userCaptchaInput);
} 
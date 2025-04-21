package group6.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

import java.awt.FontFormatException;
import java.io.IOException;

@Configuration
public class CaptchaConfig {
    
    /**
     * 验证码默认配置
     */
    @Bean
    public Captcha captcha() throws FontFormatException, IOException {
        // 三个参数分别为宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(130, 48, 4);
        // 设置字体
        captcha.setFont(Captcha.FONT_1);
        // 设置类型，纯数字或字母数字混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        return captcha;
    }
} 
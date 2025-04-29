package group6.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class TestingModeConfig {
    
    @Value("${app.testing.enabled:false}")
    private boolean testingEnabled;
    
    /**
     * 判断测试模式是否启用
     * @return 测试模式是否启用
     */
    public boolean isTestingEnabled() {
        return testingEnabled;
    }
} 
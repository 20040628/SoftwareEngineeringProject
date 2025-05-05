package group6.demo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

class CaptchaServiceImplTest {

    @InjectMocks
    private CaptchaServiceImpl captchaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCaptchaKey() {
        String captchaKey = captchaService.generateCaptchaKey();
        assertNotNull(captchaKey);
        assertFalse(captchaKey.isEmpty());
    }

    @Test
    void testValidateCaptcha_Invalid() {
        String captchaKey = captchaService.generateCaptchaKey();
        String captchaBase64 = captchaService.generateCaptchaBase64(captchaKey);

        boolean isValid = captchaService.validateCaptcha(captchaKey, "wrong");
        assertFalse(isValid);
    }
}

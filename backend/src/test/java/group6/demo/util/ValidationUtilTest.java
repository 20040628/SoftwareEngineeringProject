package group6.demo.util;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {
    @Test
    void testUsernameValidation() {
        assertTrue(ValidationUtil.isValidUsername("user_123"));
        assertFalse(ValidationUtil.isValidUsername("ab")); // 太短
        assertFalse(ValidationUtil.isValidUsername("user@name")); // 非法字符
    }

    @Test
    void testPasswordValidation() {
        assertTrue(ValidationUtil.isValidPassword("Passw0rd"));
        assertFalse(ValidationUtil.isValidPassword("password")); // 无大写和数字
        assertFalse(ValidationUtil.isValidPassword("PASSW0RD")); // 无小写
    }

    @Test
    void testEmailValidation() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertFalse(ValidationUtil.isValidEmail("invalid.email")); // 无@
    }

    @Test
    void testMobileValidation() {
        assertTrue(ValidationUtil.isValidMobile("13800138000"));
        assertFalse(ValidationUtil.isValidMobile("12345")); // 过短
    }

    @Test
    void testPriceValidation() {
        assertTrue(ValidationUtil.isValidPrice(new BigDecimal("99.99")));
        assertFalse(ValidationUtil.isValidPrice(new BigDecimal("1000.123"))); // 小数位超限
    }

    @Test
    void testLocationValidation() {
        assertTrue(ValidationUtil.isValidLocation(new BigDecimal("116.404")));
        assertFalse(ValidationUtil.isValidLocation(new BigDecimal("200.1234567"))); // 超出范围
    }

    @Test
    void testBirthdayValidation() {
        assertTrue(ValidationUtil.isValidBirthday(new Date(System.currentTimeMillis() - 86400000))); // 昨天
        assertFalse(ValidationUtil.isValidBirthday(new Date(System.currentTimeMillis() + 86400000))); // 明天
    }
}
package group6.demo.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;
    private final String testUsername = "testUser";
    private final Long testUserId = 1L;
    private final Integer testUserType = 1;
    private final Integer testRole = 0;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(testUsername, testUserId, testUserType, testRole);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken(testUsername, testUserId, testUserType, testRole);
        assertEquals(testUsername, jwtUtil.extractUsername(token));
    }

    @Test
    void testExtractUserId() {
        String token = jwtUtil.generateToken(testUsername, testUserId, testUserType, testRole);
        assertEquals(testUserId, jwtUtil.extractUserId(token));
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken(testUsername, testUserId, testUserType, testRole);
        assertTrue(jwtUtil.validateToken(token, testUsername));
    }

    @Test
    void testTokenExpiration() throws InterruptedException {
        String token = jwtUtil.generateToken(testUsername, testUserId, testUserType, testRole);
        assertFalse(jwtUtil.isTokenExpired(token));

        // 模拟过期（实际需要调整JwtUtil的jwtExpiration为极短时间测试）
        Thread.sleep(1000); // 仅示例，实际需结合Mock
        // 此处需配合JwtUtil的可配置过期时间优化
    }
}
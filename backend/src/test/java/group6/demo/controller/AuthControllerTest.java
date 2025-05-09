package group6.demo.controller;

import group6.demo.dto.CaptchaResponseDTO;
import group6.demo.dto.ForgotPasswordDTO;
import group6.demo.dto.ResetPasswordDTO;
import group6.demo.dto.UserLoginDTO;
import group6.demo.dto.UserRegistrationDTO;
import group6.demo.entity.User;
import group6.demo.service.CaptchaService;
import group6.demo.service.PasswordResetService;
import group6.demo.util.JwtUtil;
import group6.demo.service.PriceDiscountService;
import group6.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PriceDiscountService priceDiscountService;

    @Mock
    private CaptchaService captchaService;

    @Mock
    private PasswordResetService passwordResetService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCaptcha() {
        String captchaKey = "captchaKey";
        String captchaBase64 = "captchaBase64";

        when(captchaService.generateCaptchaKey()).thenReturn(captchaKey);
        when(captchaService.generateCaptchaBase64(captchaKey)).thenReturn(captchaBase64);

        ResponseEntity<CaptchaResponseDTO> response = authController.getCaptcha();

        assertEquals(200, response.getStatusCodeValue());
        CaptchaResponseDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(captchaKey, responseBody.getCaptchaKey());
        assertEquals(captchaBase64, responseBody.getCaptchaImageBase64());
        verify(captchaService, times(1)).generateCaptchaKey();
        verify(captchaService, times(1)).generateCaptchaBase64(captchaKey);
    }

    @Test
    void testLoginUser_Valid() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("Password123");
        loginDTO.setCaptchaKey("captchaKey");
        loginDTO.setCaptcha("captcha");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setUserType(0);
        user.setEmail("test@example.com");
        user.setRole(1);
        user.setStatus(1);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(captchaService.validateCaptcha("captchaKey", "captcha")).thenReturn(true);
        when(userService.loginUser(any(UserLoginDTO.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString(), anyLong(), anyInt(), anyInt())).thenReturn("token");

        ResponseEntity<?> response = authController.loginUser(loginDTO, bindingResult);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Login successful", responseBody.get("message"));
        assertEquals(1L, responseBody.get("userId"));
        assertEquals("testuser", responseBody.get("username"));
        assertEquals(0, responseBody.get("userType"));
        assertEquals("test@example.com", responseBody.get("email"));
        assertEquals(1, responseBody.get("role"));
        assertEquals("token", responseBody.get("token"));
        verify(captchaService, times(1)).validateCaptcha("captchaKey", "captcha");
        verify(userService, times(1)).loginUser(any(UserLoginDTO.class));
        verify(jwtUtil, times(1)).generateToken(anyString(), anyLong(), anyInt(), anyInt());
    }

    @Test
    void testLoginUser_InvalidCaptcha() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("Password123");
        loginDTO.setCaptchaKey("captchaKey");
        loginDTO.setCaptcha("wrongCaptcha");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(captchaService.validateCaptcha("captchaKey", "wrongCaptcha")).thenReturn(false);

        ResponseEntity<?> response = authController.loginUser(loginDTO, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Verification code is incorrect or expired", responseBody.get("captcha"));
        verify(captchaService, times(1)).validateCaptcha("captchaKey", "wrongCaptcha");
        verify(userService, times(0)).loginUser(any(UserLoginDTO.class));
    }
}

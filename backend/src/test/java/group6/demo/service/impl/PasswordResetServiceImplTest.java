package group6.demo.service.impl;

import group6.demo.entity.User;
import group6.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordResetServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendResetCode_UserExists() {
        // Setup
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setUsername("Test User");

        when(userRepository.findByEmail(email)).thenReturn(user);
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        // Execute
        boolean result = passwordResetService.sendResetCode(email);

        // Verify
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendResetCode_UserNotExists() {
        // Setup
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Execute
        boolean result = passwordResetService.sendResetCode(email);

        // Verify
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }

}
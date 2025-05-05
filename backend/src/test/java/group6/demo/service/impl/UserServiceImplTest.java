package group6.demo.service.impl;

import group6.demo.dto.UserLoginDTO;
import group6.demo.dto.UserRegistrationDTO;
import group6.demo.dto.UpdateBankCardDTO;
import group6.demo.dto.ChangePasswordDTO;
import group6.demo.dto.UserProfileDTO;
import group6.demo.entity.User;
import group6.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsUsernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(new User());

        boolean result = userService.isUsernameExists("testuser");

        assertTrue(result);
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testIsEmailExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(new User());

        boolean result = userService.isEmailExists("test@example.com");

        assertTrue(result);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testLoginUser_ValidCredentials() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("Password123");

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.matches("Password123", "encodedPassword")).thenReturn(true);

        User result = userService.loginUser(loginDTO);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoginUser_InvalidPassword() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("WrongPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.matches("WrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.loginUser(loginDTO));
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testChangeUserStatus() {
        User user = new User();
        user.setId(1L);
        user.setStatus(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<User> result = userService.changeUserStatus(1L);

        assertTrue(result.isPresent());
        assertEquals(0, result.get().getStatus());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testChangePassword_Valid() {
        User user = new User();
        user.setId(1L);
        user.setPassword("encodedOldPassword");

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("OldPassword123");
        changePasswordDTO.setNewPassword("NewPassword123");
        changePasswordDTO.setConfirmPassword("NewPassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("OldPassword123", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("NewPassword123")).thenReturn("encodedNewPassword");

        boolean result = userService.changePassword(1L, changePasswordDTO);

        assertTrue(result);
        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangePassword_InvalidOldPassword() {
        User user = new User();
        user.setId(1L);
        user.setPassword("encodedOldPassword");

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("WrongOldPassword");
        changePasswordDTO.setNewPassword("NewPassword123");
        changePasswordDTO.setConfirmPassword("NewPassword123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("WrongOldPassword", "encodedOldPassword")).thenReturn(false);

        boolean result = userService.changePassword(1L, changePasswordDTO);

        assertFalse(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(user);
    }
}
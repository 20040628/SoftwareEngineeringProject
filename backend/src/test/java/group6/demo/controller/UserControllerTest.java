package group6.demo.controller;

import group6.demo.dto.*;
import group6.demo.entity.Order;
import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.UserRepository;
import group6.demo.service.FileStorageService;
import group6.demo.service.PriceDiscountService;
import group6.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PriceDiscountService priceDiscountService;

    @Mock
    private UserService userService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private Order testOrder;
    private final Long userId = 1L;
    private final String testDate = "1990-01-01";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setAvatar("default_avatar.jpg");
        testUser.setBankCard("1234567890123456");
        testUser.setBankBalance(new BigDecimal("1000.00"));

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUser(testUser);
    }

    @Test
    void getAllUsers_Success() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        // Act
        List<UserProfileDTO> users = userController.getAllUsers();

        // Assert
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User createdUser = userController.createUser(testUser);

        // Assert
        assertEquals(testUser, createdUser);
    }

    @Test
    void getUserById_Success() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Act
        User foundUser = userController.getUserById(userId);

        // Assert
        assertEquals(testUser, foundUser);
    }

    @Test
    void getUserById_NotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User foundUser = userController.getUserById(userId);

        // Assert
        assertNull(foundUser);
    }

    @Test
    void getAllOrders_Success() {
        // Arrange
        when(orderRepository.findByUser_Id(userId)).thenReturn(Arrays.asList(testOrder));

        // Act
        List<Order> orders = userController.getAllOrders(userId);

        // Assert
        assertEquals(1, orders.size());
        assertEquals(testOrder, orders.get(0));
    }

    @Test
    void updateBirthDate_Success() throws Exception {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        doNothing().when(priceDiscountService).updateUserDiscountStatusByBirthday(userId);

        // Act
        ResponseEntity<?> response = userController.updateBirthDate(userId, testDate);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("用户生日信息更新成功", responseBody.get("message"));
        verify(priceDiscountService).updateUserDiscountStatusByBirthday(userId);
    }

    @Test
    void updateBirthDate_UserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = userController.updateBirthDate(userId, testDate);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("用户不存在", response.getBody());
    }

    @Test
    void updateUser_Success() throws Exception {
        // Arrange
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "new@example.com");
        updates.put("birthDate", testDate);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        doNothing().when(priceDiscountService).updateUserDiscountStatusByBirthday(userId);

        // Act
        ResponseEntity<?> response = userController.updateUser(userId, updates);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testUser, response.getBody());
        verify(priceDiscountService).updateUserDiscountStatusByBirthday(userId);
    }

    @Test
    void updateUser_UserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = userController.updateUser(userId, new HashMap<>());

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("用户不存在", response.getBody());
    }

    @Test
    void changeUserStatus_Success() {
        // Arrange
        when(userService.changeUserStatus(userId)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userController.changeUserStatus(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
    }

    @Test
    void getUserProfile_Success() {
        // Arrange
        UserProfileDTO profileDTO = new UserProfileDTO();
        when(userService.getUserProfile(userId)).thenReturn(profileDTO);

        // Act
        ResponseEntity<?> response = userController.getUserProfile(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(profileDTO, response.getBody());
    }

    @Test
    void getUserProfile_UserNotFound() {
        // Arrange
        when(userService.getUserProfile(userId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.getUserProfile(userId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("The user doesn't exist", response.getBody());
    }

    @Test
    void uploadAvatar_Success() throws Exception {
        // Arrange
        String filename = "avatar123.jpg";
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(fileStorageService.storeAvatar(any(), eq(userId))).thenReturn(filename);
        when(userService.updateAvatar(eq(userId), eq(filename))).thenReturn(testUser);

        // Act
        ResponseEntity<?> response = userController.uploadAvatar(userId, multipartFile);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("User avatar upload was successful", responseBody.get("message"));
        assertEquals("/uploads/avatars/" + filename, responseBody.get("avatarUrl"));
    }

    @Test
    void uploadAvatar_UserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = userController.uploadAvatar(userId, multipartFile);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("The user does not exist", response.getBody());
    }

    @Test
    void changePassword_Success() {
        // Arrange
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setNewPassword("newPass");
        dto.setConfirmPassword("newPass");
        dto.setOldPassword("oldPass");

        when(userService.changePassword(eq(userId), any(ChangePasswordDTO.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = userController.changePassword(userId, dto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Successfully modify password", responseBody.get("message"));
    }

    @Test
    void changePassword_PasswordMismatch() {
        // Arrange
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setNewPassword("newPass");
        dto.setConfirmPassword("differentPass");
        dto.setOldPassword("oldPass");

        // Act
        ResponseEntity<?> response = userController.changePassword(userId, dto);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("The new password and the confirmed password do not match", response.getBody());
    }
}
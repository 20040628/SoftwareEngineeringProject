package group6.demo.controller;

import group6.demo.dto.BankCardPaymentRequest;
import group6.demo.dto.BankCardPaymentResponse;
import group6.demo.entity.Order;
import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankCardPaymentControllerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BankCardPaymentController bankCardPaymentController;

    private Order testOrder;
    private User testUser;
    private final Long orderId = 1L;
    private final Long userId = 1L;
    private BankCardPaymentRequest validRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setBankCard("1234567890123456");
        testUser.setBankBalance(new BigDecimal("200.00"));

        testOrder = new Order();
        testOrder.setId(orderId);
        testOrder.setUser(testUser);
        testOrder.setPrice(new BigDecimal("100.00"));
        testOrder.setStatus(1); // Active status

        validRequest = new BankCardPaymentRequest();
        validRequest.setBankCard("1234567890123456");
    }


    @Test
    void processBankCardPayment_OrderNotFound() {
        // Arrange
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId, validRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Order not found", response.getBody());
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(userRepository, orderRepository);
    }

    @Test
    void processBankCardPayment_InvalidOrderStatus() {
        // Arrange
        testOrder.setStatus(2); // Completed status
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId, validRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid order status, cannot process payment", response.getBody());
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(userRepository, orderRepository);
    }

    @Test
    void processBankCardPayment_NoBankCard() {
        // Arrange
        testUser.setBankCard(null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId, validRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bank card information not found, please bind your bank card first", response.getBody());
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(userRepository, orderRepository);
    }

    @Test
    void processBankCardPayment_InsufficientBalance() {
        // Arrange
        testUser.setBankBalance(new BigDecimal("100.00")); // Not enough for order + deposit
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId, validRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Insufficient bank card balance, payment failed", response.getBody());
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(userRepository, orderRepository);
    }

    @Test
    void processBankCardPayment_InvalidBankCard() {
        // Arrange
        validRequest.setBankCard("123"); // Invalid length
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId, validRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid bank card", response.getBody());
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(userRepository, orderRepository);
    }

    @Test
    void processBankCardPayment_Success() {
        // Arrange
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId, validRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof BankCardPaymentResponse);
        BankCardPaymentResponse responseBody = (BankCardPaymentResponse) response.getBody();
        assertTrue(responseBody.isSuccess());
        assertEquals(orderId, responseBody.getOrderId());
        assertEquals("3456", responseBody.getBankCardLast4());
        assertEquals("BANK_CARD", testOrder.getPaymentMethod());
        assertEquals(2, testOrder.getStatus()); // 检查状态是否更新为已支付未开始(2)
        assertTrue(testOrder.getDepositPaid());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(testOrder);
        verify(userRepository).save(testUser);
    }

    @Test
    void checkBankCard_SuccessWithCard() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Act
        ResponseEntity<?> response = bankCardPaymentController.checkBankCard(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseMap = (Map<String, Object>) response.getBody();
        assertTrue((Boolean) responseMap.get("hasBankCard"));
        assertEquals("**** **** **** 3456", responseMap.get("maskedCard"));
        assertEquals(new BigDecimal("200.00"), responseMap.get("bankBalance"));

        verify(userRepository).findById(userId);
    }

    @Test
    void checkBankCard_SuccessWithoutCard() {
        // Arrange
        testUser.setBankCard(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Act
        ResponseEntity<?> response = bankCardPaymentController.checkBankCard(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseMap = (Map<String, Object>) response.getBody();
        assertFalse((Boolean) responseMap.get("hasBankCard"));
        assertNull(responseMap.get("maskedCard"));
        assertEquals(new BigDecimal("200.00"), responseMap.get("bankBalance"));

        verify(userRepository).findById(userId);
    }

    @Test
    void checkBankCard_UserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = bankCardPaymentController.checkBankCard(userId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
        verify(userRepository).findById(userId);
    }
}
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
        testOrder.setStatus(1); // CREATED status
    }

    @Test
    void processBankCardPayment_OrderNotFound() {
        // Arrange
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Order not found", response.getBody());
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(userRepository, orderRepository);
    }

    @Test
    void processBankCardPayment_InvalidOrderStatus() {
        // Arrange
        testOrder.setStatus(2); // PAID status
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId);

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
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Bank card information not found, please bind your bank card first", response.getBody());
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(userRepository, orderRepository);
    }

    @Test
    void processBankCardPayment_InsufficientBalance() {
        // Arrange
        testUser.setBankBalance(new BigDecimal("100.00")); // Not enough for order (100) + deposit (50)
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Insufficient bank card balance, payment failed", response.getBody());
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
        ResponseEntity<?> response = bankCardPaymentController.processBankCardPayment(orderId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof BankCardPaymentResponse);
        BankCardPaymentResponse responseBody = (BankCardPaymentResponse) response.getBody();
        assertTrue(responseBody.isSuccess());
        assertEquals(orderId, responseBody.getOrderId());
        assertEquals("3456", responseBody.getBankCardLast4());
        assertEquals(new BigDecimal("150.00"), responseBody.getAmount()); // 100 + 50 deposit
        assertEquals("BANK_CARD", testOrder.getPaymentMethod());
        assertEquals(2, testOrder.getStatus()); // Check status updated to PAID
        assertTrue(testOrder.getDepositPaid());
        assertEquals(new BigDecimal("50.00"), testOrder.getDepositAmount());
        assertFalse(testOrder.getDepositRefunded());
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

    @Test
    void processNewBankCardPayment_Success() {
        // Arrange
        BankCardPaymentRequest request = new BankCardPaymentRequest();
        request.setBankCard("1234567890123456");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processNewBankCardPayment(orderId, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseMap = (Map<String, Object>) response.getBody();
        assertEquals("Bank card payment successful", responseMap.get("message"));
        assertEquals("BANK_CARD", testOrder.getPaymentMethod());
        assertEquals(2, testOrder.getStatus());
        assertFalse(testOrder.getDepositPaid());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(testOrder);
    }

    @Test
    void processNewBankCardPayment_InvalidOrderStatus() {
        // Arrange
        BankCardPaymentRequest request = new BankCardPaymentRequest();
        request.setBankCard("1234567890123456");

        testOrder.setStatus(2); // Already paid
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        // Act
        ResponseEntity<?> response = bankCardPaymentController.processNewBankCardPayment(orderId, request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid order status, cannot process payment", response.getBody());
        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }
}
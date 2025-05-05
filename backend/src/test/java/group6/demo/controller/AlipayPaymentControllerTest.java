package group6.demo.controller;

import group6.demo.entity.Order;
import group6.demo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AlipayPaymentControllerTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private AlipayPaymentController alipayPaymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processAlipayPayment_ShouldSuccess_WhenValidOrder() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(1);
        order.setPrice(BigDecimal.valueOf(100));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<?> response = alipayPaymentController.processAlipayPayment(orderId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(true, responseBody.get("success"));
        assertEquals("ALIPAY", order.getPaymentMethod());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void processAlipayPayment_ShouldFail_WhenOrderNotFound() {
        // Arrange
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = alipayPaymentController.processAlipayPayment(orderId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("订单不存在", response.getBody());
    }

    @Test
    void processAlipayPayment_ShouldFail_WhenInvalidOrderStatus() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(2); // Not active

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<?> response = alipayPaymentController.processAlipayPayment(orderId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("订单状态不正确，无法支付", response.getBody());
    }

    @Test
    void processAlipayPayment_ShouldHandleException() {
        // Arrange
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = alipayPaymentController.processAlipayPayment(orderId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("支付失败"));
    }
}
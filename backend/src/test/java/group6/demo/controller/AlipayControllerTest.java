package group6.demo.controller;

import com.alipay.api.AlipayClient;
import group6.demo.config.AlipayConfig;
import group6.demo.entity.Order;
import group6.demo.repository.OrderRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AlipayControllerTest {

    @InjectMocks
    private AlipayController alipayController;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AlipayClient alipayClient;

    @Mock
    private AlipayConfig alipayConfig;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(alipayConfig.getAppId()).thenReturn("testAppId");
        when(alipayConfig.getAppPrivateKey()).thenReturn("testPrivateKey");
        when(alipayConfig.getAlipayPublicKey()).thenReturn("testPublicKey");

        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void pay_orderNotFound_shouldDoNothing() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> alipayController.pay(999L, response));
    }

    @Test
    void payApp_orderNotFound_shouldDoNothing() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> alipayController.payApp(999L, response));
    }
}

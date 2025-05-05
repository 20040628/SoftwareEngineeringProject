package group6.demo.service.impl;

import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriceDiscountServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PriceDiscountServiceImpl priceDiscountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateDiscountedPrice_NoDiscount() {
        User user = new User();
        user.setId(1L);
        user.setIsFrequentUser(0);
        user.setIsStudent(0);
        user.setIsSenior(0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 1L);

        assertEquals(originalPrice, discountedPrice);
    }

    @Test
    void testCalculateDiscountedPrice_FrequentUserDiscount() {
        User user = new User();
        user.setId(1L);
        user.setIsFrequentUser(1);
        user.setIsStudent(0);
        user.setIsSenior(0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal expectedPrice = originalPrice.multiply(new BigDecimal("0.90")).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 1L);

        assertEquals(expectedPrice, discountedPrice);
    }

    @Test
    void testCalculateDiscountedPrice_StudentDiscount() {
        User user = new User();
        user.setId(1L);
        user.setIsFrequentUser(0);
        user.setIsStudent(1);
        user.setIsSenior(0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal expectedPrice = originalPrice.multiply(new BigDecimal("0.85")).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 1L);

        assertEquals(expectedPrice, discountedPrice);
    }

    @Test
    void testCalculateDiscountedPrice_SeniorDiscount() {
        User user = new User();
        user.setId(1L);
        user.setIsFrequentUser(0);
        user.setIsStudent(0);
        user.setIsSenior(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal expectedPrice = originalPrice.multiply(new BigDecimal("0.80")).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 1L);

        assertEquals(expectedPrice, discountedPrice);
    }

    @Test
    void testCalculateDiscountedPrice_MultipleDiscounts() {
        User user = new User();
        user.setId(1L);
        user.setIsFrequentUser(1);
        user.setIsStudent(1);
        user.setIsSenior(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal expectedPrice = originalPrice.multiply(new BigDecimal("0.90"))
                .multiply(new BigDecimal("0.85"))
                .multiply(new BigDecimal("0.80"))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 1L);

        assertEquals(expectedPrice, discountedPrice);
    }

    @Test
    void testIsFrequentUser() {
        when(orderRepository.getTotalHoursInLastWeek(anyLong(), any(Date.class), any(Date.class))).thenReturn(10L);

        boolean result = priceDiscountService.isFrequentUser(1L);

        assertTrue(result);
    }

    @Test
    void testIsNotFrequentUser() {
        when(orderRepository.getTotalHoursInLastWeek(anyLong(), any(Date.class), any(Date.class))).thenReturn(5L);

        boolean result = priceDiscountService.isFrequentUser(1L);

        assertFalse(result);
    }

    @Test
    void testUpdateFrequentUserStatus() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.getTotalHoursInLastWeek(anyLong(), any(Date.class), any(Date.class))).thenReturn(10L);

        priceDiscountService.updateFrequentUserStatus(1L);

        assertEquals(1, user.getIsFrequentUser());
    }

    @Test
    void testUpdateAllFrequentUserStatus() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(orderRepository.getTotalHoursInLastWeek(anyLong(), any(Date.class), any(Date.class))).thenReturn(10L);

        priceDiscountService.updateAllFrequentUserStatus();

        assertEquals(1, user1.getIsFrequentUser());
        assertEquals(1, user2.getIsFrequentUser());
    }

    @Test
    void testUpdateUserDiscountStatusByBirthday() {
        User user = new User();
        user.setId(1L);
        user.setBirthday(new Date(2005 - 1900, Calendar.JANUARY, 1)); // 20 years old

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        priceDiscountService.updateUserDiscountStatusByBirthday(1L);

        assertEquals(1, user.getIsStudent());
        assertEquals(0, user.getIsSenior());
    }
}

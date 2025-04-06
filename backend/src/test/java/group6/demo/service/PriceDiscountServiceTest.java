package group6.demo.service;

import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.UserRepository;
import group6.demo.service.impl.PriceDiscountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class PriceDiscountServiceTest {

    @InjectMocks
    private PriceDiscountServiceImpl priceDiscountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    private User frequentUser;
    private User studentUser;
    private User seniorUser;
    private User frequentStudentUser;
    private User frequentSeniorUser;
    private User allDiscountsUser;
    private User regularUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        // 设置测试用户
        frequentUser = new User();
        frequentUser.setId(1L);
        frequentUser.setIsFrequentUser(1);
        frequentUser.setIsStudent(0);
        frequentUser.setIsSenior(0);
        
        studentUser = new User();
        studentUser.setId(2L);
        studentUser.setIsFrequentUser(0);
        studentUser.setIsStudent(1);
        studentUser.setIsSenior(0);
        
        seniorUser = new User();
        seniorUser.setId(3L);
        seniorUser.setIsFrequentUser(0);
        seniorUser.setIsStudent(0);
        seniorUser.setIsSenior(1);
        
        frequentStudentUser = new User();
        frequentStudentUser.setId(4L);
        frequentStudentUser.setIsFrequentUser(1);
        frequentStudentUser.setIsStudent(1);
        frequentStudentUser.setIsSenior(0);
        
        frequentSeniorUser = new User();
        frequentSeniorUser.setId(5L);
        frequentSeniorUser.setIsFrequentUser(1);
        frequentSeniorUser.setIsStudent(0);
        frequentSeniorUser.setIsSenior(1);
        
        allDiscountsUser = new User();
        allDiscountsUser.setId(6L);
        allDiscountsUser.setIsFrequentUser(1);
        allDiscountsUser.setIsStudent(1);
        allDiscountsUser.setIsSenior(1);
        
        regularUser = new User();
        regularUser.setId(7L);
        regularUser.setIsFrequentUser(0);
        regularUser.setIsStudent(0);
        regularUser.setIsSenior(0);
        
        // 模拟查询用户
        when(userRepository.findById(1L)).thenReturn(Optional.of(frequentUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(studentUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(seniorUser));
        when(userRepository.findById(4L)).thenReturn(Optional.of(frequentStudentUser));
        when(userRepository.findById(5L)).thenReturn(Optional.of(frequentSeniorUser));
        when(userRepository.findById(6L)).thenReturn(Optional.of(allDiscountsUser));
        when(userRepository.findById(7L)).thenReturn(Optional.of(regularUser));
        
        // 模拟常客判断
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = calendar.getTime();
        
        when(orderRepository.getTotalHoursInLastWeek(1L, startDate, endDate)).thenReturn(10L);
        when(orderRepository.getTotalHoursInLastWeek(7L, startDate, endDate)).thenReturn(5L);
    }
    
    @Test
    public void testRegularUserNoDiscount() {
        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 7L);
        
        // 普通用户无折扣，价格应该不变
        assertEquals(0, originalPrice.compareTo(discountedPrice));
    }
    
    @Test
    public void testFrequentUserDiscount() {
        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal expectedPrice = new BigDecimal("90.00");
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 1L);
        
        // 常客享受10%折扣
        assertEquals(0, expectedPrice.compareTo(discountedPrice));
    }
    
    @Test
    public void testStudentDiscount() {
        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal expectedPrice = new BigDecimal("85.00");
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 2L);
        
        // 学生享受15%折扣
        assertEquals(0, expectedPrice.compareTo(discountedPrice));
    }
    
    @Test
    public void testSeniorDiscount() {
        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal expectedPrice = new BigDecimal("80.00");
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 3L);
        
        // 老年人享受20%折扣
        assertEquals(0, expectedPrice.compareTo(discountedPrice));
    }
    
    @Test
    public void testFrequentStudentDiscount() {
        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal expectedPrice = new BigDecimal("76.50"); // 0.9 * 0.85 = 0.765
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 4L);
        
        // 常客+学生折扣叠加
        assertEquals(0, expectedPrice.compareTo(discountedPrice));
    }
    
    @Test
    public void testFrequentSeniorDiscount() {
        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal expectedPrice = new BigDecimal("72.00"); // 0.9 * 0.8 = 0.72
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 5L);
        
        // 常客+老年人折扣叠加
        assertEquals(0, expectedPrice.compareTo(discountedPrice));
    }
    
    @Test
    public void testAllDiscounts() {
        BigDecimal originalPrice = new BigDecimal("100.00");
        BigDecimal expectedPrice = new BigDecimal("61.20"); // 0.9 * 0.85 * 0.8 = 0.612
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(originalPrice, 6L);
        
        // 所有折扣叠加
        assertEquals(0, expectedPrice.compareTo(discountedPrice));
    }
    
    @Test
    public void testIsFrequentUser() {
        // 设置日期范围和时间计算标准，确保测试用例一致
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = calendar.getTime();
        
        // 模拟时间查询结果
        when(orderRepository.getTotalHoursInLastWeek(eq(1L), any(Date.class), any(Date.class))).thenReturn(10L);
        when(orderRepository.getTotalHoursInLastWeek(eq(7L), any(Date.class), any(Date.class))).thenReturn(5L);
        
        // 一周使用超过8小时的用户应该被判定为常客
        boolean isFrequent = priceDiscountService.isFrequentUser(1L);
        assertEquals(true, isFrequent);
        
        // 一周使用少于8小时的用户不应该被判定为常客
        boolean isNotFrequent = priceDiscountService.isFrequentUser(7L);
        assertEquals(false, isNotFrequent);
    }
} 
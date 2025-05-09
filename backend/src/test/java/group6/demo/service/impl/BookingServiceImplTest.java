package group6.demo.service.impl;

import group6.demo.dto.*;
import group6.demo.entity.*;
import group6.demo.repository.*;
import group6.demo.service.PriceDiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ScooterRepository scooterRepository;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private PriceDiscountService priceDiscountService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User testUser;
    private Scooter testScooter;
    private Order testOrder;
    private Date futureDate;
    private final Long orderId = 1L;
    private final Long userId = 1L;
    private final Long scooterId = 1L;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        testUser = new User();
        testUser.setId(userId);
        testUser.setEmail("test@example.com");
        testUser.setUsername("Test User");
        testUser.setBankBalance(new BigDecimal("1000.00"));

        testScooter = new Scooter();
        testScooter.setId(scooterId);
        testScooter.setPriceHour(new BigDecimal("5.00"));
        testScooter.setPriceFourHour(new BigDecimal("15.00"));
        testScooter.setPriceDay(new BigDecimal("30.00"));
        testScooter.setPriceWeek(new BigDecimal("150.00"));
        testScooter.setBattery(new BigDecimal("95"));

        testOrder = new Order();
        testOrder.setId(orderId);
        testOrder.setUser(testUser);
        testOrder.setScooter(testScooter);
        testOrder.setStatus(1); // active
        testOrder.setPrice(new BigDecimal("4.50"));
        testOrder.setPaymentMethod("BANK_CARD");
        testOrder.setDepositPaid(true);
        testOrder.setDepositAmount(new BigDecimal("50.00"));

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        futureDate = calendar.getTime();
        testOrder.setStartTime(futureDate);
        calendar.add(Calendar.HOUR, 1);
        testOrder.setEndTime(calendar.getTime());
    }

    @Test
    void testCreateBooking_InvalidUser() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(bookingDTO));
    }

    @Test
    void testCreateBooking_InvalidScooter() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserId(1L);
        bookingDTO.setScooterId(99L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(scooterRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(bookingDTO));
    }

    @Test
    void testCreateBooking_PastStartTime() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserId(1L);
        bookingDTO.setScooterId(1L);
        bookingDTO.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis() - 100000)));
        bookingDTO.setHireType("HOUR");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(scooterRepository.findById(1L)).thenReturn(Optional.of(testScooter));

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(bookingDTO));
    }

    @Test
    void testReturnScooter_AlreadyCompleted() {
        testOrder.setStatus(3); // 更新为新的已完成状态
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        ReturnScooterDTO returnScooterDTO = new ReturnScooterDTO();
        returnScooterDTO.setOrderId(1L);

        assertThrows(IllegalArgumentException.class, () -> bookingService.returnScooter(returnScooterDTO));
    }

    @Test
    void testGetBookingTimeline_NoBookings() {
        when(orderRepository.findActiveOrdersByScooterId(anyLong(), any())).thenReturn(Collections.emptyList());

        List<Map<String, Object>> timeline = bookingService.getBookingTimeline(1L);

        assertEquals(1, timeline.size());
        assertEquals("available", timeline.get(0).get("status"));
    }

    @Test
    void testGetBookingTimeline_WithBookings() {
        List<Order> orders = new ArrayList<>();
        orders.add(testOrder);

        when(orderRepository.findActiveOrdersByScooterId(anyLong(), any())).thenReturn(orders);

        List<Map<String, Object>> timeline = bookingService.getBookingTimeline(1L);

        assertTrue(timeline.size() >= 2);
        assertEquals("booked", timeline.get(0).get("status"));
        assertEquals("available", timeline.get(1).get("status"));
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(testOrder));

        List<Order> result = bookingService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(testOrder.getId(), result.get(0).getId());
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        Optional<Order> result = bookingService.getOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals(testOrder.getId(), result.get().getId());
    }

    @Test
    void testStaffReturnScooter() {
        // 准备测试数据
        StaffReturnScooterDTO returnDTO = new StaffReturnScooterDTO();
        returnDTO.setOrderId(orderId);
        returnDTO.setStaffId(1L);
        returnDTO.setBatteryLevel(new BigDecimal("95"));
        
        User staff = new User();
        staff.setId(1L);
        staff.setRole(0); // 管理员
        
        // 确保订单开始时间在当前时间之前
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1); // 开始时间是1小时前
        testOrder.setStartTime(calendar.getTime());
        
        testOrder.setStatus(2); // 已支付
        testOrder.setDepositPaid(true);
        testOrder.setDepositAmount(new BigDecimal("50.00"));
        testOrder.setPaymentMethod("BANK_CARD");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(staff));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        when(scooterRepository.save(any(Scooter.class))).thenReturn(testScooter);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // 执行测试
        Order result = bookingService.staffReturnScooter(returnDTO);

        // 验证结果
        assertEquals(3, result.getStatus()); // 验证状态更新为已完成
        assertNotNull(result.getReturnTime()); // 验证归还时间已设置
        assertTrue(result.getDepositRefunded()); // 验证押金已退还
        assertEquals(staff, result.getStaff()); // 验证操作员已记录
        
        // 验证方法调用
        verify(scooterRepository).save(any(Scooter.class));
        verify(userRepository).save(any(User.class));
        verify(orderRepository).save(any(Order.class));
    }
}
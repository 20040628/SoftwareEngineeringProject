package group6.demo.service.impl;

import group6.demo.dto.*;
import group6.demo.entity.*;
import group6.demo.repository.*;
import group6.demo.service.PriceDiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setUsername("Test User");
        testUser.setBankBalance(new BigDecimal("1000.00"));

        testScooter = new Scooter();
        testScooter.setId(1L);
        testScooter.setPriceHour(new BigDecimal("5.00"));
        testScooter.setPriceFourHour(new BigDecimal("15.00"));
        testScooter.setPriceDay(new BigDecimal("30.00"));
        testScooter.setPriceWeek(new BigDecimal("150.00"));
        testScooter.setBattery(new BigDecimal("95"));

        testOrder = new Order();
        testOrder.setId(1L);
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
        testOrder.setStatus(2);
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
}
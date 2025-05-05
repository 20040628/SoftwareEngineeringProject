package group6.demo.controller;

import group6.demo.dto.BookingDTO;
import group6.demo.dto.ExtendBookingDTO;
import group6.demo.dto.ReturnScooterDTO;
import group6.demo.dto.StaffBookingDTO;
import group6.demo.entity.Order;
import group6.demo.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookingTimeline() {
        Long scooterId = 1L;
        List<Map<String, Object>> timeline = new ArrayList<>();
        timeline.add(Map.of("startTime", "2025-05-04 10:00:00", "endTime", "2025-05-04 11:00:00"));

        when(bookingService.getBookingTimeline(scooterId)).thenReturn(timeline);

        ResponseEntity<?> response = bookingController.getBookingTimeline(scooterId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(timeline, response.getBody());
        verify(bookingService, times(1)).getBookingTimeline(scooterId);
    }

    @Test
    void testCreateBooking_Valid() {
        BookingDTO bookingDTO = new BookingDTO();
        Order order = new Order();
        order.setId(1L);
        order.setStartTime(new Date());
        order.setEndTime(new Date());
        order.setPriceBeforeDiscount(new BigDecimal("100.00"));
        order.setPrice(new BigDecimal("90.00"));

        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(order);

        ResponseEntity<?> response = bookingController.createBooking(bookingDTO);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Booking successful", responseBody.get("message"));
        assertEquals(1L, responseBody.get("orderId"));
        verify(bookingService, times(1)).createBooking(any(BookingDTO.class));
    }

    @Test
    void testCreateForUnregistered_Valid() {
        StaffBookingDTO bookingDTO = new StaffBookingDTO();
        Order order = new Order();
        order.setId(1L);
        order.setStartTime(new Date());
        order.setEndTime(new Date());
        order.setPriceBeforeDiscount(new BigDecimal("100.00"));
        order.setPrice(new BigDecimal("90.00"));

        when(bookingService.staffCreateBooking(any(StaffBookingDTO.class))).thenReturn(order);

        ResponseEntity<?> response = bookingController.createForUnregistered(bookingDTO);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Booking successful", responseBody.get("message"));
        assertEquals(1L, responseBody.get("orderId"));
        verify(bookingService, times(1)).staffCreateBooking(any(StaffBookingDTO.class));
    }

    @Test
    void testCancelBooking() {
        Long orderId = 1L;

        doNothing().when(bookingService).cancelBooking(orderId);

        ResponseEntity<?> response = bookingController.cancelBooking(orderId);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Booking cancelled successfully", responseBody.get("message"));
        verify(bookingService, times(1)).cancelBooking(orderId);
    }

    @Test
    void testGetAllBookings() {
        List<Order> orders = List.of(new Order(), new Order());

        when(bookingService.getAllOrders()).thenReturn(orders);

        List<Order> result = bookingController.getAllBookings();

        assertEquals(2, result.size());
        verify(bookingService, times(1)).getAllOrders();
    }

    @Test
    void testGetAllUndo() {
        Long userId = 1L;
        List<Order> orders = List.of(new Order(), new Order());

        when(bookingService.getAllUndo(userId)).thenReturn(orders);

        List<Order> result = bookingController.getAllUndo(userId);

        assertEquals(2, result.size());
        verify(bookingService, times(1)).getAllUndo(userId);
    }

    @Test
    void testGetAllOngoing() {
        Long userId = 1L;
        List<Order> orders = List.of(new Order(), new Order());

        when(bookingService.getAllOngoing(userId)).thenReturn(orders);

        List<Order> result = bookingController.getAllOngoing(userId);

        assertEquals(2, result.size());
        verify(bookingService, times(1)).getAllOngoing(userId);
    }

    @Test
    void testGetAllFinished() {
        Long userId = 1L;
        List<Order> orders = List.of(new Order(), new Order());

        when(bookingService.getAllFinished(userId)).thenReturn(orders);

        List<Order> result = bookingController.getAllFinished(userId);

        assertEquals(2, result.size());
        verify(bookingService, times(1)).getAllFinished(userId);
    }

    @Test
    void testGetBookingById() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(bookingService.getOrderById(orderId)).thenReturn(Optional.of(order));

        Optional<Order> result = bookingController.getBookingById(orderId);

        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getId());
        verify(bookingService, times(1)).getOrderById(orderId);
    }

    @Test
    void testExtendBooking_Valid() {
        Long orderId = 1L;
        ExtendBookingDTO extendBookingDTO = new ExtendBookingDTO();
        Order order = new Order();
        order.setId(orderId);
        order.setStartTime(new Date());
        order.setEndTime(new Date());
        order.setPriceBeforeDiscount(new BigDecimal("100.00"));
        order.setPrice(new BigDecimal("90.00"));

        when(bookingService.extendBooking(anyLong(), any(ExtendBookingDTO.class))).thenReturn(order);

        ResponseEntity<?> response = bookingController.exntendBooking(orderId, extendBookingDTO);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Extend Booking successfully", responseBody.get("message"));
        assertEquals(orderId, responseBody.get("orderId"));
        verify(bookingService, times(1)).extendBooking(anyLong(), any(ExtendBookingDTO.class));
    }

    @Test
    void testReturnScooter_Valid() {
        ReturnScooterDTO returnScooterDTO = new ReturnScooterDTO();
        Order order = new Order();
        order.setId(1L);
        order.setReturnTime(new Date());
        order.setDepositRefunded(true);
        order.setDepositAmount(new BigDecimal("50.00"));

        when(bookingService.returnScooter(any(ReturnScooterDTO.class))).thenReturn(order);

        ResponseEntity<?> response = bookingController.returnScooter(returnScooterDTO);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("电动车归还成功", responseBody.get("message"));
        assertEquals(1L, responseBody.get("orderId"));
        assertEquals(true, responseBody.get("depositRefunded"));
        assertEquals(new BigDecimal("50.00"), responseBody.get("depositAmount"));
        assertEquals("您已成功归还电动车，并且电量大于90%，押金已退还。", responseBody.get("depositMessage"));
        verify(bookingService, times(1)).returnScooter(any(ReturnScooterDTO.class));
    }
}

package group6.demo.controller;

import group6.demo.dto.BookingDTO;
import group6.demo.entity.Order;
import group6.demo.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetBookingTimeline_Success() throws Exception {
        // Simulate scooter ID
        Long scooterId = 1L;

        // Mock booking timeline data
        List<Map<String, Object>> mockTimeline = List.of(
                Map.of("startTime", "2024-03-04T10:00:00", "endTime", "2024-03-04T12:00:00")
        );

        // Mock service method
        when(bookingService.getBookingTimeline(scooterId)).thenReturn(mockTimeline);

        // Perform GET request and verify response
        mockMvc.perform(get("/api/bookings/timeline/{scooterId}", scooterId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].startTime").value("2024-03-04T10:00:00"));
    }

    @Test
    void testCreateBooking_Success() throws Exception {
        // Simulate booking data
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setScooterId(1L);
        bookingDTO.setUserId(2L);
        bookingDTO.setHireType("HOUR");
        bookingDTO.setStartTime("2025-03-03 02:59:21.756000");

        // Mock order data
        Order mockOrder = new Order();
        mockOrder.setId(100L);
        BigDecimal price = BigDecimal.valueOf(50.0);
        mockOrder.setPrice(price);

        // Mock service method
        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(mockOrder);

        // Perform POST request and verify response
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Booking successful"))
                .andExpect(jsonPath("$.orderId").value(100));
    }
}
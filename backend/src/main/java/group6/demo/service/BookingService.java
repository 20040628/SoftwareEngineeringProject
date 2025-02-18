package group6.demo.service;

import group6.demo.dto.BookingDTO;
import group6.demo.entity.Order;
import java.util.List;
import java.util.Map;

public interface BookingService {
    Order createBooking(BookingDTO bookingDTO);
    void sendConfirmationEmail(Order order);
    List<Map<String, Object>> getBookingTimeline(Long scooterId);
} 
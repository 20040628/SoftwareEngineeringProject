package group6.demo.service;

import group6.demo.dto.BookingDTO;
import group6.demo.dto.ExtendBookingDTO;
import group6.demo.dto.StaffBookingDTO;
import group6.demo.entity.Order;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookingService {
    Order createBooking(BookingDTO bookingDTO);
    Order staffCreateBooking(StaffBookingDTO bookingDTO);

    void sendConfirmationEmail(Order order);
    List<Map<String, Object>> getBookingTimeline(Long scooterId);
    void cancelBooking(Long orderId);

    List<Order> getAllOrders();
    List<Order> getAllUndo(Long userId);
    List<Order> getAllOngoing(Long userId);
    List<Order> getAllFinished(Long userId);

    Optional<Order> getOrderById(Long id);

    Order extendBooking(Long id, ExtendBookingDTO extendBookingDTO);
} 
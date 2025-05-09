package group6.demo.service;

import group6.demo.dto.BookingDTO;
import group6.demo.dto.ExtendBookingDTO;
import group6.demo.dto.ReturnScooterDTO;
import group6.demo.dto.StaffBookingDTO;
import group6.demo.dto.StaffReturnScooterDTO;
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
    
    /**
     * Return a scooter
     * The order must be in PAID status and current time must be after start time
     * @param returnScooterDTO Return information
     * @return Updated order
     */
    Order returnScooter(ReturnScooterDTO returnScooterDTO);
    
    /**
     * 管理员代表用户还车
     * @param staffReturnScooterDTO 管理员还车信息
     * @return 更新后的订单
     */
    Order staffReturnScooter(StaffReturnScooterDTO staffReturnScooterDTO);
} 
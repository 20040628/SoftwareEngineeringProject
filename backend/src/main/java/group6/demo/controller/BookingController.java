package group6.demo.controller;

import group6.demo.dto.BookingDTO;
import group6.demo.dto.ExtendBookingDTO;
import group6.demo.dto.StaffBookingDTO;
import group6.demo.entity.Order;
import group6.demo.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://127.0.0.1:5173", "http://127.0.0.1:5174"}, allowCredentials = "true")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/timeline/{scooterId}")
    public ResponseEntity<?> getBookingTimeline(@PathVariable Long scooterId) {
        try {
            List<Map<String, Object>> timeline = bookingService.getBookingTimeline(scooterId);
            return ResponseEntity.ok(timeline);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get timeline: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        try {
            Order order = bookingService.createBooking(bookingDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Booking successful");
            response.put("orderId", order.getId());
            response.put("startTime", order.getStartTime());
            response.put("endTime", order.getEndTime());
            response.put("priceBeforeDiscount", order.getPriceBeforeDiscount());
            response.put("price", order.getPrice());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Booking failed: " + e.getMessage());
        }
    }

    @PostMapping("/forUnregistered")
    public ResponseEntity<?> createForUnregistered(@Valid @RequestBody StaffBookingDTO bookingDTO){
        try {
            Order order = bookingService.staffCreateBooking(bookingDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Booking successful");
            response.put("orderId", order.getId());
            response.put("startTime", order.getStartTime());
            response.put("endTime", order.getEndTime());
            response.put("priceBeforeDiscount", order.getPriceBeforeDiscount());
            response.put("price", order.getPrice());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Booking failed: " + e.getMessage());
        }
    }


    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long orderId) {
        try {
            bookingService.cancelBooking(orderId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Booking cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to cancel booking: " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public List<Order> getAllBookings() {
        return bookingService.getAllOrders();
    }

    @GetMapping("/getAllUndo/{userId}")
    public List<Order> getAllUndo(@PathVariable Long userId) {return bookingService.getAllUndo(userId);}

    @GetMapping("/getAllOngoing/{userId}")
    public List<Order> getAllOngoing(@PathVariable Long userId) {return bookingService.getAllOngoing(userId);}

    @GetMapping("/getAllFinished/{userId}")
    public List<Order> getAllFinished(@PathVariable Long userId) {return bookingService.getAllFinished(userId);}

    @GetMapping("/{orderId}")
    public Optional<Order> getBookingById(@PathVariable Long orderId) {
        return bookingService.getOrderById(orderId);
    }

    @PostMapping("/extend/{orderId}")
    public ResponseEntity<?> exntendBooking(@PathVariable Long orderId, @Valid @RequestBody ExtendBookingDTO extendBookingDTO){
        try {
            // 无法续订已完成的订单
            Order order = bookingService.extendBooking(orderId, extendBookingDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Extend Booking successfully");
            response.put("orderId", order.getId());
            response.put("startTime", order.getStartTime());
            response.put("endTime", order.getEndTime());
            response.put("priceBeforeDiscount", order.getPriceBeforeDiscount());
            response.put("price", order.getPrice());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Extend Booking failed: " + e.getMessage());
        }
    }
}
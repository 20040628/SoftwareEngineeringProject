package group6.demo.controller;

import group6.demo.dto.BookingDTO;
import group6.demo.entity.Order;
import group6.demo.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
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
            response.put("price", order.getPrice());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Booking failed: " + e.getMessage());
        }
    }
} 
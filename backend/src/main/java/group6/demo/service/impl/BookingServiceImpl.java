package group6.demo.service.impl;

import group6.demo.dto.BookingDTO;
import group6.demo.entity.Order;
import group6.demo.entity.Scooter;
import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.ScooterRepository;
import group6.demo.repository.UserRepository;
import group6.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScooterRepository scooterRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public Order createBooking(BookingDTO bookingDTO) {
        User user = userRepository.findById(bookingDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("User email is required for booking");
        }

        Scooter scooter = scooterRepository.findById(bookingDTO.getScooterId())
                .orElseThrow(() -> new IllegalArgumentException("Scooter not found"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime;
        try {
            startTime = dateFormat.parse(bookingDTO.getStartTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }

        // Validate if start time is after current time
        if (startTime.before(new Date())) {
            throw new IllegalArgumentException("Booking time cannot be earlier than current time");
        }

        Order order = new Order();
        order.setOrderTime(new Date());
        order.setStartTime(startTime);
        order.setStatus(1); // active
        order.setUser(user);
        order.setScooter(scooter);
        order.setHirePeriod(bookingDTO.getHireType());

        // Calculate end time and price based on hire type
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        
        BigDecimal price;
        switch (bookingDTO.getHireType()) {
            case "HOUR":
                calendar.add(Calendar.HOUR, 1);
                price = scooter.getPriceHour();
                break;
            case "FOUR_HOURS":
                calendar.add(Calendar.HOUR, 4);
                price = scooter.getPriceFourHour();
                break;
            case "DAY":
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                price = scooter.getPriceDay();
                break;
            case "WEEK":
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                price = scooter.getPriceWeek();
                break;
            default:
                throw new IllegalArgumentException("Invalid hire type");
        }

        // Check for booking conflicts
        Date endTime = calendar.getTime();
        List<Order> conflictingOrders = orderRepository.findConflictingOrders(scooter.getId(), startTime, endTime);
        if (!conflictingOrders.isEmpty()) {
            throw new IllegalArgumentException("Selected time period is already booked");
        }

        order.setEndTime(endTime);
        order.setPrice(price);

        Order savedOrder = orderRepository.save(order);
        sendConfirmationEmail(savedOrder);
        
        return savedOrder;
    }

    @Override
    public void sendConfirmationEmail(Order order) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        
        String hirePeriodText;
        switch (order.getHirePeriod()) {
            case "HOUR":
                hirePeriodText = "1 Hour";
                break;
            case "FOUR_HOURS":
                hirePeriodText = "4 Hours";
                break;
            case "DAY":
                hirePeriodText = "1 Day";
                break;
            case "WEEK":
                hirePeriodText = "1 Week";
                break;
            default:
                hirePeriodText = order.getHirePeriod();
        }
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("E-Scooter Rental System <bc_somebody@qq.com>");
        message.setTo(order.getUser().getEmail());
        message.setSubject("E-Scooter Booking Confirmation");
        
        String emailContent = String.format(
            "Dear %s,\n\n" +
            "Your e-scooter booking has been confirmed!\n\n" +
            "Booking Details:\n" +
            "Order ID: %d\n" +
            "Location: %s\n" +
            "Start Time: %s\n" +
            "End Time: %s\n" +
            "Rental Duration: %s\n" +
            "Rental Fee: $%.2f\n\n" +
            "Thank you for using our service!\n\n" +
            "Best regards,\n" +
            "E-Scooter Rental Team",
            order.getUser().getUsername(),
            order.getId(),
            order.getScooter().getLocation(),
            dateFormat.format(order.getStartTime()),
            dateFormat.format(order.getEndTime()),
            hirePeriodText,
            order.getPrice()
        );
        
        message.setText(emailContent);
        emailSender.send(message);
    }

    @Override
    public List<Map<String, Object>> getBookingTimeline(Long scooterId) {
        // 获取从当前时间开始的所有预订
        Date now = new Date();
        List<Order> orders = orderRepository.findActiveOrdersByScooterId(scooterId, now);
        
        List<Map<String, Object>> timeline = new ArrayList<>();
        
        // 将预订信息转换为时间轴数据
        for (Order order : orders) {
            Map<String, Object> timeSlot = new HashMap<>();
            timeSlot.put("startTime", order.getStartTime());
            timeSlot.put("endTime", order.getEndTime());
            timeSlot.put("status", "booked");
            timeSlot.put("hirePeriod", order.getHirePeriod());
            timeline.add(timeSlot);
        }
        
        // 如果有预订，在预订之间添加可用时间段
        if (!orders.isEmpty()) {
            for (int i = 0; i < orders.size() - 1; i++) {
                Order currentOrder = orders.get(i);
                Order nextOrder = orders.get(i + 1);
                
                // 如果两个预订之间有空隙，添加一个可用时间段
                if (currentOrder.getEndTime().before(nextOrder.getStartTime())) {
                    Map<String, Object> availableSlot = new HashMap<>();
                    availableSlot.put("startTime", currentOrder.getEndTime());
                    availableSlot.put("endTime", nextOrder.getStartTime());
                    availableSlot.put("status", "available");
                    timeline.add(availableSlot);
                }
            }
            
            // 添加最后一个预订之后的可用时间段
            Order lastOrder = orders.get(orders.size() - 1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastOrder.getEndTime());
            cal.add(Calendar.DAY_OF_MONTH, 7); // 显示未来7天的可用时间
            
            Map<String, Object> lastAvailableSlot = new HashMap<>();
            lastAvailableSlot.put("startTime", lastOrder.getEndTime());
            lastAvailableSlot.put("endTime", cal.getTime());
            lastAvailableSlot.put("status", "available");
            timeline.add(lastAvailableSlot);
        } else {
            // 如果没有预订，显示从现在开始的7天都可用
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_MONTH, 7);
            
            Map<String, Object> availableSlot = new HashMap<>();
            availableSlot.put("startTime", now);
            availableSlot.put("endTime", cal.getTime());
            availableSlot.put("status", "available");
            timeline.add(availableSlot);
        }
        
        return timeline;
    }
} 
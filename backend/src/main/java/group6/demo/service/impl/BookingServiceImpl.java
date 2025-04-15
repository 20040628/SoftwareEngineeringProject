package group6.demo.service.impl;

import group6.demo.dto.BookingDTO;
import group6.demo.dto.ExtendBookingDTO;
import group6.demo.dto.StaffBookingDTO;
import group6.demo.entity.Order;
import group6.demo.entity.Scooter;
import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.ScooterRepository;
import group6.demo.repository.UserRepository;
import group6.demo.service.BookingService;
import group6.demo.service.PriceDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private PriceDiscountService priceDiscountService;

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
        order.setPriceBeforeDiscount(price);

        // 应用折扣价格
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(price, user.getId());
        
        // Check for booking conflicts
        Date endTime = calendar.getTime();
        List<Order> conflictingOrders = orderRepository.findConflictingOrders(scooter.getId(), startTime, endTime);
        if (!conflictingOrders.isEmpty()) {
            throw new IllegalArgumentException("Selected time period is already booked");
        }

        order.setEndTime(endTime);
        order.setPrice(discountedPrice);

        Order savedOrder = orderRepository.save(order);

        sendConfirmationEmail(savedOrder);
        
        return savedOrder;
    }

    @Override
    public Order staffCreateBooking(StaffBookingDTO bookingDTO){
        // check if staff exists
        User staff = userRepository.findById(bookingDTO.getStaffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

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
        order.setStaff(staff);

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
        order.setPriceBeforeDiscount(price);

        // 应用折扣价格
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(price, user.getId());

        // Check for booking conflicts
        Date endTime = calendar.getTime();
        List<Order> conflictingOrders = orderRepository.findConflictingOrders(scooter.getId(), startTime, endTime);
        if (!conflictingOrders.isEmpty()) {
            throw new IllegalArgumentException("Selected time period is already booked");
        }

        order.setEndTime(endTime);
        order.setPrice(discountedPrice);

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
        // get all the bookings from the current time
        Date now = new Date();
        List<Order> orders = orderRepository.findActiveOrdersByScooterId(scooterId, now);
        
        List<Map<String, Object>> timeline = new ArrayList<>();
        
        // convert the booking information to the timeline data
        for (Order order : orders) {
            Map<String, Object> timeSlot = new HashMap<>();
            timeSlot.put("startTime", order.getStartTime());
            timeSlot.put("endTime", order.getEndTime());
            timeSlot.put("status", "booked");
            timeSlot.put("hirePeriod", order.getHirePeriod());
            timeline.add(timeSlot);
        }
        
        // if there are bookings, add the available time between the bookings
        if (!orders.isEmpty()) {
            for (int i = 0; i < orders.size() - 1; i++) {
                Order currentOrder = orders.get(i);
                Order nextOrder = orders.get(i + 1);
                
                // if there is a gap between two bookings, add an available time
                if (currentOrder.getEndTime().before(nextOrder.getStartTime())) {
                    Map<String, Object> availableSlot = new HashMap<>();
                    availableSlot.put("startTime", currentOrder.getEndTime());
                    availableSlot.put("endTime", nextOrder.getStartTime());
                    availableSlot.put("status", "available");
                    timeline.add(availableSlot);
                }
            }
            
            Order lastOrder = orders.get(orders.size() - 1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastOrder.getEndTime());
            cal.add(Calendar.DAY_OF_MONTH, 7); // show the available time for the next 7 days
            
            Map<String, Object> lastAvailableSlot = new HashMap<>();
            lastAvailableSlot.put("startTime", lastOrder.getEndTime());
            lastAvailableSlot.put("endTime", cal.getTime());
            lastAvailableSlot.put("status", "available");
            timeline.add(lastAvailableSlot);
        } else {
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

    @Override
    public void cancelBooking(Long orderId) {
        // 根据订单 ID 查找订单
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        // 检查订单状态，如果已经取消则无需再次取消 status=2代表订单已经取消
        if (order.getStatus() == 2) {
            throw new IllegalArgumentException("Order is already cancelled");
        }

        // 更新订单状态为已取消
        order.setStatus(3);
        orderRepository.save(order);

        // 发送取消预订的邮件通知用户 待添加
        sendCancellationEmail(order);
    }


    private void sendCancellationEmail(Order order) {
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
        message.setSubject("E-Scooter Booking Cancellation");

        String emailContent = String.format(
                "Dear %s,\n\n" +
                        "Your e-scooter booking has been cancelled!\n\n" +
                        "Booking Details:\n" +
                        "Order ID: %d\n" +
                        "Location: %s\n" +
                        "Start Time: %s\n" +
                        "End Time: %s\n" +
                        "Rental Duration: %s\n" +
                        "Rental Fee: $%.2f\n\n" +
                        "If you have any questions, please contact our customer service.\n\n" +
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
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllUndo(Long userId) {return orderRepository.findAllUndo(userId);}
    @Override
    public List<Order> getAllOngoing(Long userId) {return orderRepository.findAllOngoing(userId);}
    @Override
    public List<Order> getAllFinished(Long userId) {return orderRepository.findAllFinished(userId);}

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order extendBooking(Long id, ExtendBookingDTO extendBookingDTO){
        Order oldOrder = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));;

        // 检查用户是否存在和是否有邮箱
        User user = userRepository.findById(oldOrder.getUser().getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("User email is required for booking");
        }

        Scooter scooter = scooterRepository.findById(oldOrder.getScooter().getId()).orElseThrow(() -> new IllegalArgumentException("Scooter not found"));
        Date oldEndTime = oldOrder.getEndTime();

        // Calculate start time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldEndTime);
        calendar.add(Calendar.SECOND, 1);
        Date newStartTime = calendar.getTime();

        // create a new order
        Order order = new Order();
        order.setOrderTime(new Date());
        order.setStartTime(newStartTime);
        order.setStatus(1);
        order.setUser(user);
        order.setScooter(scooter);
        order.setHirePeriod(extendBookingDTO.getHireType());

        // Calculate end time and price based on hire type
        calendar.setTime(newStartTime);
        BigDecimal price;
        switch (extendBookingDTO.getHireType()) {
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

        order.setPriceBeforeDiscount(price);

        // 应用折扣价格
        BigDecimal discountedPrice = priceDiscountService.calculateDiscountedPrice(price, user.getId());

        // Check for booking conflicts
        Date endTime = calendar.getTime();
        List<Order> conflictingOrders = orderRepository.findConflictingOrders(scooter.getId(), newStartTime, endTime);
        if (!conflictingOrders.isEmpty()) {
            throw new IllegalArgumentException("Selected time period is already booked");
        }

        order.setEndTime(endTime);
        order.setPrice(discountedPrice);

        Order newOrder = orderRepository.save(order);

        sendConfirmationEmail(newOrder);

        return newOrder;
    }
} 
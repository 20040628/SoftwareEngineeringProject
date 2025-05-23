package group6.demo.service.impl;

import group6.demo.dto.BookingDTO;
import group6.demo.dto.ExtendBookingDTO;
import group6.demo.dto.ReturnScooterDTO;
import group6.demo.dto.StaffBookingDTO;
import group6.demo.dto.StaffReturnScooterDTO;
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
import org.springframework.transaction.annotation.Transactional;

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
        order.setStatus(1); // CREATED (已创建)
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
        order.setStatus(1); // CREATED (已创建)
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
            "Battery: %s\n" +
            "Speed: %s\n" +
            "Start Time: %s\n" +
            "End Time: %s\n" +
            "Rental Duration: %s\n" +
            "Rental Fee: $%.2f\n\n" +
            "Rental Store Id: %s\n" +
            "Thank you for using our service!\n\n" +
            "Best regards,\n" +
            "E-Scooter Rental Team",
            order.getUser().getUsername(),
            order.getId(),
            order.getScooter().getBattery(),
            order.getScooter().getSpeed(),
            dateFormat.format(order.getStartTime()),
            dateFormat.format(order.getEndTime()),
            hirePeriodText,
            order.getPrice(),
            order.getScooter().getStore().getId()
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
        // Find order by ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        // Check order status, if already completed or cancelled then cannot cancel again
        if (order.getStatus() == 3 || order.getStatus() == 4) {
            throw new IllegalArgumentException("Order has been completed or cancelled, cannot cancel again");
        }

        // Update order status to CANCELLED(4)
        order.setStatus(4);
        orderRepository.save(order);

        // Send cancellation email to notify user
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
                        "Battery: %s\n" +
                        "Speed: %s\n" +
                        "Start Time: %s\n" +
                        "End Time: %s\n" +
                        "Rental Duration: %s\n" +
                        "Rental Fee: $%.2f\n\n" +
                        "Rental Store Id: %s\n" +
                        "If you have any questions, please contact our customer service.\n\n" +
                        "Best regards,\n" +
                        "E-Scooter Rental Team",
                order.getUser().getUsername(),
                order.getId(),
                order.getScooter().getBattery(),
                order.getScooter().getSpeed(),
                dateFormat.format(order.getStartTime()),
                dateFormat.format(order.getEndTime()),
                hirePeriodText,
                order.getPrice(),
                order.getScooter().getStore().getId()
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
        order.setStatus(1); // CREATED (已创建)
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

    @Override
    @Transactional
    public Order returnScooter(ReturnScooterDTO returnScooterDTO) {
        // Find order
        Order order = orderRepository.findById(returnScooterDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Validate order status must be PAID(2) and current time must be after start time
        if (order.getStatus() != 2 || new Date().before(order.getStartTime())) {
            throw new IllegalArgumentException("Only paid orders that have started can be returned");
        }

        // Get user and scooter information
        User user = order.getUser();
        Scooter scooter = order.getScooter();

        // Record return time
        Date returnTime = new Date();
        order.setReturnTime(returnTime);
        
        // Update order status to COMPLETED(3)
        order.setStatus(3);

        // If paid with bank card and battery level > 90%, refund deposit
        if ("BANK_CARD".equals(order.getPaymentMethod()) && order.getDepositPaid()) {
            BigDecimal batteryLevel = scooter.getBattery();
            
            // If battery level > 90, refund deposit
            if (batteryLevel != null && batteryLevel.compareTo(new BigDecimal("90")) > 0) {
                // Mark deposit as refunded
                order.setDepositRefunded(true);
                
                // Refund deposit to user's bank card balance
                if (user.getBankBalance() != null) {
                    BigDecimal newBalance = user.getBankBalance().add(order.getDepositAmount());
                    user.setBankBalance(newBalance);
                    userRepository.save(user);
                }
                
                // Send email notification for deposit refund
                sendDepositRefundEmail(order);
            }
        }

        // Save updated order
        return orderRepository.save(order);
    }
    
    /**
     * Send deposit refund email notification
     * @param order Order information
     */
    private void sendDepositRefundEmail(Order order) {
        try {
            User user = order.getUser();
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                return;
            }
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Deposit Refund Notification");
            message.setText("Dear " + user.getUsername() + ",\n\n" +
                    "Hello! Your order #" + order.getId() + " has been completed. Since the scooter was returned with a battery level above 90%, " +
                    "we have refunded your deposit of $" + order.getDepositAmount() + " to your bank card account.\n\n" +
                    "Thank you for using our service!\n\n" +
                    "Have a pleasant journey,\n" +
                    "E-Scooter Rental Platform Team");
            
            emailSender.send(message);
        } catch (Exception e) {
            // Log email sending failure, but don't affect the main business process
            System.err.println("Failed to send deposit refund email: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Order staffReturnScooter(StaffReturnScooterDTO staffReturnScooterDTO) {
        // 验证管理员身份
        User staff = userRepository.findById(staffReturnScooterDTO.getStaffId())
                .orElseThrow(() -> new IllegalArgumentException("管理员不存在"));
                
        if (staff.getRole() != 0) {
            throw new IllegalArgumentException("只有管理员才能执行此操作");
        }
        
        // 查找订单
        Order order = orderRepository.findById(staffReturnScooterDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));
        
        // 验证订单状态必须是已支付(2)且当前时间必须晚于开始时间
        if (order.getStatus() != 2 || new Date().before(order.getStartTime())) {
            throw new IllegalArgumentException("只有已支付且已开始的订单才能被归还");
        }
        
        // 获取用户和滑板车信息
        User user = order.getUser();
        Scooter scooter = order.getScooter();
        
        // 如果提供了电池电量，更新滑板车电池电量
        if (staffReturnScooterDTO.getBatteryLevel() != null) {
            scooter.setBattery(staffReturnScooterDTO.getBatteryLevel());
            scooterRepository.save(scooter);
        }
        
        // 记录归还时间
        Date returnTime = new Date();
        order.setReturnTime(returnTime);
        
        // 更新订单状态为已完成(3)
        order.setStatus(3);
        
        // 记录由哪位管理员操作的
        order.setStaff(staff);
        
        // 如果通过银行卡支付且已支付押金，根据电池电量决定是否退还押金
        if ("BANK_CARD".equals(order.getPaymentMethod()) && order.getDepositPaid()) {
            BigDecimal batteryLevel = scooter.getBattery();
            
            // 如果电池电量 > 90%，退还押金
            if (batteryLevel != null && batteryLevel.compareTo(new BigDecimal("90")) > 0) {
                // 标记押金已退还
                order.setDepositRefunded(true);
                
                // 退还押金到用户银行卡余额
                if (user.getBankBalance() != null) {
                    BigDecimal newBalance = user.getBankBalance().add(order.getDepositAmount());
                    user.setBankBalance(newBalance);
                    userRepository.save(user);
                }
                
                // 发送押金退还通知邮件
                sendDepositRefundEmail(order);
            }
        }
        
        // 存储更新后的订单
        return orderRepository.save(order);
    }
} 
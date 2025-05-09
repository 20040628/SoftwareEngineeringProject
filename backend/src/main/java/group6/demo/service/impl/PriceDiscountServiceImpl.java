package group6.demo.service.impl;

import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.UserRepository;
import group6.demo.service.PriceDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class PriceDiscountServiceImpl implements PriceDiscountService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    // 折扣比例
    private static final BigDecimal FREQUENT_USER_DISCOUNT = new BigDecimal("0.90"); // 10% 折扣
    private static final BigDecimal STUDENT_DISCOUNT = new BigDecimal("0.85");      // 15% 折扣
    private static final BigDecimal SENIOR_DISCOUNT = new BigDecimal("0.80");       // 20% 折扣
    
    // 判断学生和老人的年龄界限
    private static final int STUDENT_MIN_AGE = 18;
    private static final int STUDENT_MAX_AGE = 25;
    private static final int SENIOR_MIN_AGE = 60;
    
    @Override
    public BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, Long userId) {
        // 如果原价为空或者用户ID为空，直接返回原价
        if (originalPrice == null || userId == null) {
            System.out.println("Discount calculation: Original price or user ID is null");
            return originalPrice;
        }
        
        // 获取用户信息
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            System.out.println("Discount calculation: User not found - " + userId);
            return originalPrice;
        }
        
        User user = userOptional.get();
        
        // 计算折扣
        BigDecimal discountRate = BigDecimal.ONE; // 默认无折扣

        // 应用折扣规则
        
        // 1. 检查是否为常客
        if (user.getIsFrequentUser() != null && user.getIsFrequentUser() == 1) {
            discountRate = discountRate.multiply(FREQUENT_USER_DISCOUNT);
            System.out.println("Applying frequent customer discount: " + FREQUENT_USER_DISCOUNT);
        }
        
        // 2. 检查是否为学生
        if (user.getIsStudent() != null && user.getIsStudent() == 1) {
            discountRate = discountRate.multiply(STUDENT_DISCOUNT);
            System.out.println("Applying student discount: " + STUDENT_DISCOUNT);
        }
        
        // 3. 检查是否为老年人
        if (user.getIsSenior() != null && user.getIsSenior() == 1) {
            discountRate = discountRate.multiply(SENIOR_DISCOUNT);
            System.out.println("Applying senior discount: " + SENIOR_DISCOUNT);
        }
        
        // 计算折扣后价格
        BigDecimal discountedPrice = originalPrice.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
        System.out.println("Discount calculation: Original price=" + originalPrice + ", Discount rate=" + discountRate + ", Discounted price=" + discountedPrice);
        
        return discountedPrice;
    }
    
    /**
     * 根据用户生日自动更新学生和老人身份状态
     * @param user 用户信息
     */
    private void updateUserDiscountStatus(User user) {
        if (user == null) return;
        
        System.out.println("Starting to check discount eligibility for user ID=" + user.getId());
        System.out.println("User birthday information: " + (user.getBirthday() != null ? user.getBirthday().toString() : "null"));
        
        boolean updated = false;
        
        // 如果有生日信息，计算年龄并更新学生和老人状态
        if (user.getBirthday() != null) {
            Calendar birthCal = Calendar.getInstance();
            birthCal.setTime(user.getBirthday());
            
            Calendar now = Calendar.getInstance();
            
            System.out.println("Current date: " + now.getTime());
            System.out.println("Birth date: " + birthCal.getTime());
            System.out.println("Current year: " + now.get(Calendar.YEAR));
            System.out.println("Birth year: " + birthCal.get(Calendar.YEAR));
            System.out.println("Current day of year: " + now.get(Calendar.DAY_OF_YEAR));
            System.out.println("Birth day of year: " + birthCal.get(Calendar.DAY_OF_YEAR));
            
            // 计算年龄
            int age = now.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
            
            System.out.println("Initial age calculation: " + age);
            
            // 检查是否已经过了今年的生日
            if (now.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                age--; // 如果还没到生日，年龄减1
                System.out.println("Current date is before birthday, adjusted age: " + age);
            }
            
            // 更新学生状态 (18-25岁为学生年龄段)
            boolean isStudent = age >= 18 && age <= 25;
            System.out.println("Is student age (18-25): " + isStudent);
            System.out.println("Current student status: " + (user.getIsStudent() != null ? user.getIsStudent() : "null"));
            
            // 只有当状态需要变更时才更新
            if ((isStudent && (user.getIsStudent() == null || user.getIsStudent() != 1)) || 
                (!isStudent && user.getIsStudent() != null && user.getIsStudent() == 1)) {
                user.setIsStudent(isStudent ? 1 : 0);
                System.out.println("Updating user ID=" + user.getId() + " student status: " + (isStudent ? "Yes" : "No") + ", Age=" + age);
                updated = true;
            } else {
                System.out.println("No need to update student status, keeping as: " + (user.getIsStudent() == 1 ? "Yes" : "No"));
            }
            
            // 更新老人状态 (60岁以上为老人)
            boolean isSenior = age >= 60;
            System.out.println("Is senior age (60+): " + isSenior);
            System.out.println("Current senior status: " + (user.getIsSenior() != null ? user.getIsSenior() : "null"));
            
            // 只有当状态需要变更时才更新
            if ((isSenior && (user.getIsSenior() == null || user.getIsSenior() != 1)) || 
                (!isSenior && user.getIsSenior() != null && user.getIsSenior() == 1)) {
                user.setIsSenior(isSenior ? 1 : 0);
                System.out.println("Updating user ID=" + user.getId() + " senior status: " + (isSenior ? "Yes" : "No") + ", Age=" + age);
                updated = true;
            } else {
                System.out.println("No need to update senior status, keeping as: " + (user.getIsSenior() == 1 ? "Yes" : "No"));
            }
        } else {
            System.out.println("Warning: User ID=" + user.getId() + " has no birthday information, cannot calculate discount eligibility");
        }
        
        // 如果有更新，保存用户信息
        if (updated) {
            System.out.println("User discount status has changed, saving to database...");
            try {
            userRepository.save(user);
                System.out.println("Save successful, updated status: Student=" + user.getIsStudent() + ", Senior=" + user.getIsSenior());
            } catch (Exception e) {
                System.err.println("Failed to update user discount status, user ID=" + user.getId() + ": " + e.getMessage());
            }
        } else {
            System.out.println("User discount status unchanged, no need to save");
        }
    }
    
    @Override
    public boolean isFrequentUser(Long userId) {
        // 获取一周前的日期
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = calendar.getTime();
        
        // 查询用户一周内使用总时间（小时）
        Long totalHours = orderRepository.getTotalHoursInLastWeek(userId, startDate, endDate);
        
        // 如果总时间为null或小于8小时，则不是常客
        return totalHours != null && totalHours >= 8;
    }
    
    @Override
    public void updateFrequentUserStatus(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 更新常客状态
            boolean isFrequent = isFrequentUser(userId);
            user.setIsFrequentUser(isFrequent ? 1 : 0);
            
            // 同时更新学生和老人身份
            updateUserDiscountStatus(user);
            
            // 不需要在这里保存，因为updateUserDiscountStatus已处理保存
        }
    }
    
    /**
     * 每周一凌晨2点自动更新所有用户的折扣状态
     */
    @Scheduled(cron = "0 0 2 ? * MON")
    public void updateAllFrequentUserStatus() {
        userRepository.findAll().forEach(user -> {
            try {
                // 更新常客状态
                boolean isFrequent = isFrequentUser(user.getId());
                user.setIsFrequentUser(isFrequent ? 1 : 0);
                
                // 更新学生和老人身份
                updateUserDiscountStatus(user);
                
                // 保存更新
                userRepository.save(user);
            } catch (Exception e) {
                // 记录错误日志但不中断处理
                System.err.println("Updating user discount status failed, user ID=" + user.getId() + ": " + e.getMessage());
            }
        });
    }
    
    @Override
    public void updateUserDiscountStatusByBirthday(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            updateUserDiscountStatus(user);
        } else {
            System.err.println("Cannot update discount status, user not found: " + userId);
        }
    }
} 
package group6.demo.service.impl;

import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.UserRepository;
import group6.demo.service.PriceDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        if (originalPrice == null || userId == null) {
            System.out.println("折扣计算: 原价或用户ID为空");
            return originalPrice;
        }
        
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            System.out.println("折扣计算: 用户不存在 - " + userId);
            return originalPrice;
        }
        
        User user = userOptional.get();
        
        // 在计算折扣前，根据用户生日更新学生和老人身份
        updateUserDiscountStatus(user);
        
        BigDecimal discountRate = BigDecimal.ONE; // 默认无折扣
        
        // 判断是否为常客
        if (user.getIsFrequentUser() != null && user.getIsFrequentUser() == 1) {
            discountRate = discountRate.multiply(FREQUENT_USER_DISCOUNT);
            System.out.println("应用常客折扣: " + FREQUENT_USER_DISCOUNT);
        }
        
        // 判断是否为学生
        if (user.getIsStudent() != null && user.getIsStudent() == 1) {
            discountRate = discountRate.multiply(STUDENT_DISCOUNT);
            System.out.println("应用学生折扣: " + STUDENT_DISCOUNT);
        }
        
        // 判断是否为老年人
        if (user.getIsSenior() != null && user.getIsSenior() == 1) {
            discountRate = discountRate.multiply(SENIOR_DISCOUNT);
            System.out.println("应用老年人折扣: " + SENIOR_DISCOUNT);
        }
        
        // 计算折扣价格
        BigDecimal discountedPrice = originalPrice.multiply(discountRate).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println("折扣计算: 原价=" + originalPrice + ", 折扣率=" + discountRate + ", 折后价=" + discountedPrice);
        return discountedPrice;
    }
    
    /**
     * 根据用户生日自动更新学生和老人身份状态
     * @param user 用户信息
     */
    private void updateUserDiscountStatus(User user) {
        boolean updated = false;
        
        System.out.println("开始检查用户ID=" + user.getId() + "的折扣资格");
        System.out.println("用户生日信息: " + (user.getBirthday() != null ? user.getBirthday().toString() : "null"));
        
        if (user.getBirthday() != null) {
            Calendar now = Calendar.getInstance();
            Calendar birthCal = Calendar.getInstance();
            birthCal.setTime(user.getBirthday());
            
            System.out.println("当前日期: " + now.getTime());
            System.out.println("生日日期: " + birthCal.getTime());
            System.out.println("当前年份: " + now.get(Calendar.YEAR));
            System.out.println("生日年份: " + birthCal.get(Calendar.YEAR));
            System.out.println("当前一年中的日期: " + now.get(Calendar.DAY_OF_YEAR));
            System.out.println("生日一年中的日期: " + birthCal.get(Calendar.DAY_OF_YEAR));
            
            // 计算年龄
            int age = now.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
            System.out.println("初步计算年龄: " + age);
            
            if (now.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                age--;
                System.out.println("当前日期小于生日日期，年龄调整为: " + age);
            }
            
            // 判断是否为学生（18-25岁）
            boolean isStudent = (age >= STUDENT_MIN_AGE && age <= STUDENT_MAX_AGE);
            System.out.println("是否为学生年龄(18-25): " + isStudent);
            System.out.println("当前学生状态: " + (user.getIsStudent() != null ? user.getIsStudent() : "null"));
            
            if ((user.getIsStudent() == null) || (user.getIsStudent() == 1) != isStudent) {
                user.setIsStudent(isStudent ? 1 : 0);
                updated = true;
                System.out.println("更新用户ID=" + user.getId() + "的学生状态: " + (isStudent ? "是" : "否") + ", 年龄=" + age);
            } else {
                System.out.println("无需更新学生状态，保持为: " + (user.getIsStudent() == 1 ? "是" : "否"));
            }
            
            // 判断是否为老人（60岁以上）
            boolean isSenior = (age >= SENIOR_MIN_AGE);
            System.out.println("是否为老人年龄(60+): " + isSenior);
            System.out.println("当前老人状态: " + (user.getIsSenior() != null ? user.getIsSenior() : "null"));
            
            if ((user.getIsSenior() == null) || (user.getIsSenior() == 1) != isSenior) {
                user.setIsSenior(isSenior ? 1 : 0);
                updated = true;
                System.out.println("更新用户ID=" + user.getId() + "的老人状态: " + (isSenior ? "是" : "否") + ", 年龄=" + age);
            } else {
                System.out.println("无需更新老人状态，保持为: " + (user.getIsSenior() == 1 ? "是" : "否"));
            }
        } else {
            System.out.println("警告: 用户ID=" + user.getId() + "没有生日信息，无法计算折扣资格");
        }
        
        // 如果状态有更新，保存到数据库
        if (updated) {
            System.out.println("用户折扣状态已变更，正在保存到数据库...");
            userRepository.save(user);
            System.out.println("保存成功, 更新后状态: 学生=" + user.getIsStudent() + ", 老人=" + user.getIsSenior());
        } else {
            System.out.println("用户折扣状态未变更，不需要保存");
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
                System.err.println("更新用户折扣状态失败，用户ID=" + user.getId() + ": " + e.getMessage());
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
            System.err.println("无法更新折扣状态，用户不存在: " + userId);
        }
    }
} 
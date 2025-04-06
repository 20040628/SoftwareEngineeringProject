package group6.demo.service;

import java.math.BigDecimal;

/**
 * 折扣价格服务接口
 * 根据用户类型提供不同的折扣策略
 */
public interface PriceDiscountService {
    /**
     * 计算折扣价格
     * @param originalPrice 原始价格
     * @param userId 用户ID
     * @return 折扣后的价格
     */
    BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, Long userId);
    
    /**
     * 判断用户是否为常客（每周使用8小时以上）
     * @param userId 用户ID
     * @return 是否为常客
     */
    boolean isFrequentUser(Long userId);
    
    /**
     * 更新用户的常客状态
     * @param userId 用户ID
     */
    void updateFrequentUserStatus(Long userId);
    
    /**
     * 根据生日自动更新用户的学生和老人折扣状态
     * @param userId 用户ID
     */
    void updateUserDiscountStatusByBirthday(Long userId);
} 
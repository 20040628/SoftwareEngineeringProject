package group6.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 滑板车及折扣价格数据传输对象
 */
@Data
public class ScooterWithDiscountDTO {
    private Long id;
    private Integer status;
    // 原始价格
    private BigDecimal priceHour;
    private BigDecimal priceFourHour;
    private BigDecimal priceDay;
    private BigDecimal priceWeek;
    
    // 折扣价格
    private BigDecimal discountedPriceHour;
    private BigDecimal discountedPriceFourHour;
    private BigDecimal discountedPriceDay;
    private BigDecimal discountedPriceWeek;
    
    // 是否有折扣
    private boolean hasDiscount;

    private BigDecimal speed;
    private BigDecimal battery;
} 
package group6.demo.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 每日收入统计数据传输对象
 */
@Data
public class DailyRevenueDTO {
    // 日期
    private Date date;
    
    // 星期几 (1-7, 1表示周一)
    private Integer dayOfWeek;
    
    // 星期几名称 (如"周一")
    private String dayOfWeekName;
    
    // 小时租赁收入
    private BigDecimal hourlyRevenue;
    
    // 4小时租赁收入
    private BigDecimal fourHoursRevenue;
    
    // 日租赁收入
    private BigDecimal dailyRevenue;
    
    // 周租赁收入
    private BigDecimal weeklyRevenue;
    
    // 总收入
    private BigDecimal totalRevenue;
    
    // 订单数量
    private Integer ordersCount;
    
    // 折扣总额（原价-折扣价）
    private BigDecimal totalDiscount;
} 
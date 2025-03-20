package group6.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WeeklyRevenueDTO {
    private Long id;
    private Date weekStartDate;
    private Date weekEndDate;
    private BigDecimal hourlyRevenue;
    private BigDecimal fourHoursRevenue;
    private BigDecimal dailyRevenue;
    private BigDecimal weeklyRevenue;
    private BigDecimal totalRevenue;
    private Integer ordersCount;
} 
package group6.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "b_daily_revenue")
@Data
public class DailyRevenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "revenue_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date revenueDate;
    
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;
    
    @Column(name = "day_of_week_name", nullable = false)
    private String dayOfWeekName;
    
    @Column(name = "hourly_revenue", nullable = false)
    private BigDecimal hourlyRevenue;
    
    @Column(name = "four_hours_revenue", nullable = false)
    private BigDecimal fourHoursRevenue;
    
    @Column(name = "daily_revenue", nullable = false)
    private BigDecimal dailyRevenue;
    
    @Column(name = "weekly_revenue", nullable = false)
    private BigDecimal weeklyRevenue;
    
    @Column(name = "total_revenue", nullable = false)
    private BigDecimal totalRevenue;
    
    @Column(name = "orders_count", nullable = false)
    private Integer ordersCount;
    
    @Column(name = "total_discount", nullable = false)
    private BigDecimal totalDiscount;
    
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
} 
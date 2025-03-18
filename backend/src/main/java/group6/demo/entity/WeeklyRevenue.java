package group6.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "b_weekly_revenue")
@Data
public class WeeklyRevenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "week_start_date", nullable = false)
    private Date weekStartDate;
    
    @Column(name = "week_end_date", nullable = false)
    private Date weekEndDate;
    
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
    
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
} 
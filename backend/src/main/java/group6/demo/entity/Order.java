package group6.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "b_order")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_time", nullable = false)
    private Date orderTime;
    
    @Column(nullable = false)
    private Integer status; // 1: active, 2: completed, 3: cancelled
    
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    
    @Column(name = "end_time", nullable = false)
    private Date endTime;
    
    @Column(name = "hire_period", nullable = false)
    private String hirePeriod;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "scooter_id", nullable = false)
    private Scooter scooter;
} 
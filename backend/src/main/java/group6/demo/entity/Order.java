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

    // 1: CREATED (Created)
    // 2: PAID (Paid)
    // 3: COMPLETED (Completed)
    // 4: CANCELLED (Cancelled)
    @Column(nullable = false)
    private Integer status;
    
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    
    @Column(name = "end_time", nullable = false)
    private Date endTime;
    
    @Column(name = "hire_period", nullable = false)
    private String hirePeriod;

    @Column(nullable = false)
    private BigDecimal PriceBeforeDiscount;

    @Column(nullable = false)
    private BigDecimal price;
    
    // 押金相关字段
    @Column(name = "deposit_paid")
    private Boolean depositPaid = false;
    
    @Column(name = "deposit_amount")
    private BigDecimal depositAmount = BigDecimal.ZERO;
    
    @Column(name = "deposit_refunded")
    private Boolean depositRefunded = false;
    
    @Column(name = "return_time")
    private Date returnTime;
    
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "scooter_id", nullable = false)
    private Scooter scooter;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = true)
    private User staff;
} 
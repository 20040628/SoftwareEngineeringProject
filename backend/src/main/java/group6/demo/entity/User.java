package group6.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "b_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    private String avatar;
    
    @Temporal(TemporalType.DATE)
    private Date birthday;
    
    private Integer userType;
    private String mobile;
    private String email;
    private String paymentMethod;
    private String bankCard;
    
    // 银行卡余额
    private BigDecimal bankBalance;
    
    private Integer status;
    private Integer role;
    private Integer isFrequentUser;
    
    // 新增字段：是否为学生 (1:是, 0:否)
    private Integer isStudent;
    
    // 新增字段：是否为老年人 (1:是, 0:否)
    private Integer isSenior;
} 
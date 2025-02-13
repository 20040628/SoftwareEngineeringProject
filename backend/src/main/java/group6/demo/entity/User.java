package group6.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
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
    private Integer status;
    private Integer role;
    private Integer isFrequentUser;
} 
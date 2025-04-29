package group6.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String mobile;
    private String avatar;
    private Date birthday;
    private Integer userType;
    private Integer status;
    private Integer role;
    
    // 银行卡信息
    private boolean hasBankCard;
    private String maskedBankCard; // 脱敏的银行卡号，只显示后4位
    private BigDecimal bankBalance;
    
    // 折扣信息
    private Integer isStudent;
    private Integer isSenior;
    private Integer isFrequentUser;
    
    // 头像路径（完整URL）
    private String avatarUrl;
} 
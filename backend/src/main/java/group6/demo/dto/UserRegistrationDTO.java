package group6.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserRegistrationDTO {
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", 
            message = "Username must be 3-20 characters long and can only contain letters, numbers and underscore")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^\\d{10,13}$",
            message = "Mobile number must be 10-13 digits")
    private String mobile;

    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    private Date birthday;

    @Pattern(regexp = "^[0-9]{13,19}$", message = "Bank card number must be 13-19 digits")
    private String bankCard;
    // 银行卡余额字段 - 在服务层生成随机值
    private BigDecimal bankBalance;

    private Integer isStudent;
    private Integer isSenior;

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", birthday=" + (birthday != null ? birthday.toString() : "null") +
                '}';
    }
} 
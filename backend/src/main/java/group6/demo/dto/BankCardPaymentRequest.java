package group6.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
public class BankCardPaymentRequest {
    @Pattern(regexp = "^[0-9]{3,4}$", message = "安全码必须为3-4位数字")
    private String securityCode; // CVV/安全码
} 
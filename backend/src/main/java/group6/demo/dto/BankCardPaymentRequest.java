package group6.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
public class BankCardPaymentRequest {
    @Pattern(regexp = "^[0-9]{6}$", message = "The security code must have 6 digits.")
    private String securityCode; // CVV/安全码
} 
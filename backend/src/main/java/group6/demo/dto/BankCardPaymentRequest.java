package group6.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
public class BankCardPaymentRequest {
    private String bankCard;
} 
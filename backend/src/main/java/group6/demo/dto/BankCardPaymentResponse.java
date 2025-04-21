package group6.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BankCardPaymentResponse {
    private boolean success;
    private String message;
    private Long orderId;
    private String bankCardLast4;
    private BigDecimal amount;
    private Date paymentTime;
} 
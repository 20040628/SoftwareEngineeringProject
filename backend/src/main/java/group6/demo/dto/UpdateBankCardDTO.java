package group6.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class UpdateBankCardDTO {
    @Pattern(regexp = "^[0-9]{13,19}$", message = "Bank card number must be 13-19 digits")
    private String bankCard;
    
    // 银行卡余额字段 - 在服务层生成随机值
    private BigDecimal bankBalance;
} 
package group6.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReturnScooterDTO {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    // 可以添加其他还车相关信息，如用户反馈等
    private String remarks;
} 
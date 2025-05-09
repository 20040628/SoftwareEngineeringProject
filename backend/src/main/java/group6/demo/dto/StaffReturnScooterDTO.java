package group6.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class StaffReturnScooterDTO {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    @NotNull(message = "管理员ID不能为空")
    private Long staffId;
    
    // 可自定义电池电量
    private BigDecimal batteryLevel;
    
    // 备注信息
    private String remarks;
} 
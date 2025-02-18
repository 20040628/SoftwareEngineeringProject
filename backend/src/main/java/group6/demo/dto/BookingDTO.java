package group6.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "滑板车ID不能为空")
    private Long scooterId;
    
    @NotNull(message = "租赁类型不能为空")
    private String hireType; // HOUR, FOUR_HOURS, DAY, WEEK
    
    @NotNull(message = "开始时间不能为空")
    private String startTime;
} 
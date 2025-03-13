package group6.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingDTO {
    @NotNull(message = "user ID cannot be empty")
    private Long userId;
    
    @NotNull(message = "scooter ID cannot be empty")
    private Long scooterId;
    
    @NotNull(message = "hire type cannot be empty")
    private String hireType; // HOUR, FOUR_HOURS, DAY, WEEK
    
    @NotNull(message = "start time cannot be empty")
    private String startTime;
} 
package group6.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class AvailableScooterDTO {
    @NotNull(message = "hire type cannot be empty")
    // HOUR, FOUR_HOURS, DAY, WEEK
    private String hireType;

    @NotNull(message = "start time cannot be empty")
    private String startTime;
}

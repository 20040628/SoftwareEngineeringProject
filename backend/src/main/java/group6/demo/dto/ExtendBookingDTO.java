package group6.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExtendBookingDTO {
    // HOUR, FOUR_HOURS, DAY, WEEK
    @NotNull(message = "hire type cannot be empty")
    private String hireType;
}

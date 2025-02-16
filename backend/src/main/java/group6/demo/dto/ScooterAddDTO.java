package group6.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Digits;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScooterAddDTO {

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "The price per hour is required")
    @Digits(integer = 3, fraction = 2,
            message = "Price per hour must be a decimal with up to 3 digits and 2 decimal places")
    private BigDecimal priceHour;

    @NotNull(message = "The price per four hours is required")
    @Digits(integer = 3, fraction = 2,
            message = "Price per four hours must be a decimal with up to 3 digits and 2 decimal places")
    private BigDecimal priceFourHour;

    @NotNull(message = "The price per day is required")
    @Digits(integer = 3, fraction = 2,
            message = "Price per day must be a decimal with up to 3 digits and 2 decimal places")
    private BigDecimal priceDay;

    @NotNull(message = "The price per week is required")
    @Digits(integer = 3, fraction = 2,
            message = "Price per week must be a decimal with up to 3 digits and 2 decimal places")
    private BigDecimal priceWeek;
}

package group6.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Digits;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScooterAddDTO {
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

    @NotNull(message = "The longitude of the location is required")
    @Digits(integer = 3, fraction = 6,
            message = "longitude must be a decimal with up to 3 digits and 6 decimal places")
    private BigDecimal longitude;

    @NotNull(message = "The latitude of the location is required")
    @Digits(integer = 3, fraction = 6,
            message = "latitude must be a decimal with up to 3 digits and 6 decimal places")
    private BigDecimal latitude;

    @NotNull(message = "The battery of the scooter is required")
    @Digits(integer = 3, fraction = 2,
            message = "battery must be a decimal with up to 3 digits and 2 decimal places")
    private BigDecimal battery;

    @NotNull(message = "The speed of the scooter is required")
    @Digits(integer = 3, fraction = 2,
            message = "speed must be a decimal with up to 3 digits and 2 decimal places")
    private BigDecimal speed;
}

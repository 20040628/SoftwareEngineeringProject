package group6.demo.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreAddDTO {
    @NotNull(message = "The name of the store is required")
    private String name;

    @NotNull(message = "The longitude of the location is required")
    @Digits(integer = 3, fraction = 6,
            message = "longitude must be a decimal with up to 3 digits and 6 decimal places, and between -180 and 180")
    private BigDecimal longitude;

    @NotNull(message = "The latitude of the location is required")
    @Digits(integer = 3, fraction = 6,
            message = "latitude must be a decimal with up to 3 digits and 6 decimal places, and between -90 and 90")
    private BigDecimal latitude;
}

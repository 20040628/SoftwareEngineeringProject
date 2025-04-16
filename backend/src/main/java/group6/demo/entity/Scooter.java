package group6.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "b_scooter")
@Data
public class Scooter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal priceHour;
    private BigDecimal priceFourHour;
    private BigDecimal priceDay;
    private BigDecimal priceWeek;
    // status: 1: valid 0:invalid
    private Integer status;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal battery;
    private BigDecimal speed;
}

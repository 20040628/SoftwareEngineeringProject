package group6.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ForgotPasswordDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
} 
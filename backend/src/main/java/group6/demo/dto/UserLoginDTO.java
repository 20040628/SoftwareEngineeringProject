package group6.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
    
    @NotBlank(message = "Captcha is required")
    private String captcha;
    
    @NotBlank(message = "Captcha key is required")
    private String captchaKey;
} 
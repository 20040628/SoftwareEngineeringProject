package group6.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class ChangePasswordDTO {
    @NotBlank(message = "The old password cannot be left blank")
    private String oldPassword;
    
    @NotBlank(message = "The new password cannot be left blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",
            message = "The password must consist of at least 8 characters, including at least one capital letter, one lowercase letter and one digit.")
    private String newPassword;
    
    @NotBlank(message = "The confirmation password cannot be left blank")
    private String confirmPassword;
} 
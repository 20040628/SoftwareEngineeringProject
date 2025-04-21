package group6.demo.dto;

import lombok.Data;

@Data
public class CaptchaResponseDTO {
    private String captchaKey;
    private String captchaImageBase64;
} 
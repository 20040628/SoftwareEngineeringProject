package group6.demo.controller;

import group6.demo.dto.CaptchaResponseDTO;
import group6.demo.dto.UserLoginDTO;
import group6.demo.dto.UserRegistrationDTO;
import group6.demo.entity.User;
import group6.demo.service.CaptchaService;
import group6.demo.util.JwtUtil;
import group6.demo.service.PriceDiscountService;
import group6.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}, allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PriceDiscountService priceDiscountService;
    
    @Autowired
    private CaptchaService captchaService;
    
    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponseDTO> getCaptcha() {
        try {
            String captchaKey = captchaService.generateCaptchaKey();
            String captchaBase64 = captchaService.generateCaptchaBase64(captchaKey);
            
            CaptchaResponseDTO responseDTO = new CaptchaResponseDTO();
            responseDTO.setCaptchaKey(captchaKey);
            responseDTO.setCaptchaImageBase64(captchaBase64);
            
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO loginDTO,
                                     BindingResult bindingResult) {
        // Check validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        
        // 验证验证码
        if (!captchaService.validateCaptcha(loginDTO.getCaptchaKey(), loginDTO.getCaptcha())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("captcha", "验证码错误或已过期");
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            User user = userService.loginUser(loginDTO);

            // Check user status
            if (user.getStatus() == 0) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("You do not have permission to log in, please contact the platform.");
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getUserType(), user.getRole());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("userType", user.getUserType());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("token", token);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            // 打印收到的注册信息，特别是生日信息
            System.out.println("收到注册请求：" + registrationDTO.getUsername());
            System.out.println("生日信息：" + (registrationDTO.getBirthday() != null ? registrationDTO.getBirthday().toString() : "null"));
            
            // 检查用户名是否已存在
            if (userService.isUsernameExists(registrationDTO.getUsername())) {
                return ResponseEntity.badRequest().body("Username already exists");
            }
            
            // 检查邮箱是否已存在
            if (userService.isEmailExists(registrationDTO.getEmail())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }
            
            // 注册用户
            User registeredUser = userService.registerUser(registrationDTO);
            
            // 注册后直接更新折扣状态
            System.out.println("注册成功，正在更新折扣状态...");
            priceDiscountService.updateUserDiscountStatusByBirthday(registeredUser.getId());
            
            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", registeredUser.getId());
            response.put("username", registeredUser.getUsername());
            response.put("isStudent", registeredUser.getIsStudent());
            response.put("isSenior", registeredUser.getIsSenior());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error during registration: " + e.getMessage());
        }
    }
    
    // Validate token endpoint
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Invalid or missing token");
            }
            
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            Long userId = jwtUtil.extractUserId(token);
            Integer userType = jwtUtil.extractUserType(token);
            Integer role = jwtUtil.extractRole(token);
            
            if (username != null && !jwtUtil.isTokenExpired(token)) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("userId", userId);
                response.put("username", username);
                response.put("userType", userType);
                response.put("role", role);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Token validation failed: " + e.getMessage());
        }
    }
} 
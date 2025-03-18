package group6.demo.controller;

import group6.demo.dto.UserLoginDTO;
import group6.demo.dto.UserRegistrationDTO;
import group6.demo.entity.User;
import group6.demo.service.UserService;
import group6.demo.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

        try {
            User user = userService.loginUser(loginDTO);
            
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO,
                                        BindingResult bindingResult) {
        // Check validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // Check if username exists
        if (userService.isUsernameExists(registrationDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Check if email exists
        if (userService.isEmailExists(registrationDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        try {
            User registeredUser = userService.registerUser(registrationDTO);
            
            // Generate JWT token
            String token = jwtUtil.generateToken(registeredUser.getUsername(), registeredUser.getId(), 
                registeredUser.getUserType(), registeredUser.getRole());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful");
            response.put("userId", registeredUser.getId());
            response.put("username", registeredUser.getUsername());
            response.put("role", registeredUser.getRole());
            response.put("token", token);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Handle validation errors
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle other unexpected errors
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
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
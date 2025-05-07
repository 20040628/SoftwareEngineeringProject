package group6.demo.controller;

import group6.demo.dto.FeedbackDTO;
import group6.demo.service.FeedbackService;
import group6.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://118.24.22.77"}, allowCredentials = "true")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private JwtUtil jwtUtil;

    // 创建反馈
    @PostMapping
    public ResponseEntity<?> createFeedback(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody Map<String, String> request) {
        try {
            String token = authHeader.substring(7);
            Long userId = jwtUtil.extractUserId(token);
            String content = request.get("content");

            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Content cannot be empty");
            }

            FeedbackDTO feedback = feedbackService.createFeedback(userId, content);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create feedback: " + e.getMessage());
        }
    }

    // 获取用户的所有反馈
    @GetMapping("/user")
    public ResponseEntity<?> getUserFeedbacks(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Long userId = jwtUtil.extractUserId(token);

            List<FeedbackDTO> feedbacks = feedbackService.getUserFeedbacks(userId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get feedbacks: " + e.getMessage());
        }
    }

    // 获取所有反馈（管理员）
    @GetMapping("/all")
    public ResponseEntity<?> getAllFeedbacks(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("Permission denied");
            }

            List<FeedbackDTO> feedbacks = feedbackService.getAllFeedbacks();
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get feedbacks: " + e.getMessage());
        }
    }

    // 更新反馈（管理员）
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedback(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable Long id,
                                          @RequestBody Map<String, Object> request) {
        try {
            String token = authHeader.substring(7);
            Integer role = jwtUtil.extractRole(token);

            if (role != 0) {
                return ResponseEntity.status(403).body("Permission denied");
            }

            Integer priority = request.get("priority") != null ? 
                    Integer.valueOf(request.get("priority").toString()) : null;
            String status = (String) request.get("status");
            String adminResponse = (String) request.get("adminResponse");

            FeedbackDTO feedback = feedbackService.updateFeedback(id, priority, status, adminResponse);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update feedback: " + e.getMessage());
        }
    }
} 
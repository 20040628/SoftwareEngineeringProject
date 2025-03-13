package group6.demo.service.impl;

import group6.demo.dto.FeedbackDTO;
import group6.demo.entity.Feedback;
import group6.demo.repository.FeedbackRepository;
import group6.demo.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Override
    public FeedbackDTO createFeedback(Long userId, String content) {
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setContent(content);
        feedback.setCreateTime(LocalDateTime.now());
        feedback.setStatus("pending");
        feedback.setPriority(0);
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return convertToDTO(savedFeedback);
    }
    
    @Override
    public FeedbackDTO updateFeedback(Long id, Integer priority, String status, String adminResponse) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));
        
        if (priority != null) {
            feedback.setPriority(priority);
        }
        if (status != null) {
            feedback.setStatus(status);
        }
        if (adminResponse != null) {
            feedback.setAdminResponse(adminResponse);
            feedback.setResponseTime(LocalDateTime.now());
        }
        
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return convertToDTO(updatedFeedback);
    }
    
    @Override
    public List<FeedbackDTO> getUserFeedbacks(Long userId) {
        return feedbackRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedbackDTO> getAllFeedbacks() {
        return feedbackRepository.findAllByOrderByPriorityDescCreateTimeDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // convert the entity to DTO
    private FeedbackDTO convertToDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setUserId(feedback.getUserId());
        dto.setContent(feedback.getContent());
        dto.setCreateTime(feedback.getCreateTime());
        dto.setStatus(feedback.getStatus());
        dto.setPriority(feedback.getPriority());
        dto.setAdminResponse(feedback.getAdminResponse());
        dto.setResponseTime(feedback.getResponseTime());
        return dto;
    }
} 
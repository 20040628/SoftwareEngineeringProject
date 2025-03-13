package group6.demo.service;

import group6.demo.dto.FeedbackDTO;
import java.util.List;

public interface FeedbackService {
    // create a new feedback
    FeedbackDTO createFeedback(Long userId, String content);
    
    // update the feedback priority and status (admin use)
    FeedbackDTO updateFeedback(Long id, Integer priority, String status, String adminResponse);
    
    // get all the feedbacks of a user
    List<FeedbackDTO> getUserFeedbacks(Long userId);
    
    // get all the feedbacks (admin use)
    List<FeedbackDTO> getAllFeedbacks();
} 
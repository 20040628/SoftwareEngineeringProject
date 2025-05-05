package group6.demo.service.impl;

import group6.demo.dto.FeedbackDTO;
import group6.demo.entity.Feedback;
import group6.demo.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFeedback() {
        Feedback feedback = new Feedback();
        feedback.setUserId(1L);
        feedback.setContent("Test feedback");
        feedback.setCreateTime(LocalDateTime.now());
        feedback.setStatus("pending");
        feedback.setPriority(0);

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        FeedbackDTO result = feedbackService.createFeedback(1L, "Test feedback");

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("Test feedback", result.getContent());
        assertEquals("pending", result.getStatus());
        assertEquals(0, result.getPriority());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void testUpdateFeedback() {
        Feedback feedback = new Feedback();
        feedback.setId(1L);
        feedback.setUserId(1L);
        feedback.setContent("Test feedback");
        feedback.setCreateTime(LocalDateTime.now());
        feedback.setStatus("pending");
        feedback.setPriority(0);

        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        FeedbackDTO result = feedbackService.updateFeedback(1L, 1, "resolved", "Admin response");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getPriority());
        assertEquals("resolved", result.getStatus());
        assertEquals("Admin response", result.getAdminResponse());
        assertNotNull(result.getResponseTime());
        verify(feedbackRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void testUpdateFeedback_NotFound() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> feedbackService.updateFeedback(1L, 1, "resolved", "Admin response"));
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserFeedbacks() {
        Feedback feedback1 = new Feedback();
        feedback1.setUserId(1L);
        feedback1.setContent("Feedback 1");
        Feedback feedback2 = new Feedback();
        feedback2.setUserId(1L);
        feedback2.setContent("Feedback 2");

        when(feedbackRepository.findByUserId(1L)).thenReturn(List.of(feedback1, feedback2));

        List<FeedbackDTO> result = feedbackService.getUserFeedbacks(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Feedback 1", result.get(0).getContent());
        assertEquals("Feedback 2", result.get(1).getContent());
        verify(feedbackRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetAllFeedbacks() {
        Feedback feedback1 = new Feedback();
        feedback1.setPriority(1);
        feedback1.setCreateTime(LocalDateTime.now().minusDays(1));
        Feedback feedback2 = new Feedback();
        feedback2.setPriority(2);
        feedback2.setCreateTime(LocalDateTime.now());

        when(feedbackRepository.findAllByOrderByPriorityDescCreateTimeDesc()).thenReturn(List.of(feedback2, feedback1));

        List<FeedbackDTO> result = feedbackService.getAllFeedbacks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getPriority());
        assertEquals(1, result.get(1).getPriority());
        verify(feedbackRepository, times(1)).findAllByOrderByPriorityDescCreateTimeDesc();
    }
}

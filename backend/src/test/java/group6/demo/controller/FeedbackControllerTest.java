package group6.demo.controller;

import group6.demo.dto.FeedbackDTO;
import group6.demo.service.FeedbackService;
import group6.demo.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private FeedbackController feedbackController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFeedback_Valid() {
        String token = "Bearer validToken";
        Long userId = 1L;
        String content = "This is a feedback";

        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setContent(content);

        when(jwtUtil.extractUserId("validToken")).thenReturn(userId);
        when(feedbackService.createFeedback(userId, content)).thenReturn(feedbackDTO);

        ResponseEntity<?> response = feedbackController.createFeedback(token, Map.of("content", content));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(feedbackDTO, response.getBody());
        verify(jwtUtil, times(1)).extractUserId("validToken");
        verify(feedbackService, times(1)).createFeedback(userId, content);
    }

    @Test
    void testCreateFeedback_EmptyContent() {
        String token = "Bearer validToken";
        Long userId = 1L;
        String content = "";

        when(jwtUtil.extractUserId("validToken")).thenReturn(userId);

        ResponseEntity<?> response = feedbackController.createFeedback(token, Map.of("content", content));

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Content cannot be empty", response.getBody());
        verify(jwtUtil, times(1)).extractUserId("validToken");
        verify(feedbackService, times(0)).createFeedback(anyLong(), anyString());
    }

    @Test
    void testGetUserFeedbacks_Valid() {
        String token = "Bearer validToken";
        Long userId = 1L;

        List<FeedbackDTO> feedbacks = List.of(new FeedbackDTO(), new FeedbackDTO());

        when(jwtUtil.extractUserId("validToken")).thenReturn(userId);
        when(feedbackService.getUserFeedbacks(userId)).thenReturn(feedbacks);

        ResponseEntity<?> response = feedbackController.getUserFeedbacks(token);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(feedbacks, response.getBody());
        verify(jwtUtil, times(1)).extractUserId("validToken");
        verify(feedbackService, times(1)).getUserFeedbacks(userId);
    }

    @Test
    void testGetAllFeedbacks_Valid() {
        String token = "Bearer validToken";
        Integer role = 0;

        List<FeedbackDTO> feedbacks = List.of(new FeedbackDTO(), new FeedbackDTO());

        when(jwtUtil.extractRole("validToken")).thenReturn(role);
        when(feedbackService.getAllFeedbacks()).thenReturn(feedbacks);

        ResponseEntity<?> response = feedbackController.getAllFeedbacks(token);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(feedbacks, response.getBody());
        verify(jwtUtil, times(1)).extractRole("validToken");
        verify(feedbackService, times(1)).getAllFeedbacks();
    }

    @Test
    void testGetAllFeedbacks_PermissionDenied() {
        String token = "Bearer validToken";
        Integer role = 1;

        when(jwtUtil.extractRole("validToken")).thenReturn(role);

        ResponseEntity<?> response = feedbackController.getAllFeedbacks(token);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Permission denied", response.getBody());
        verify(jwtUtil, times(1)).extractRole("validToken");
        verify(feedbackService, times(0)).getAllFeedbacks();
    }

    @Test
    void testUpdateFeedback_Valid() {
        String token = "Bearer validToken";
        Integer role = 0;
        Long feedbackId = 1L;
        Integer priority = 1;
        String status = "resolved";
        String adminResponse = "Admin response";

        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setId(feedbackId);
        feedbackDTO.setPriority(priority);
        feedbackDTO.setStatus(status);
        feedbackDTO.setAdminResponse(adminResponse);

        when(jwtUtil.extractRole("validToken")).thenReturn(role);
        when(feedbackService.updateFeedback(feedbackId, priority, status, adminResponse)).thenReturn(feedbackDTO);

        ResponseEntity<?> response = feedbackController.updateFeedback(token, feedbackId, Map.of(
                "priority", priority,
                "status", status,
                "adminResponse", adminResponse
        ));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(feedbackDTO, response.getBody());
        verify(jwtUtil, times(1)).extractRole("validToken");
        verify(feedbackService, times(1)).updateFeedback(feedbackId, priority, status, adminResponse);
    }

    @Test
    void testUpdateFeedback_PermissionDenied() {
        String token = "Bearer validToken";
        Integer role = 1;
        Long feedbackId = 1L;
        Integer priority = 1;
        String status = "resolved";
        String adminResponse = "Admin response";

        when(jwtUtil.extractRole("validToken")).thenReturn(role);

        ResponseEntity<?> response = feedbackController.updateFeedback(token, feedbackId, Map.of(
                "priority", priority,
                "status", status,
                "adminResponse", adminResponse
        ));

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Permission denied", response.getBody());
        verify(jwtUtil, times(1)).extractRole("validToken");
        verify(feedbackService, times(0)).updateFeedback(anyLong(), anyInt(), anyString(), anyString());
    }
}

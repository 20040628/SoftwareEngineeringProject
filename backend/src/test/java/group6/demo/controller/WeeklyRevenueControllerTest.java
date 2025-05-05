package group6.demo.controller;

import group6.demo.dto.DailyRevenueDTO;
import group6.demo.dto.WeeklyRevenueDTO;
import group6.demo.service.WeeklyRevenueService;
import group6.demo.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WeeklyRevenueControllerTest {

    @Mock
    private WeeklyRevenueService weeklyRevenueService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private WeeklyRevenueController weeklyRevenueController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentWeekRevenue_ValidToken() {
        String token = "Bearer validToken";
        Integer role = 0;
        WeeklyRevenueDTO revenueDTO = new WeeklyRevenueDTO();

        when(jwtUtil.extractRole("validToken")).thenReturn(role);
        when(weeklyRevenueService.generateCurrentWeekRevenue()).thenReturn(revenueDTO);

        ResponseEntity<?> response = weeklyRevenueController.getCurrentWeekRevenue(token);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(revenueDTO, response.getBody());
        verify(jwtUtil, times(1)).extractRole("validToken");
        verify(weeklyRevenueService, times(1)).generateCurrentWeekRevenue();
    }

    @Test
    void testGetCurrentWeekRevenue_InvalidToken() {
        String token = "Bearer invalidToken";

        when(jwtUtil.extractRole("invalidToken")).thenThrow(new io.jsonwebtoken.JwtException("invalid token"));

        ResponseEntity<?> response = weeklyRevenueController.getCurrentWeekRevenue(token);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("invalid token: invalid token", response.getBody());
        verify(jwtUtil, times(1)).extractRole("invalidToken");
        verify(weeklyRevenueService, times(0)).generateCurrentWeekRevenue();
    }

    @Test
    void testGetWeeklyRevenueByDate_ValidToken() {
        String token = "Bearer validToken";
        Integer role = 0;
        Date date = new Date();
        WeeklyRevenueDTO revenueDTO = new WeeklyRevenueDTO();

        when(jwtUtil.extractRole("validToken")).thenReturn(role);
        when(weeklyRevenueService.getWeeklyRevenueByDate(date)).thenReturn(revenueDTO);

        ResponseEntity<?> response = weeklyRevenueController.getWeeklyRevenueByDate(token, date);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(revenueDTO, response.getBody());
        verify(jwtUtil, times(1)).extractRole("validToken");
        verify(weeklyRevenueService, times(1)).getWeeklyRevenueByDate(date);
    }

    @Test
    void testGetWeeklyRevenueByDate_InvalidToken() {
        String token = "Bearer invalidToken";
        Date date = new Date();

        when(jwtUtil.extractRole("invalidToken")).thenThrow(new io.jsonwebtoken.JwtException("invalid token"));

        ResponseEntity<?> response = weeklyRevenueController.getWeeklyRevenueByDate(token, date);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("invalid token: invalid token", response.getBody());
        verify(jwtUtil, times(1)).extractRole("invalidToken");
        verify(weeklyRevenueService, times(0)).getWeeklyRevenueByDate(any(Date.class));
    }

    @Test
    void testGetRecentWeeklyRevenues_ValidToken() {
        String token = "Bearer validToken";
        Integer role = 0;
        int count = 4;
        List<WeeklyRevenueDTO> revenues = List.of(new WeeklyRevenueDTO(), new WeeklyRevenueDTO());

        when(jwtUtil.extractRole("validToken")).thenReturn(role);
        when(weeklyRevenueService.getRecentWeeklyRevenues(count)).thenReturn(revenues);

        ResponseEntity<?> response = weeklyRevenueController.getRecentWeeklyRevenues(token, count);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(revenues, response.getBody());
        verify(jwtUtil, times(1)).extractRole("validToken");
        verify(weeklyRevenueService, times(1)).getRecentWeeklyRevenues(count);
    }

    @Test
    void testGetRecentWeeklyRevenues_InvalidToken() {
        String token = "Bearer invalidToken";
        int count = 4;

        when(jwtUtil.extractRole("invalidToken")).thenThrow(new io.jsonwebtoken.JwtException("invalid token"));

        ResponseEntity<?> response = weeklyRevenueController.getRecentWeeklyRevenues(token, count);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("invalid token: invalid token", response.getBody());
        verify(jwtUtil, times(1)).extractRole("invalidToken");
        verify(weeklyRevenueService, times(0)).getRecentWeeklyRevenues(anyInt());
    }

    @Test
    void testGetWeeklyRevenuesByDateRange_ValidToken() {
        String token = "Bearer validToken";
        Integer role = 0;
        Date startDate = new Date();
        Date endDate = new Date();
        List<WeeklyRevenueDTO> revenues = List.of(new WeeklyRevenueDTO(), new WeeklyRevenueDTO());

        when(jwtUtil.extractRole("validToken")).thenReturn(role);
        when(weeklyRevenueService.getWeeklyRevenuesByDateRange(startDate, endDate)).thenReturn(revenues);

        ResponseEntity<?> response = weeklyRevenueController.getWeeklyRevenuesByDateRange(token, startDate, endDate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(revenues, response.getBody());
        verify(jwtUtil, times(1)).extractRole("validToken");
        verify(weeklyRevenueService, times(1)).getWeeklyRevenuesByDateRange(startDate, endDate);
    }

    @Test
    void testGetWeeklyRevenuesByDateRange_InvalidToken() {
        String token = "Bearer invalidToken";
        Date startDate = new Date();
        Date endDate = new Date();

        when(jwtUtil.extractRole("invalidToken")).thenThrow(new io.jsonwebtoken.JwtException("invalid token"));

        ResponseEntity<?> response = weeklyRevenueController.getWeeklyRevenuesByDateRange(token, startDate, endDate);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("invalid token: invalid token", response.getBody());
        verify(jwtUtil, times(1)).extractRole("invalidToken");
        verify(weeklyRevenueService, times(0)).getWeeklyRevenuesByDateRange(any(Date.class), any(Date.class));
    }

    @Test
    void testUpdateWeeklyRevenue_ValidToken() {
        String token = "Bearer validToken";
        Integer role = 0;

        when(jwtUtil.extractRole("validToken")).thenReturn(role);
        doNothing().when(weeklyRevenueService).updateWeeklyRevenue();

        ResponseEntity<?> response = weeklyRevenueController.updateWeeklyRevenue(token);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("revenue statistics updated successfully", response.getBody());
        verify(jwtUtil, times(1)).extractRole("validToken");
        verify(weeklyRevenueService, times(1)).updateWeeklyRevenue();
    }

    @Test
    void testUpdateWeeklyRevenue_InvalidToken() {
        String token = "Bearer invalidToken";

        when(jwtUtil.extractRole("invalidToken")).thenThrow(new io.jsonwebtoken.JwtException("invalid token"));

        ResponseEntity<?> response = weeklyRevenueController.updateWeeklyRevenue(token);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("invalid token: invalid token", response.getBody());
        verify(jwtUtil, times(1)).extractRole("invalidToken");
        verify(weeklyRevenueService, times(0)).updateWeeklyRevenue();
    }

    @Test
    void testGetDailyRevenuesInWeek_ValidToken() {
        String token = "Bearer validToken";
        Integer role = 0;
        Date weekStartDate = new Date();
        List<DailyRevenueDTO> dailyRevenues = List.of(new DailyRevenueDTO(), new DailyRevenueDTO());

        when(jwtUtil.extractRole("validToken")).thenReturn(role);
        when(weeklyRevenueService.getDailyRevenuesInWeek(weekStartDate)).thenReturn(dailyRevenues);

        ResponseEntity<?> response = weeklyRevenueController.getDailyRevenuesInWeek(token, weekStartDate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dailyRevenues, response.getBody());
        verify(jwtUtil, times(1)).extractRole("validToken");
        verify(weeklyRevenueService, times(1)).getDailyRevenuesInWeek(weekStartDate);
    }

    @Test
    void testGetDailyRevenuesInWeek_InvalidToken() {
        String token = "Bearer invalidToken";
        Date weekStartDate = new Date();

        when(jwtUtil.extractRole("invalidToken")).thenThrow(new io.jsonwebtoken.JwtException("invalid token"));

        ResponseEntity<?> response = weeklyRevenueController.getDailyRevenuesInWeek(token, weekStartDate);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("无效的令牌: invalid token", response.getBody());
        verify(jwtUtil, times(1)).extractRole("invalidToken");
        verify(weeklyRevenueService, times(0)).getDailyRevenuesInWeek(any(Date.class));
    }
}

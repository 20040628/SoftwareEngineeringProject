package group6.demo.service.impl;

import group6.demo.dto.WeeklyRevenueDTO;
import group6.demo.entity.Order;
import group6.demo.entity.WeeklyRevenue;
import group6.demo.repository.DailyRevenueRepository;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.WeeklyRevenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeeklyRevenueServiceImplTest {

    @InjectMocks
    private WeeklyRevenueServiceImpl weeklyRevenueService;

    @Mock
    private WeeklyRevenueRepository weeklyRevenueRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DailyRevenueRepository dailyRevenueRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCurrentWeekRevenue_whenRecordExists() {
        Date now = new Date();
        WeeklyRevenue mockRevenue = new WeeklyRevenue();
        mockRevenue.setWeekStartDate(now);
        mockRevenue.setWeekEndDate(now);
        mockRevenue.setHourlyRevenue(BigDecimal.TEN);
        mockRevenue.setFourHoursRevenue(BigDecimal.ZERO);
        mockRevenue.setDailyRevenue(BigDecimal.ZERO);
        mockRevenue.setWeeklyRevenue(BigDecimal.ZERO);
        mockRevenue.setTotalRevenue(BigDecimal.TEN);
        mockRevenue.setOrdersCount(1);

        when(weeklyRevenueRepository.findByWeekStartDate(any(Date.class))).thenReturn(Optional.of(mockRevenue));

        WeeklyRevenueDTO dto = weeklyRevenueService.generateCurrentWeekRevenue();

        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getHourlyRevenue());
        verify(weeklyRevenueRepository, times(1)).findByWeekStartDate(any(Date.class));
    }

    @Test
    void testGetWeeklyRevenueByDate_futureDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        Date futureDate = calendar.getTime();

        when(weeklyRevenueRepository.findByDate(futureDate)).thenReturn(Optional.empty());

        WeeklyRevenueDTO dto = weeklyRevenueService.getWeeklyRevenueByDate(futureDate);

        assertNotNull(dto);
        assertEquals(BigDecimal.ZERO, dto.getTotalRevenue());
        assertEquals(0, dto.getOrdersCount());
    }

    @Test
    void testGetRecentWeeklyRevenues() {
        List<WeeklyRevenue> mockList = new ArrayList<>();
        WeeklyRevenue mockRevenue = new WeeklyRevenue();
        mockRevenue.setWeekStartDate(new Date());
        mockRevenue.setTotalRevenue(BigDecimal.TEN);
        mockList.add(mockRevenue);

        when(weeklyRevenueRepository.findRecentWeeklyRevenues(PageRequest.of(0, 1)))
                .thenReturn(mockList);

        List<WeeklyRevenueDTO> result = weeklyRevenueService.getRecentWeeklyRevenues(1);

        assertEquals(1, result.size());
        assertEquals(BigDecimal.TEN, result.get(0).getTotalRevenue());
    }

    @Test
    void testGetWeeklyRevenuesByDateRange() {
        Date startDate = new Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000L);
        Date endDate = new Date();

        WeeklyRevenue revenue = new WeeklyRevenue();
        revenue.setWeekStartDate(startDate);
        revenue.setTotalRevenue(BigDecimal.TEN);

        when(weeklyRevenueRepository.findByWeekStartDateBetweenOrderByWeekStartDateDesc(
                eq(startDate), eq(endDate)))
                .thenReturn(Collections.singletonList(revenue));

        List<WeeklyRevenueDTO> dtos = weeklyRevenueService.getWeeklyRevenuesByDateRange(startDate, endDate);

        assertEquals(1, dtos.size());
        assertEquals(BigDecimal.TEN, dtos.get(0).getTotalRevenue());
    }

    @Test
    void testUpdateWeeklyRevenue_createsNewIfNotExists() {
        when(weeklyRevenueRepository.findByWeekStartDate(any(Date.class)))
                .thenReturn(Optional.empty());
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        weeklyRevenueService.updateWeeklyRevenue();

        verify(weeklyRevenueRepository).save(any(WeeklyRevenue.class));
    }

    @Test
    void testUpdateWeeklyRevenue_updatesIfExists() {
        WeeklyRevenue existingRevenue = new WeeklyRevenue();
        existingRevenue.setWeekStartDate(new Date());
        existingRevenue.setWeekEndDate(new Date());

        when(weeklyRevenueRepository.findByWeekStartDate(any(Date.class)))
                .thenReturn(Optional.of(existingRevenue));
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        weeklyRevenueService.updateWeeklyRevenue();

        verify(weeklyRevenueRepository).save(existingRevenue);
    }
}

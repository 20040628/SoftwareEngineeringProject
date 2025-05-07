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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeeklyRevenueServiceImplTest {

    @InjectMocks
    private WeeklyRevenueServiceImpl weeklyRevenueService;

    @Mock
    private WeeklyRevenueRepository weeklyRevenueRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DailyRevenueRepository dailyRevenueRepository;
    
    @Captor
    private ArgumentCaptor<WeeklyRevenue> weeklyRevenueCaptor;

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

    @Test
    void testCalculateWeeklyRevenue_WithCompletedOrders() {
        // Setup test data
        WeeklyRevenue weeklyRevenue = new WeeklyRevenue();
        weeklyRevenue.setWeekStartDate(new Date());
        weeklyRevenue.setWeekEndDate(new Date());
        
        List<Order> testOrders = new ArrayList<>();
        
        // Create test orders with different hire periods
        Order hourlyOrder = new Order();
        hourlyOrder.setStatus(4); // Completed
        hourlyOrder.setStartTime(new Date());
        hourlyOrder.setHirePeriod("HOUR");
        hourlyOrder.setPrice(BigDecimal.TEN);
        testOrders.add(hourlyOrder);
        
        Order fourHoursOrder = new Order();
        fourHoursOrder.setStatus(4); // Completed
        fourHoursOrder.setStartTime(new Date());
        fourHoursOrder.setHirePeriod("FOUR_HOURS");
        fourHoursOrder.setPrice(BigDecimal.valueOf(20));
        testOrders.add(fourHoursOrder);
        
        Order dailyOrder = new Order();
        dailyOrder.setStatus(4); // Completed
        dailyOrder.setStartTime(new Date());
        dailyOrder.setHirePeriod("DAY");
        dailyOrder.setPrice(BigDecimal.valueOf(30));
        testOrders.add(dailyOrder);
        
        Order weeklyOrder = new Order();
        weeklyOrder.setStatus(4); // Completed
        weeklyOrder.setStartTime(new Date());
        weeklyOrder.setHirePeriod("WEEK");
        weeklyOrder.setPrice(BigDecimal.valueOf(100));
        testOrders.add(weeklyOrder);
        
        // Create an order with invalid status
        Order invalidStatusOrder = new Order();
        invalidStatusOrder.setStatus(3); // Active, not completed
        invalidStatusOrder.setStartTime(new Date());
        invalidStatusOrder.setHirePeriod("HOUR");
        invalidStatusOrder.setPrice(BigDecimal.valueOf(5));
        testOrders.add(invalidStatusOrder);
        
        when(orderRepository.findAll()).thenReturn(testOrders);
        when(weeklyRevenueRepository.save(any(WeeklyRevenue.class))).thenReturn(weeklyRevenue);
        
        // Call the method
        weeklyRevenueService.updateWeeklyRevenue();
        
        // Verify the revenue calculation
        verify(weeklyRevenueRepository).save(weeklyRevenueCaptor.capture());
        WeeklyRevenue savedRevenue = weeklyRevenueCaptor.getValue();
        
        // Assert the values (only completed orders with status 4 should be counted)
        assertEquals(BigDecimal.TEN, savedRevenue.getHourlyRevenue());
        assertEquals(BigDecimal.valueOf(20), savedRevenue.getFourHoursRevenue());
        assertEquals(BigDecimal.valueOf(30), savedRevenue.getDailyRevenue());
        assertEquals(BigDecimal.valueOf(100), savedRevenue.getWeeklyRevenue());
        assertEquals(BigDecimal.valueOf(160), savedRevenue.getTotalRevenue()); // 10 + 20 + 30 + 100
        assertEquals(4, savedRevenue.getOrdersCount()); // 5 orders, but 1 is invalid
    }
}

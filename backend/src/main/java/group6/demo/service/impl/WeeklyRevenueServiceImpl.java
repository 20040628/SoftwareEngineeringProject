package group6.demo.service.impl;

import group6.demo.dto.WeeklyRevenueDTO;
import group6.demo.entity.Order;
import group6.demo.entity.WeeklyRevenue;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.WeeklyRevenueRepository;
import group6.demo.service.WeeklyRevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeeklyRevenueServiceImpl implements WeeklyRevenueService {

    @Autowired
    private WeeklyRevenueRepository weeklyRevenueRepository;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * translate entity object to DTO
     */
    private WeeklyRevenueDTO convertToDTO(WeeklyRevenue weeklyRevenue) {
        if (weeklyRevenue == null) {
            return null;
        }
        
        WeeklyRevenueDTO dto = new WeeklyRevenueDTO();
        dto.setId(weeklyRevenue.getId());
        dto.setWeekStartDate(weeklyRevenue.getWeekStartDate());
        dto.setWeekEndDate(weeklyRevenue.getWeekEndDate());
        dto.setHourlyRevenue(weeklyRevenue.getHourlyRevenue());
        dto.setFourHoursRevenue(weeklyRevenue.getFourHoursRevenue());
        dto.setDailyRevenue(weeklyRevenue.getDailyRevenue());
        dto.setWeeklyRevenue(weeklyRevenue.getWeeklyRevenue());
        dto.setTotalRevenue(weeklyRevenue.getTotalRevenue());
        dto.setOrdersCount(weeklyRevenue.getOrdersCount());
        
        return dto;
    }

    /**
     * get the start date of current week (Monday 00:00:00)
     */
    private Date getCurrentWeekStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * get the end date of current week (Sunday 23:59:59)
     */
    private Date getCurrentWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(Calendar.WEEK_OF_YEAR, 1); // because Calendar.SUNDAY is the first day of the week
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * get the start date of the week that the date belongs to (Monday 00:00:00)
     */
    private Date getWeekStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * get the end date of the week that the date belongs to (Sunday 23:59:59)
     */
    private Date getWeekEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(Calendar.WEEK_OF_YEAR, 1); // because Calendar.SUNDAY is the first day of the week
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    @Override
    public WeeklyRevenueDTO generateCurrentWeekRevenue() {
        Date weekStartDate = getCurrentWeekStartDate();
        Date weekEndDate = getCurrentWeekEndDate();
        
        // find if there is a record of the current week
        Optional<WeeklyRevenue> existingWeeklyRevenue = weeklyRevenueRepository.findByWeekStartDate(weekStartDate);
        
        // if there is a record, then return it
        if (existingWeeklyRevenue.isPresent()) {
            return convertToDTO(existingWeeklyRevenue.get());
        }
        
        // if there is no record, then create a new one
        return createWeeklyRevenue(weekStartDate, weekEndDate);
    }

    /**
     * create the weekly revenue statistics
     */
    private WeeklyRevenueDTO createWeeklyRevenue(Date weekStartDate, Date weekEndDate) {
        // query all the completed orders in the specified week (status=2 means completed)
        // use start_time as the criterion
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getStatus() == 2 && 
                        order.getStartTime().after(weekStartDate) && 
                        order.getStartTime().before(weekEndDate))
                .collect(Collectors.toList());
        
        // count the revenue by the hire period
        BigDecimal hourlyRevenue = BigDecimal.ZERO;
        BigDecimal fourHoursRevenue = BigDecimal.ZERO;
        BigDecimal dailyRevenue = BigDecimal.ZERO;
        BigDecimal weeklyRevenue = BigDecimal.ZERO;
        
        for (Order order : orders) {
            switch (order.getHirePeriod()) {
                case "HOUR":
                    hourlyRevenue = hourlyRevenue.add(order.getPrice());
                    break;
                case "FOUR_HOURS":
                    fourHoursRevenue = fourHoursRevenue.add(order.getPrice());
                    break;
                case "DAY":
                    dailyRevenue = dailyRevenue.add(order.getPrice());
                    break;
                case "WEEK":
                    weeklyRevenue = weeklyRevenue.add(order.getPrice());
                    break;
                default:
                    // ignore the unsupported hire period
                    break;
            }
        }
        
        // calculate the total revenue
        BigDecimal totalRevenue = hourlyRevenue.add(fourHoursRevenue).add(dailyRevenue).add(weeklyRevenue);
        
        // create and save the weekly revenue record
        WeeklyRevenue revenueRecord = new WeeklyRevenue();
        revenueRecord.setWeekStartDate(weekStartDate);
        revenueRecord.setWeekEndDate(weekEndDate);
        revenueRecord.setHourlyRevenue(hourlyRevenue);
        revenueRecord.setFourHoursRevenue(fourHoursRevenue);
        revenueRecord.setDailyRevenue(dailyRevenue);
        revenueRecord.setWeeklyRevenue(weeklyRevenue);
        revenueRecord.setTotalRevenue(totalRevenue);
        revenueRecord.setOrdersCount(orders.size());
        revenueRecord.setCreatedAt(new Date());
        revenueRecord.setUpdatedAt(new Date());
        
        WeeklyRevenue savedWeeklyRevenue = weeklyRevenueRepository.save(revenueRecord);
        
        return convertToDTO(savedWeeklyRevenue);
    }

    @Override
    public WeeklyRevenueDTO getWeeklyRevenueByDate(Date date) {
        // find if there is a record of the week that the date belongs to
        Optional<WeeklyRevenue> weeklyRevenue = weeklyRevenueRepository.findByDate(date);
        
        if (weeklyRevenue.isPresent()) {
            return convertToDTO(weeklyRevenue.get());
        }
        
        // if there is no record, then create a new one
        Date weekStartDate = getWeekStartDate(date);
        Date weekEndDate = getWeekEndDate(date);
        
        return createWeeklyRevenue(weekStartDate, weekEndDate);
    }

    @Override
    public List<WeeklyRevenueDTO> getRecentWeeklyRevenues(int count) {
        List<WeeklyRevenue> recentRevenues = weeklyRevenueRepository.findRecentWeeklyRevenues(PageRequest.of(0, count));
        
        return recentRevenues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WeeklyRevenueDTO> getWeeklyRevenuesByDateRange(Date startDate, Date endDate) {
        List<WeeklyRevenue> revenues = weeklyRevenueRepository.findByWeekStartDateBetweenOrderByWeekStartDateDesc(startDate, endDate);
        
        return revenues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "0 0 0 * * MON") // execute every Monday at 00:00:00
    public void updateWeeklyRevenue() {
        // get the date range of last week
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        
        Date lastWeekDate = calendar.getTime();
        Date weekStartDate = getWeekStartDate(lastWeekDate);
        Date weekEndDate = getWeekEndDate(lastWeekDate);
        
        // find if there is a record of the current week
        Optional<WeeklyRevenue> existingWeeklyRevenue = weeklyRevenueRepository.findByWeekStartDate(weekStartDate);
        
        if (existingWeeklyRevenue.isPresent()) {
            // update the existing record
            updateExistingWeeklyRevenue(existingWeeklyRevenue.get());
        } else {
            // create a new record
            createWeeklyRevenue(weekStartDate, weekEndDate);
        }
    }

    /**
     * update the existing weekly revenue record
     */
    private void updateExistingWeeklyRevenue(WeeklyRevenue revenue) {
        Date weekStartDate = revenue.getWeekStartDate();
        Date weekEndDate = revenue.getWeekEndDate();
        
        // query all the completed orders in the specified week
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getStatus() == 2 && 
                        order.getStartTime().after(weekStartDate) && 
                        order.getStartTime().before(weekEndDate))
                .collect(Collectors.toList());
        
        // count the revenue by the hire period
        BigDecimal hourlyRevenue = BigDecimal.ZERO;
        BigDecimal fourHoursRevenue = BigDecimal.ZERO;
        BigDecimal dailyRevenue = BigDecimal.ZERO;
        BigDecimal weeklyRevenue = BigDecimal.ZERO;
        
        for (Order order : orders) {
            switch (order.getHirePeriod()) {
                case "HOUR":
                    hourlyRevenue = hourlyRevenue.add(order.getPrice());
                    break;
                case "FOUR_HOURS":
                    fourHoursRevenue = fourHoursRevenue.add(order.getPrice());
                    break;
                case "DAY":
                    dailyRevenue = dailyRevenue.add(order.getPrice());
                    break;
                case "WEEK":
                    weeklyRevenue = weeklyRevenue.add(order.getPrice());
                    break;
                default:
                    // ignore the unsupported hire period
                    break;
            }
        }
        
        // calculate the total revenue
        BigDecimal totalRevenue = hourlyRevenue.add(fourHoursRevenue).add(dailyRevenue).add(weeklyRevenue);
        
        // update the record
        revenue.setHourlyRevenue(hourlyRevenue);
        revenue.setFourHoursRevenue(fourHoursRevenue);
        revenue.setDailyRevenue(dailyRevenue);
        revenue.setWeeklyRevenue(weeklyRevenue);
        revenue.setTotalRevenue(totalRevenue);
        revenue.setOrdersCount(orders.size());
        revenue.setUpdatedAt(new Date());
        
        weeklyRevenueRepository.save(revenue);
    }
} 
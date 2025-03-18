package group6.demo.service;

import group6.demo.dto.WeeklyRevenueDTO;
import group6.demo.entity.WeeklyRevenue;

import java.util.Date;
import java.util.List;

public interface WeeklyRevenueService {
    
    /**
     * generate the current week revenue statistics
     * @return the current week revenue statistics data
     */
    WeeklyRevenueDTO generateCurrentWeekRevenue();
    
    /**
     * get the weekly revenue statistics by date
     * @param date any date
     * @return the weekly revenue statistics of the date
     */
    WeeklyRevenueDTO getWeeklyRevenueByDate(Date date);
    
    /**
     * get the recent n weeks revenue statistics
     * @param count the number of weeks
     * @return the list of the recent n weeks revenue statistics
     */
    List<WeeklyRevenueDTO> getRecentWeeklyRevenues(int count);
    
    /**
     * get the weekly revenue statistics by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return the list of the weekly revenue statistics of the date range
     */
    List<WeeklyRevenueDTO> getWeeklyRevenuesByDateRange(Date startDate, Date endDate);
    
    /**
     * update the weekly revenue statistics (scheduled task)
     */
    void updateWeeklyRevenue();
} 
package group6.demo.service;

import group6.demo.dto.DailyRevenueDTO;
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
    
    /**
     * 获取指定周内每日的收入统计
     * @param weekStartDate 周开始日期
     * @return 包含7天收入详情的列表
     */
    List<DailyRevenueDTO> getDailyRevenuesInWeek(Date weekStartDate);
    
    /**
     * 生成指定日期的每日收入统计并保存到数据库
     * @param date 日期
     * @return 每日收入统计DTO
     */
    DailyRevenueDTO generateAndSaveDailyRevenue(Date date);
    
    /**
     * 获取指定日期范围内的每日收入统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每日收入统计列表
     */
    List<DailyRevenueDTO> getDailyRevenuesByDateRange(Date startDate, Date endDate);
    
    /**
     * 获取最近n天的每日收入统计
     * @param days 天数
     * @return 每日收入统计列表
     */
    List<DailyRevenueDTO> getRecentDailyRevenues(int days);
    
    /**
     * 更新每日收入统计（定时任务）
     * 计算前一天的收入统计并保存
     */
    void updateDailyRevenue();
} 
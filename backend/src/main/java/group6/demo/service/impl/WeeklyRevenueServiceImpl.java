package group6.demo.service.impl;

import group6.demo.dto.DailyRevenueDTO;
import group6.demo.dto.WeeklyRevenueDTO;
import group6.demo.entity.DailyRevenue;
import group6.demo.entity.Order;
import group6.demo.entity.WeeklyRevenue;
import group6.demo.repository.DailyRevenueRepository;
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
    
    @Autowired
    private DailyRevenueRepository dailyRevenueRepository;

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
        // 输出调试信息
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar currentCal = Calendar.getInstance();
        System.out.println("请求日期: " + date + ", 年份: " + cal.get(Calendar.YEAR) + 
                          ", 月份: " + (cal.get(Calendar.MONTH) + 1) + ", 日: " + cal.get(Calendar.DAY_OF_MONTH));
        System.out.println("系统当前时间: " + new Date() + ", 年份: " + currentCal.get(Calendar.YEAR) + 
                          ", 月份: " + (currentCal.get(Calendar.MONTH) + 1) + ", 日: " + currentCal.get(Calendar.DAY_OF_MONTH));
        
        // find if there is a record of the week that the date belongs to
        Optional<WeeklyRevenue> weeklyRevenue = weeklyRevenueRepository.findByDate(date);
        
        if (weeklyRevenue.isPresent()) {
            return convertToDTO(weeklyRevenue.get());
        }
        
        // 修改比较逻辑：只有当请求日期在当前日期之后超过一天时，才认为是未来日期
        Date currentDate = new Date();
        Calendar futureCal = Calendar.getInstance();
        futureCal.setTime(currentDate);
        futureCal.add(Calendar.DAY_OF_MONTH, 1); // 添加一天的缓冲期
        Date futureDate = futureCal.getTime();
        
        if (date.after(futureDate)) {
            System.out.println("检测到未来日期：请求日期比当前日期晚超过1天");
            // 如果是未来日期，返回一个空的收入记录（零值），而不是尝试创建新记录
            WeeklyRevenueDTO emptyDto = new WeeklyRevenueDTO();
            Date weekStartDate = getWeekStartDate(date);
            Date weekEndDate = getWeekEndDate(date);
            
            emptyDto.setWeekStartDate(weekStartDate);
            emptyDto.setWeekEndDate(weekEndDate);
            emptyDto.setHourlyRevenue(BigDecimal.ZERO);
            emptyDto.setFourHoursRevenue(BigDecimal.ZERO);
            emptyDto.setDailyRevenue(BigDecimal.ZERO);
            emptyDto.setWeeklyRevenue(BigDecimal.ZERO);
            emptyDto.setTotalRevenue(BigDecimal.ZERO);
            emptyDto.setOrdersCount(0);
            
            return emptyDto;
        }
        
        // 如果是历史日期或当前日期，且记录不存在，则创建新记录
        System.out.println("创建历史日期的周收入记录");
        Date weekStartDate = getWeekStartDate(date);
        Date weekEndDate = getWeekEndDate(date);
        
        try {
            return createWeeklyRevenue(weekStartDate, weekEndDate);
        } catch (Exception e) {
            System.out.println("创建周收入记录失败：" + e.getMessage());
            // 如果创建失败，返回空记录
            WeeklyRevenueDTO emptyDto = new WeeklyRevenueDTO();
            emptyDto.setWeekStartDate(weekStartDate);
            emptyDto.setWeekEndDate(weekEndDate);
            emptyDto.setHourlyRevenue(BigDecimal.ZERO);
            emptyDto.setFourHoursRevenue(BigDecimal.ZERO);
            emptyDto.setDailyRevenue(BigDecimal.ZERO);
            emptyDto.setWeeklyRevenue(BigDecimal.ZERO);
            emptyDto.setTotalRevenue(BigDecimal.ZERO);
            emptyDto.setOrdersCount(0);
            
            return emptyDto;
        }
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

    /**
     * 将DailyRevenue实体转换为DTO
     */
    private DailyRevenueDTO convertToDailyRevenueDTO(DailyRevenue dailyRevenue) {
        if (dailyRevenue == null) {
            return null;
        }
        
        DailyRevenueDTO dto = new DailyRevenueDTO();
        dto.setDate(dailyRevenue.getRevenueDate());
        dto.setDayOfWeek(dailyRevenue.getDayOfWeek());
        dto.setDayOfWeekName(dailyRevenue.getDayOfWeekName());
        dto.setHourlyRevenue(dailyRevenue.getHourlyRevenue());
        dto.setFourHoursRevenue(dailyRevenue.getFourHoursRevenue());
        dto.setDailyRevenue(dailyRevenue.getDailyRevenue());
        dto.setWeeklyRevenue(dailyRevenue.getWeeklyRevenue());
        dto.setTotalRevenue(dailyRevenue.getTotalRevenue());
        dto.setOrdersCount(dailyRevenue.getOrdersCount());
        dto.setTotalDiscount(dailyRevenue.getTotalDiscount());
        
        return dto;
    }
    
    /**
     * 将DailyRevenueDTO转换为实体
     */
    private DailyRevenue convertToDailyRevenueEntity(DailyRevenueDTO dto) {
        if (dto == null) {
            return null;
        }
        
        DailyRevenue entity = new DailyRevenue();
        entity.setRevenueDate(dto.getDate());
        entity.setDayOfWeek(dto.getDayOfWeek());
        entity.setDayOfWeekName(dto.getDayOfWeekName());
        entity.setHourlyRevenue(dto.getHourlyRevenue() != null ? dto.getHourlyRevenue() : BigDecimal.ZERO);
        entity.setFourHoursRevenue(dto.getFourHoursRevenue() != null ? dto.getFourHoursRevenue() : BigDecimal.ZERO);
        entity.setDailyRevenue(dto.getDailyRevenue() != null ? dto.getDailyRevenue() : BigDecimal.ZERO);
        entity.setWeeklyRevenue(dto.getWeeklyRevenue() != null ? dto.getWeeklyRevenue() : BigDecimal.ZERO);
        entity.setTotalRevenue(dto.getTotalRevenue() != null ? dto.getTotalRevenue() : BigDecimal.ZERO);
        entity.setOrdersCount(dto.getOrdersCount() != null ? dto.getOrdersCount() : 0);
        entity.setTotalDiscount(dto.getTotalDiscount() != null ? dto.getTotalDiscount() : BigDecimal.ZERO);
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        
        return entity;
    }
    
    /**
     * 计算指定日期的收入并返回DTO
     */
    private DailyRevenueDTO calculateDailyRevenue(Date date) {
        // 创建新的DTO对象
        DailyRevenueDTO dailyRevenue = new DailyRevenueDTO();
        dailyRevenue.setDate(date);
        
        // 设置星期几 (1-7, 1表示周一)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // 将星期日(1)调整为7，其他天数减1
        dayOfWeek = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
        dailyRevenue.setDayOfWeek(dayOfWeek);
        
        // 设置星期几的名称
        String[] dayNames = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        dailyRevenue.setDayOfWeekName(dayNames[dayOfWeek - 1]);
        
        // 查询按小时租赁的收入
        BigDecimal hourlyRevenue = orderRepository.getHourlyRevenueByDate(date);
        dailyRevenue.setHourlyRevenue(hourlyRevenue != null ? hourlyRevenue : BigDecimal.ZERO);
        
        // 查询按4小时租赁的收入
        BigDecimal fourHoursRevenue = orderRepository.getFourHoursRevenueByDate(date);
        dailyRevenue.setFourHoursRevenue(fourHoursRevenue != null ? fourHoursRevenue : BigDecimal.ZERO);
        
        // 查询按天租赁的收入
        BigDecimal dailyRevenueAmount = orderRepository.getDailyRevenueByDate(date);
        dailyRevenue.setDailyRevenue(dailyRevenueAmount != null ? dailyRevenueAmount : BigDecimal.ZERO);
        
        // 查询按周租赁的收入
        BigDecimal weeklyRevenue = orderRepository.getWeeklyRevenueByDate(date);
        dailyRevenue.setWeeklyRevenue(weeklyRevenue != null ? weeklyRevenue : BigDecimal.ZERO);
        
        // 计算当天总收入
        BigDecimal totalRevenue = orderRepository.getTotalRevenueByDate(date);
        dailyRevenue.setTotalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        
        // 查询折扣总额
        BigDecimal totalDiscount = orderRepository.getTotalDiscountByDate(date);
        dailyRevenue.setTotalDiscount(totalDiscount != null ? totalDiscount : BigDecimal.ZERO);
        
        // 查询订单数量
        Integer ordersCount = orderRepository.getOrdersCountByDate(date);
        dailyRevenue.setOrdersCount(ordersCount != null ? ordersCount : 0);
        
        return dailyRevenue;
    }

    @Override
    public DailyRevenueDTO generateAndSaveDailyRevenue(Date date) {
        // 先查找是否已存在该日期的记录
        Optional<DailyRevenue> existingRevenue = dailyRevenueRepository.findByRevenueDate(date);
        
        if (existingRevenue.isPresent()) {
            // 如果已存在，直接返回
            return convertToDailyRevenueDTO(existingRevenue.get());
        }
        
        // 计算该日期的收入数据
        DailyRevenueDTO dailyRevenueDTO = calculateDailyRevenue(date);
        
        // 转换为实体并保存
        DailyRevenue dailyRevenue = convertToDailyRevenueEntity(dailyRevenueDTO);
        DailyRevenue savedRevenue = dailyRevenueRepository.save(dailyRevenue);
        
        return convertToDailyRevenueDTO(savedRevenue);
    }
    
    @Override
    public List<DailyRevenueDTO> getDailyRevenuesByDateRange(Date startDate, Date endDate) {
        List<DailyRevenue> revenues = dailyRevenueRepository.findByRevenueDateBetweenOrderByRevenueDateDesc(startDate, endDate);
        
        return revenues.stream()
                .map(this::convertToDailyRevenueDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DailyRevenueDTO> getRecentDailyRevenues(int days) {
        List<DailyRevenue> recentRevenues = dailyRevenueRepository.findRecentDailyRevenues(PageRequest.of(0, days));
        
        return recentRevenues.stream()
                .map(this::convertToDailyRevenueDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点执行
    public void updateDailyRevenue() {
        // 获取昨天的日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();
        
        // 生成并保存昨天的收入统计
        generateAndSaveDailyRevenue(yesterday);
    }

    @Override
    public List<DailyRevenueDTO> getDailyRevenuesInWeek(Date weekStartDate) {
        // 计算周的结束日期
        Calendar calendar = Calendar.getInstance();
        Date startDate = getWeekStartDate(weekStartDate); // 确保是周一开始
        
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(startDate);
        endCalendar.add(Calendar.DAY_OF_MONTH, 6); // 设置为本周日
        Date endDate = endCalendar.getTime();
        
        // 查询数据库是否已存在该周的每日收入记录
        List<DailyRevenue> dbRevenues = dailyRevenueRepository.findDailyRevenuesInWeek(startDate, endDate);
        
        // 如果数据库中已有完整的7天数据，直接返回
        if (dbRevenues.size() == 7) {
            return dbRevenues.stream()
                .map(this::convertToDailyRevenueDTO)
                .collect(Collectors.toList());
        }
        
        // 否则需要计算并保存缺失的日期数据
        List<DailyRevenueDTO> dailyRevenues = new ArrayList<>();
        
        // 循环7天，生成每天的收入统计
        for (int i = 0; i < 7; i++) {
            // 计算当前日期
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            Date currentDate = calendar.getTime();
            
            // 查询数据库是否已存在该日期的记录
            Optional<DailyRevenue> existingRevenue = dailyRevenueRepository.findByRevenueDate(currentDate);
            
            DailyRevenueDTO dailyRevenue;
            if (existingRevenue.isPresent()) {
                // 如果已存在，直接使用数据库中的数据
                dailyRevenue = convertToDailyRevenueDTO(existingRevenue.get());
            } else {
                // 如果不存在，计算并保存到数据库
                dailyRevenue = generateAndSaveDailyRevenue(currentDate);
            }
            
            // 添加到结果列表
            dailyRevenues.add(dailyRevenue);
        }
        
        return dailyRevenues;
    }
} 
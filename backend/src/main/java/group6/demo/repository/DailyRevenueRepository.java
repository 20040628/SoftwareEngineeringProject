package group6.demo.repository;

import group6.demo.entity.DailyRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyRevenueRepository extends JpaRepository<DailyRevenue, Long> {
    
    // 根据日期查找每日收入记录
    Optional<DailyRevenue> findByRevenueDate(Date revenueDate);
    
    // 查找指定日期范围内的每日收入记录
    List<DailyRevenue> findByRevenueDateBetweenOrderByRevenueDateDesc(Date startDate, Date endDate);
    
    // 查找指定周内的每日收入记录
    @Query("SELECT d FROM DailyRevenue d WHERE d.revenueDate >= :weekStartDate AND d.revenueDate <= :weekEndDate ORDER BY d.revenueDate ASC")
    List<DailyRevenue> findDailyRevenuesInWeek(@Param("weekStartDate") Date weekStartDate, @Param("weekEndDate") Date weekEndDate);
    
    // 查找最近n天的收入记录
    @Query("SELECT d FROM DailyRevenue d ORDER BY d.revenueDate DESC")
    List<DailyRevenue> findRecentDailyRevenues(org.springframework.data.domain.Pageable pageable);
} 
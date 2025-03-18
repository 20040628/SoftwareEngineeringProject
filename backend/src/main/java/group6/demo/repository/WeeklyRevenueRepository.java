package group6.demo.repository;

import group6.demo.entity.WeeklyRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyRevenueRepository extends JpaRepository<WeeklyRevenue, Long> {
    
    // 根据起始日期查找对应的周收入记录
    Optional<WeeklyRevenue> findByWeekStartDate(Date weekStartDate);
    
    // 查找某个日期所在的周收入记录
    @Query("SELECT w FROM WeeklyRevenue w WHERE :date BETWEEN w.weekStartDate AND w.weekEndDate")
    Optional<WeeklyRevenue> findByDate(@Param("date") Date date);
    
    // 查找最近n周的收入记录
    @Query("SELECT w FROM WeeklyRevenue w ORDER BY w.weekStartDate DESC")
    List<WeeklyRevenue> findRecentWeeklyRevenues(Pageable pageable);
    
    // 查找某个日期范围内的收入记录
    List<WeeklyRevenue> findByWeekStartDateBetweenOrderByWeekStartDateDesc(Date startDate, Date endDate);
} 
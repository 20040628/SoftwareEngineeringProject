package group6.demo.repository;

import group6.demo.entity.Order;
import group6.demo.entity.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.scooter.id = :scooterId AND o.status = 1 AND o.startTime >= :startDate ORDER BY o.startTime")
    List<Order> findActiveOrdersByScooterId(@Param("scooterId") Long scooterId, @Param("startDate") Date startDate);

    @Query("SELECT o FROM Order o WHERE o.scooter.id = :scooterId AND o.status = 1 " +
           "AND ((o.startTime BETWEEN :startTime AND :endTime) " +
           "OR (o.endTime BETWEEN :startTime AND :endTime) " +
           "OR (:startTime BETWEEN o.startTime AND o.endTime))")
    List<Order> findConflictingOrders(
        @Param("scooterId") Long scooterId,
        @Param("startTime") Date startTime,
        @Param("endTime") Date endTime
    );

    List<Order> findByUser_Id(Long userId);
    
    /**
     * 查询用户在过去一周内的订单总使用时间（小时）
     * 只计算已完成的订单（status = 2）
     * @param userId 用户ID
     * @param startDate 一周前的日期
     * @param endDate 当前日期
     * @return 使用时间总和（小时）
     */
    @Query("SELECT SUM(TIMESTAMPDIFF(HOUR, o.startTime, o.endTime)) FROM Order o " +
           "WHERE o.user.id = :userId AND o.status = 2 " +
           "AND o.startTime >= :startDate AND o.endTime <= :endDate")
    Long getTotalHoursInLastWeek(
        @Param("userId") Long userId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate
    );

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.startTime > CURRENT_TIMESTAMP AND o.status != 3")
    List<Order> findAllUndo(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND CURRENT_TIMESTAMP BETWEEN o.startTime AND o.endTime AND o.status != 3")
    List<Order> findAllOngoing(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.endTime < CURRENT_TIMESTAMP AND o.status != 3")
    List<Order> findAllFinished(@Param("userId") Long userId);
    
    /**
     * 根据日期和租赁类型统计收入（按小时租赁）
     * 只计算已完成的订单（status = 2）
     * @param date 日期
     * @return 当天小时租赁总收入
     */
    @Query("SELECT SUM(o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.hirePeriod = 'HOUR' AND o.status = 2")
    BigDecimal getHourlyRevenueByDate(@Param("date") Date date);
    
    /**
     * 根据日期和租赁类型统计收入（按4小时租赁）
     * 只计算已完成的订单（status = 2）
     * @param date 日期
     * @return 当天4小时租赁总收入
     */
    @Query("SELECT SUM(o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.hirePeriod = 'FOUR_HOURS' AND o.status = 2")
    BigDecimal getFourHoursRevenueByDate(@Param("date") Date date);
    
    /**
     * 根据日期和租赁类型统计收入（按天租赁）
     * 只计算已完成的订单（status = 2）
     * @param date 日期
     * @return 当天日租赁总收入
     */
    @Query("SELECT SUM(o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.hirePeriod = 'DAY' AND o.status = 2")
    BigDecimal getDailyRevenueByDate(@Param("date") Date date);
    
    /**
     * 根据日期和租赁类型统计收入（按周租赁）
     * 只计算已完成的订单（status = 2）
     * @param date 日期
     * @return 当天周租赁总收入
     */
    @Query("SELECT SUM(o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.hirePeriod = 'WEEK' AND o.status = 2")
    BigDecimal getWeeklyRevenueByDate(@Param("date") Date date);
    
    /**
     * 根据日期统计总收入
     * 只计算已完成的订单（status = 2）
     * @param date 日期
     * @return 当天总收入
     */
    @Query("SELECT SUM(o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.status = 2")
    BigDecimal getTotalRevenueByDate(@Param("date") Date date);
    
    /**
     * 根据日期统计总折扣金额（原价-折扣价）
     * 只计算已完成的订单（status = 2）
     * @param date 日期
     * @return 当天总折扣金额
     */
    @Query("SELECT SUM(o.PriceBeforeDiscount - o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.status = 2")
    BigDecimal getTotalDiscountByDate(@Param("date") Date date);
    
    /**
     * 根据日期统计订单数量
     * 只计算已完成的订单（status = 2）
     * @param date 日期
     * @return 当天订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.status = 2")
    Integer getOrdersCountByDate(@Param("date") Date date);
} 
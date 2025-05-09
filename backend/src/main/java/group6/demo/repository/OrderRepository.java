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
    @Query("SELECT o FROM Order o WHERE o.scooter.id = :scooterId AND o.status = 2 AND o.startTime >= :startDate ORDER BY o.startTime")
    List<Order> findActiveOrdersByScooterId(@Param("scooterId") Long scooterId, @Param("startDate") Date startDate);

    @Query("SELECT o FROM Order o WHERE o.scooter.id = :scooterId AND o.status = 2 " +
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
     * Query the total usage time (hours) of a user's orders in the past week
     * Only counts completed orders (status = 3)
     * @param userId User ID
     * @param startDate Date one week ago
     * @param endDate Current date
     * @return Sum of usage time (hours)
     */
    @Query("SELECT SUM(TIMESTAMPDIFF(HOUR, o.startTime, o.endTime)) FROM Order o " +
           "WHERE o.user.id = :userId AND o.status = 3 " +
           "AND o.startTime >= :startDate AND o.endTime <= :endDate")
    Long getTotalHoursInLastWeek(
        @Param("userId") Long userId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate
    );

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.startTime > CURRENT_TIMESTAMP AND o.status IN (1, 2)")
    List<Order> findAllUndo(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND CURRENT_TIMESTAMP BETWEEN o.startTime AND o.endTime AND o.status = 2")
    List<Order> findAllOngoing(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.endTime < CURRENT_TIMESTAMP AND o.status = 3")
    List<Order> findAllFinished(@Param("userId") Long userId);
    
    /**
     * Calculate revenue by date and rental type (hourly rental)
     * Only counts completed orders (status = 3)
     * @param date Date
     * @return Total hourly rental revenue for the day
     */
    @Query("SELECT SUM(o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.hirePeriod = 'HOUR' AND o.status = 3")
    BigDecimal getHourlyRevenueByDate(@Param("date") Date date);
    
    /**
     * Calculate revenue by date and rental type (4-hour rental)
     * Only counts completed orders (status = 3)
     * @param date Date
     * @return Total 4-hour rental revenue for the day
     */
    @Query("SELECT SUM(o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.hirePeriod = 'FOUR_HOURS' AND o.status = 3")
    BigDecimal getFourHoursRevenueByDate(@Param("date") Date date);
    
    /**
     * Calculate revenue by date and rental type (daily rental)
     * Only counts completed orders (status = 3)
     * @param date Date
     * @return Total daily rental revenue for the day
     */
    @Query("SELECT SUM(o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.hirePeriod = 'DAY' AND o.status = 3")
    BigDecimal getDailyRevenueByDate(@Param("date") Date date);
    
    /**
     * Calculate revenue by date and rental type (weekly rental)
     * Only counts completed orders (status = 3)
     * @param date Date
     * @return Total weekly rental revenue for the day
     */
    @Query("SELECT SUM(o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.hirePeriod = 'WEEK' AND o.status = 3")
    BigDecimal getWeeklyRevenueByDate(@Param("date") Date date);
    
    /**
     * Calculate total revenue by date
     * Only counts completed orders (status = 3)
     * @param date Date
     * @return Total revenue for the day
     */
    @Query("SELECT SUM(o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.status = 3")
    BigDecimal getTotalRevenueByDate(@Param("date") Date date);
    
    /**
     * Calculate total discount amount by date (original price - discounted price)
     * Only counts completed orders (status = 3)
     * @param date Date
     * @return Total discount amount for the day
     */
    @Query("SELECT SUM(o.PriceBeforeDiscount - o.price) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.status = 3")
    BigDecimal getTotalDiscountByDate(@Param("date") Date date);
    
    /**
     * Count orders by date
     * Only counts completed orders (status = 3)
     * @param date Date
     * @return Order count for the day
     */
    @Query("SELECT COUNT(o) FROM Order o " +
           "WHERE DATE(o.startTime) = DATE(:date) AND o.status = 3")
    Integer getOrdersCountByDate(@Param("date") Date date);
} 
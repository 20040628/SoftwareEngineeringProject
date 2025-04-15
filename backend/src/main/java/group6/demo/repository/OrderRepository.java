package group6.demo.repository;

import group6.demo.entity.Order;
import group6.demo.entity.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Date;

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
} 
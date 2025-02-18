package group6.demo.repository;

import group6.demo.entity.Order;
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
} 
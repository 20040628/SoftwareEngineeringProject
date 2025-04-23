package group6.demo.repository;

import group6.demo.entity.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ScooterRepository extends JpaRepository<Scooter, Long> {
    List<Scooter> findByStoreId(Long storeId);
}

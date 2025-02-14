package group6.demo.repository;

import group6.demo.entity.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScooterRepository extends JpaRepository<Scooter, Long> {

}

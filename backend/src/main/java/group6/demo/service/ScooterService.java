package group6.demo.service;

import group6.demo.dto.ScooterAddDTO;
import group6.demo.entity.Scooter;

import java.util.List;
import java.util.Optional;


public interface ScooterService {
    Scooter addScooter(ScooterAddDTO scooterAddDTO);

    List<Scooter> getAllScooters();
    List<Scooter> getAllScootersUsers();
    Optional<Scooter> getScooterById(Long id);

    Optional<Scooter> changeScooterStatus(Long id);

}

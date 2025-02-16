package group6.demo.service;

import group6.demo.dto.ScooterAddDTO;
import group6.demo.entity.Scooter;

import java.util.List;


public interface ScooterService {
    Scooter addScooter(ScooterAddDTO scooterAddDTO);

    List<Scooter> getAllScooters();
}

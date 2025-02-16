package group6.demo.service.impl;

import group6.demo.dto.ScooterAddDTO;
import group6.demo.entity.Scooter;
import group6.demo.repository.ScooterRepository;
import group6.demo.service.ScooterService;
import group6.demo.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScooterServiceImlp implements ScooterService {
    @Autowired
    private ScooterRepository scooterRepository;

    @Override
    public Scooter addScooter(ScooterAddDTO scooterAddDTO){
        // Validate input data
//        if (scooterAddDTO.getLocation()) {
//            throw new IllegalArgumentException("");
//        }
        if (!ValidationUtil.isValidPrice(scooterAddDTO.getPriceHour())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 2 decimal places");
        }
        if (!ValidationUtil.isValidPrice(scooterAddDTO.getPriceFourHour())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 2 decimal places");
        }
        if (!ValidationUtil.isValidPrice(scooterAddDTO.getPriceDay())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 2 decimal places");
        }
        if (!ValidationUtil.isValidPrice(scooterAddDTO.getPriceWeek())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 2 decimal places");
        }

        // Create new scooter entity
        Scooter scooter = new Scooter();
        scooter.setLocation(scooterAddDTO.getLocation());
        scooter.setPriceHour(scooterAddDTO.getPriceHour());
        scooter.setPriceFourHour(scooterAddDTO.getPriceFourHour());
        scooter.setPriceDay(scooterAddDTO.getPriceDay());
        scooter.setPriceWeek(scooterAddDTO.getPriceWeek());
        // Set default values(1:available;0:unavailable)
        scooter.setStatus(1);

        return scooterRepository.save(scooter);
    }

    @Override
    public List<Scooter> getAllScooters() {
        return scooterRepository.findAll();
    }
}

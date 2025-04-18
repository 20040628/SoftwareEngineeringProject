package group6.demo.service.impl;

import group6.demo.dto.ScooterAddDTO;
import group6.demo.entity.Scooter;
import group6.demo.repository.ScooterRepository;
import group6.demo.service.ScooterService;
import group6.demo.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ScooterServiceImlp implements ScooterService {
    @Autowired
    private ScooterRepository scooterRepository;

    @Override
    public Scooter addScooter(ScooterAddDTO scooterAddDTO){
        // Validate input data
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

        if (!ValidationUtil.isValidLocation(scooterAddDTO.getLongitude())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 6 decimal places");
        }
        if (!ValidationUtil.isValidLocation(scooterAddDTO.getLatitude())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 6 decimal places");
        }
        if (!ValidationUtil.isValidBattery(scooterAddDTO.getBattery())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 2 decimal places");
        }
        if (!ValidationUtil.isValidSpeed(scooterAddDTO.getSpeed())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 2 decimal places");
        }
        // Create new scooter entity
        Scooter scooter = new Scooter();
        scooter.setPriceHour(scooterAddDTO.getPriceHour());
        scooter.setPriceFourHour(scooterAddDTO.getPriceFourHour());
        scooter.setPriceDay(scooterAddDTO.getPriceDay());
        scooter.setPriceWeek(scooterAddDTO.getPriceWeek());
        scooter.setLongitude(scooterAddDTO.getLongitude());
        scooter.setLatitude(scooterAddDTO.getLatitude());
        scooter.setBattery(scooterAddDTO.getBattery());
        scooter.setSpeed(scooterAddDTO.getSpeed());
        // Set default values(1:available;0:unavailable)
        scooter.setStatus(1);

        return scooterRepository.save(scooter);
    }

    @Override
    public List<Scooter> getAllScooters() {
        return scooterRepository.findAll();
    }

    @Override
    public List<Scooter> getAllScootersUsers() {
        return scooterRepository.findByBatteryNot(BigDecimal.ZERO);
    }
    @Override
    public Optional<Scooter> getScooterById(Long id) {
        return scooterRepository.findById(id);
    }

    /**
     * 将指定ID的滑板车状态从可用更新为不可用
     * @param id 滑板车的ID
     * @return 更新后的滑板车对象，如果未找到则返回空的Optional
     */
    @Override
    public Optional<Scooter> changeScooterStatus(Long id) {
        // find scooter
        Optional<Scooter> Scooter = scooterRepository.findById(id);

        if (Scooter.isPresent()) {
            Scooter scooter = Scooter.get();
            // 检查滑板车当前状态是否为可用
            if (scooter.getStatus() == 1) {
                scooter.setStatus(0);
                return Optional.of(scooterRepository.save(scooter));
            }
            else {
                scooter.setStatus(1);
                return Optional.of(scooterRepository.save(scooter));
            }
        }
        return Scooter;
    }
}

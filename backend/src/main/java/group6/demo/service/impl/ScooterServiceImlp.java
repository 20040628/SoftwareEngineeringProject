package group6.demo.service.impl;

import group6.demo.dto.AvailableScooterDTO;
import group6.demo.dto.ScooterAddDTO;
import group6.demo.entity.Scooter;
import group6.demo.entity.Order;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.ScooterRepository;
import group6.demo.service.ScooterService;
import group6.demo.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScooterServiceImlp implements ScooterService {
    @Autowired
    private ScooterRepository scooterRepository;
    @Autowired
    private OrderRepository orderRepository;

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

    // A list of scooters has battery and without conflicting orders
    @Override
    public List<Scooter> getScootersAvailable(AvailableScooterDTO availableDTO) {
        // 获取所有电量大于0的滑板车
        List<Scooter> allChargedScooters = scooterRepository.findByBatteryNot(BigDecimal.ZERO);

        // Check start time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime;
        try {
            startTime = dateFormat.parse(availableDTO.getStartTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }
        // Validate if start time is after current time
        if (startTime.before(new Date())) {
            throw new IllegalArgumentException("Start time cannot be earlier than current time");
        }

        // Calculate end time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        switch (availableDTO.getHireType()) {
            case "HOUR":
                calendar.add(Calendar.HOUR, 1);
                break;
            case "FOUR_HOURS":
                calendar.add(Calendar.HOUR, 4);
                break;
            case "DAY":
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case "WEEK":
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            default:
                throw new IllegalArgumentException("Invalid hire type");
        }
        Date endTime = calendar.getTime();

        // get scooter with no conflict orders
        List<Scooter> availableScooters = new ArrayList<>();
        for (Scooter scooter : allChargedScooters) {
            List<Order> conflictingOrders = orderRepository.findConflictingOrders(scooter.getId(), startTime, endTime);
            if (conflictingOrders.isEmpty()) {
                availableScooters.add(scooter);
            }
        }

        return availableScooters;
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

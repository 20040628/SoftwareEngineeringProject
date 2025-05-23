package group6.demo.service.impl;

import group6.demo.dto.AvailableScooterDTO;
import group6.demo.dto.ScooterAddDTO;
import group6.demo.entity.Scooter;
import group6.demo.entity.Order;
import group6.demo.entity.Store;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.ScooterRepository;
import group6.demo.repository.StoreRepository;
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
    @Autowired
    private StoreRepository storeRepository;

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

        if (!ValidationUtil.isValidBattery(scooterAddDTO.getBattery())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 2 decimal places");
        }
        if (!ValidationUtil.isValidSpeed(scooterAddDTO.getSpeed())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 2 decimal places");
        }
        // check if store exists
        Store store = storeRepository.findById(scooterAddDTO.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        // Create new scooter entity
        Scooter scooter = new Scooter();
        scooter.setPriceHour(scooterAddDTO.getPriceHour());
        scooter.setPriceFourHour(scooterAddDTO.getPriceFourHour());
        scooter.setPriceDay(scooterAddDTO.getPriceDay());
        scooter.setPriceWeek(scooterAddDTO.getPriceWeek());
        scooter.setBattery(scooterAddDTO.getBattery());
        scooter.setSpeed(scooterAddDTO.getSpeed());
        scooter.setStore(store);
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
    public List<Scooter> getScootersAvailable(AvailableScooterDTO availableDTO, Long storeId) {
        // 首先调用 getScooterByStoreId 方法获取指定 storeId 的滑板车列表
        List<Scooter> scootersByStore = getScooterByStoreId(storeId);

        // 获取所有电量大于0的滑板车（这里改为从指定 storeId 的滑板车中筛选）
        List<Scooter> allChargedScooters = new ArrayList<>();
        for (Scooter scooter : scootersByStore) {
            if (scooter.getBattery().compareTo(BigDecimal.ZERO) > 0) {
                allChargedScooters.add(scooter);
            }
        }

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
    public List<Scooter> getScooterByStoreId(Long storeId) {
        return scooterRepository.findByStoreId(storeId);
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

    @Override
    public boolean updateScooter(Long id, ScooterAddDTO dto){
        Optional<Scooter> optionalScooter = scooterRepository.findById(id);
        if (optionalScooter.isEmpty()) {
            return false;
        }

        Scooter scooter = optionalScooter.get();
        boolean changed = false;

        // 判断每个字段是否有变化，有就更新
        if (dto.getPriceHour() != null && !dto.getPriceHour().equals(scooter.getPriceHour())) {
            scooter.setPriceHour(dto.getPriceHour());
            changed = true;
        }
        if (dto.getPriceFourHour() != null && !dto.getPriceFourHour().equals(scooter.getPriceFourHour())) {
            scooter.setPriceFourHour(dto.getPriceFourHour());
            changed = true;
        }
        if (dto.getPriceDay() != null && !dto.getPriceDay().equals(scooter.getPriceDay())) {
            scooter.setPriceDay(dto.getPriceDay());
            changed = true;
        }
        if (dto.getPriceWeek() != null && !dto.getPriceWeek().equals(scooter.getPriceWeek())) {
            scooter.setPriceWeek(dto.getPriceWeek());
            changed = true;
        }
        if (dto.getBattery() != null && !dto.getBattery().equals(scooter.getBattery())) {
            scooter.setBattery(dto.getBattery());
            changed = true;
        }
        if (dto.getSpeed() != null && !dto.getSpeed().equals(scooter.getSpeed())) {
            scooter.setSpeed(dto.getSpeed());
            changed = true;
        }
        if (dto.getStoreId() != null && (scooter.getStore() == null || !dto.getStoreId().equals(scooter.getStore().getId()))) {
            Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
            scooter.setStore(store);
            changed = true;
        }

        if (changed) {
            scooterRepository.save(scooter);
        }

        return changed;
    }
}

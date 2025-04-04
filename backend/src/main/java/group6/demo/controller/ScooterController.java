package group6.demo.controller;

import group6.demo.dto.ScooterAddDTO;
import group6.demo.dto.ScooterWithDiscountDTO;
import group6.demo.entity.Scooter;
import group6.demo.service.PriceDiscountService;
import group6.demo.service.ScooterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/scooters")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ScooterController {
    @Autowired
    private ScooterService scooterService;
    
    @Autowired
    private PriceDiscountService priceDiscountService;

    @PostMapping("/add")
    public ResponseEntity<?> addScooter(@Valid @RequestBody ScooterAddDTO scooterAddDTO,
                                           BindingResult bindingResult){
        // Check validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Scooter scooter = scooterService.addScooter(scooterAddDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Add successfully");
            response.put("scooterId", scooter.getId());
            response.put("location", scooter.getLocation());
            response.put("price_hour", scooter.getPriceHour());
            response.put("price_four_hour", scooter.getPriceFourHour());
            response.put("price_day", scooter.getPriceDay());
            response.put("price_week", scooter.getPriceWeek());
            response.put("status", scooter.getStatus());
            response.put("longitude", scooter.getLongitude());
            response.put("latitude", scooter.getLatitude());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Handle validation errors
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle other unexpected errors
            return ResponseEntity.badRequest().body("adding scooter failed: " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllScooters(@RequestParam(required = false) Long userId) {
        try {
            List<Scooter> scooters = scooterService.getAllScooters();
            
            // 如果未提供userId，返回原始滑板车列表
            if (userId == null) {
                return ResponseEntity.ok(scooters);
            }
            
            // 如果提供了userId，计算折扣价格
            List<ScooterWithDiscountDTO> scootersWithDiscount = new ArrayList<>();
            
            for (Scooter scooter : scooters) {
                ScooterWithDiscountDTO dto = getScooterWithDiscount(scooter, userId);
                scootersWithDiscount.add(dto);
            }
            
            return ResponseEntity.ok(scootersWithDiscount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get scooters: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScooterById(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        try {
            Optional<Scooter> scooterOptional = scooterService.getScooterById(id);
            if (scooterOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Scooter not found");
            }
            
            Scooter scooter = scooterOptional.get();
            
            // 如果未提供userId，返回原始滑板车信息
            if (userId == null) {
                return ResponseEntity.ok(scooter);
            }
            
            // 如果提供了userId，计算并返回带折扣的DTO
            ScooterWithDiscountDTO dto = getScooterWithDiscount(scooter, userId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get scooter: " + e.getMessage());
        }
    }

    @GetMapping("/changeStatus/{id}")
    public Optional<Scooter> changeScooterStatus(@PathVariable Long id) {
        return scooterService.changeScooterStatus(id);
    }

    // 获取单个滑板车信息及折扣价格
    private ScooterWithDiscountDTO getScooterWithDiscount(Scooter scooter, Long userId) {
        if (scooter == null) {
            return null;
        }
        
        ScooterWithDiscountDTO dto = new ScooterWithDiscountDTO();
        BeanUtils.copyProperties(scooter, dto);
        
        if (userId != null) {
            // 先更新用户折扣状态
            priceDiscountService.updateUserDiscountStatusByBirthday(userId);
            
            // 再计算折扣价格
            BigDecimal discountedPriceHour = priceDiscountService.calculateDiscountedPrice(scooter.getPriceHour(), userId);
            BigDecimal discountedPriceFourHour = priceDiscountService.calculateDiscountedPrice(scooter.getPriceFourHour(), userId);
            BigDecimal discountedPriceDay = priceDiscountService.calculateDiscountedPrice(scooter.getPriceDay(), userId);
            BigDecimal discountedPriceWeek = priceDiscountService.calculateDiscountedPrice(scooter.getPriceWeek(), userId);
            
            dto.setDiscountedPriceHour(discountedPriceHour);
            dto.setDiscountedPriceFourHour(discountedPriceFourHour);
            dto.setDiscountedPriceDay(discountedPriceDay);
            dto.setDiscountedPriceWeek(discountedPriceWeek);
            dto.setHasDiscount(true);
            
            // 更新常客状态
            priceDiscountService.updateFrequentUserStatus(userId);
        }
        
        return dto;
    }
}

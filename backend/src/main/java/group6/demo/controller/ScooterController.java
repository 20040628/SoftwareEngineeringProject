package group6.demo.controller;

import group6.demo.dto.ScooterAddDTO;
import group6.demo.entity.Scooter;
import group6.demo.service.ScooterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scooters")
@CrossOrigin(origins = "http://localhost:5173")
public class ScooterController {
    @Autowired
    private ScooterService scooterService;

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
    public List<Scooter> getAllScooters() {
        return scooterService.getAllScooters();
    }
}

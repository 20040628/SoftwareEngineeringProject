package group6.demo.controller;

import group6.demo.dto.StoreAddDTO;
import group6.demo.entity.Store;
import group6.demo.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class StoreController {
    @Autowired
    private StoreService storeService;

    @PostMapping("/add")
    public ResponseEntity<?> addStore(@Valid @RequestBody StoreAddDTO storeAddDTO,
                                      BindingResult bindingResult) {
        // Check validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Store store = storeService.addStore(storeAddDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Add successfully");
            response.put("storeId", store.getId());
            response.put("longitude", store.getLongitude());
            response.put("latitude", store.getLatitude());
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e) {
            // Handle validation errors
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle other unexpected errors
            return ResponseEntity.badRequest().body("adding store failed: " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStores() {
        try {
            List<Store> stores = storeService.getAllStores();
            return ResponseEntity.ok(stores);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get all stores: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable Long id) {
        try {
            Optional<Store> storeOptional =storeService.getStoreById(id);
            if (storeOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Store not found");
            }
            Store store = storeOptional.get();
            return ResponseEntity.ok(store);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to get store: " + e.getMessage());
        }
    }
}

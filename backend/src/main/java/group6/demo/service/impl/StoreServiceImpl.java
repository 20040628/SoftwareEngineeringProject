package group6.demo.service.impl;

import group6.demo.dto.StoreAddDTO;
import group6.demo.entity.Store;
import group6.demo.repository.StoreRepository;
import group6.demo.service.StoreService;
import group6.demo.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Override
    public Store addStore(StoreAddDTO storeAddDTO) {
        // Validate input data
        if (!ValidationUtil.isValidLocation(storeAddDTO.getLongitude())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 6 decimal places");
        }
        if (!ValidationUtil.isValidLocation(storeAddDTO.getLatitude())) {
            throw new IllegalArgumentException("Invalid format. Must be a decimal with up to 3 digits and 6 decimal places");
        }

        // Create new store entity
        Store store = new Store();
        store.setName(storeAddDTO.getName());
        store.setLatitude(storeAddDTO.getLatitude());
        store.setLongitude(storeAddDTO.getLongitude());
        return storeRepository.save(store);
    }

    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public Optional<Store> getStoreById(Long id) {
        return storeRepository.findById(id);
    }
}

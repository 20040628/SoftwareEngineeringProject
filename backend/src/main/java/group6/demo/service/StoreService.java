package group6.demo.service;

import group6.demo.dto.StoreAddDTO;
import group6.demo.entity.Store;

import java.util.List;
import java.util.Optional;

public interface StoreService {
    Store addStore(StoreAddDTO storeAddDTO);
    List<Store> getAllStores();
    Optional<Store> getStoreById(Long id);
}

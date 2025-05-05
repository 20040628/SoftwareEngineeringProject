package group6.demo.service.impl;

import group6.demo.dto.StoreAddDTO;
import group6.demo.entity.Store;
import group6.demo.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StoreServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddStore_InvalidLongitude() {
        StoreAddDTO storeAddDTO = new StoreAddDTO();
        storeAddDTO.setName("Test Store");
        storeAddDTO.setLatitude(new BigDecimal("30.765000"));
        storeAddDTO.setLongitude(new BigDecimal("1039.84500")); // Invalid longitude

        assertThrows(IllegalArgumentException.class, () -> storeService.addStore(storeAddDTO));
    }

    @Test
    void testGetAllStores() {
        Store store1 = new Store();
        store1.setName("Store 1");
        Store store2 = new Store();
        store2.setName("Store 2");

        when(storeRepository.findAll()).thenReturn(List.of(store1, store2));

        List<Store> result = storeService.getAllStores();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(storeRepository, times(1)).findAll();
    }

    @Test
    void testGetStoreById_ExistingId() {
        Store store = new Store();
        store.setName("Test Store");

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        Optional<Store> result = storeService.getStoreById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Store", result.get().getName());
        verify(storeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStoreById_NonExistingId() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Store> result = storeService.getStoreById(1L);

        assertFalse(result.isPresent());
        verify(storeRepository, times(1)).findById(1L);
    }
}

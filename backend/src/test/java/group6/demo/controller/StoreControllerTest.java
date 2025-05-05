package group6.demo.controller;

import group6.demo.dto.StoreAddDTO;
import group6.demo.entity.Store;
import group6.demo.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StoreControllerTest {

    @Mock
    private StoreService storeService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private StoreController storeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddStore_ValidData() {
        StoreAddDTO storeAddDTO = new StoreAddDTO();
        storeAddDTO.setName("Test Store");
        storeAddDTO.setLatitude(new BigDecimal("30.765000"));
        storeAddDTO.setLongitude(new BigDecimal("103.984500"));

        Store store = new Store();
        store.setId(1L);
        store.setName("Test Store");
        store.setLatitude(new BigDecimal("30.765000"));
        store.setLongitude(new BigDecimal("103.984500"));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(storeService.addStore(any(StoreAddDTO.class))).thenReturn(store);

        ResponseEntity<?> response = storeController.addStore(storeAddDTO, bindingResult);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Add successfully", responseBody.get("message"));
        assertEquals(1L, responseBody.get("storeId"));
        verify(storeService, times(1)).addStore(any(StoreAddDTO.class));
    }

    @Test
    void testAddStore_InvalidData() {
        StoreAddDTO storeAddDTO = new StoreAddDTO();
        storeAddDTO.setName("Test Store");
        storeAddDTO.setLatitude(new BigDecimal("30.765000"));
        storeAddDTO.setLongitude(new BigDecimal("103.984500"));

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("storeAddDTO", "latitude", "Invalid latitude")));

        ResponseEntity<?> response = storeController.addStore(storeAddDTO, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Invalid latitude", responseBody.get("latitude"));
        verify(storeService, times(0)).addStore(any(StoreAddDTO.class));
    }

    @Test
    void testGetAllStores() {
        List<Store> stores = List.of(new Store(), new Store());

        when(storeService.getAllStores()).thenReturn(stores);

        ResponseEntity<?> response = storeController.getAllStores();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stores, response.getBody());
        verify(storeService, times(1)).getAllStores();
    }

    @Test
    void testGetStoreById_ExistingId() {
        Store store = new Store();
        store.setId(1L);
        store.setName("Test Store");

        when(storeService.getStoreById(1L)).thenReturn(Optional.of(store));

        ResponseEntity<?> response = storeController.getStoreById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(store, response.getBody());
        verify(storeService, times(1)).getStoreById(1L);
    }

    @Test
    void testGetStoreById_NonExistingId() {
        when(storeService.getStoreById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = storeController.getStoreById(1L);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Store not found", response.getBody());
        verify(storeService, times(1)).getStoreById(1L);
    }
}

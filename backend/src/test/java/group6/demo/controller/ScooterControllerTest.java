package group6.demo.controller;

import group6.demo.dto.AvailableScooterDTO;
import group6.demo.dto.ScooterAddDTO;
import group6.demo.dto.ScooterWithDiscountDTO;
import group6.demo.entity.Scooter;
import group6.demo.service.PriceDiscountService;
import group6.demo.service.ScooterService;
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

class ScooterControllerTest {

    @Mock
    private ScooterService scooterService;

    @Mock
    private PriceDiscountService priceDiscountService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ScooterController scooterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddScooter_InvalidData() {
        ScooterAddDTO scooterAddDTO = new ScooterAddDTO();
        scooterAddDTO.setPriceHour(new BigDecimal("5.00"));
        scooterAddDTO.setPriceFourHour(new BigDecimal("10.00"));
        scooterAddDTO.setPriceDay(new BigDecimal("20.00"));
        scooterAddDTO.setPriceWeek(new BigDecimal("100.00"));
        scooterAddDTO.setBattery(new BigDecimal("100.00"));
        scooterAddDTO.setSpeed(new BigDecimal("50.00"));
        scooterAddDTO.setStoreId(1L);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("scooterAddDTO", "priceHour", "Invalid price")));

        ResponseEntity<?> response = scooterController.addScooter(scooterAddDTO, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Invalid price", responseBody.get("priceHour"));
        verify(scooterService, times(0)).addScooter(any(ScooterAddDTO.class));
    }

    @Test
    void testGetAllScooters() {
        List<Scooter> scooters = List.of(new Scooter(), new Scooter());

        when(scooterService.getAllScooters()).thenReturn(scooters);

        ResponseEntity<?> response = scooterController.getAllScooters();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(scooters, response.getBody());
        verify(scooterService, times(1)).getAllScooters();
    }

    @Test
    void testGetScootersAvailable() {
        AvailableScooterDTO availableDTO = new AvailableScooterDTO();
        availableDTO.setStartTime("2025-05-04 10:00:00");
        availableDTO.setHireType("HOUR");

        Scooter scooter = new Scooter();
        scooter.setBattery(new BigDecimal("100.00"));
        scooter.setId(1L);

        when(scooterService.getScootersAvailable(any(AvailableScooterDTO.class), anyLong())).thenReturn(List.of(scooter));
        when(priceDiscountService.calculateDiscountedPrice(any(BigDecimal.class), anyLong())).thenReturn(new BigDecimal("5.00"));

        ResponseEntity<?> response = scooterController.getScootersAvailable(1L, 1L, availableDTO);

        assertEquals(200, response.getStatusCodeValue());
        List<ScooterWithDiscountDTO> responseBody = (List<ScooterWithDiscountDTO>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.size());
        verify(scooterService, times(1)).getScootersAvailable(any(AvailableScooterDTO.class), anyLong());
    }

    @Test
    void testGetScooterByStoreId() {
        List<Scooter> scooters = List.of(new Scooter(), new Scooter());

        when(scooterService.getScooterByStoreId(anyLong())).thenReturn(scooters);

        ResponseEntity<?> response = scooterController.getScooterByStoreId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(scooters, response.getBody());
        verify(scooterService, times(1)).getScooterByStoreId(anyLong());
    }

    @Test
    void testGetScooterById() {
        Scooter scooter = new Scooter();
        scooter.setId(1L);

        when(scooterService.getScooterById(anyLong())).thenReturn(Optional.of(scooter));

        ResponseEntity<?> response = scooterController.getScooterById(1L, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(scooter, response.getBody());
        verify(scooterService, times(1)).getScooterById(anyLong());
    }

    @Test
    void testChangeScooterStatus() {
        Scooter scooter = new Scooter();
        scooter.setId(1L);
        scooter.setStatus(1);

        when(scooterService.changeScooterStatus(anyLong())).thenReturn(Optional.of(scooter));

        Optional<Scooter> result = scooterController.changeScooterStatus(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(scooterService, times(1)).changeScooterStatus(anyLong());
    }
}

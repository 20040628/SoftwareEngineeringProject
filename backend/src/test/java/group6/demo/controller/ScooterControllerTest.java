package group6.demo.controller;

import group6.demo.dto.AvailableScooterDTO;
import group6.demo.dto.ScooterAddDTO;
import group6.demo.entity.Scooter;
import group6.demo.entity.Store;
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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Test class for ScooterController
class ScooterControllerTest {

    // Mocking the ScooterService dependency
    @Mock
    private ScooterService scooterService;

    @Mock
    private PriceDiscountService priceDiscountService;

    // Injecting the mock dependencies into the ScooterController
    @InjectMocks
    private ScooterController scooterController;

    // Mocking the BindingResult for validation purposes
    @Mock
    private BindingResult bindingResult;

    // This method is executed before each test case
    @BeforeEach
    void setUp() {
        // Initializes the mocks
        MockitoAnnotations.openMocks(this);
    }

    // Test case for successful addition of a scooter
    @Test
    void testAddScooter_Success() {
        ScooterAddDTO dto = new ScooterAddDTO();
        Scooter scooter = new Scooter();
        scooter.setId(1L);
//        scooter.setLongitude(new BigDecimal("104.06"));
//        scooter.setLatitude(new BigDecimal("30.67"));

        // Mocking the behavior of BindingResult and ScooterService
        when(bindingResult.hasErrors()).thenReturn(false);
        when(scooterService.addScooter(dto)).thenReturn(scooter);

        // Calling the method under test
        ResponseEntity<?> response = scooterController.addScooter(dto, bindingResult);
        // Asserting the expected response
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("scooterId"));
    }

    // Test case for validation failure when adding a scooter
    @Test
    void testAddScooter_ValidationFailure() {
        ScooterAddDTO dto = new ScooterAddDTO();

        // Mocking the behavior of BindingResult to simulate a validation error
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(new FieldError("scooterAddDTO", "longitude", "Longitude is required")));

        // Calling the method under test
        ResponseEntity<?> response = scooterController.addScooter(dto, bindingResult);
        // Asserting the expected response
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("longitude"));
    }

    // Test case for service failure when adding a scooter
    @Test
    void testAddScooter_ServiceFailure() {
        ScooterAddDTO dto = new ScooterAddDTO();

        // Mocking the behavior of BindingResult and ScooterService to simulate a service error
        when(bindingResult.hasErrors()).thenReturn(false);
        when(scooterService.addScooter(dto)).thenThrow(new IllegalArgumentException("Invalid data"));

        // Calling the method under test
        ResponseEntity<?> response = scooterController.addScooter(dto, bindingResult);
        // Asserting the expected response
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid data", response.getBody());
    }

    // Test case for retrieving all scooters
    @Test
    void testGetAllScooters() {
        Scooter scooter1 = new Scooter();
        scooter1.setId(1L);
//        scooter1.setLongitude(new BigDecimal("104.06"));
//        scooter1.setLatitude(new BigDecimal("30.67"));

        Scooter scooter2 = new Scooter();
        scooter2.setId(2L);
//        scooter2.setLongitude(new BigDecimal("104.07"));
//        scooter2.setLatitude(new BigDecimal("30.68"));

        // Preparing a list of scooters to be returned by the mock service
        List<Scooter> scooters = Arrays.asList(scooter1, scooter2);
        when(scooterService.getAllScooters()).thenReturn(scooters);

        // Calling the method under test - without userId
        ResponseEntity<?> response = scooterController.getAllScooters();
        // Asserting the expected response
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List);
        assertEquals(2, ((List<?>) response.getBody()).size());
    }

    // Test case for retrieving scooters with discount
    @Test
    void testGetAllScootersWithDiscount() {
        // 设置测试数据
        Scooter scooter = new Scooter();
        scooter.setId(1L);
//        scooter.setLongitude(new BigDecimal("104.06"));
//        scooter.setLatitude(new BigDecimal("30.67"));
        scooter.setPriceHour(new BigDecimal("50.00"));
        scooter.setPriceFourHour(new BigDecimal("160.00"));
        scooter.setPriceDay(new BigDecimal("300.00"));
        scooter.setPriceWeek(new BigDecimal("1000.00"));
        
        List<Scooter> scooters = Arrays.asList(scooter);

        // 设置 AvailableScooterDTO（模拟用户传来的租车时间）
        AvailableScooterDTO dto = new AvailableScooterDTO();
        dto.setStartTime("2025-08-21 10:00:00.756000");
        dto.setHireType("HOUR"); // 模拟租用方式

        when(scooterService.getScootersAvailable(dto, 1L)).thenReturn(scooters);
        
        // 设置折扣计算结果
        when(priceDiscountService.calculateDiscountedPrice(new BigDecimal("50.00"), 1L)).thenReturn(new BigDecimal("42.50"));
        when(priceDiscountService.calculateDiscountedPrice(new BigDecimal("160.00"), 1L)).thenReturn(new BigDecimal("136.00"));
        when(priceDiscountService.calculateDiscountedPrice(new BigDecimal("300.00"), 1L)).thenReturn(new BigDecimal("255.00"));
        when(priceDiscountService.calculateDiscountedPrice(new BigDecimal("1000.00"), 1L)).thenReturn(new BigDecimal("850.00"));
        
        // 调用方法 - 使用userId
        ResponseEntity<?> response = scooterController.getScootersAvailable(1L,2L, dto);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List);
        
        List<?> responseList = (List<?>) response.getBody();
        assertEquals(1, responseList.size());
        
        Map<?, ?> scooterInfo = (Map<?, ?>) responseList.get(0);
        assertEquals(1L, scooterInfo.get("id"));
        assertEquals(new BigDecimal("50.00"), scooterInfo.get("priceHour"));
        assertEquals(new BigDecimal("42.50"), scooterInfo.get("discountedPriceHour"));
        assertEquals(true, scooterInfo.get("hasDiscount"));
    }
    
    // Test case for retrieving a specific scooter with discount
    @Test
    void testGetScooterByIdWithDiscount() {
        // 设置测试数据
        Scooter scooter = new Scooter();
        scooter.setId(1L);
//        scooter.setLongitude(new BigDecimal("104.06"));
//        scooter.setLatitude(new BigDecimal("30.67"));
        scooter.setPriceHour(new BigDecimal("50.00"));
        scooter.setPriceFourHour(new BigDecimal("160.00"));
        scooter.setPriceDay(new BigDecimal("300.00"));
        scooter.setPriceWeek(new BigDecimal("1000.00"));
        
        when(scooterService.getScooterById(1L)).thenReturn(Optional.of(scooter));
        
        // 设置折扣计算结果
        when(priceDiscountService.calculateDiscountedPrice(new BigDecimal("50.00"), 1L)).thenReturn(new BigDecimal("42.50"));
        when(priceDiscountService.calculateDiscountedPrice(new BigDecimal("160.00"), 1L)).thenReturn(new BigDecimal("136.00"));
        when(priceDiscountService.calculateDiscountedPrice(new BigDecimal("300.00"), 1L)).thenReturn(new BigDecimal("255.00"));
        when(priceDiscountService.calculateDiscountedPrice(new BigDecimal("1000.00"), 1L)).thenReturn(new BigDecimal("850.00"));
        
        // 调用方法 - 使用userId
        ResponseEntity<?> response = scooterController.getScooterById(1L, 1L);
        
        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        
        Map<?, ?> scooterInfo = (Map<?, ?>) response.getBody();
        assertEquals(1L, scooterInfo.get("id"));
        assertEquals(new BigDecimal("50.00"), scooterInfo.get("priceHour"));
        assertEquals(new BigDecimal("42.50"), scooterInfo.get("discountedPriceHour"));
        assertEquals(true, scooterInfo.get("hasDiscount"));
    }
}

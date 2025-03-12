package group6.demo.controller;

import group6.demo.dto.ScooterAddDTO;
import group6.demo.entity.Scooter;
import group6.demo.service.ScooterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Test class for ScooterController
class ScooterControllerTest {

    // Mocking the ScooterService dependency
    @Mock
    private ScooterService scooterService;

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
        scooter.setLocation("Downtown");

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
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(new FieldError("scooterAddDTO", "location", "Location is required")));

        // Calling the method under test
        ResponseEntity<?> response = scooterController.addScooter(dto, bindingResult);
        // Asserting the expected response
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("location"));
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
        scooter1.setLocation("Location1");

        Scooter scooter2 = new Scooter();
        scooter2.setId(2L);
        scooter2.setLocation("Location2");

        // Preparing a list of scooters to be returned by the mock service
        List<Scooter> scooters = Arrays.asList(scooter1, scooter2);
        when(scooterService.getAllScooters()).thenReturn(scooters);

        // Calling the method under test
        List<Scooter> response = scooterController.getAllScooters();
        // Asserting the expected response
        assertEquals(2, response.size());
    }
}

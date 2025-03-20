package group6.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestControllerTest {

    @InjectMocks
    private TestController testController;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApiResponse() {
        // Call the test method of TestController
        Map<String, String> response = testController.test();

        assertNotNull(response);
        assertTrue(response.containsKey("message"));
        assertTrue(response.containsKey("timestamp"));
        assertEquals("后端连接成功！", response.get("message"));
    }
}


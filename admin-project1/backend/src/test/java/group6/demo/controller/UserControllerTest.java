package group6.demo.controller;

import group6.demo.entity.User;
import group6.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        // Create mock user data
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("User1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("User2");

        // Mock the findAll method of UserRepository
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // Call the getAllUsers method of UserController
        List<User> response = userController.getAllUsers();

        // Verify the response
        assertEquals(2, response.size());
    }

    @Test
    void testCreateUser() {
        // Create mock user data
        User user = new User();
        user.setId(1L);
        user.setUsername("NewUser");

        // Mock the save method of UserRepository
        when(userRepository.save(user)).thenReturn(user);

        // Call the createUser method of UserController
        User response = userController.createUser(user);

        // Verify the response
        assertNotNull(response);
        assertEquals("NewUser", response.getUsername());
    }

    @Test
    void testGetUserById_Found() {
        // Create mock user data
        User user = new User();
        user.setId(1L);
        user.setUsername("User1");

        // Mock the findById method of UserRepository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Call the getUserById method of UserController
        User response = userController.getUserById(1L);

        // Verify the response
        assertNotNull(response);
        assertEquals("User1", response.getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        // Mock the findById method of UserRepository to return empty
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the getUserById method of UserController
        User response = userController.getUserById(1L);

        // Verify the response
        assertNull(response);
    }
}
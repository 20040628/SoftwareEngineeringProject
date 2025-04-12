package group6.demo.service;

import group6.demo.dto.UserLoginDTO;
import group6.demo.dto.UserRegistrationDTO;
import group6.demo.entity.User;

import java.util.Optional;

public interface UserService {
    User registerUser(UserRegistrationDTO registrationDTO);
    User loginUser(UserLoginDTO loginDTO);
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
    Optional<User> changeUserStatus(Long id);
} 
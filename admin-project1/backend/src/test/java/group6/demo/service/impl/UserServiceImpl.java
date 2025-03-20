package group6.demo.service.impl;

import group6.demo.dto.UserLoginDTO;
import group6.demo.dto.UserRegistrationDTO;
import group6.demo.entity.User;
import group6.demo.repository.UserRepository;
import group6.demo.service.UserService;
import group6.demo.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegistrationDTO registrationDTO) {
        // Validate input data
        if (!ValidationUtil.isValidUsername(registrationDTO.getUsername())) {
            throw new IllegalArgumentException("Invalid username format. Username must be 3-20 characters long and can only contain letters, numbers and underscore.");
        }
        
        if (!ValidationUtil.isValidPassword(registrationDTO.getPassword())) {
            throw new IllegalArgumentException("Invalid password format. Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number.");
        }
        
        if (!ValidationUtil.isValidEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        
        if (!ValidationUtil.isValidMobile(registrationDTO.getMobile())) {
            throw new IllegalArgumentException("Invalid mobile number format. Mobile number must be 10-13 digits.");
        }
        
        if (!ValidationUtil.isValidBirthday(registrationDTO.getBirthday())) {
            throw new IllegalArgumentException("Invalid birthday. Birthday cannot be in the future.");
        }

        // Create new user entity
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEmail(registrationDTO.getEmail());
        user.setMobile(registrationDTO.getMobile());
        user.setBirthday(registrationDTO.getBirthday());
        
        // Set default values
        user.setUserType(0);  // 0: normal user
        user.setPaymentMethod(null);  // no payment method
        user.setAvatar("default_avatar.jpg");
        user.setStatus(1);  // 1: account enabled
        user.setRole(1);    // 1: normal user
        user.setIsFrequentUser(0);  // 0: not frequent user
        
        return userRepository.save(user);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User loginUser(UserLoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        
        return user;
    }
} 
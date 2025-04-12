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

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegistrationDTO registrationDTO) {
        // 调试日志
        System.out.println("开始注册用户: " + registrationDTO.getUsername());
        System.out.println("收到的生日信息: " + (registrationDTO.getBirthday() != null ? registrationDTO.getBirthday().toString() : "null"));
        
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
        
        System.out.println("验证通过，开始创建用户实体");

        // Create new user entity
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEmail(registrationDTO.getEmail());
        user.setMobile(registrationDTO.getMobile());
        user.setBirthday(registrationDTO.getBirthday());
        
        System.out.println("设置生日: " + (user.getBirthday() != null ? user.getBirthday().toString() : "null"));
        
        // Set default values
        user.setUserType(0);  // 0: normal user
        user.setPaymentMethod(null);  // no payment method
        user.setAvatar("default_avatar.jpg");
        user.setStatus(1);  // 1: account enabled
        user.setRole(1);    // 1: normal user
        user.setIsFrequentUser(0);  // 0: not frequent user
        user.setIsStudent(0);  // 初始设置为非学生
        user.setIsSenior(0);   // 初始设置为非老人
        
        // 保存用户
        User savedUser = userRepository.save(user);
        System.out.println("用户创建成功，ID: " + savedUser.getId());
        System.out.println("保存的生日信息: " + (savedUser.getBirthday() != null ? savedUser.getBirthday().toString() : "null"));
        
        return savedUser;
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

    @Override
    public Optional<User> changeUserStatus(Long id){
        Optional<User> User = userRepository.findById(id);
        if (User.isPresent()) {
            User user = User.get();
            if (user.getStatus()==1){
                user.setStatus(0);
            } else {
                user.setStatus(1);
            }
            return Optional.of(userRepository.save(user));
        }
        return User;
    }
}


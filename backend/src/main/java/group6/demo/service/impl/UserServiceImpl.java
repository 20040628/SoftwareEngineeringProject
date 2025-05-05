package group6.demo.service.impl;

import group6.demo.dto.UserLoginDTO;
import group6.demo.dto.UserRegistrationDTO;
import group6.demo.dto.UpdateBankCardDTO;
import group6.demo.dto.ChangePasswordDTO;
import group6.demo.dto.UserProfileDTO;
import group6.demo.entity.User;
import group6.demo.repository.UserRepository;
import group6.demo.service.UserService;
import group6.demo.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

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
        user.setAvatar("default_avatar.jpg");
        user.setStatus(1);  // 1: account enabled
        user.setRole(1);    // 1: normal user
        user.setIsFrequentUser(0);  // 0: not frequent user
        user.setIsStudent(0);  // 初始设置为非学生
        user.setIsSenior(0);   // 初始设置为非老人

        // 设置银行卡和余额
        user.setBankCard(registrationDTO.getBankCard());
        // 随机生成银行卡余额 (1000-10000元之间)
        Random random = new Random();
        double balance = 1000 + random.nextDouble() * 9000;
        // 四舍五入保留两位小数
        BigDecimal bankBalance = new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_UP);
        user.setBankBalance(bankBalance);

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
    public boolean isBankcardExists(String bankcard) { return userRepository.findByBankCard(bankcard) != null;}

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
    
    @Override
    public User updateBankCard(Long userId, UpdateBankCardDTO updateBankCardDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        User user = optionalUser.get();
        user.setBankCard(updateBankCardDTO.getBankCard());
        
        // 随机生成银行卡余额 (1000-10000元之间)
        Random random = new Random();
        double balance = 1000 + random.nextDouble() * 9000;
        // 四舍五入保留两位小数
        BigDecimal bankBalance = new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_UP);
        user.setBankBalance(bankBalance);
        
        return userRepository.save(user);
    }
    
    // 实现新增的方法
    
    @Override
    public boolean changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return false;
        }
        
        User user = optionalUser.get();
        
        // 验证旧密码
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            return false;
        }
        
        // 验证新密码和确认密码是否一致
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            return false;
        }
        
        // 设置新密码
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        
        return true;
    }
    
    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return null;
        }
        
        User user = optionalUser.get();
        UserProfileDTO profileDTO = new UserProfileDTO();
        
        // 设置基本信息
        profileDTO.setId(user.getId());
        profileDTO.setUsername(user.getUsername());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setMobile(user.getMobile());
        profileDTO.setAvatar(user.getAvatar());
        profileDTO.setBirthday(user.getBirthday());
        profileDTO.setUserType(user.getUserType());
        profileDTO.setStatus(user.getStatus());
        profileDTO.setRole(user.getRole());
        
        // 设置折扣信息
        profileDTO.setIsStudent(user.getIsStudent());
        profileDTO.setIsSenior(user.getIsSenior());
        profileDTO.setIsFrequentUser(user.getIsFrequentUser());
        
        // 设置银行卡信息
        String bankCard = user.getBankCard();
        if (bankCard != null && !bankCard.isEmpty()) {
            profileDTO.setHasBankCard(true);
            // 只显示后4位，其余用*遮盖
            String maskedCard = "**** **** **** " + bankCard.substring(bankCard.length() - 4);
            profileDTO.setMaskedBankCard(maskedCard);
            profileDTO.setBankBalance(user.getBankBalance());
        } else {
            profileDTO.setHasBankCard(false);
        }
        
        // 设置头像
        String avatarData = user.getAvatar();
        profileDTO.setAvatar(avatarData);
        
        // 直接将Base64数据设置为头像URL
        // 如果是旧版默认头像，返回默认路径
        if ("default_avatar.jpg".equals(avatarData)) {
            profileDTO.setAvatarUrl("/uploads/avatars/default_avatar.jpg");
        } else if (avatarData != null && avatarData.startsWith("data:image/")) {
            // 对于Base64格式的图片，直接使用
            profileDTO.setAvatarUrl(avatarData);
        } else {
            // 旧数据兼容处理
            profileDTO.setAvatarUrl("/uploads/avatars/" + (avatarData != null ? avatarData : "default_avatar.jpg"));
        }
        
        return profileDTO;
    }

    @Override
    public List<UserProfileDTO> getAllUserProfiles() {
        // 获取所有用户 ID
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> getUserProfile(user.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public User updateAvatar(Long userId, String base64Image) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        
        User user = optionalUser.get();
        user.setAvatar(base64Image);
        return userRepository.save(user);
    }
    
    @Override
    public boolean unbindBankCard(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return false;
        }
        
        User user = optionalUser.get();
        user.setBankCard(null);
        user.setBankBalance(null);
        userRepository.save(user);
        
        return true;
    }
}


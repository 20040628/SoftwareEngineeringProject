package group6.demo.controller;

import group6.demo.dto.ChangePasswordDTO;
import group6.demo.dto.UpdateBankCardDTO;
import group6.demo.dto.UserProfileDTO;
import group6.demo.entity.Order;
import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.UserRepository;
import group6.demo.service.FileStorageService;
import group6.demo.service.PriceDiscountService;
import group6.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private PriceDiscountService priceDiscountService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping("/orders/{userId}")
    public List<Order> getAllOrders(@PathVariable Long userId) {
        return orderRepository.findByUser_Id(userId);
    }

    /**
     * 更新用户生日信息并自动计算折扣资格
     * @param userId 用户ID
     * @param birthDate 生日
     * @return 更新结果
     */
    @PostMapping("/updateBirthDate/{userId}")
    public ResponseEntity<?> updateBirthDate(
            @PathVariable Long userId,
            @RequestParam String birthDate) {
        
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("用户不存在");
            }
            
            // 解析并设置生日
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = format.parse(birthDate);
            user.setBirthday(birthday);
            
            // 保存用户信息
            userRepository.save(user);
            
            // 自动更新折扣状态
            priceDiscountService.updateUserDiscountStatusByBirthday(userId);
            
            // 重新获取用户信息（已更新折扣状态）
            user = userRepository.findById(userId).orElse(null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "用户生日信息更新成功");
            response.put("userId", user.getId());
            response.put("birthDate", format.format(user.getBirthday()));
            response.put("isStudent", user.getIsStudent());
            response.put("isSenior", user.getIsSenior());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新用户生日失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("用户不存在");
            }
            
            boolean updatedBirthday = false;
            
            // 处理可能的更新字段
            if (updates.containsKey("email")) {
                user.setEmail(updates.get("email").toString());
            }
            
            if (updates.containsKey("mobile")) {
                user.setMobile(updates.get("mobile").toString());
            }
            
            if (updates.containsKey("avatar")) {
                user.setAvatar(updates.get("avatar").toString());
            }
            
            // 更新生日
            if (updates.containsKey("birthDate") && updates.get("birthDate") != null) {
                String birthDateStr = updates.get("birthDate").toString();
                if (!birthDateStr.isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthday = format.parse(birthDateStr);
                    user.setBirthday(birthday);
                    updatedBirthday = true;
                }
            }
            
            // 保存用户基本信息
            user = userRepository.save(user);
            
            // 如果更新了生日，自动更新折扣状态
            if (updatedBirthday) {
                priceDiscountService.updateUserDiscountStatusByBirthday(id);
                // 重新获取更新后的用户信息
                user = userRepository.findById(id).orElse(null);
            }
            
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新用户信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/changeStatus/{id}")
    public Optional<User> changeUserStatus(@PathVariable Long id) {
        return userService.changeUserStatus(id);
    }
    
    /**
     * 更新用户银行卡信息
     * @param userId 用户ID
     * @param updateBankCardDTO 银行卡信息DTO
     * @return 更新结果
     */
    @PostMapping("/updateBankCard/{userId}")
    public ResponseEntity<?> updateBankCard(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateBankCardDTO updateBankCardDTO) {
        
        try {
            User updatedUser = userService.updateBankCard(userId, updateBankCardDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Bank card information updated successfully");
            response.put("userId", updatedUser.getId());
            response.put("bankCard", updatedUser.getBankCard());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update bank card information: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户个人中心完整信息
     * @param userId 用户ID
     * @return 用户个人中心信息
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            UserProfileDTO profileDTO = userService.getUserProfile(userId);
            if (profileDTO == null) {
                return ResponseEntity.badRequest().body("用户不存在");
            }
            return ResponseEntity.ok(profileDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("获取用户个人信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 上传用户头像
     * @param userId 用户ID
     * @param file 头像文件
     * @return 上传结果
     */
    @PostMapping("/avatar/{userId}")
    public ResponseEntity<?> uploadAvatar(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        
        try {
            // 查询用户
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("用户不存在");
            }
            
            // 保存文件
            String filename = fileStorageService.storeAvatar(file, userId);
            
            // 删除旧头像（如果不是默认头像）
            if (user.getAvatar() != null && !user.getAvatar().equals("default_avatar.jpg")) {
                fileStorageService.deleteFile(user.getAvatar());
            }
            
            // 更新用户头像
            user = userService.updateAvatar(userId, filename);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "用户头像上传成功");
            response.put("userId", user.getId());
            response.put("avatar", user.getAvatar());
            response.put("avatarUrl", "/uploads/avatars/" + filename);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("上传头像失败: " + e.getMessage());
        }
    }
    
    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param changePasswordDTO 密码修改信息
     * @return 修改结果
     */
    @PostMapping("/changePassword/{userId}")
    public ResponseEntity<?> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        
        try {
            // 验证新密码和确认密码是否一致
            if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("The new password and the confirmed password do not match");
            }
            
            boolean changed = userService.changePassword(userId, changePasswordDTO);
            if (!changed) {
                return ResponseEntity.badRequest().body("Password modification failed. The old password might be incorrect");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Successfully modify password");
            response.put("userId", userId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Password modification failed: " + e.getMessage());
        }
    }
    
    /**
     * 解绑银行卡
     * @param userId 用户ID
     * @return 解绑结果
     */
    @PostMapping("/unbindBankCard/{userId}")
    public ResponseEntity<?> unbindBankCard(@PathVariable Long userId) {
        try {
            boolean unbound = userService.unbindBankCard(userId);
            if (!unbound) {
                return ResponseEntity.badRequest().body("解绑银行卡失败，用户可能不存在");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "银行卡解绑成功");
            response.put("userId", userId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("解绑银行卡失败: " + e.getMessage());
        }
    }
}
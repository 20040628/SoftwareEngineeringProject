package group6.demo.controller;

import group6.demo.entity.Order;
import group6.demo.entity.User;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.UserRepository;
import group6.demo.service.PriceDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
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
    public List<Order> getUserOrders(@PathVariable Long userId) {
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
}
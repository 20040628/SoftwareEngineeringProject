package group6.demo.controller;

import group6.demo.constants.DefaultAvatarConstants;
import group6.demo.entity.User;
import group6.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据迁移控制器
 * 用于执行数据迁移相关操作
 */
@RestController
@RequestMapping("/api/admin/migration")
public class MigrationController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 迁移用户头像数据，将文件系统中的头像转换为Base64编码并存储到数据库
     * 
     * @return 迁移结果
     */
    @PostMapping("/avatars")
    public ResponseEntity<?> migrateAvatars() {
        try {
            // 获取所有用户
            List<User> users = userRepository.findAll();
            int totalCount = users.size();
            int successCount = 0;
            int skipCount = 0;
            int errorCount = 0;
            
            // 头像目录
            Path avatarDir = Paths.get("uploads/avatars");
            
            for (User user : users) {
                try {
                    String avatar = user.getAvatar();
                    
                    // 跳过已经是Base64格式的头像
                    if (avatar != null && avatar.startsWith("data:image/")) {
                        skipCount++;
                        continue;
                    }
                    
                    // 默认头像特殊处理
                    if ("default_avatar.jpg".equals(avatar)) {
                        // 将旧的默认头像值更新为Base64格式
                        user.setAvatar(DefaultAvatarConstants.DEFAULT_AVATAR_BASE64);
                        userRepository.save(user);
                        successCount++;
                        continue;
                    }
                    
                    // 文件不存在或为null，跳过
                    if (avatar == null || avatar.isEmpty()) {
                        // 设置为默认头像
                        user.setAvatar(DefaultAvatarConstants.DEFAULT_AVATAR_BASE64);
                        userRepository.save(user);
                        skipCount++;
                        continue;
                    }
                    
                    // 构建头像文件路径
                    Path avatarPath = avatarDir.resolve(avatar);
                    File avatarFile = avatarPath.toFile();
                    
                    if (!avatarFile.exists() || !avatarFile.isFile()) {
                        // 文件不存在，设置为默认头像
                        user.setAvatar(DefaultAvatarConstants.DEFAULT_AVATAR_BASE64);
                        userRepository.save(user);
                        skipCount++;
                        continue;
                    }
                    
                    // 读取文件内容并转换为Base64
                    byte[] fileContent = Files.readAllBytes(avatarPath);
                    String contentType = Files.probeContentType(avatarPath);
                    if (contentType == null) {
                        // 根据文件扩展名推断内容类型
                        String fileName = avatarFile.getName().toLowerCase();
                        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                            contentType = "image/jpeg";
                        } else if (fileName.endsWith(".png")) {
                            contentType = "image/png";
                        } else if (fileName.endsWith(".gif")) {
                            contentType = "image/gif";
                        } else {
                            contentType = "application/octet-stream";
                        }
                    }
                    
                    String base64Data = Base64.getEncoder().encodeToString(fileContent);
                    String base64Image = "data:" + contentType + ";base64," + base64Data;
                    
                    // 更新用户头像
                    user.setAvatar(base64Image);
                    userRepository.save(user);
                    
                    successCount++;
                } catch (Exception e) {
                    errorCount++;
                    // 异常处理，记录错误但继续迁移其他用户
                    System.err.println("Error migrating avatar for user ID " + user.getId() + ": " + e.getMessage());
                }
            }
            
            // 返回迁移结果
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Avatar migration completed");
            result.put("totalUsers", totalCount);
            result.put("successCount", successCount);
            result.put("skipCount", skipCount);
            result.put("errorCount", errorCount);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Migration failed: " + e.getMessage());
        }
    }
} 
package group6.demo.service;

import group6.demo.dto.UserLoginDTO;
import group6.demo.dto.UserRegistrationDTO;
import group6.demo.dto.UpdateBankCardDTO;
import group6.demo.dto.ChangePasswordDTO;
import group6.demo.dto.UserProfileDTO;
import group6.demo.entity.User;

import java.util.Optional;

public interface UserService {
    User registerUser(UserRegistrationDTO registrationDTO);
    User loginUser(UserLoginDTO loginDTO);
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
    boolean isBankcardExists(String bankcard);
    Optional<User> changeUserStatus(Long id);
    User updateBankCard(Long userId, UpdateBankCardDTO updateBankCardDTO);
    
    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param changePasswordDTO 密码修改信息
     * @return 修改成功返回true，否则返回false
     */
    boolean changePassword(Long userId, ChangePasswordDTO changePasswordDTO);
    
    /**
     * 获取用户个人中心信息
     * @param userId 用户ID
     * @return 用户个人信息DTO
     */
    UserProfileDTO getUserProfile(Long userId);
    
    /**
     * 更新用户头像
     * @param userId 用户ID
     * @param filename 头像文件名
     * @return 更新后的用户
     */
    User updateAvatar(Long userId, String filename);
    
    /**
     * 解绑银行卡
     * @param userId 用户ID
     * @return 解绑成功返回true，否则返回false
     */
    boolean unbindBankCard(Long userId);
} 
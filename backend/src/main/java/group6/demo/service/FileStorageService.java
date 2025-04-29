package group6.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    /**
     * 保存用户上传的头像
     * @param file 上传的文件
     * @param userId 用户ID
     * @return 存储后的文件名
     * @throws IOException 如果文件保存失败
     */
    String storeAvatar(MultipartFile file, Long userId) throws IOException;
    
    /**
     * 获取文件的存储路径
     * @param filename 文件名
     * @return 文件的存储路径
     */
    Path getFilePath(String filename);
    
    /**
     * 删除之前的头像文件
     * @param filename 要删除的文件名
     * @return 是否删除成功
     */
    boolean deleteFile(String filename);
} 
package group6.demo.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;

/**
 * Base64编码工具类
 */
public class Base64Util {

    /**
     * 将MultipartFile转换为Base64编码字符串
     *
     * @param file 文件
     * @return Base64编码字符串
     * @throws IOException 如果文件读取失败
     */
    public static String encodeToBase64(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        
        byte[] fileContent = file.getBytes();
        String contentType = file.getContentType();
        String base64Encoded = Base64.getEncoder().encodeToString(fileContent);
        
        // 返回完整的Base64图片字符串，包含MIME类型前缀
        return "data:" + contentType + ";base64," + base64Encoded;
    }
    
    /**
     * 检查字符串是否是合法的Base64编码的图片
     *
     * @param base64String Base64编码字符串
     * @return 是否是合法的Base64编码的图片
     */
    public static boolean isValidBase64Image(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return false;
        }
        
        // 检查是否包含图片的Base64编码格式前缀
        if (!base64String.startsWith("data:image/")) {
            return false;
        }
        
        // 检查是否包含base64标识
        if (!base64String.contains(";base64,")) {
            return false;
        }
        
        return true;
    }
} 
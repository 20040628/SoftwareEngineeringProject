package group6.demo.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Pattern;

public class ValidationUtil {
    // Username validation: 3-20 characters, letters, numbers and underscore
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    
    // Password validation: minimum 8 characters, at least one uppercase letter, one lowercase letter, one number
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
    
    // Email validation
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    // Mobile validation: numbers only, 10-13 digits
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^\\d{10,13}$");

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidMobile(String mobile) {
        return mobile != null && MOBILE_PATTERN.matcher(mobile).matches();
    }

    public static boolean isValidBirthday(Date birthday) {
        if (birthday == null) return false;
        Date now = new Date();
        return birthday.before(now);
    }

    // scooter price validation: Integers have a maximum of three digits, decimals have a maximum of two digits
    public static final Pattern PRICE_PATTERN = Pattern.compile("^(\\d{1,3})(\\.\\d{1,2})?$");
    public static boolean isValidPrice(BigDecimal price) {
        if (price == null) {
            return false;
        }
        else{
            String priceStr = price.toPlainString();
            return PRICE_PATTERN.matcher(priceStr).matches();
        }
    }
} 
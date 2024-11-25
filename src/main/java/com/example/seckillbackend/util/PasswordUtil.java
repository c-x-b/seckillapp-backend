package com.example.seckillbackend.util;

import com.example.seckillbackend.service.UserServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.SecureRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordUtil {

    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 生成随机盐值
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 加密密码
    public static String encryptPassword(String password, String salt) {
        String saltedPassword = password + salt;
        String encoded = encoder.encode(saltedPassword);
        return encoded;
    }

    public static Boolean validatePassword(String password, String salt, String encoded) {
        return encoder.matches(password + salt, encoded);
    }
}
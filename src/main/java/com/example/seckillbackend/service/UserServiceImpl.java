package com.example.seckillbackend.service;

import com.example.seckillbackend.entity.User;
import com.example.seckillbackend.repository.UserRepository;
import com.example.seckillbackend.service.UserService;
import com.example.seckillbackend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(User user) {
        // 检查用户名、邮箱、手机号是否已存在
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        if (userRepository.findByMobile(user.getMobile()) != null) {
            throw new RuntimeException("手机号已被注册");
        }

        logger.info("检查完成，可以注册");

        // 生成盐值
        String salt = PasswordUtil.generateSalt();
        user.setSalt(salt);

        // 密码加密
        String encryptedPassword = PasswordUtil.encryptPassword(user.getPassword(), salt);
        user.setPassword(encryptedPassword);

        // 设置注册日期
        user.setRegisterDate(new Date());

        // 保存用户
        return userRepository.save(user);
    }
}
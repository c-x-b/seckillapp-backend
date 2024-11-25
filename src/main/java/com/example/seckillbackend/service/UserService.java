package com.example.seckillbackend.service;

import com.example.seckillbackend.entity.User;

public interface UserService {
    User register(User user);
    User login(String username, String password);
    User findById(Long id);
}
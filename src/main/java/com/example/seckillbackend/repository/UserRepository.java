package com.example.seckillbackend.repository;

import com.example.seckillbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByMobile(String mobile);
    User findById(long id);
}
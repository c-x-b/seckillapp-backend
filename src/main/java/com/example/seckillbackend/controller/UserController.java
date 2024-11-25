package com.example.seckillbackend.controller;

import com.example.seckillbackend.entity.LoginRequest;
import com.example.seckillbackend.entity.User;
import com.example.seckillbackend.service.UserService;
import com.example.seckillbackend.util.JwtUtil;
import com.example.seckillbackend.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*",maxAge=3600)
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        //logger.info("register user: {}", user);
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Access-Control-Allow-Origin", "*");
        try {
            User registeredUser = userService.register(user);
            logger.info("注册成功:{}", registeredUser);
            return new ResponseEntity<>(new Response(200, "注册成功", registeredUser), headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("注册失败:{}", e.getMessage());
            return new ResponseEntity<>(new Response(400, e.getMessage(), null), headers, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest) {
        try {
            //String decryptedPassword = RSAUtil.decrypt(loginRequest.getPassword());
            User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            // 生成 Token
            logger.info("密码验证成功");
            String token = JwtUtil.generateToken(user);
            logger.info("Token生成成功");
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);
            return new Response(200, "登录成功", data);
        } catch (Exception e) {
            return new Response(400, e.getMessage(), null);
        }
    }

    @GetMapping("/user/info")
    public Response getUserInfo(HttpServletRequest request) {
        Long userId =Long.valueOf((Integer) request.getAttribute("userId"));
        logger.info("Token 验证通过，用户ID：{}", userId);
        String username = (String) request.getAttribute("username");
        logger.info("用户登录：{}", username);
        User user = userService.findById(userId);
        return new Response(200, "获取用户信息成功", user);
    }

    @GetMapping("/")
    public String index() {
        return "Hello, World!";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "Register!";
    }
}

// 响应封装类

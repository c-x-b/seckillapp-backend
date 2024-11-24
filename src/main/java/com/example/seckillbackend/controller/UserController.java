package com.example.seckillbackend.controller;

import com.example.seckillbackend.entity.User;
import com.example.seckillbackend.service.UserService;
import com.example.seckillbackend.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Response register(@RequestBody User user) {
        //logger.info("register user: {}", user);
        try {
            User registeredUser = userService.register(user);
            logger.info("注册成功:{}", registeredUser);
            return new Response(200, "注册成功", registeredUser);
        } catch (Exception e) {
            logger.info("注册失败:{}", e.getMessage());
            return new Response(400, e.getMessage(), null);
        }
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
class Response {
    private int code;
    private String message;
    private Object data;

    public Response(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() { return code; }
    public void SetCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void SetMessage(String message) { this.message = message; }
    public Object getData() { return data; }
    public void SetData(Object data) { this.data = data; }
}
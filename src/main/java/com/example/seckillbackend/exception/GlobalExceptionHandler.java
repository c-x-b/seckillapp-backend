package com.example.seckillbackend.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.seckillbackend.entity.Response;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        return new Response(500, "服务器内部错误：" + e.getMessage(), null);
    }

}
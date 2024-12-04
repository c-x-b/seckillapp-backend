package com.example.seckillbackend.filter;

import com.example.seckillbackend.controller.UserController;
import com.example.seckillbackend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.juli.logging.Log;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter implements Filter {

    private static final List<String> EXCLUDE_URLS = Arrays.asList("/api/", "/api/login", "/api/register", "/api/products");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI(); // 如果请求路径在排除列表中，直接放行
        if (EXCLUDE_URLS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 如果是OPTIONS请求，直接放行
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = JwtUtil.parseToken(token);
                // 将用户信息存储在请求属性中，便于后续使用
                httpRequest.setAttribute("userId", claims.get("userId"));
                httpRequest.setAttribute("username", claims.getSubject());
            } catch (Exception e) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token 无效或已过期");
                return;
            }
        } else {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "缺少 Authorization 头");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
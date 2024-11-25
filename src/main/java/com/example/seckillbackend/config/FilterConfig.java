package com.example.seckillbackend.config;

import com.example.seckillbackend.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

//    @Bean
//    public FilterRegistrationBean<JwtFilter> jwtFilter() {
//        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new JwtFilter());
//        registrationBean.addUrlPatterns("/api/*"); // 设置需要过滤的 URL 模式，可以根据实际情况调整
//        return registrationBean;
//    }
}

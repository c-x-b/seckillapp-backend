package com.example.seckillbackend.service;

import com.example.seckillbackend.entity.Product;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;

public interface ProductService {
    // 查找商品，支持分页、关键词搜索、类别、价格区间筛选
    Page<Product> findProducts(int page, int size, String keyword, Long categoryId, BigDecimal priceMin, BigDecimal priceMax);

    Product findById(Long id);
    // 其他方法
}
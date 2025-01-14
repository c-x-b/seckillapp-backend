package com.example.seckillbackend.service;

import com.example.seckillbackend.entity.Order;
import com.example.seckillbackend.entity.SeckillOrderRequest;
import com.example.seckillbackend.entity.SeckillProduct;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;

public interface SeckillService {
    Page<SeckillProduct> findSeckillProducts(int page, int size);

    SeckillProduct findById(Long id);

    Order createOrder(SeckillOrderRequest seckillOrderRequest, Long userId);

    boolean trySeckill(Long seckillId, Integer seckillAmount, Long userId);
}

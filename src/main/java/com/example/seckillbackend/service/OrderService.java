package com.example.seckillbackend.service;

import com.example.seckillbackend.entity.Order;
import com.example.seckillbackend.entity.Product;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;

public interface OrderService {
    Page<Order> findOrders(Long userId, int page, int size);

    Order findOrderById(Long orderId);

    void saveOrder(Order order);
}
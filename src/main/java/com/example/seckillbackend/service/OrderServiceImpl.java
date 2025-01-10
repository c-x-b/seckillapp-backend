package com.example.seckillbackend.service;

import com.example.seckillbackend.dto.OrderRequest;
import com.example.seckillbackend.entity.Order;
import com.example.seckillbackend.entity.Product;
import com.example.seckillbackend.repository.OrderRepository;
import com.example.seckillbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Order> findOrders(Long userId, int page, int size) {
        return orderRepository.findByUserId(userId, PageRequest.of(page - 1, size));
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order createOrder(OrderRequest orderRequest, Long userId) {
        // 查询商品信息
        Product product = productRepository.findById(orderRequest.getGoodsId())
                .orElseThrow(() -> new IllegalArgumentException("商品不存在"));

        // 检查库存是否充足
        if (product.getStock() < orderRequest.getOrderAmount()) {
            throw new IllegalArgumentException("库存不足");
        }

        // 减少库存
        product.setStock(product.getStock() - orderRequest.getOrderAmount());
        productRepository.save(product);

        // 创建订单
        Order order = new Order();
        order.setUserId(userId); // 转换为 Long 类型
        order.setGoodsId(orderRequest.getGoodsId());
        order.setGoodsCount(orderRequest.getOrderAmount());
        order.setGoodsName(product.getName());
        order.setGoodsPrice(product.getPrice());
        order.setDeliveryAddrId(null); // 配送地址需要在后续功能中扩展
        order.setOrderChannel(1); // 假设固定为 PC 端（1）
        order.setStatus(0); // 订单状态为未支付
        order.setCreateDate(LocalDateTime.now());

        // 保存订单到数据库
        return orderRepository.save(order);
    }


}
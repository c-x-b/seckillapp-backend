package com.example.seckillbackend.service;

import com.example.seckillbackend.entity.Order;
import com.example.seckillbackend.entity.Product;
import com.example.seckillbackend.entity.SeckillOrderRequest;
import com.example.seckillbackend.entity.SeckillProduct;
import com.example.seckillbackend.repository.OrderRepository;
import com.example.seckillbackend.repository.ProductRepository;
import com.example.seckillbackend.repository.SeckillRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    private static final Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);

    @Autowired
    private SeckillRepository seckillRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void initSeckillStock() {
        List<SeckillProduct> seckillProducts = seckillRepository.findAll();
        for (SeckillProduct product : seckillProducts) {
            String stockKey = "seckill:stock:" + product.getId();
            redisTemplate.opsForValue().set(stockKey, product.getStockCount());

            redisTemplate.opsForValue().set("seckillProducts::" + product.getId(), product);
        }
        logger.info("Seckill stock initialized in Redis");
    }

    @Override
    public Page<SeckillProduct> findSeckillProducts(int page, int size) {
        return seckillRepository.findAll(PageRequest.of(page - 1, size));
    }

    @Override
    public SeckillProduct findById(Long id) {
        logger.info("findById: Fetching SeckillProduct with id: {}", id);

        // 从缓存中获取 SeckillProduct 静态数据
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            Object redisData = redisTemplate.opsForValue().get("seckillProducts::" + id);
            SeckillProduct seckillProduct = null;

            if (redisData instanceof String) {
                String json = (String) redisData;
                seckillProduct = objectMapper.readValue(json, SeckillProduct.class);
                logger.debug("SeckillProduct from Redis: {}", seckillProduct);
            } else if (redisData instanceof LinkedHashMap) {
                // 直接将 LinkedHashMap 转换为 SeckillProduct 对象
                seckillProduct = objectMapper.convertValue(redisData, SeckillProduct.class);
                logger.debug("SeckillProduct from Redis (as LinkedHashMap): {}", seckillProduct);
            }

            if (seckillProduct == null) {
                // 如果缓存中没有，则从数据库中加载并写入缓存
                seckillProduct = seckillRepository.findById(id).orElse(null);
                logger.debug("SeckillProduct from DB: {}", seckillProduct);
                if (seckillProduct != null) {
                    redisTemplate.opsForValue().set("seckillProducts::" + id, seckillProduct);
                }
            }

            if (seckillProduct != null) {
                String stockKey = "seckill:stock:" + id;
                Integer stock = (Integer) redisTemplate.opsForValue().get(stockKey);
                logger.debug("Stock for seckill ID {}: {}", id, stock);

                if (stock != null) {
                    // 更新库存值到 SeckillProduct 对象
                    seckillProduct.setStockCount(stock);
                } else {
                    logger.warn("Stock for seckill ID {} not found in Redis.", id);
                }
            }
            return seckillProduct;
        } catch (Exception e) {
            logger.error("Error fetching SeckillProduct with id: {}", id, e);
        }

        return null;
    }

    @Override
    public Order createOrder(SeckillOrderRequest seckillOrderRequest, Long userId) {
        SeckillProduct seckillProduct = seckillRepository.findById(seckillOrderRequest.getSeckillId()).orElse(null);
        if (seckillProduct == null || seckillProduct.getStockCount() <= 0) {
            throw new RuntimeException("商品不存在或库存不足");
        }

        Product product = productRepository.findById(seckillProduct.getGoodsId()).orElse(null);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setGoodsId(seckillProduct.getGoodsId());
        order.setGoodsCount(seckillOrderRequest.getOrderAmount());
        order.setGoodsName(product.getName());
        order.setGoodsPrice(seckillProduct.getSeckillPrice());
        order.setDeliveryAddrId(null);
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(LocalDateTime.now());

        // TODO: 减少库存
        seckillProduct.setStockCount(seckillProduct.getStockCount() - seckillOrderRequest.getOrderAmount());
        seckillRepository.save(seckillProduct);

        orderRepository.save(order);

        return order;
    }

    @Override
    public boolean trySeckill(Long seckillId, Integer seckillAmount, Long userId) {
        String stockKey = "seckill:stock:" + seckillId;
        String usersKey = "seckill:users:" + seckillId;

        logger.debug("{} 想要购买 {}", userId, seckillId);
        // 检查用户是否已购买
        if (redisTemplate.opsForSet().isMember(usersKey, userId)) {
            return false; // 用户已购买
        }
        logger.debug("{} 可以购买 {}", userId, seckillId);

        // 扣减库存（使用 Redis 原子操作）
        Long stock = redisTemplate.opsForValue().decrement(stockKey);
        if (stock == null || stock < 0) {
            if (stock != null && stock < 0) {
                // 回滚库存
                redisTemplate.opsForValue().increment(stockKey);
            }
            return false; // 库存不足
        }

        // 标记用户已购买
        redisTemplate.opsForSet().add(usersKey, userId);

        return true; // 秒杀成功，允许进入消息队列
    }
}

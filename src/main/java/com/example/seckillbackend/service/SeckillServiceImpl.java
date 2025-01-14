package com.example.seckillbackend.service;

import com.example.seckillbackend.entity.Order;
import com.example.seckillbackend.entity.SeckillOrderRequest;
import com.example.seckillbackend.entity.SeckillProduct;
import com.example.seckillbackend.repository.SeckillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillRepository seckillRepository;

    @Override
    public Page<SeckillProduct> findSeckillProducts(int page, int size) {
        return seckillRepository.findAll(PageRequest.of(page - 1, size));
    }

    @Override
    public SeckillProduct findById(Long id) {
        return seckillRepository.findById(id).orElse(null);
    }

    @Override
    public Order createOrder(SeckillOrderRequest seckillOrderRequest, Long userId) {
        //TODO: 验证秒杀条件，在订单表和秒杀订单表中插入数据，并削减商品库存
        return null;
    }
}

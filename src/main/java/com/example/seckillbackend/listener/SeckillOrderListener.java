package com.example.seckillbackend.listener;

import com.example.seckillbackend.entity.SeckillOrderRequest;
import com.example.seckillbackend.service.SeckillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class SeckillOrderListener {

    private static final Logger logger = LoggerFactory.getLogger(SeckillOrderListener.class);

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "${seckill.rabbitmq.queue}")
    public void receiveMessage(Map<String, Object> message) {
        try {
            SeckillOrderRequest orderRequest = objectMapper.convertValue(message.get("orderRequest"), SeckillOrderRequest.class);
            Long userId = objectMapper.convertValue(message.get("userId"), Long.class);
            logger.info("Processed seckill order for userId: {}", userId);

            // Process the seckill order
            seckillService.createOrder(orderRequest, userId);

            logger.info("Processed seckill order done");
        } catch (Exception e) {
            logger.error("Failed to process seckill order", e);
        }
    }
}
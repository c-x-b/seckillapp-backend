package com.example.seckillbackend.controller;

import com.example.seckillbackend.dto.OrderDTO;
import com.example.seckillbackend.dto.OrderRequest;
import com.example.seckillbackend.entity.Order;
import com.example.seckillbackend.entity.Response;
import com.example.seckillbackend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public Map<String, Object> getOrders(@RequestParam int page, @RequestParam int size, @RequestAttribute Long userId) {
        //Long userId =Long.valueOf((Integer) request.getAttribute("userId"));
        logger.info("尝试获取用户ID为 {} 的订单信息", userId);
        Page<Order> orderPage = orderService.findOrders(userId, page, size);
        List<OrderDTO> orderDTOs = orderPage.getContent().stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setGoodsName(order.getGoodsName());
            dto.setGoodsCount(order.getGoodsCount());
            dto.setStatus(order.getStatus());
            dto.setCreateDate(order.getCreateDate());
            return dto;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        Map<String, Object> data = new HashMap<>();
        data.put("records", orderDTOs);
        data.put("total", orderPage.getTotalElements());
        response.put("data", data);
        return response;
    }

    @GetMapping("/order")
    public Map<String, Object> getOrderById(@RequestParam Long id, @RequestAttribute Long userId) {
        Order order = orderService.findOrderById(id);
        Map<String, Object> response = new HashMap<>();
        if (order != null) {
            if (order.getUserId().equals(userId)) {
                response.put("code", 200);
                response.put("data", order);
            } else {
                response.put("code", 403);
                response.put("message", "You do not have permission to view this order");
            }
        } else {
            response.put("code", 404);
            response.put("message", "Order not found");
        }
        return response;
    }


    @PostMapping("/AddOrder")
    public Response createOrder(@RequestBody OrderRequest orderRequest,@RequestAttribute Long userId) {
        try {
            // 调用服务层创建订单
            Order order = orderService.createOrder(orderRequest, userId);

            // 返回订单信息
            return new Response(200, "订单创建成功", order);
        } catch (IllegalArgumentException e) {
            return new Response(400, e.getMessage(), null);
        } catch (Exception e) {
            return new Response(500, "服务器内部错误", null);
        }
    }


}
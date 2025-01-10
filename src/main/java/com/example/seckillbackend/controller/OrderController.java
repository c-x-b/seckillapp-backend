package com.example.seckillbackend.controller;

import com.example.seckillbackend.dto.OrderDTO;
import com.example.seckillbackend.entity.Order;
import com.example.seckillbackend.entity.Product;
import com.example.seckillbackend.entity.Response;
import com.example.seckillbackend.service.OrderService;
import com.example.seckillbackend.service.ProductService;
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

    @Autowired
    private ProductService productService;

    @GetMapping("/orders")
    public Response getOrders(@RequestParam int page, @RequestParam int size, @RequestAttribute Long userId) {
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

        Map<String, Object> data = new HashMap<>();
        data.put("records", orderDTOs);
        data.put("total", orderPage.getTotalElements());
        return new Response(200, "获取订单列表成功", data);
    }

    @GetMapping("/order")
    public Response getOrderById(@RequestParam Long id, @RequestAttribute Long userId) {
        Order order = orderService.findOrderById(id);
        if (order != null) {
            if (order.getUserId().equals(userId)) {
                return new Response(200, "获取订单详情成功", order);
            } else {
                return new Response(403, "无权查看此订单", null);
            }
        } else {
            return new Response(404, "订单不存在", null);
        }
    }

    @PostMapping("/order/cancel")
    public Response cancelOrderById(@RequestBody Map<String, Object> params, @RequestAttribute Long userId) {
        Long id = Long.valueOf((Integer) params.get("id"));
        Order order = orderService.findOrderById(id);
        if (order != null) {
            if (order.getUserId().equals(userId)) {
                if (order.canBeCancelled()) {
                    order.setStatus(6);
                    orderService.saveOrder(order);

                    // 恢复商品库存
                    Product product = productService.findById(order.getGoodsId());
                    if (product != null) {
                        product.setStock(product.getStock() + order.getGoodsCount());
                        productService.saveProduct(product);
                        return new Response(200, "取消订单成功", null);
                    } else {
                        return new Response(404, "商品不存在", null);
                    }
                } else {
                    return new Response(400, "订单状态不允许取消", null);
                }
            } else {
                return new Response(403, "无权取消此订单", null);
            }
        } else {
            return new Response(404, "订单不存在", null);
        }
    }
}
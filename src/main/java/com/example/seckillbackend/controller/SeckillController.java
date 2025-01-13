package com.example.seckillbackend.controller;

import com.example.seckillbackend.dto.OrderRequest;
import com.example.seckillbackend.entity.Product;
import com.example.seckillbackend.entity.Response;
import com.example.seckillbackend.entity.SeckillProduct;
import com.example.seckillbackend.service.SeckillService;
import com.example.seckillbackend.service.ProductService;
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
public class SeckillController {
    private static final Logger logger = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private ProductService productService;

    @GetMapping("/seckills")
    public Response getSeckills(@RequestParam int page, @RequestParam int size) {
        try {
            Page<SeckillProduct> seckillPage = seckillService.findSeckillProducts(page, size);
            List<Map<String, Object>> content = seckillPage.getContent().stream().map(product -> {
                Map<String, Object> productMap = new HashMap<>();
                Product goods = productService.findById(product.getGoodsId());
                if (goods != null) {
                    productMap.put("id", product.getId());
                    productMap.put("name", goods.getName());
                    productMap.put("seckillPrice", product.getSeckillPrice());
                    productMap.put("stock", product.getStockCount());
                    productMap.put("startTime", product.getStartDate());
                    productMap.put("endTime", product.getEndDate());
                    productMap.put("remainingTime", 0);
                    productMap.put("img", goods.getImg());
                } else {
                    throw new IllegalArgumentException("Product not found for goodsId: " + product.getGoodsId());
                }
                return productMap;
            }).collect(Collectors.toList());

            Map<String, Object> data = new HashMap<>();
            data.put("content", content);
            data.put("totalElements", seckillPage.getTotalElements());

            return new Response(200, "获取秒杀商品列表成功", data);
        } catch (IllegalArgumentException e) {
            return new Response(400, e.getMessage(), null);
        } catch (Exception e) {
            return new Response(500, "服务器内部错误", null);
        }
    }

    @GetMapping("/seckill/{id}")
    public Response getSeckillById(@PathVariable Long id) {
        try {
            SeckillProduct seckillProduct = seckillService.findById(id);
            if (seckillProduct == null) {
                return new Response(404, "秒杀商品不存在", null);
            }

            Product product = productService.findById(seckillProduct.getGoodsId());
            if (product == null) {
                return new Response(404, "商品不存在", null);
            }

            Map<String, Object> productDetails = new HashMap<>();
            productDetails.put("name", product.getName());
            productDetails.put("title", product.getTitle());
            productDetails.put("originalPrice", product.getPrice());
            productDetails.put("seckillPrice", seckillProduct.getSeckillPrice());
            productDetails.put("stock", seckillProduct.getStockCount());
            productDetails.put("purchaseLimit", seckillProduct.getPurchaseLimit());
            productDetails.put("startTime", seckillProduct.getStartDate());
            productDetails.put("endTime", seckillProduct.getEndDate());
            productDetails.put("countdown", 0);
            productDetails.put("detail", product.getDetail());
            productDetails.put("img", product.getImg());

            return new Response(200, "获取秒杀商品详情成功", productDetails);
        } catch (Exception e) {
            return new Response(500, "服务器内部错误", null);
        }
    }

    @PostMapping("/seckill/createOrder")
    public Response createSeckillOrder(@RequestBody OrderRequest orderRequest, @RequestAttribute Long userId) {
        return null;
    }
}

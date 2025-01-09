package com.example.seckillbackend.controller;

import com.example.seckillbackend.entity.Product;
import com.example.seckillbackend.entity.Response;
import com.example.seckillbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.seckillbackend.entity.ProductResponse;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins="*",maxAge=3600)
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/getProducts")
    public Response getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword,
            //@RequestParam(required = false) Long category,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax
    ) {
        Page<Product> productPage = productService.findProducts(page, size, keyword,priceMin, priceMax);

        if (page > productPage.getTotalPages()) {
            return new Response(404, "已经没有商品了", null);
        }

        // 构造分页数据封装
        ProductResponse productResponse = new ProductResponse(
                productPage.getContent(), // 当前页商品列表
                productPage.getTotalElements(), // 总记录数
                productPage.getTotalPages(), // 总页数
                productPage.getNumber() + 1 // 当前页码，从1开始
        );

        // 返回统一的响应格式
        return new Response(200, "获取商品列表成功", productResponse);
    }

    @GetMapping("/{id}")
    public Response getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product != null) {
            return new Response(200, "获取商品详情成功", product);
        } else {
            return new Response(404, "商品不存在", null);
        }
    }

    // 添加商品接口
    @PostMapping("/myadd")
    public Response addProduct(@RequestBody Product product) {
        try {
            // 检查商品名称是否重复
            if (productService.existsByName(product.getName())) {
                return new Response(400, "商品已存在，请使用其他名称", null);
            }

            // 保存商品
            Product savedProduct = productService.saveProduct(product);
            return new Response(200, "商品添加成功", savedProduct);
        } catch (Exception e) {
            return new Response(500, "商品添加失败：" + e.getMessage(), null);
        }
    }

    // 批量添加商品接口
    @PostMapping("/myaddBatch")
    public Response addProducts(@RequestBody List<Product> products) {
        List<Product> savedProducts = new ArrayList<>(); // 用于保存成功存储的商品
        List<String> duplicateNames = new ArrayList<>(); // 用于保存重复的商品名称

        for (Product product : products) {
            // 检查商品名称是否重复
            if (productService.existsByName(product.getName())) {
                duplicateNames.add(product.getName());
            } else {
                // 保存商品
                Product savedProduct = productService.saveProduct(product);
                savedProducts.add(savedProduct);
            }
        }

        // 构造响应结果
        if (duplicateNames.isEmpty()) {
            // 如果没有重复商品
            return new Response(200, "所有商品添加成功", savedProducts);
        } else {
            // 如果有重复商品，返回部分成功和重复商品信息
            Map<String, Object> result = new HashMap<>();
            result.put("savedProducts", savedProducts);
            result.put("duplicateNames", duplicateNames);

            return new Response(400, "部分商品添加成功，但以下商品名称已存在：" + duplicateNames, result);
        }
    }

}


/*
这里的 @RequestMapping("/api") 会让 ProductController 类中的所有方法都以 /api 为前缀。因此，getProducts() 方法的路径实际上是 /api/products。

访问这个方法的 URL 就是：http://localhost:8080/api/products
getProducts() 方法会响应这个请求并返回商品列表数据。
 */
package com.example.seckillbackend.controller;

import com.example.seckillbackend.entity.Product;
import com.example.seckillbackend.entity.Response;
import com.example.seckillbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import com.example.seckillbackend.entity.ProductResponse;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public Response getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax
    ) {
        Page<Product> productPage = productService.findProducts(page, size, keyword, category, priceMin, priceMax);

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

    @GetMapping("/products/{id}")
    public Response getProductDetail(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product != null) {
            return new Response(200, "获取商品详情成功", product);
        } else {
            return new Response(404, "商品不存在", null);
        }
    }



}


/*
这里的 @RequestMapping("/api") 会让 ProductController 类中的所有方法都以 /api 为前缀。因此，getProducts() 方法的路径实际上是 /api/products。

访问这个方法的 URL 就是：http://localhost:8080/api/products
getProducts() 方法会响应这个请求并返回商品列表数据。
 */
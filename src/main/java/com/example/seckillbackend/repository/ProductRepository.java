package com.example.seckillbackend.repository;

import com.example.seckillbackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // 自定义查询方法（如果需要）
}

/*
可以使用JPA默认的方法：

@Autowired
private ProductRepository productRepository111;

// 查询所有商品
List<Product> products = productRepository.findAll();

// 根据主键查询商品
Optional<Product> product = productRepository.findById(1L);

// 删除商品
productRepository.deleteById(1L);

// 保存商品
productRepository.save(new Product());

实现与数据库的交互。
*/
package com.example.seckillbackend.repository;

import com.example.seckillbackend.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // 自定义查询方法（如果需要）
    Product findByName(String name); // 根据商品名称查询商品

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 使用悲观锁，在查询时锁定该行，防止其他事务同时修改。
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);
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
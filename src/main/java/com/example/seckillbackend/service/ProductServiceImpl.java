package com.example.seckillbackend.service;

import com.example.seckillbackend.entity.Product;
import com.example.seckillbackend.repository.ProductRepository;
import com.example.seckillbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> findProducts(int page, int size, String keyword,
                                      //Long categoryId,
                                      BigDecimal priceMin, BigDecimal priceMax) {
        Pageable pageable = PageRequest.of(page - 1, size); //创建分页参数对象，告诉数据库从哪一页开始、每页需要返回多少条数据。

        /*
            使用 JPA 的 Specification 接口动态生成 SQL 查询条件。
            root：表示当前实体（Product），可以用它访问商品的字段（如 name, price）。
            query：表示查询对象，允许对结果排序或分组（这里没有使用）。
            cb（CriteriaBuilder）：用于构建 SQL 条件（如 WHERE, LIKE, >=）。
        */
        Specification<Product> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            /*
             *   如果 keyword 不为空，就为查询条件添加模糊搜索。
             *   cb.like：生成 SQL 的 LIKE 条件，用于模糊匹配字符串。
             *   % 是 SQL 的通配符，表示任意字符。
             *   这里用 cb.or 指定关键词可以匹配 name 或 title 字段。
             */
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + keyword + "%"));
            }
            // 如果有类别关联，则添加类别条件
            /*
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            */
            if (priceMin != null) {
                predicates.add(cb.ge(root.get("price"), priceMin));
            }
            if (priceMax != null) {
                predicates.add(cb.le(root.get("price"), priceMax));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return productRepository.findAll(specification, pageable);
    }


    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }


    @Override
    public Product saveProduct(Product product) {
        // 保存商品到数据库
        return productRepository.save(product);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.findByName(name) != null; // 查询商品名称是否已存在
    }
}
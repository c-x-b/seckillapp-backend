package com.example.seckillbackend.repository;

import com.example.seckillbackend.entity.SeckillProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SeckillRepository extends JpaRepository<SeckillProduct, Long> {
}
